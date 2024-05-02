package com.gretea5.finder.util.view

import android.content.Context
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.widget.addTextChangedListener
import com.gretea5.finder.R

object ViewEventUtils {
    fun setRadioGroupNameDataUIListener(
        context: Context,
        radioGroup: RadioGroup,
        yesButton: RadioButton,
        noButton: RadioButton,
        addViewBtn: ImageView,
        parentLinearLayout: LinearLayout,
        viewModelSetter: (String) -> Unit
    ) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                yesButton.id -> {
                    viewModelSetter(context.getString(R.string.condition_present))
                    parentLinearLayout.visibility = View.VISIBLE
                    addViewBtn.visibility = View.VISIBLE
                }
                noButton.id -> {
                    viewModelSetter(context.getString(R.string.condition_absent))
                    parentLinearLayout.removeAllViewsInLayout()
                    parentLinearLayout.visibility = View.GONE
                    parentLinearLayout.visibility = View.INVISIBLE
                    addViewBtn.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun setRadioGroupListener(
        context: Context,
        radioGroup: RadioGroup,
        yesButton: RadioButton,
        noButton: RadioButton,
        infoEditText: EditText,
        visibleView: View,
        visibleType: Int,
        viewModelSetter: (String) -> Unit
    ) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                yesButton.id -> {
                    viewModelSetter(context.getString(R.string.condition_present))
                    visibleView.visibility = View.VISIBLE
                }
                noButton.id -> {
                    infoEditText.setText(context.getString(R.string.empty_string))
                    viewModelSetter(context.getString(R.string.condition_absent))
                    visibleView.visibility = visibleType
                }
            }
        }
    }

    fun setEditTextListener(
        editText: EditText,
        viewModelSetter: (String) -> Unit
    ) {
        editText.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                viewModelSetter(it.toString())
            }
        }
    }
}