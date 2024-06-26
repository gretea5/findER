package com.gretea5.finder.ui.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Paint
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.model.QuestionnaireLinkModel
import com.gretea5.finder.databinding.FragmentQuestionnaireLinkBinding
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.getPhoneNumber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireLinkFragment : Fragment() {
    private var _binding : FragmentQuestionnaireLinkBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController

    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaireLinkBinding.inflate(inflater)

        fetchSerialNumber()
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.requestQnLinkBtn.setOnClickListener {
            linkQuestionnaire()
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        })

        //연동 취소 버튼
        binding.qnLinkCancelBtn.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun fetchSerialNumber() {
        val phoneNumber = getPhoneNumber(requireActivity())

        api.getSerialNumber(phoneNumber).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    val color = ContextCompat.getColor(requireContext(), R.color.main_color)

                    binding.serialNumberCopyBtn.setColorFilter(color)

                    val serialNumber = response.body().toString()

                    binding.mySerialNumber.text = serialNumber

                    binding.serialNumberCopyBtn.setOnClickListener {
                        val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("label", serialNumber)

                        clipboard.setPrimaryClip(clip)

                        Toast.makeText(requireContext(), "복사 성공!", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })
    }

    private fun linkQuestionnaire() {
        val phoneNumber = getPhoneNumber(requireActivity())
        val linkedSerialNumber = binding.etOtherPersonSerialNumber.text.toString()

        val questionnaireLinkModel = QuestionnaireLinkModel(phoneNumber, linkedSerialNumber)

        api.linkQuestionnaire(questionnaireLinkModel).enqueue(object: Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })
    }
}