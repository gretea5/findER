package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireModeBinding

class QuestionnaireModeFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireModeBinding
    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentQuestionnaireModeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        
        binding.writeQuestionnaireBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.linkQuestionnaireBtn.isChecked = false
            }
        }

        binding.linkQuestionnaireBtn.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.writeQuestionnaireBtn.isChecked = false
            }
        }

        binding.beforeBtn.setOnClickListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        //fragment에서 백버튼 클릭시
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                    requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
                }
            })

        binding.nextBtn.setOnClickListener {
            when(binding.modeRadioGroup.checkedRadioButtonId) {
                R.id.writeQuestionnaireBtn -> {
                    navController.navigate(R.id.action_questionnaireModeFragment_to_questionnaireFirstFragment)
                }
                R.id.linkQuestionnaireBtn -> {
                    navController.navigate(R.id.action_questionnaireModeFragment_to_questionnaireLinkFragment)
                }
            }
        }
    }
}