package com.gretea5.finder.ui.dialog

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.NumberPicker
import androidx.fragment.app.DialogFragment
import com.gretea5.finder.R
import java.util.*

class YearMonthPickerDialog : DialogFragment() {

    private val MAX_YEAR = 2099
    private val MIN_YEAR = 1980

    private var listener: DatePickerDialog.OnDateSetListener? = null
    var cal: Calendar = Calendar.getInstance()

    fun setListener(listener: DatePickerDialog.OnDateSetListener?) {
        this.listener = listener
    }

    private lateinit var btnConfirm: Button
    private lateinit var btnCancel: Button

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity?.layoutInflater

        val dialog: View = inflater!!.inflate(R.layout.year_month_picker, null)

        btnConfirm = dialog.findViewById(R.id.btn_confirm)
        btnCancel = dialog.findViewById(R.id.btn_cancel)

        val monthPicker = dialog.findViewById<NumberPicker>(R.id.picker_month)
        val yearPicker = dialog.findViewById<NumberPicker>(R.id.picker_year)

        btnCancel.setOnClickListener { dialog1: View? ->
            this@YearMonthPickerDialog.dialog?.cancel()
        }

        btnConfirm.setOnClickListener { dialog1: View? ->
            listener?.onDateSet(null, yearPicker.value, monthPicker.value, 0)
            this@YearMonthPickerDialog.dialog?.cancel()
        }

        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = cal[Calendar.MONTH] + 1

        val year = cal[Calendar.YEAR]
        yearPicker.minValue = MIN_YEAR
        yearPicker.maxValue = MAX_YEAR
        yearPicker.value = year

        builder.setView(dialog)

        return builder.create()
    }
}