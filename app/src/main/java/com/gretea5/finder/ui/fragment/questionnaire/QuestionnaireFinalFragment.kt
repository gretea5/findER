package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
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
    private lateinit var binding : FragmentQuestionnaireFinalBinding
    private lateinit var navController: NavController

    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private val api = ApiService.create()

    private val qnFinalBeforeBtn: TextView by lazy { binding.qnFinalBeforeBtn }
    private val qnFinalCompleteBtn: TextView by lazy { binding.qnFinalCompleteBtn }

    private val smokeRadioGroup: RadioGroup by lazy { binding.smokeRadioGroup }
    private val smokeNoBtn: RadioButton by lazy { binding.smokeNoBtn }
    private val smokeYesBtn: RadioButton by lazy { binding.smokeYesBtn }
    private val smokeInfo: EditText by lazy { binding.smokeInfo }

    private val drinkRadioGroup: RadioGroup by lazy { binding.drinkRadioGroup }
    private val drinkNoBtn: RadioButton by lazy { binding.drinkNoBtn }
    private val drinkYesBtn: RadioButton by lazy { binding.drinkYesBtn }
    private val drinkInfo: EditText by lazy { binding.drinkInfo }

    private val etcRadioGroup: RadioGroup by lazy { binding.etcRadioGroup }
    private val etcNoBtn: RadioButton by lazy { binding.etcNoBtn }
    private val etcYesBtn: RadioButton by lazy { binding.etcYesBtn }
    private val etcInfo: EditText by lazy { binding.etcInfo }

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

        updateByViewModelValues()

        setListeners()
    }

    private fun updateByViewModelValues() {
        updateUIByValue(
            value = viewModel.smoke.value,
            yesBtn = smokeYesBtn,
            noBtn = smokeNoBtn,
            infoView = smokeInfo,
            inVisibleType = View.GONE
        )

        updateUIByValue(
            value = viewModel.drink.value,
            yesBtn = drinkYesBtn,
            noBtn = drinkNoBtn,
            infoView = drinkInfo,
            inVisibleType = View.GONE
        )

        updateUIByValue(
            value = viewModel.etc.value,
            yesBtn = etcYesBtn,
            noBtn = etcNoBtn,
            infoView = etcInfo,
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
        qnFinalBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        qnFinalCompleteBtn.text = if (getUpdateMode(requireActivity()))
            getString(R.string.update)
        else
            getString(R.string.complete)

        //수정/완료 버튼 클릭시
        qnFinalCompleteBtn.setOnClickListener {
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
            radioGroup = smokeRadioGroup,
            yesButton = smokeYesBtn,
            noButton = smokeNoBtn,
            infoEditText = smokeInfo,
            visibleView = smokeInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setSmoke(it) }
        )

        setEditTextListener(
            editText = smokeInfo,
            viewModelSetter = { viewModel.setSmoke(it) }
        )

        setRadioGroupListener(
            radioGroup = drinkRadioGroup,
            yesButton = drinkYesBtn,
            noButton = drinkNoBtn,
            infoEditText = drinkInfo,
            visibleView = drinkInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setDrink(it) }
        )

        setEditTextListener(
            editText = drinkInfo,
            viewModelSetter = { viewModel.setDrink(it) }
        )

        setRadioGroupListener(
            radioGroup = etcRadioGroup,
            yesButton = etcYesBtn,
            noButton = etcNoBtn,
            infoEditText = etcInfo,
            visibleView = etcInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setEtc(it) }
        )

        setEditTextListener(
            editText = etcInfo,
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