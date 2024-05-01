package com.gretea5.finder.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.gretea5.finder.R
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.databinding.ItemQuestionnaireBinding

class QuestionnaireAdapter(
    private val qnList: List<QuestionnaireModel>,
    private val context: Context
    )
    : RecyclerView.Adapter<QuestionnaireAdapter.QuestionnaireViewHolder>() {

    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    var itemClick: ItemClick? = null

    inner class QuestionnaireViewHolder(private val binding : ItemQuestionnaireBinding)
        : RecyclerView.ViewHolder(binding.root) {
        fun bind(questionnaireModel: QuestionnaireModel) {
            binding.qnItemNameTextView.text = questionnaireModel.name
            binding.qnItemAgeTextView.text = "${questionnaireModel.age}세"
            binding.qnItemGenderTextView.text = "(${questionnaireModel.gender})"

            val color = ContextCompat.getColor(context, R.color.main_color)

            if (questionnaireModel.drink != "X") {
                binding.qnItemDrink.setColorFilter(color)
            }

            if (questionnaireModel.smoke != "X") {
                binding.qnItemSmoke.setColorFilter(color)
            }

            if (questionnaireModel.medicine != "X") {
                binding.qnItemMedicine.setColorFilter(color)
            }

            if (questionnaireModel.allergy != "X") {
                binding.qnItemAllergy.setColorFilter(color)
            }

            if (questionnaireModel.surgery != "X") {
                binding.qnItemSurgery.setColorFilter(color)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionnaireViewHolder {
        val binding = ItemQuestionnaireBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return QuestionnaireViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QuestionnaireViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            itemClick?.onClick(it, position)
        }
        holder.bind(qnList[position])
    }

    override fun getItemCount(): Int = qnList.size
}