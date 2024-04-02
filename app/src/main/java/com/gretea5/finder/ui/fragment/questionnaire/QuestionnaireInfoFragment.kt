package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.model.QuestionnaireUnlinkModel
import com.gretea5.finder.databinding.FragmentQuestionnaireInfoBinding
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


//        viewModel.name.observe(viewLifecycleOwner, Observer {
//            binding.qnInfoName.text = it
//        })
//
//        viewModel.age.observe(viewLifecycleOwner, Observer {
//            binding.qnInfoAge.text = "${it}세"
//        })
//
//        viewModel.gender.observe(viewLifecycleOwner, Observer {
//            binding.qnInfoGender.text = "(${it})"
//        })

        val phoneNumber = getPhoneNumber()

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
            setUpdateMode()
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

    private fun setUpdateMode() {
        //전화번호 getPreferences 저장
        val sharedPref = requireActivity().getSharedPreferences("pref", 0)
        val sharedEditor = sharedPref.edit()
        sharedEditor.putBoolean(getString(R.string.edit_mode), true)
        sharedEditor.apply()
    }

    private fun getPhoneNumber(): String {
        val pref = requireActivity().getSharedPreferences("pref", 0)

        return pref.getString(getString(R.string.phonenumber_key), "") ?: ""
    }

    private fun deleteQuestionnaire(phoneNumber: String) {
        api.deleteQuestionnaire(phoneNumber).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    Log.d("deleteQuestionnaire", response.code().toString())
                    Log.d("deleteQuestionnaire", response.body().toString())
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