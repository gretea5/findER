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
import com.gretea5.finder.databinding.FragmentQuestionnaireFirstBinding
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireFirstFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireFirstBinding
    private lateinit var navController : NavController
    private val viewModel: QuestionnaireViewModel by activityViewModels()

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

        //viewModel 값 설정
        binding.qnName.setText(viewModel.name.value.toString())
        binding.qnAge.setText(viewModel.age.value.toString())
        binding.qnAddress.setText(viewModel.address.value.toString())

        when(viewModel.gender.value) {
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

        //edittext변경시 viewModel에 저장
        binding.qnName.addTextChangedListener {
            viewModel.setName(it.toString())
        }

        binding.qnAddress.addTextChangedListener {
            viewModel.setAddress(it.toString())
        }

        binding.qnAge.addTextChangedListener {
            viewModel.setAge(it.toString())
        }

        binding.genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setGender(when (checkedId) {
                R.id.qnMale -> "남성"
                R.id.qnFemale -> "여성"
                else -> ""
            })
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navController.navigateUp()
                if(!getUpdateMode()) {
                    viewModel.resetViewModelData()
                }
            }
        })

        //이전 버튼 클릭시
        binding.qnFirstBeforeBtn.setOnClickListener {
            navController.navigateUp()
            if(!getUpdateMode()) {
                viewModel.resetViewModelData()
            }
        }

        //다음 버튼 클릭시
        binding.qnFirstNextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionnaireFirstFragment_to_questionnaireSecondFragment)
        }
    }

    private fun getUpdateMode() : Boolean {
        val sharedPref = requireActivity().getSharedPreferences("pref", 0)
        return sharedPref.getBoolean(getString(R.string.edit_mode), false)
    }
}