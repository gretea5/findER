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
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireThirdBinding
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireThirdFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireThirdBinding
    private lateinit var navController: NavController
    private val viewModel: QuestionnaireViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireThirdBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        Log.d("QuestionnaireThirdFragment", "============================")
        Log.d("QuestionnaireThirdFragment1", viewModel.medicine.value.toString())
        Log.d("QuestionnaireThirdFragment2", viewModel.surgery.value.toString())
        Log.d("QuestionnaireThirdFragment3", viewModel.disease.value.toString())
        Log.d("QuestionnaireThirdFragment", "============================")

        //viewmodel 약값에 따른 UI 갱신
        when(viewModel.medicine.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.medicineNoBtn.isChecked = true
                binding.medicineYesBtn.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.medicineNoBtn.isChecked = false
                binding.medicineYesBtn.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.medicineNoBtn.isChecked = false
                binding.medicineYesBtn.isChecked = false
            }
            else -> {
                binding.medicineNoBtn.isChecked = false
                binding.medicineYesBtn.isChecked = true
                binding.medicineInfo.setText(viewModel.medicine.value)
            }
        }

        //약 UI 이벤트에 따른 viwwModel 갱신
        binding.medicineRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.medicineYesBtn -> { viewModel.setMedicine("O") }
                R.id.medicineNoBtn -> {
                    binding.medicineInfo.setText("")
                    viewModel.setMedicine("X")
                }
            }
        }

        //즉 빈 문자열이 들어왔을때, "O"으로 변경이 되는 문제가 존재,
        binding.medicineInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setMedicine(it.toString())
            }
        }

        //viewmodel 수술 이력 값에 따른 UI 갱신
        when(viewModel.surgery.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.surgeryNoBtn.isChecked = true
                binding.surgeryYesBtn.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.surgeryNoBtn.isChecked = false
                binding.surgeryYesBtn.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.surgeryNoBtn.isChecked = false
                binding.surgeryYesBtn.isChecked = false
            }
            else -> {
                binding.surgeryNoBtn.isChecked = false
                binding.surgeryYesBtn.isChecked = true
                binding.surgeryInfo.setText(viewModel.surgery.value)
            }
        }

        //수술 이력 UI 이벤트에 따른 viewModel 갱신
        binding.surgeryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.surgeryYesBtn -> { viewModel.setSurgery("O") }
                R.id.surgeryNoBtn -> {
                    binding.surgeryInfo.setText("")
                    viewModel.setSurgery("X")
                }
            }
        }

        binding.surgeryInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setSurgery(it.toString())
            }

        }

        //viewModel 수술 이력 값에 따른 UI 갱신
        when(viewModel.disease.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.diseaseNoBtn.isChecked = true
                binding.diseaseYesBtn.isChecked = false
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.diseaseNoBtn.isChecked = false
                binding.diseaseYesBtn.isChecked = true
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.diseaseNoBtn.isChecked = false
                binding.diseaseYesBtn.isChecked = false
            }
            else -> {
                binding.diseaseNoBtn.isChecked = false
                binding.diseaseYesBtn.isChecked = true
                binding.diseaseInfo.setText(viewModel.disease.value)
            }
        }

        //수술 이력 UI 이벤트에 따른 viewModel 갱신
        binding.diseaseRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.diseaseYesBtn -> { viewModel.setDisease("O") }
                R.id.diseaseNoBtn -> {
                    //해당 없음을 체크 했을 경우 내용 삭제
                    binding.diseaseInfo.setText("")

                    //"X"값으로 갱신
                    viewModel.setDisease("X")
                }
            }
        }

        binding.diseaseInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setDisease(it.toString())
            }
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            })

        binding.qnThirdBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        binding.qnThirdNextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionnaireThirdFragment_to_questionnaireFinalFragment)
        }
    }
}