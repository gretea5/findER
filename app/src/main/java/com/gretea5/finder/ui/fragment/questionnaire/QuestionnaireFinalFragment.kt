package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.databinding.FragmentQuestionnaireFinalBinding
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireFinalFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireFinalBinding
    private lateinit var navController: NavController

    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireFinalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        when(viewModel.smoke.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.smokeNoBtn.isChecked = true
                binding.smokeYesBtn.isChecked = false
                binding.smokeInfo.visibility = View.GONE
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.smokeNoBtn.isChecked = false
                binding.smokeYesBtn.isChecked = true
                binding.smokeInfo.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.smokeNoBtn.isChecked = false
                binding.smokeYesBtn.isChecked = false
                binding.smokeInfo.visibility = View.GONE
            }
            else -> {
                binding.smokeNoBtn.isChecked = false
                binding.smokeYesBtn.isChecked = true
                binding.smokeInfo.visibility = View.VISIBLE
                binding.smokeInfo.setText(viewModel.smoke.value)
            }
        }

        //흡연여부
        binding.smokeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.smokeYesBtn -> {
                    viewModel.setSmoke("O")
                    binding.smokeInfo.visibility = View.VISIBLE
                }
                R.id.smokeNoBtn -> {
                    viewModel.setSmoke("X")
                    binding.smokeInfo.visibility = View.GONE
                    binding.smokeInfo.setText("")
                }
            }
        }

        binding.smokeInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setSmoke(it.toString())
            }
        }

        when(viewModel.drink.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.drinkNoBtn.isChecked = true
                binding.drinkYesBtn.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.drinkNoBtn.isChecked = false
                binding.drinkYesBtn.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.drinkNoBtn.isChecked = false
                binding.drinkYesBtn.isChecked = false
            }
            else -> {
                binding.drinkNoBtn.isChecked = false
                binding.drinkYesBtn.isChecked = true
                binding.drinkInfo.setText(viewModel.drink.value)
            }
        }

        binding.drinkRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.drinkYesBtn -> { viewModel.setDrink("O") }
                R.id.drinkNoBtn -> {
                    binding.drinkInfo.setText("")
                    viewModel.setDrink("X")
                }
            }
        }

        binding.drinkInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setDrink(it.toString())
            }
        }

        when(viewModel.etc.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.etcNoBtn.isChecked = true
                binding.etcYesBtn.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.etcNoBtn.isChecked = false
                binding.etcYesBtn.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.etcNoBtn.isChecked = false
                binding.etcYesBtn.isChecked = false
            }
            else -> {
                binding.etcNoBtn.isChecked = false
                binding.etcYesBtn.isChecked = true
                binding.etcInfo.setText(viewModel.etc.value)
            }
        }

        binding.etcRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.etcYesBtn -> { viewModel.setEtc("O") }
                R.id.etcNoBtn -> {
                    //해당 없음을 체크 했을 경우 내용 삭제
                    binding.etcInfo.setText("")

                    //"X"값으로 갱신
                    viewModel.setEtc("X")
                }
            }
        }

        binding.etcInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setEtc(it.toString())
            }
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            })

        //이전 버튼 클릭시
        binding.qnFinalBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        binding.qnFinalCompleteBtn.text = if (getUpdateMode())
            getString(R.string.update)
        else
            getString(R.string.complete)

        //수정/완료 버튼 클릭시
        binding.qnFinalCompleteBtn.setOnClickListener {
            if(getUpdateMode())  {
                updateQuestionnaire()
            }
            else {
                postQuestionnaire()
            }
        }
    }

    private fun updateQuestionnaire() {
        val questionnaireModel = viewModel.getQuestionnaireModel()

        api.updateQuestionnaire(questionnaireModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                navController.navigate(R.id.action_questionnaireFinalFragment_to_questionnaireFragment)

                viewModel.resetViewModelData()

                offUpdateMode()
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun postQuestionnaire() {
        viewModel.setPhoneNumber(getPhoneNumber())

        val questionnaireModel = viewModel.getQuestionnaireModel()

        api.writeQuestionnaire(questionnaireModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    val result = response.body()

                    navController.navigate(R.id.action_questionnaireFinalFragment_to_questionnaireFragment)

                    viewModel.resetViewModelData()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun getPhoneNumber(): String {
        val pref = requireActivity().getSharedPreferences("pref", 0)
        return pref.getString(getString(R.string.phonenumber_key), "") ?: ""
    }

    private fun getUpdateMode() : Boolean {
        val sharedPref = requireActivity().getSharedPreferences("pref", 0)
        return sharedPref.getBoolean(getString(R.string.edit_mode), false)
    }

    private fun offUpdateMode() {
        //전화번호 getPreferences 저장
        val sharedPref = requireActivity().getSharedPreferences("pref", 0)
        val sharedEditor = sharedPref.edit()
        sharedEditor.putBoolean(getString(R.string.edit_mode), false)
        sharedEditor.apply()
    }
}