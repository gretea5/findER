package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.databinding.FragmentQuestionnaireFinalBinding
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.getPhoneNumber
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.getUpdateMode
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.offUpdateMode
import com.gretea5.finder.util.view.ViewEventUtils.setRadioGroupListener
import com.gretea5.finder.util.view.ViewEventUtils.setEditTextListener
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireFinalFragment : Fragment() {
    private var _binding: FragmentQuestionnaireFinalBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaireFinalBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        updateByViewModelValues()

        setListeners()
    }

    private fun updateByViewModelValues() {
        updateUIByValue(
            value = viewModel.smoke.value,
            yesBtn = binding.smokeYesBtn,
            noBtn = binding.smokeNoBtn,
            infoView = binding.smokeInfo,
            inVisibleType = View.GONE
        )

        updateUIByValue(
            value = viewModel.drink.value,
            yesBtn = binding.drinkYesBtn,
            noBtn = binding.drinkNoBtn,
            infoView = binding.drinkInfo,
            inVisibleType = View.GONE
        )

        updateUIByValue(
            value = viewModel.etc.value,
            yesBtn = binding.etcYesBtn,
            noBtn = binding.etcNoBtn,
            infoView = binding.etcInfo,
            inVisibleType = View.GONE
        )
    }

    private fun updateUIByValue(
        value: String?,
        yesBtn: RadioButton,
        noBtn: RadioButton,
        infoView: EditText,
        inVisibleType: Int) {
        when (value) {
            "X" -> {
                noBtn.isChecked = true
                yesBtn.isChecked = false
                infoView.visibility = inVisibleType
            }
            "O" -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                noBtn.isChecked = false
                yesBtn.isChecked = false
                infoView.visibility = inVisibleType
            }
            else -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.setText(value)
                infoView.visibility = View.VISIBLE
            }
        }
    }

    private fun setListeners() {
        setToolBarListener()
        setBackButtonListener()
        setInputListener()
    }

    private fun setToolBarListener() {
        //이전 버튼 클릭시
        binding.qnFinalBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        binding.qnFinalCompleteBtn.text = if (getUpdateMode(requireActivity()))
            getString(R.string.update)
        else
            getString(R.string.complete)

        //수정/완료 버튼 클릭시
        binding.qnFinalCompleteBtn.setOnClickListener {
            if(getUpdateMode(requireActivity()))  {
                updateQuestionnaire()
            }
            else {
                postQuestionnaire()
            }
        }
    }

    private fun setBackButtonListener() {
        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            })
    }

    private fun setInputListener() {
        setRadioGroupListener(
            radioGroup = binding.smokeRadioGroup,
            yesButton = binding.smokeYesBtn,
            noButton = binding.smokeNoBtn,
            infoEditText = binding.smokeInfo,
            visibleView = binding.smokeInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setSmoke(it) }
        )

        setEditTextListener(
            editText = binding.smokeInfo,
            viewModelSetter = { viewModel.setSmoke(it) }
        )

        setRadioGroupListener(
            radioGroup = binding.drinkRadioGroup,
            yesButton = binding.drinkYesBtn,
            noButton = binding.drinkNoBtn,
            infoEditText = binding.drinkInfo,
            visibleView = binding.drinkInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setDrink(it) }
        )

        setEditTextListener(
            editText = binding.drinkInfo,
            viewModelSetter = { viewModel.setDrink(it) }
        )

        setRadioGroupListener(
            radioGroup = binding.etcRadioGroup,
            yesButton = binding.etcYesBtn,
            noButton = binding.etcNoBtn,
            infoEditText = binding.etcInfo,
            visibleView = binding.etcInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setEtc(it) }
        )

        setEditTextListener(
            editText = binding.etcInfo,
            viewModelSetter = { viewModel.setEtc(it) }
        )
    }

    private fun updateQuestionnaire() {
        val questionnaireModel = viewModel.getQuestionnaireModel()

        api.updateQuestionnaire(questionnaireModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                navController.navigate(R.id.action_questionnaireFinalFragment_to_questionnaireFragment)

                viewModel.resetViewModelData()

                offUpdateMode(requireActivity())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun postQuestionnaire() {
        viewModel.setPhoneNumber(getPhoneNumber(requireActivity()))

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
}