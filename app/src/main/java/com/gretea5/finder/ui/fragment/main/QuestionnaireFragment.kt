package com.gretea5.finder.ui.fragment.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.databinding.FragmentQuestionnaireBinding
import com.gretea5.finder.ui.Adapter.QuestionnaireAdapter
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireBinding
    private lateinit var qnAdapter : QuestionnaireAdapter
    private lateinit var qnRecyclerView: RecyclerView
    private lateinit var navController : NavController

    private val viewModel: QuestionnaireViewModel by activityViewModels()
    private var qnList : List<QuestionnaireModel> = listOf()
    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireBinding.inflate(inflater)

        qnRecyclerView = binding.qnRecyclerView
        qnRecyclerView.layoutManager = LinearLayoutManager(activity)
        qnAdapter = QuestionnaireAdapter(qnList)
        qnRecyclerView.adapter = qnAdapter

        getQuestionnaireListData()

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        getQuestionnaireListData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        binding.toolbar.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.questionnaireMenuMove -> {
                    findNavController().navigate(R.id.action_questionnaireFragment_to_questionnaireModeFragment)
                    true
                }
                else -> false
            }
        }
    }

    private fun getPhoneNumber(): String {
        val pref = requireActivity().getSharedPreferences("pref", 0)

        return pref.getString(getString(R.string.phonenumber_key), "") ?: ""
    }

    private fun getQuestionnaireListData() {
        api.getQuestionnaires(getPhoneNumber()).enqueue(object: Callback<List<QuestionnaireModel>> {
            override fun onResponse(
                call: Call<List<QuestionnaireModel>>,
                response: Response<List<QuestionnaireModel>>
            ) {
                val data = response.body()

                Log.d("getQuestionnaireListData", data.toString())

                data?.let {
                    qnList = it
                    qnAdapter = QuestionnaireAdapter(qnList)
                    qnRecyclerView.adapter = qnAdapter
                    qnAdapter.notifyDataSetChanged()

                    qnSetItemClickListener()
                }
            }

            override fun onFailure(call: Call<List<QuestionnaireModel>>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun qnSetItemClickListener() {
        qnAdapter.itemClick = object : QuestionnaireAdapter.ItemClick {
            override fun onClick(view: View, position: Int) {
                viewModel.setViewModel(qnList[position])
                navController.navigate(R.id.action_questionnaireFragment_to_questionnaireInfoFragment)
            }
        }
    }
}
