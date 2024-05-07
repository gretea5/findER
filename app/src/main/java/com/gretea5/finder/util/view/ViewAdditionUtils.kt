package com.gretea5.finder.util.view

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.FragmentManager
import com.gretea5.finder.R

object ViewAdditionUtils {
    fun addInputView(
        parent: LinearLayout,
        context: Context,
        fragmentManager: FragmentManager,
        name: String = context.getString(R.string.empty_string),
        date: String = context.getString(R.string.empty_string),
        nameHint: String = context.getString(R.string.empty_string),
        dateHint: String = context.getString(R.string.empty_string),
    ) {

        val childLinearLayout = LinearLayout(context)
        ViewCustomizationUtils.customLinearLayout(childLinearLayout, context)

        val nameEdittext = EditText(context)
        val dateEdittext = EditText(context)

        nameEdittext.setText(name)
        dateEdittext.setText(date)

        ViewCustomizationUtils.customNameEdittext(nameEdittext, context, nameHint)
        ViewCustomizationUtils.customDateEdittext(dateEdittext, context, dateHint)

        ViewEventUtils.setDateEdittextFocusListener(dateEdittext, fragmentManager)

        addParentLinearLayout(
            parent = parent,
            child = childLinearLayout,
            nameEditText = nameEdittext,
            dateEdittext = dateEdittext
        )
    }

    private fun addParentLinearLayout(
        parent: LinearLayout,
        child: LinearLayout,
        nameEditText: EditText,
        dateEdittext: EditText
    ) {
        child.addView(nameEditText)
        child.addView(dateEdittext)

        parent.addView(child)
    }
}