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
import com.gretea5.finder.databinding.FragmentQuestionnaireFinalBinding

class QuestionnaireFinalFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireFinalBinding
    private lateinit var navController: NavController

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
        }
    }
}