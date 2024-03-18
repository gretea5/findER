package com.gretea5.finder.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireBinding
import com.gretea5.finder.ui.activity.QuestionnaireActivity

class QuestionnaireFragment : Fragment() {

    private lateinit var binding : FragmentQuestionnaireBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.questionnaireMenuMove -> {
                    Log.d("QuestionnaireFragment", "plus clicked!")

                    startActivity(Intent(requireContext(), QuestionnaireActivity::class.java))
                    requireActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

                    true
                }
                else -> false
            }
        }
    }
}