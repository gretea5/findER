package com.gretea5.finder.ui.fragment

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentErDetailBinding
import com.gretea5.finder.ui.viewmodel.ERViewModel
import com.gretea5.finder.util.dial.DialUtil.openDial
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import java.lang.Exception

class ERDetailFragment : Fragment() {
    private var _binding : FragmentErDetailBinding? = null
    private val binding get() = _binding!!

    private var _kakaoMap : KakaoMap? = null
    private val kakaoMap get() = _kakaoMap!!

    private lateinit var _fusedLocationClient : FusedLocationProviderClient
    private val fusedLocationClient get() = _fusedLocationClient!!

    private val erViewModel : ERViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErDetailBinding.inflate(inflater)
        _fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

        binding.detailMapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}

            override fun onMapError(p0: Exception?) {}

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                _kakaoMap = p0

                val lat = erViewModel.erDetailData.value?.latitude!!
                val lon = erViewModel.erDetailData.value?.longitude!!

                //마커 찍기
                val styles = LabelStyles.from(LabelStyle.from(R.drawable.icon_map_marker))

                val options = LabelOptions.from(LatLng.from(lat, lon)).setStyles(styles)

                val layer = kakaoMap.labelManager?.lodLayer
                layer?.addLodLabel(options)

                //위도 경도 중앙에 찍기
                val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon))
                kakaoMap.moveCamera(cameraUpdate)
            }
        })

        var name = erViewModel.erDetailData.value?.name!!
        var address = erViewModel.erDetailData.value?.address!!
        val mapAddress = erViewModel.erDetailData.value?.mapAddress!!
        val tel = erViewModel.erDetailData.value?.tel!!
        val erTel = erViewModel.erDetailData.value?.ertel!!
        val bedCount = erViewModel.erDetailData.value?.bedCount!!.toString()
        val bedTime = erViewModel.erDetailData.value?.bedTime!!
        val distance = erViewModel.erDetailData.value?.distance!!
        val duration = erViewModel.erDetailData.value?.duration!!
        val ambulance = erViewModel.erDetailData.value?.ambulance!!
        val ct = erViewModel.erDetailData.value?.ct!!
        val mri = erViewModel.erDetailData.value?.mri!!
        val subject = erViewModel.erDetailData.value?.subject!!

        //이름 주소 ems 처리
        if (name.length >= 15) {
            name = "${name.substring(0, 16)}..."
        }

        if (address.length >= 17) {
            address = "${address.substring(0, 18)}..."
        }

        //mapAddress가 비었을 시, 안보이게 처리
        if (mapAddress.isEmpty()) {
            binding.mapAddressArea.visibility = View.GONE
        }

        val mainColor = ContextCompat.getColor(requireContext(), R.color.main_color)
        val redColor = ContextCompat.getColor(requireContext(), R.color.red)

        //구급차 가용 여부가 될 때,
        if (ambulance == "Y") {
            binding.erDetailUsable.text = "구급차 가용 가능"
            binding.erDetailUsable.setTextColor(mainColor)

            binding.erDetailImage.setColorFilter(mainColor)
        }

        //구급차 가용 여부가 되지 않을 때,
        if (ambulance != "Y") {
            binding.erDetailUsable.text = "구급차 가용 불가"
            binding.erDetailUsable.setTextColor(redColor)

            binding.erDetailImage.setColorFilter(redColor)
        }

        //ct 여부
        if (ct == "Y") {
            binding.ctDetailTitleTextView.setTextColor(mainColor)
            binding.ctDetailTextView.setTextColor(mainColor)

            binding.ctDetailTextView.text = "O"
        }

        if (ct != "Y") {
            binding.ctDetailTitleTextView.setTextColor(redColor)
            binding.ctDetailTextView.setTextColor(redColor)

            binding.ctDetailTextView.text = "X"
        }

        //mri
        if (mri == "Y") {
            binding.mriDetailTitleTextView.setTextColor(mainColor)
            binding.mriDetailTextView.setTextColor(mainColor)

            binding.mriDetailTextView.text = "O"
        }

        if (mri != "Y") {
            binding.mriDetailTitleTextView.setTextColor(redColor)
            binding.mriDetailTextView.setTextColor(redColor)

            binding.mriDetailTextView.text = "X"
        }


        binding.erName.text = name
        binding.detailAddress.text = address
        binding.detailMapAddress.text = mapAddress
        binding.detailTel.text = tel
        binding.detailErTel.text = erTel
        binding.detailBed.text = bedCount
        binding.detailBedTime.text = bedTime
        binding.detailDuration.text = duration
        binding.detailDistance.text = "${distance}km"
        binding.detailSubject.text = subject

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.addressCopyBtn.setOnClickListener {
            val address = erViewModel.erDetailData.value?.address!!

            val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("label", address)

            clipboard.setPrimaryClip(clip)

            Toast.makeText(requireContext(), "주소 복사 성공!", Toast.LENGTH_SHORT).show()
        }

        binding.kakaoMapBtn.setOnClickListener {
            if (checkLocationPermission()) {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location ->
                        val startLat = location.latitude
                        val startLon = location.longitude

                        val endLat = erViewModel.erDetailData.value?.latitude!!
                        val endLon = erViewModel.erDetailData.value?.longitude!!

                        openKakaoMapForNavigation(
                            startLat = startLat,
                            startLon = startLon,
                            endLat = endLat,
                            endLon = endLon
                        )
                    }
                    .addOnFailureListener {}
            }
        }

        //다이얼 이동 처리
        binding.detailTel.setOnClickListener {
            val tel = erViewModel.erDetailData.value?.tel?.replace("-", "")?.trim()!!
            openDial(requireContext(), tel)
        }

        binding.detailErTel.setOnClickListener {
            val erTel = erViewModel.erDetailData.value?.ertel?.replace("-", "")?.trim()!!
            openDial(requireContext(), erTel)
        }
    }

    override fun onResume() {
        super.onResume()

        binding.detailMapView.resume()
    }

    override fun onPause() {
        super.onPause()

        binding.detailMapView.pause()
    }

    @SuppressLint("MissingPermission")
    private fun openKakaoMapForNavigation(
        startLat: Double,
        startLon: Double,
        endLat: Double,
        endLon: Double
    ) {
        val url = "kakaomap://route?sp=$startLat,$startLon&ep=$endLat,$endLon&by=CAR"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))

        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            // 카카오맵 앱이 없는 경우, 구글 플레이 스토어로 이동
            val playStoreIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("market://details?id=net.daum.android.map")
            )

            startActivity(playStoreIntent)
        }
    }

    private fun checkLocationPermission() : Boolean {
        return (ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }
}