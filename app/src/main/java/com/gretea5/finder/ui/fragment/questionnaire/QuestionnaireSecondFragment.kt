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
    private var _binding : FragmentQuestionnaireSecondBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController
    private val viewModel: QuestionnaireViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaireSecondBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_rh_plus)) == true) {
            binding.rhRadioGroup.check(R.id.rhPlusBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_rh_minus)) == true) {
            binding.rhRadioGroup.check(R.id.rhMinusBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_a)) == true) {
            binding.bloodRadioGroup.check(R.id.aBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_b)) == true) {
            binding.bloodRadioGroup.check(R.id.bBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_o)) == true) {
            binding.bloodRadioGroup.check(R.id.oBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_ab)) == true) {
            binding.bloodRadioGroup.check(R.id.abBtn)
        }

        binding.rhRadioGroup.setOnCheckedChangeListener { _, checkedId  ->
            val rhType = when (checkedId) {
                R.id.rhPlusBtn -> getString(R.string.blood_rh_plus)
                R.id.rhMinusBtn -> getString(R.string.blood_rh_minus)
                else -> getString(R.string.empty_string)
            }
            val bloodType = when (binding.bloodRadioGroup.checkedRadioButtonId) {
                R.id.aBtn -> getString(R.string.blood_a)
                R.id.bBtn -> getString(R.string.blood_b)
                R.id.abBtn -> getString(R.string.blood_ab)
                R.id.oBtn -> getString(R.string.blood_o)
                else -> getString(R.string.empty_string)
            }
            viewModel.onBloodTypeSelected(rhType, bloodType)
        }

        binding.bloodRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val rhType = when (binding.rhRadioGroup.checkedRadioButtonId) {
                R.id.rhPlusBtn -> getString(R.string.blood_rh_plus)
                R.id.rhMinusBtn -> getString(R.string.blood_rh_minus)
                else -> getString(R.string.empty_string)
            }
            val bloodType = when (checkedId) {
                R.id.aBtn -> getString(R.string.blood_a)
                R.id.bBtn -> getString(R.string.blood_b)
                R.id.abBtn -> getString(R.string.blood_ab)
                R.id.oBtn -> getString(R.string.blood_o)
                else -> getString(R.string.empty_string)
            }
            viewModel.onBloodTypeSelected(rhType, bloodType)
        }

        when(viewModel.allergy.value) {
            //해당 없음인 값인 경우,
            getString(R.string.condition_absent) -> {
                binding.noAllergy.isChecked = true
                binding.yesAllergy.isChecked = false
                binding.allergyInfo.visibility = View.GONE
            }
            //해당 있음인 값인 경우,
            getString(R.string.condition_present) -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = true
                binding.allergyInfo.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            getString(R.string.empty_string) -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = false
                binding.allergyInfo.visibility = View.GONE
            }
            else -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = true
                binding.allergyInfo.visibility = View.VISIBLE
                binding.allergyInfo.setText(viewModel.allergy.value)
            }
        }

        binding.allergyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.yesAllergy -> {
                    viewModel.setAllergy(getString(R.string.condition_present))
                    binding.allergyInfo.visibility = View.VISIBLE
                }
                R.id.noAllergy -> {
                    viewModel.setAllergy(getString(R.string.condition_absent))
                    binding.allergyInfo.setText(getString(R.string.empty_string))
                    binding.allergyInfo.visibility = View.GONE
                }
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