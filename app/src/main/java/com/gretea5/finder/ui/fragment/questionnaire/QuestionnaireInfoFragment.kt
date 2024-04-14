package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.model.QuestionnaireUnlinkModel
import com.gretea5.finder.databinding.FragmentQuestionnaireInfoBinding
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.getPhoneNumber
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.setUpdateMode
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireInfoFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireInfoBinding
    private lateinit var navController : NavController

    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireInfoBinding.inflate(inflater)

        binding.qnInfoName.text = viewModel.name.value
        binding.qnInfoAge.text = "${viewModel.age.value}세"
        binding.qnInfoGender.text = "(${viewModel.gender.value})"
        binding.qnInfoPhoneNumber.text = viewModel.phoneNumber.value
        binding.qnInfoAddress.text = viewModel.address.value
        binding.qnInfoAllergy.text = viewModel.allergy.value
        binding.qnInfoMedicine.text = viewModel.medicine.value
        binding.qnInfoSurgery.text = viewModel.surgery.value
        binding.qnInfoDisease.text = viewModel.disease.value
        binding.qnInfoDrink.text = viewModel.drink.value
        binding.qnInfoSmoke.text = viewModel.smoke.value
        binding.qnInfoEtc.text = viewModel.etc.value

        val phoneNumber = getPhoneNumber(requireActivity())

        if(viewModel.phoneNumber.value.equals(phoneNumber)) {
            binding.qnInfoDeleteBtn.text = getString(R.string.delete)
        }
        else {
            binding.qnInfoDeleteBtn.text = getString(R.string.linkDelete)
            binding.qnInfoUpdateBtn.visibility = View.INVISIBLE
        }

        binding.qnInfoDeleteBtn.setOnClickListener {
            if (viewModel.phoneNumber.value.equals(phoneNumber)) {
                deleteQuestionnaire(phoneNumber)
            }
            else {
                unLinkQuestionnaire(phoneNumber, viewModel.phoneNumber.value.toString())
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()


        binding.qnInfoUpdateBtn.setOnClickListener {
            setUpdateMode(requireActivity())
            navController.navigate(R.id.action_questionnaireInfoFragment_to_questionnaireFirstFragment)
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                    viewModel.resetViewModelData()
                }
            })
    }



    private fun deleteQuestionnaire(phoneNumber: String) {
        api.deleteQuestionnaire(phoneNumber).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    navController.navigateUp()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })
    }

    private fun unLinkQuestionnaire(phoneNumber1: String, phoneNumber2: String) {
        val data = QuestionnaireUnlinkModel(phoneNumber1, phoneNumber2)

        api.unLinkQuestionnaire(data).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    navController.navigateUp()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })
    }
}