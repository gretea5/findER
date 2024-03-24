package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireFinalBinding
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireFinalFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireFinalBinding
    private lateinit var navController: NavController
    private val viewModel: QuestionnaireViewModel by activityViewModels()

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

        //viewmodel 흡연여부 값에 따른 UI 갱신
        when(viewModel.smoke.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.smokeNoBtn.isChecked = true
                binding.smokeYesBtn.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.smokeNoBtn.isChecked = false
                binding.smokeYesBtn.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.smokeNoBtn.isChecked = false
                binding.smokeYesBtn.isChecked = false
            }
            else -> {
                binding.smokeNoBtn.isChecked = false
                binding.smokeYesBtn.isChecked = true
                binding.smokeInfo.setText(viewModel.smoke.value)
            }
        }

        binding.smokeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.smokeYesBtn -> { viewModel.setSmoke("O") }
                R.id.smokeNoBtn -> {
                    binding.smokeInfo.setText("")
                    viewModel.setSmoke("X")
                }
            }
        }

        binding.smokeInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setSmoke(it.toString())
            }
        }

        //viewmodel 수술 이력 값에 따른 UI 갱신
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

        //viewModel 수술 이력 값에 따른 UI 갱신
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

        //수술 이력 UI 이벤트에 따른 viewModel 갱신
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

        //다음 버튼 클릭시
        binding.qnFinalCompleteBtn.setOnClickListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)

            viewModel.resetViewModelData()
        }
    }
}