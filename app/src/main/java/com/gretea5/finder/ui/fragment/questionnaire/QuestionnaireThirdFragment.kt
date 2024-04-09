package com.gretea5.finder.ui.fragment.questionnaire

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireThirdBinding
import com.gretea5.finder.ui.dialog.YearMonthPickerDialog
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireThirdFragment : Fragment() {
    private lateinit var binding : FragmentQuestionnaireThirdBinding
    private lateinit var navController: NavController
    private val viewModel: QuestionnaireViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireThirdBinding.inflate(inflater)

        binding.addMedicineInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.medicineAddLayout,
                nameHint = getString(R.string.medicine_edittext_name),
                dateHint = getString(R.string.medicine_edittext_date)
            )
        }

        binding.addSurgeryInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.surgeryAddLayout,
                nameHint = getString(R.string.surgery_edittext_name),
                dateHint = getString(R.string.surgery_edittext_date)
            )
        }

        binding.addDiseaseInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.diseaseAddLayout,
                nameHint = getString(R.string.disease_edittext_name),
                dateHint = getString(R.string.disease_edittext_date)
            )
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        //viewmodel 약값에 따른 UI 갱신
        when(viewModel.medicine.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.medicineNoBtn.isChecked = true
                binding.medicineYesBtn.isChecked = false
                binding.addMedicineInfoBtn.visibility = View.INVISIBLE
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.medicineNoBtn.isChecked = false
                binding.medicineYesBtn.isChecked = true
                binding.addMedicineInfoBtn.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.medicineNoBtn.isChecked = false
                binding.medicineYesBtn.isChecked = false
                binding.addMedicineInfoBtn.visibility = View.INVISIBLE

            }
            else -> {
                binding.medicineNoBtn.isChecked = false
                binding.medicineYesBtn.isChecked = true
                binding.medicineInfo.setText(viewModel.medicine.value)
                binding.addMedicineInfoBtn.visibility = View.VISIBLE
            }
        }

        //약 UI 이벤트에 따른 viwwModel 갱신
        binding.medicineRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.medicineYesBtn -> {
                    viewModel.setMedicine("O")

                    binding.addMedicineInfoBtn.visibility = View.VISIBLE
                }
                R.id.medicineNoBtn -> {
                    binding.medicineInfo.setText("")
                    viewModel.setMedicine("X")

                    binding.addMedicineInfoBtn.visibility = View.INVISIBLE
                }
            }
        }

        //즉 빈 문자열이 들어왔을때, "O"으로 변경이 되는 문제가 존재,
        binding.medicineInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setMedicine(it.toString())
            }
        }

        //viewmodel 수술 이력 값에 따른 UI 갱신
        when(viewModel.surgery.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.surgeryNoBtn.isChecked = true
                binding.surgeryYesBtn.isChecked = false
                binding.addSurgeryInfoBtn.visibility = View.INVISIBLE
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.surgeryNoBtn.isChecked = false
                binding.surgeryYesBtn.isChecked = true
                binding.addSurgeryInfoBtn.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.surgeryNoBtn.isChecked = false
                binding.surgeryYesBtn.isChecked = false
                binding.addSurgeryInfoBtn.visibility = View.INVISIBLE
            }
            else -> {
                binding.surgeryNoBtn.isChecked = false
                binding.surgeryYesBtn.isChecked = true
                binding.surgeryInfo.setText(viewModel.surgery.value)
                binding.addSurgeryInfoBtn.visibility = View.VISIBLE
            }
        }

        //수술 이력 UI 이벤트에 따른 viewModel 갱신
        binding.surgeryRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.surgeryYesBtn -> {
                    viewModel.setSurgery("O")

                    binding.addSurgeryInfoBtn.visibility = View.VISIBLE
                }
                R.id.surgeryNoBtn -> {
                    binding.surgeryInfo.setText("")
                    viewModel.setSurgery("X")

                    binding.addSurgeryInfoBtn.visibility = View.INVISIBLE
                }
            }
        }

        binding.surgeryInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setSurgery(it.toString())
            }

        }

        //viewModel 수술 이력 값에 따른 UI 갱신
        when(viewModel.disease.value) {
            //해당 없음인 값인 경우,
            "X" -> {
                binding.diseaseNoBtn.isChecked = true
                binding.diseaseYesBtn.isChecked = false
                binding.addDiseaseInfoBtn.visibility = View.INVISIBLE
            }
            //해당 있음인 값인 경우,
            "O" -> {
                binding.diseaseNoBtn.isChecked = false
                binding.diseaseYesBtn.isChecked = true
                binding.addDiseaseInfoBtn.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            "" -> {
                binding.diseaseNoBtn.isChecked = false
                binding.diseaseYesBtn.isChecked = false
                binding.addDiseaseInfoBtn.visibility = View.INVISIBLE
            }
            else -> {
                binding.diseaseNoBtn.isChecked = false
                binding.diseaseYesBtn.isChecked = true
                binding.diseaseInfo.setText(viewModel.disease.value)
                binding.addDiseaseInfoBtn.visibility = View.VISIBLE
            }
        }

        //수술 이력 UI 이벤트에 따른 viewModel 갱신
        binding.diseaseRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.diseaseYesBtn -> {
                    viewModel.setDisease("O")

                    binding.addDiseaseInfoBtn.visibility = View.VISIBLE
                }
                R.id.diseaseNoBtn -> {
                    //해당 없음을 체크 했을 경우 내용 삭제
                    binding.diseaseInfo.setText("")

                    //"X"값으로 갱신
                    viewModel.setDisease("X")

                    binding.addDiseaseInfoBtn.visibility = View.INVISIBLE
                }
            }
        }

        binding.diseaseInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setDisease(it.toString())
            }
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            })

        binding.qnThirdBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        binding.qnThirdNextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionnaireThirdFragment_to_questionnaireFinalFragment)
        }
    }

    private fun addInputView(
        parent: LinearLayout,
        nameHint: String,
        dateHint: String
    ) {
        val context = requireContext()

        val childLinearLayout = LinearLayout(context)
        customLinearLayout(childLinearLayout)

        val nameEdittext = EditText(context)
        customNameEdittext(nameEdittext, context, nameHint)

        val dateEdittext = EditText(context)
        customDateEdittext(dateEdittext, context, dateHint)

        setDateEdittextFocusListener(dateEdittext)

        addParentLinearLayout(
            parent = parent,
            child = childLinearLayout,
            nameEditText = nameEdittext,
            dateEdittext = dateEdittext
        )
    }

    //edittext를 감싸고 있는 linearLayout custom
    private fun customLinearLayout(linearLayout: LinearLayout) {
        val linearLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        linearLayoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)
        linearLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)

        linearLayout.layoutParams = linearLayoutParams
        linearLayout.orientation = LinearLayout.HORIZONTAL
    }

    //이름을 입력받는 edittext custom
    private fun customNameEdittext(
        editText: EditText,
        context: Context,
        hint: String
    ) {
        val nameEdittextParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        nameEdittextParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
        editText.layoutParams = nameEdittextParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        editText.hint = hint

        editText.setPadding(
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )
    }

    private fun customDateEdittext(
        editText: EditText,
        context: Context,
        hint: String
    ) {
        setDateEdittextStyle(editText, context, hint)
        setDateEdittextCalendarIcon(editText, context)
    }

    private fun setDateEdittextStyle(
        editText: EditText,
        context: Context,
        hint: String
    ) {
        val dateEdittextParams = LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        dateEdittextParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin) // 간격을 위한 마진 추가
        editText.layoutParams = dateEdittextParams

        editText.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        editText.hint = hint
        editText.setPadding( // padding 값을 지정
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )
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

    private fun setDateEdittextFocusListener(editText: EditText) {
        editText.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val pickerDialog = YearMonthPickerDialog()

                pickerDialog.setListener { _, year, month, _ ->
                    val selectedDate = "$year-$month"
                    editText.setText(selectedDate)
                }

                pickerDialog.show(childFragmentManager, "YearMonthPickerDialog")
            }
        }
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