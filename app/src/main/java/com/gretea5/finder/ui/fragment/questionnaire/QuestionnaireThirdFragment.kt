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
import com.gretea5.finder.databinding.FragmentQuestionnaireThirdBinding

class QuestionnaireThirdFragment : Fragment() {

    private lateinit var binding : FragmentQuestionnaireThirdBinding
    private lateinit var navController: NavController

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