package com.gretea5.finder.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gretea5.finder.BuildConfig
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.KakaoApiClient
import com.gretea5.finder.data.KakaoApiService
import com.gretea5.finder.data.model.AddressResponse
import com.gretea5.finder.data.model.LocationResponse
import com.gretea5.finder.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
import com.kakao.vectormap.label.LabelOptions
import com.kakao.vectormap.label.LabelStyle
import com.kakao.vectormap.label.LabelStyles
import com.kakao.vectormap.label.LodLabelLayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception


class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

    private var _kakaoMap: KakaoMap? = null
    private val kakaoMap get() = _kakaoMap!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapBinding.inflate(inflater)

        binding.mapView.start(object : MapLifeCycleCallback() {
            override fun onMapDestroy() {}
            override fun onMapError(p0: Exception?) {}

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                _kakaoMap = p0

                kakaoMap.setOnCameraMoveEndListener { kakaoMap, _, _ ->
                    val swPos = kakaoMap.fromScreenPoint(kakaoMap.viewport.left, kakaoMap.viewport.bottom)!!
                    val nePos = kakaoMap.fromScreenPoint(kakaoMap.viewport.right, kakaoMap.viewport.top)!!

                    val swLat = swPos.latitude
                    val swLon = swPos.longitude

                    val neLat = nePos.latitude
                    val neLon = nePos.longitude

                    setERLabels(
                        swLat = swLat,
                        swLon = swLon,
                        neLat = neLat,
                        neLon = neLon
                    )
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

    private fun setERLabels(
        swLat: Double,
        swLon: Double,
        neLat: Double,
        neLon : Double) {

        val api = ApiService.create()

        val call = api.getNearByLocations(swLat, swLon, neLat, neLon)
        call.enqueue(object : Callback<List<LocationResponse>> {
            override fun onResponse(
                call: Call<List<LocationResponse>>,
                response: Response<List<LocationResponse>>
            ) {
                if (response.isSuccessful) {
                    val locations = response.body()
                    val labelOptionsList = mutableListOf<LabelOptions>()

                    val styles = LabelStyles.from(LabelStyle.from(R.drawable.icon_map_marker))

                    locations?.let {
                        for (location in locations) {
                            val labelOptions = LabelOptions.from(
                                LatLng.from(location.latitude, location.longitude)
                            )

                            labelOptions.styles = styles
                            labelOptions.labelId = location.hpID

                            labelOptionsList.add(labelOptions)
                        }
                    }

                    val layer: LodLabelLayer = kakaoMap.labelManager?.lodLayer!!

                    val label = layer.addLodLabels(labelOptionsList)
                }
            }

            override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {}
        })
    }
}