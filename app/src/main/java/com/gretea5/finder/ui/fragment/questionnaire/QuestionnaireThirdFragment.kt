package com.gretea5.finder.ui.fragment.questionnaire

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireThirdBinding
import com.gretea5.finder.ui.dialog.YearMonthPickerDialog
import com.gretea5.finder.util.view.ViewEventUtils.setEditTextListener
import com.gretea5.finder.util.view.ViewEventUtils.setRadioGroupListener
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel

class QuestionnaireThirdFragment : Fragment() {
    private var _binding: FragmentQuestionnaireThirdBinding? = null

    private val binding get() = _binding!!
    private lateinit var navController: NavController

    private val viewModel: QuestionnaireViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaireThirdBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        Log.d("viewModel medicine", viewModel.medicine.value.toString())
        Log.d("viewModel surgery", viewModel.surgery.value.toString())
        Log.d("viewModel disease", viewModel.disease.value.toString())

        //viewModel값에 따른 UI 갱신
        updateByViewModelValues()

        //리스너 지정
        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun saveMedicineViewModelData() {
        val parentLinearLayout = binding.medicineAddLayout

        val childCount: Int = parentLinearLayout.childCount

        val builder = StringBuilder()

        for (i in 0 until childCount) {
            val childLinearLayout: LinearLayout = parentLinearLayout.getChildAt(i) as LinearLayout

            val nameEdittext: EditText = childLinearLayout.getChildAt(0) as EditText
            val name = nameEdittext.text.toString()

            val dateEdittext: EditText = childLinearLayout.getChildAt(1) as EditText
            val date = dateEdittext.text.toString()

            builder.append(name).append(" ").append(date).append("\n")
        }

        Log.d("saveMedicineViewModelData", builder.toString())

        viewModel.setMedicine(builder.toString())
    }


    private fun updateByViewModelValues() {
        //viewmodel 약값에 따른 UI 갱신
        updateUIByValue(
            value = viewModel.medicine.value,
            yesBtn = binding.medicineYesBtn,
            noBtn = binding.medicineNoBtn,
            infoView = binding.medicineInfo,
            inVisibleType = View.INVISIBLE,
            addInfoBtn = binding.addMedicineInfoBtn
        )

        updateUIByValue(
            value = viewModel.surgery.value,
            yesBtn = binding.surgeryYesBtn,
            noBtn = binding.surgeryNoBtn,
            infoView = binding.surgeryInfo,
            inVisibleType = View.INVISIBLE,
            addInfoBtn = binding.addSurgeryInfoBtn
        )

        updateUIByValue(
            value = viewModel.disease.value,
            yesBtn = binding.diseaseYesBtn,
            noBtn = binding.diseaseNoBtn,
            infoView = binding.diseaseInfo,
            inVisibleType = View.INVISIBLE,
            addInfoBtn = binding.addDiseaseInfoBtn
        )
    }

    //value값에 따른 라디오 그룹과 edittext 설정
    private fun updateUIByValue(
        value: String?,
        yesBtn: RadioButton,
        noBtn: RadioButton,
        infoView: EditText,
        inVisibleType: Int,
        addInfoBtn: ImageView? = null) {
        when (value) {
            "X" -> {
                noBtn.isChecked = true
                yesBtn.isChecked = false
                infoView.text.clear()
                addInfoBtn?.visibility = inVisibleType
            }
            "O" -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.text.clear()
                addInfoBtn?.visibility = View.VISIBLE
            }
            "" -> {
                noBtn.isChecked = false
                yesBtn.isChecked = false
                infoView.text.clear()
                addInfoBtn?.visibility = inVisibleType
            }
            else -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.setText(value)
                addInfoBtn?.visibility = View.VISIBLE
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
        binding.qnThirdBeforeBtn.setOnClickListener {
            Log.d("qnThirdBeforeBtn", "clicked!")
            navController.navigateUp()
        }

        binding.qnThirdNextBtn.setOnClickListener {
            Log.d("qnThirdNextBtn", "clicked!")
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
            radioGroup = binding.medicineRadioGroup,
            yesButton = binding.medicineYesBtn,
            noButton = binding.medicineNoBtn,
            infoEditText = binding.medicineInfo,
            visibleView = binding.addMedicineInfoBtn,
            visibleType = View.INVISIBLE,
            viewModelSetter = { viewModel.setMedicine(it) }
        )

        setEditTextListener(
            editText = binding.medicineInfo,
            viewModelSetter = { viewModel.setMedicine(it) }
        )

        setRadioGroupListener(
            radioGroup = binding.surgeryRadioGroup,
            yesButton = binding.surgeryYesBtn,
            noButton = binding.surgeryNoBtn,
            infoEditText = binding.surgeryInfo,
            visibleView = binding.addSurgeryInfoBtn,
            visibleType = View.INVISIBLE,
            viewModelSetter = { viewModel.setSurgery(it) }
        )

        setEditTextListener(
            editText = binding.surgeryInfo,
            viewModelSetter = { viewModel.setSurgery(it) }
        )

        setRadioGroupListener(
            radioGroup = binding.diseaseRadioGroup,
            yesButton = binding.diseaseYesBtn,
            noButton = binding.diseaseNoBtn,
            infoEditText = binding.diseaseInfo,
            visibleView = binding.addDiseaseInfoBtn,
            visibleType = View.INVISIBLE,
            viewModelSetter = { viewModel.setDisease(it) }
        )

        setEditTextListener(
            editText = binding.diseaseInfo,
            viewModelSetter = { viewModel.setDisease(it) }
        )
    }

    private fun setAddViewListener() {
        binding.addMedicineInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.medicineAddLayout,
                context = requireContext(),
                nameHint = getString(R.string.medicine_edittext_name),
                dateHint = getString(R.string.medicine_edittext_date)
            )
        }

        binding.addSurgeryInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.surgeryAddLayout,
                context = requireContext(),
                nameHint = getString(R.string.surgery_edittext_name),
                dateHint = getString(R.string.surgery_edittext_date)
            )
        }

        binding.addDiseaseInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.diseaseAddLayout,
                context = requireContext(),
                nameHint = getString(R.string.disease_edittext_name),
                dateHint = getString(R.string.disease_edittext_date)
            )
        }
    }

    private fun addInputView(
        parent: LinearLayout,
        context: Context,
        nameHint: String,
        dateHint: String
    ) {

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