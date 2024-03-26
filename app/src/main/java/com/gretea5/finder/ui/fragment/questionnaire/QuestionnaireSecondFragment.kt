package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
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
import com.gretea5.finder.databinding.FragmentQuestionnaireSecondBinding
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireSecondFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireSecondBinding
    private lateinit var navController: NavController
    private val viewModel: QuestionnaireViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireSecondBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        if(viewModel.bloodType.value?.contains ("Rh+") == true) {
            binding.rhRadioGroup.check(R.id.rhPlusBtn)
        }

        if(viewModel.bloodType.value?.contains ("Rh-") == true) {
            binding.rhRadioGroup.check(R.id.rhMinusBtn)
        }

        if(viewModel.bloodType.value?.contains("A") == true) {
            binding.bloodRadioGroup.check(R.id.aBtn )
        }

        if(viewModel.bloodType.value?.contains("B") == true) {
            binding.bloodRadioGroup.check(R.id.bBtn)
        }

        if(viewModel.bloodType.value?.contains("O") == true) {
            binding.bloodRadioGroup.check(R.id.oBtn)
        }

        if(viewModel.bloodType.value?.contains("AB") == true) {
            binding.bloodRadioGroup.check(R.id.abBtn)
        }

        binding.rhRadioGroup.setOnCheckedChangeListener { _, checkedId  ->
            val rhType = when (checkedId) {
                R.id.rhPlusBtn -> "Rh+"
                R.id.rhMinusBtn -> "Rh-"
                else -> ""
            }
            val bloodType = when (binding.bloodRadioGroup.checkedRadioButtonId) {
                R.id.aBtn -> "A"
                R.id.bBtn -> "B"
                R.id.abBtn -> "AB"
                R.id.oBtn -> "O"
                else -> ""
            }
            viewModel.onBloodTypeSelected(rhType, bloodType)
        }

        binding.bloodRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val rhType = when (binding.rhRadioGroup.checkedRadioButtonId) {
                R.id.rhPlusBtn -> "Rh+"
                R.id.rhMinusBtn -> "Rh-"
                else -> ""
            }
            val bloodType = when (checkedId) {
                R.id.aBtn -> "A"
                R.id.bBtn -> "B"
                R.id.abBtn -> "AB"
                R.id.oBtn -> "O"
                else -> ""
            }
            viewModel.onBloodTypeSelected(rhType, bloodType)
        }

        when(viewModel.allergy.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.noAllergy.isChecked = true
                binding.yesAllergy.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = false
            }
            else -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = true
                binding.allergyInfo.setText(viewModel.allergy.value)
            }
        }

        binding.allergyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.yesAllergy -> { viewModel.setAllergy("O") }
                R.id.noAllergy -> { viewModel.setAllergy("X") }
            }
        }

        binding.allergyInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setAllergy(it.toString())
            }
        }


        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            })

        binding.qnSecondBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        binding.qnSecondNextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionnaireSecondFragment_to_questionnaireThirdFragment)
        }
    }
}