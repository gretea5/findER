package com.gretea5.finder.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireModeBinding

class QuestionnaireModeFragment : Fragment() {

    private lateinit var binding : FragmentQuestionnaireModeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireModeBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.beforeBtn.setOnClickListener {
            requireActivity().finish()
            requireActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
        }

        binding.nextBtn.setOnClickListener {

        }
    }
}