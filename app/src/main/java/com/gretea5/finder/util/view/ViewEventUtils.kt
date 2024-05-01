package com.gretea5.finder.util.view

import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.core.widget.addTextChangedListener

object ViewEventUtils {
    fun setRadioGroupNameDataUIListener(
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
                    viewModelSetter("O")
                    parentLinearLayout.visibility = View.VISIBLE
                    addViewBtn.visibility = View.VISIBLE
                }
                noButton.id -> {
                    viewModelSetter("X")
                    parentLinearLayout.removeAllViewsInLayout()
                    parentLinearLayout.visibility = View.GONE
                    parentLinearLayout.visibility = View.INVISIBLE
                    addViewBtn.visibility = View.INVISIBLE
                }
            }
        }
    }

    fun setRadioGroupListener(
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
                    viewModelSetter("O")
                    visibleView.visibility = View.VISIBLE
                }
                noButton.id -> {
                    infoEditText.setText("")
                    viewModelSetter("X")
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