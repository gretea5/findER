package com.gretea5.finder.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gretea5.finder.BuildConfig
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.KakaoApiClient
import com.gretea5.finder.data.KakaoApiService
import com.gretea5.finder.data.model.AddressResponse
import com.gretea5.finder.data.model.ERDetail
import com.gretea5.finder.data.model.ERPreview
import com.gretea5.finder.data.model.LocationResponse
import com.gretea5.finder.databinding.FragmentMapBinding
import com.gretea5.finder.ui.viewmodel.ERViewModel
import com.gretea5.finder.util.dial.DialUtil.openDial
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.getLabels
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.saveLabels
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class MapFragment : Fragment() {
    private val LOGTAG = "MapFragment"
    private lateinit var _fusedLocationClient : FusedLocationProviderClient
    private val fusedLocationClient get() = _fusedLocationClient!!
    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var _kakaoMap: KakaoMap? = null
    private val kakaoMap get() = _kakaoMap!!

    private var labelClicked = false

    private val erViewModel : ERViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //MapFragment가 처음 onCreate가 될 시에, 서버에서 응급실 Label 정보 가져오기
        saveEmergencyLabels()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater)
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(p0: Exception?) {}

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                _kakaoMap = p0

                val labels = getLabels(requireContext())
                showMapLabels(labels)

                setLabelClickListener()

                kakaoMap.setOnMapClickListener { _, _, _, _ ->
                    if (labelClicked) {
                        binding.erPreView.visibility = View.VISIBLE
                        labelClicked = false
                    }
                    else {
                        binding.erPreView.visibility = View.INVISIBLE
                    }
                }

                kakaoMap.setOnCameraMoveStartListener { _, _ ->
                    //label이 클릭되었을 경우,
                    if (labelClicked) {
                        binding.erPreView.visibility = View.VISIBLE
                    }
                    else {
                        binding.erPreView.visibility = View.INVISIBLE
                    }
                }

                kakaoMap.setOnCameraMoveEndListener { _, _, _ ->
                    if (labelClicked) {
                        binding.erPreView.visibility = View.VISIBLE
                        labelClicked = false
                    }
                    else {
                        binding.erPreView.visibility = View.INVISIBLE
                    }
                }

                if (checkLocationPermission()) {
                    setCenterCurrentLocation()
                }
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchIconImageView.setOnClickListener {
            if (binding.searchEditText.toString().isNotEmpty()) {
                searchName(binding.searchEditText.text.toString())
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.locationBtn -> {
                    requestLocationPermission()
                    true
                }
                else -> false
            }
        }
    }

    override fun onResume() {
        super.onResume()

        binding.mapView.resume()
    }

    override fun onPause() {
        super.onPause()

        binding.mapView.pause()
    }

    private fun setLabelClickListener() {
        kakaoMap.setOnLodLabelClickListener { _, _, lodLabel ->
            //위치 권한이 부여 되었을 경우,
            if (checkLocationPermission()) {
                labelClicked = true

                //응급의료기관 식별자
                val hpID = lodLabel.labelId

                //위치를 받아서 프리뷰를 보여준다.
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val lat = location.latitude
                            val lon = location.longitude

                            showERPreview(
                                hpID = hpID,
                                lat = lat,
                                lon = lon
                            )
                        }
                    }
                    .addOnFailureListener {}

                //label의 위도, 경도
                val labelLat = lodLabel.position.latitude
                val labelLon = lodLabel.position.longitude

                val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(labelLat, labelLon))
                kakaoMap.moveCamera(
                    cameraUpdate,
                    CameraAnimation.from(300, true, true)
                )
            }
            //위치 권한이 부여되지 않았을 경우,
            else {
                val toast = Toast.makeText(context, "왼쪽 상단 위치 버튼을 눌러 위치 권한을 허용해주세요.", Toast.LENGTH_SHORT)
                toast.show()
            }
        }
    }

    private fun showMapLabels(labels : List<LocationResponse>) {
        val labelOptionsList = mutableListOf<LabelOptions>()

        val styles = LabelStyles.from(LabelStyle.from(R.drawable.icon_map_marker))

        for (label in labels) {
            val labelOptions = LabelOptions.from(LatLng.from(label.latitude, label.longitude))

            labelOptions.labelId = label.hpID
            labelOptions.styles = styles

            labelOptionsList.add(labelOptions)
        }

        val layer = kakaoMap.labelManager?.lodLayer!!
        layer.addLodLabels(labelOptionsList)
    }

    private fun showERPreview(
        hpID: String,
        lat: Double,
        lon: Double
    ) {
        val api = ApiService.create()

        val call = api.getERPreview(hpID = hpID, lat = lat, lon = lon)
        call.enqueue(object: Callback<ERPreview> {
            override fun onResponse(call: Call<ERPreview>, response: Response<ERPreview>) {
                if (response.isSuccessful) {
                    val erPreview = response.body()!!

                    bindERPreview(erPreview)
                }
            }

            override fun onFailure(call: Call<ERPreview>, t: Throwable) {}
        })
    }

    private fun bindERPreview(erPreview: ERPreview) {
        binding.preViewName.text = erPreview.name.trim()
        binding.preViewAddress.text = erPreview.address
        binding.preViewTel.text = erPreview.tel
        binding.preViewBed.text = erPreview.bedCount.toString()
        binding.preViewBedTime.text = erPreview.bedTime
        binding.preViewDistance.text = "${erPreview.distance}km"
        binding.preViewDuration.text = erPreview.duration
        binding.preViewEmergencyTel.text = erPreview.ertel

        //ems 설정
        val nameLength = binding.preViewName.text.toString().length
        val addressLength = binding.preViewAddress.text.toString().length

        if (nameLength >= 7) {
            binding.preViewName.setEms(7)
        }

        if (addressLength >= 11) {
            binding.preViewAddress.setEms(11)
        }

        //자세히보기시 클릭시 이동
        binding.preViewDetail.setOnClickListener {
            if (checkLocationPermission()) {
                //위치를 받아서 프리뷰를 보여준다.
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        if (location != null) {
                            val hpID = erPreview.hpID
                            val lat = location.latitude
                            val lon = location.longitude

                            //응급실 상세보기 정보 viewModel에 저장
                            setDetailData(
                                hpID = hpID,
                                lat = lat,
                                lon = lon
                            )
                        }
                    }
                    .addOnFailureListener {}
            }
        }

        //다이얼 이동 처리
        binding.preViewTel.setOnClickListener {
            val tel = erPreview.tel.replace("-", "")
            openDial(requireContext(), tel)
        }

        binding.preViewEmergencyTel.setOnClickListener {
            val erTel = erPreview.ertel.replace("-", "")
            openDial(requireContext(), erTel)
        }
    }

    private fun setDetailData(
        hpID: String,
        lat: Double,
        lon: Double
    ) {
        val api = ApiService.create()

        val call = api.getERDetailData(
            hpID = hpID,
            lat = lat,
            lon = lon
        )

        call.enqueue(object: Callback<ERDetail> {
            override fun onResponse(call: Call<ERDetail>, response: Response<ERDetail>) {
                if (response.isSuccessful) {
                    val erDetailData = response.body()!!

                    erViewModel.setERDetailData(erDetailData)

                    findNavController().navigate(R.id.action_mapFragment_to_ERDetailFragment)
                }
            }

            override fun onFailure(call: Call<ERDetail>, t: Throwable) {}
        })
    }

    //서버에서 받아온 label에 대한 정보를 sharedPreference에 저장
    private fun saveEmergencyLabels() {
        val api = ApiService.create()

        val call = api.getERAll()
        call.enqueue(object: Callback<List<LocationResponse>> {
            override fun onResponse(
                call: Call<List<LocationResponse>>,
                response: Response<List<LocationResponse>>
            ) {
                if (response.isSuccessful) {
                    val labels = response.body() ?: emptyList()

                    saveLabels(requireContext(), labels)
                }
            }

            override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {}
        })
    }

    private fun searchName(keyword: String) {
        val client = KakaoApiClient.getClient().create(KakaoApiService::class.java)
        val call = client.searchAddress("KakaoAK ${BuildConfig.KAKAO_REST_KEY}", keyword)

        call.enqueue(object: Callback<AddressResponse> {
            override fun onResponse(
                call: Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                if (response.isSuccessful) {
                    val keywordResponse = response.body()!!

                    if (keywordResponse.documents.isNotEmpty()) {
                        val lat = keywordResponse.documents[0].y.toDouble()
                        val lon = keywordResponse.documents[0].x.toDouble()

                        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon))
                        kakaoMap.moveCamera(cameraUpdate)

                        binding.searchEditText.text.clear()

                        binding.searchEditText.clearFocus()
                    }
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {}
        })
    }

    private fun checkLocationPermission() : Boolean {
        return (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    private fun requestLocationPermission() {
        if (checkLocationPermission()) {
            setCenterCurrentLocation()
        } else {
            requestLocationPermissionMode()
        }
    }

    @SuppressLint("MissingPermission")
    private fun setCenterCurrentLocation() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude

                    val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon))

                    kakaoMap.moveCamera(cameraUpdate)
                }
            }
            .addOnFailureListener {}
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED
            ) {
                setCenterCurrentLocation()
            } else {
                requestLocationPermissionMode()
            }
        }
    }

    private fun requestLocationPermissionMode() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) || ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            Toast.makeText(context, "위치 권한을 허용해주세요.", Toast.LENGTH_SHORT).show()
            showAppSettings()
        } else {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun showAppSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", requireActivity().packageName, null)

        intent.data = uri

        startActivity(intent)
    }
}