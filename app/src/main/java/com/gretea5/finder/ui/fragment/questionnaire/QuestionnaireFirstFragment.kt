package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireFirstBinding
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireFirstFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireFirstBinding
    private lateinit var navController : NavController
    private lateinit var viewModel: QuestionnaireViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionnaireFirstBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        viewModel = ViewModelProvider(requireActivity()).get(QuestionnaireViewModel::class.java)

        //viewModel 값 설정
        binding.qnName.setText(viewModel.questionnaireInfo.name)
        binding.qnAge.setText(viewModel.questionnaireInfo.age)
        binding.qnAddress.setText(viewModel.questionnaireInfo.address)

        when(viewModel.questionnaireInfo.gender) {
            "남성" -> {
                binding.qnMale.isChecked = true
                binding.qnFemale.isChecked = false
            }
            "여성" -> {
                binding.qnMale.isChecked = false
                binding.qnFemale.isChecked = true
            }
            else -> {
                binding.qnMale.isChecked = false
                binding.qnFemale.isChecked = false
            }
        }

        binding.qnName.addTextChangedListener {
            viewModel.questionnaireInfo.name = binding.qnName.toString()
        }

        binding.qnAddress.addTextChangedListener {
            viewModel.questionnaireInfo.address = binding.qnAddress.toString()
        }

        binding.qnAge.addTextChangedListener {
            viewModel.questionnaireInfo.address = binding.qnAddress.toString()
        }

        when(binding.genderRadioGroup.checkedRadioButtonId) {
            R.id.qnMale -> { viewModel.questionnaireInfo.gender = "남성"}
            R.id.qnFemale -> { viewModel.questionnaireInfo.gender = "여성"}
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
            }
        })

        //이전 버튼 클릭시
        binding.qnFirstBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        //다음 버튼 클릭시
        binding.qnFirstNextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionnaireFirstFragment_to_questionnaireSecondFragment)
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.resetQuestionnaireInfo()
    }
}