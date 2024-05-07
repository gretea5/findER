package com.gretea5.finder.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gretea5.finder.databinding.FragmentMapBinding
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.MapLifeCycleCallback
import java.lang.Exception


class MapFragment : Fragment() {
    private var _binding: FragmentMapBinding? = null
    private val binding get() = _binding!!

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
                //TODO("Not yet implemented")
                Log.d("KakaoMap", "onMapReady");
            }
        })

        return binding.root
    }
}