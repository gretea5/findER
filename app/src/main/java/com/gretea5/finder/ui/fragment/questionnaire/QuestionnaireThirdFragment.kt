package com.gretea5.finder.ui.fragment.questionnaire

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
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

    private val qnThirdBeforeBtn: TextView by lazy { binding.qnThirdBeforeBtn }
    private val qnThirdNextBtn: TextView by lazy { binding.qnThirdNextBtn }

    private val medicineRadioGroup: RadioGroup by lazy { binding.medicineRadioGroup }
    private val medicineNoBtn: RadioButton by lazy { binding.medicineNoBtn }
    private val medicineYesBtn: RadioButton by lazy { binding.medicineYesBtn }
    private val medicineInfo: EditText by lazy { binding.medicineInfo }
    private val medicineAddLayout: LinearLayout by lazy { binding.medicineAddLayout }
    private val addMedicineInfoBtn: ImageView by lazy { binding.addMedicineInfoBtn }

    private val surgeryRadioGroup: RadioGroup by lazy { binding.surgeryRadioGroup }
    private val surgeryNoBtn: RadioButton by lazy { binding.surgeryNoBtn }
    private val surgeryYesBtn: RadioButton by lazy { binding.surgeryYesBtn }
    private val surgeryInfo: EditText by lazy { binding.surgeryInfo }
    private val surgeryAddLayout: LinearLayout by lazy { binding.surgeryAddLayout }
    private val addSurgeryInfoBtn: ImageView by lazy { binding.addSurgeryInfoBtn }

    private val diseaseRadioGroup: RadioGroup by lazy { binding.diseaseRadioGroup }
    private val diseaseNoBtn: RadioButton by lazy { binding.diseaseNoBtn }
    private val diseaseYesBtn: RadioButton by lazy { binding.diseaseYesBtn }
    private val diseaseInfo: EditText by lazy { binding.diseaseInfo }
    private val diseaseAddLayout: LinearLayout by lazy { binding.diseaseAddLayout }
    private val addDiseaseInfoBtn: ImageView by lazy { binding.addDiseaseInfoBtn }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentQuestionnaireThirdBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        //viewModel값에 따른 UI 갱신
        updateByViewModelValues()

        //리스너 지정
        setListeners()
    }

    private fun updateByViewModelValues() {
        //viewmodel 약값에 따른 UI 갱신
        updateUIByValue(viewModel.medicine.value, medicineYesBtn, medicineNoBtn, medicineInfo, addMedicineInfoBtn)

        //viewmodel 수술 이력 값에 따른 UI 갱신
        updateUIByValue(viewModel.surgery.value, surgeryYesBtn, surgeryNoBtn, surgeryInfo, addSurgeryInfoBtn)

        //viewModel 질병 이력 값에 따른 UI 갱신
        updateUIByValue(viewModel.disease.value, diseaseYesBtn, diseaseNoBtn, diseaseInfo, addDiseaseInfoBtn)
    }

    //value값에 따른 라디오 그룹과 edittext 설정
    private fun updateUIByValue(
        value: String?,
        yesBtn: RadioButton,
        noBtn: RadioButton,
        infoView: EditText,
        addInfoBtn: ImageView) {
        when (value) {
            "X" -> {
                noBtn.isChecked = true
                yesBtn.isChecked = false
                infoView.text.clear()
                addInfoBtn.visibility = View.INVISIBLE
            }
            "O" -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.text.clear()
                addInfoBtn.visibility = View.VISIBLE
            }
            "" -> {
                noBtn.isChecked = false
                yesBtn.isChecked = false
                infoView.text.clear()
                addInfoBtn.visibility = View.INVISIBLE
            }
            else -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.setText(value)
                addInfoBtn.visibility = View.VISIBLE
            }
        }
    }

    private fun setListeners() {
        setToolBarListener()
        setBackButtonListener()
        setInputListener()
        setAddViewListener()
    }

    private fun setToolBarListener() {
        qnThirdBeforeBtn.setOnClickListener {
            navController.navigateUp()
        }

        qnThirdNextBtn.setOnClickListener {
            navController.navigate(R.id.action_questionnaireThirdFragment_to_questionnaireFinalFragment)
        }
    }

    private fun setBackButtonListener() {
        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                }
            })
    }

    private fun setInputListener() {
        setRadioGroupListener(
            radioGroup = medicineRadioGroup,
            yesButton = medicineYesBtn,
            noButton = medicineNoBtn,
            infoEditText = medicineInfo,
            infoButton = addMedicineInfoBtn,
            viewModelSetter = { viewModel.setMedicine(it) }
        )

        setEditTextListener(
            editText = medicineInfo,
            viewModelSetter = { viewModel.setMedicine(it) }
        )

        setRadioGroupListener(
            radioGroup = surgeryRadioGroup,
            yesButton = surgeryYesBtn,
            noButton = surgeryNoBtn,
            infoEditText = surgeryInfo,
            infoButton = addSurgeryInfoBtn,
            viewModelSetter = { viewModel.setSurgery(it) }
        )

        setEditTextListener(
            editText = surgeryInfo,
            viewModelSetter = { viewModel.setSurgery(it) }
        )

        setRadioGroupListener(
            radioGroup = diseaseRadioGroup,
            yesButton = diseaseYesBtn,
            noButton = diseaseNoBtn,
            infoEditText = diseaseInfo,
            infoButton = addDiseaseInfoBtn,
            viewModelSetter = { viewModel.setDisease(it) }
        )

        setEditTextListener(
            editText = diseaseInfo,
            viewModelSetter = { viewModel.setDisease(it) }
        )
    }

    private fun setRadioGroupListener(
        radioGroup: RadioGroup,
        yesButton: RadioButton,
        noButton: RadioButton,
        infoEditText: EditText,
        infoButton: ImageView,
        viewModelSetter: (String) -> Unit
    ) {
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                yesButton.id -> {
                    viewModelSetter("O")
                    infoButton.visibility = View.VISIBLE
                }
                noButton.id -> {
                    infoEditText.setText("")
                    viewModelSetter("X")
                    infoButton.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setEditTextListener(
        editText: EditText,
        viewModelSetter: (String) -> Unit
    ) {
        editText.addTextChangedListener {
            if (!it.isNullOrBlank()) {
                viewModelSetter(it.toString())
            }
        }
    }

    private fun setAddViewListener() {
        addMedicineInfoBtn.setOnClickListener {
            addInputView(
                parent = medicineAddLayout,
                nameHint = getString(R.string.medicine_edittext_name),
                dateHint = getString(R.string.medicine_edittext_date)
            )
        }

        addSurgeryInfoBtn.setOnClickListener {
            addInputView(
                parent = surgeryAddLayout,
                nameHint = getString(R.string.surgery_edittext_name),
                dateHint = getString(R.string.surgery_edittext_date)
            )
        }

        addDiseaseInfoBtn.setOnClickListener {
            addInputView(
                parent = diseaseAddLayout,
                nameHint = getString(R.string.disease_edittext_name),
                dateHint = getString(R.string.disease_edittext_date)
            )
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
        dateEdittextParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
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