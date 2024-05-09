package com.gretea5.finder.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gretea5.finder.BuildConfig
import com.gretea5.finder.data.KakaoApiClient
import com.gretea5.finder.data.KakaoApiService
import com.gretea5.finder.data.model.AddressResponse
import com.gretea5.finder.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraUpdateFactory
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
            override fun onMapDestroy() {
                //TODO("Not yet implemented")
                Log.d("KakaoMap", "onMapDestroy");
            }

            override fun onMapError(p0: Exception?) {
                //TODO("Not yet implemented")
                Log.d("KakaoMap", "onMapError");
            }

        }, object : KakaoMapReadyCallback() {
            override fun onMapReady(p0: KakaoMap) {
                _kakaoMap = p0
                Log.d("KakaoMap", "onMapReady");
            }
        })

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchIconImageView.setOnClickListener {
            if (binding.searchEditText.toString().isNotEmpty()) {
                Log.d("searchKeyword", "setOnClickListener")
                searchName(binding.searchEditText.text.toString())
            }
        }
    }

    private fun searchName(keyword: String) {
        val client = KakaoApiClient.getClient().create(KakaoApiService::class.java)
        val call = client.searchAddress("KakaoAK ${BuildConfig.KAKAO_REST_KEY}", keyword)

        Log.d("searchKeyword", keyword)

        call.enqueue(object: Callback<AddressResponse> {
            override fun onResponse(
                call: Call<AddressResponse>,
                response: Response<AddressResponse>
            ) {
                if (response.isSuccessful) {
                    val keywordResponse = response.body()!!

                    if (keywordResponse.documents.isNotEmpty()) {
                        val lat = keywordResponse.documents.get(0).y.toDouble()
                        val lon = keywordResponse.documents.get(0).x.toDouble()

                        val cameraUpdate = CameraUpdateFactory.newCenterPosition(LatLng.from(lat, lon))
                        kakaoMap.moveCamera(cameraUpdate)
                    }
                }
            }

            override fun onFailure(call: Call<AddressResponse>, t: Throwable) {
                Log.d("searchKeyword", call.toString())
                Log.d("searchKeyword", t.toString())
            }
        })
    }
}