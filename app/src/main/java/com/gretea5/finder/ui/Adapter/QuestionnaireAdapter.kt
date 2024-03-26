package com.gretea5.finder.ui.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.databinding.ItemQuestionnaireBinding

class QuestionnaireAdapter(private val qnList : List<QuestionnaireModel>)
    : RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireViewHolder>() {

    inner class QuestionnaireViewHolder(private val binding : ItemQuestionnaireBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(questionnaireModel: QuestionnaireModel) {
            binding.qnItemNameTextView.text = questionnaireModel.name
            binding.qnItemAgeTextView.text = "${questionnaireModel.age}세"
            binding.qnItemGenderTextView.text = "(${questionnaireModel.gender})"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireViewHolder {
        val binding = ItemQuestionnaireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionnaireViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {
        holder.bind(qnList[position])
    }

    override fun getItemCount(): Int = qnList.size
}