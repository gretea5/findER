package com.gretea5.finder.util.view

import android.content.Context
import android.widget.EditText
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.gretea5.finder.R

object ViewCustomizationUtils {
    //edittext를 감싸고 있는 linearLayout custom
    fun customLinearLayout(linearLayout: LinearLayout, context: Context) {
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayoutParams.topMargin = context.resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)
        linearLayoutParams.bottomMargin = context.resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)

        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL
    }

    //이름을 입력받는 edittext custom
    fun customNameEdittext(
        editText: EditText,
        context: Context,
        hint: String
    ) {
        val nameEdittextParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        nameEdittextParams.marginEnd = context.resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
        editText.layoutParams = nameEdittextParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.edittext_accessible_border)
        editText.hint = hint

        editText.setPadding(
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )
    }

    fun setDateEdittextStyle(
        editText: EditText,
        context: Context,
        hint: String
    ) {
        val dateEdittextParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        dateEdittextParams.marginEnd = context.resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
        editText.layoutParams = dateEdittextParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.edittext_accessible_border)
        editText.hint = hint
        editText.setPadding( // padding 값을 지정
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            context.resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )
    }

    fun customDateEdittext(
        editText: EditText,
        context: Context,
        hint: String
    ) {
        setDateEdittextStyle(editText, context, hint)
        setDateEdittextCalendarIcon(editText, context)
    }

    private fun setDateEdittextCalendarIcon(editText: EditText, context: Context) {
        val calendarIcon = ContextCompat.getDrawable(context, R.drawable.icon_calendar)

        calendarIcon?.setBounds(0, 0, 70, 70)

        editText.setCompoundDrawables(
            null,
            null,
            calendarIcon,
            null
        )
    }
}