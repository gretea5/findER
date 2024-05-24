package com.gretea5.finder.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentErDetailBinding
import com.gretea5.finder.ui.viewmodel.ERViewModel
import com.kakao.vectormap.KakaoMap
import com.kakao.vectormap.KakaoMapReadyCallback
import com.kakao.vectormap.LatLng
import com.kakao.vectormap.MapLifeCycleCallback
import com.kakao.vectormap.camera.CameraAnimation
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

    private val erViewModel : ERViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentErDetailBinding.inflate(inflater)

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

        //이름 ems 처리
        var name = erViewModel.erDetailData.value?.name

        if (name?.length!! >= 15) {
            name = "${name.substring(0, 16)}..."
        }

        binding.erName.text = name

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
    }

    override fun onResume() {
        super.onResume()

        binding.detailMapView.resume()
    }

    override fun onPause() {
        super.onPause()

        binding.detailMapView.pause()
    }
}