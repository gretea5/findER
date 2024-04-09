package com.gretea5.finder.ui.fragment.questionnaire

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.marginEnd
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireThirdBinding
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
            addMedicineInputView()
        }

        binding.addSurgeryInfoBtn.setOnClickListener {
            addSurgeryInputView()
        }

        binding.addDiseaseInfoBtn.setOnClickListener {
            addDiseaseInputView()
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

    private fun addMedicineInputView() {
        val context = requireContext()

        val medicineInfoLayout = LinearLayout(context)

        val medicineInfoLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        medicineInfoLayoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)
        medicineInfoLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)

        medicineInfoLayout.layoutParams = medicineInfoLayoutParams
        medicineInfoLayout.orientation = LinearLayout.HORIZONTAL

        val medicineNameEditText = EditText(context)

        val medicineNameParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        medicineNameParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
        medicineNameEditText.layoutParams = medicineNameParams

        medicineNameEditText.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        medicineNameEditText.hint = "약이름"

        medicineNameEditText.setPadding(
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )

        val medicineDateEdittext = EditText(context)

        val medicineDateParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        medicineDateParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin) // 간격을 위한 마진 추가
        medicineDateEdittext.layoutParams = medicineDateParams

        medicineDateEdittext.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        medicineDateEdittext.hint = "복용시작일"
        medicineDateEdittext.setPadding( // padding 값을 지정
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )

        val calendarIcon = ContextCompat.getDrawable(context, R.drawable.icon_calendar)

        calendarIcon?.setBounds(0, 0, 70, 70)

        medicineDateEdittext.setCompoundDrawables(
            null,
            null,
            calendarIcon,
            null
        )

        medicineInfoLayout.addView(medicineNameEditText)
        medicineInfoLayout.addView(medicineDateEdittext)


        binding.medicineAddLayout.addView(medicineInfoLayout)
    }

    private fun addSurgeryInputView() {
        val context = requireContext()

        val surgeryInfoLayout = LinearLayout(context)

        val surgeryInfoLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        surgeryInfoLayoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)
        surgeryInfoLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)

        surgeryInfoLayout.layoutParams = surgeryInfoLayoutParams
        surgeryInfoLayout.orientation = LinearLayout.HORIZONTAL

        val surgeryNameEditText = EditText(context)

        val surgeryNameParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        surgeryNameParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
        surgeryNameEditText.layoutParams = surgeryNameParams

        surgeryNameEditText.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        surgeryNameEditText.hint = "수술명"

        surgeryNameEditText.setPadding(
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )

        val surgeryDateEdittext = EditText(context)

        val surgeryDateParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        surgeryDateParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin) // 간격을 위한 마진 추가
        surgeryDateEdittext.layoutParams = surgeryDateParams

        surgeryDateEdittext.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        surgeryDateEdittext.hint = "수술일"
        surgeryDateEdittext.setPadding( // padding 값을 지정
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )

        val calendarIcon = ContextCompat.getDrawable(context, R.drawable.icon_calendar)

        calendarIcon?.setBounds(0, 0, 70, 70)

        surgeryDateEdittext.setCompoundDrawables(
            null,
            null,
            calendarIcon,
            null
        )

        surgeryInfoLayout.addView(surgeryNameEditText)
        surgeryInfoLayout.addView(surgeryDateEdittext)

        binding.surgeryAddLayout.addView(surgeryInfoLayout)
    }

    private fun addDiseaseInputView() {
        val context = requireContext()

        val diseaseInfoLayout = LinearLayout(context)

        val diseaseInfoLayoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        diseaseInfoLayoutParams.topMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)
        diseaseInfoLayoutParams.bottomMargin = resources.getDimensionPixelSize(R.dimen.write_qn_linearlayout_margin)

        diseaseInfoLayout.layoutParams = diseaseInfoLayoutParams
        diseaseInfoLayout.orientation = LinearLayout.HORIZONTAL

        val diseaseNameEditText = EditText(context)

        val diseaseNameParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )

        diseaseNameParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin)
        diseaseNameEditText.layoutParams = diseaseNameParams

        diseaseNameEditText.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        diseaseNameEditText.hint = "질환 이름"

        diseaseNameEditText.setPadding(
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )

        val diseaseDateEdittext = EditText(context)

        val diseaseDateParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            1f
        )
        diseaseDateParams.marginEnd = resources.getDimensionPixelSize(R.dimen.write_qn_name_margin) // 간격을 위한 마진 추가
        diseaseDateEdittext.layoutParams = diseaseDateParams

        diseaseDateEdittext.background = ContextCompat.getDrawable(context, R.drawable.edittext_unaccessible_border)
        diseaseDateEdittext.hint = "질환 진단 시기"
        diseaseDateEdittext.setPadding( // padding 값을 지정
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding),
            resources.getDimensionPixelSize(R.dimen.write_qn_name_padding)
        )

        val calendarIcon = ContextCompat.getDrawable(context, R.drawable.icon_calendar)

        calendarIcon?.setBounds(0, 0, 70, 70)

        diseaseDateEdittext.setCompoundDrawables(
            null,
            null,
            calendarIcon,
            null
        )

        diseaseInfoLayout.addView(diseaseNameEditText)
        diseaseInfoLayout.addView(diseaseDateEdittext)


        binding.diseaseAddLayout.addView(diseaseInfoLayout)
    }
}