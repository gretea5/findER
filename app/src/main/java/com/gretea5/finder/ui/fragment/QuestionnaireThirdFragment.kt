package com.gretea5.finder.ui.fragment

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
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.databinding.FragmentQuestionnaireThirdBinding
import com.gretea5.finder.util.view.ViewEventUtils.setRadioGroupNameDataUIListener
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel
import com.gretea5.finder.util.view.ViewAdditionUtils.addInputView

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

        Log.d("QuestionnaireThirdFragment medicine", viewModel.medicine.value.toString())
        Log.d("QuestionnaireThirdFragment surgery", viewModel.surgery.value.toString())
        Log.d("QuestionnaireThirdFragment disease", viewModel.disease.value.toString())

        //viewModel값에 따른 UI 갱신
        updateByViewModelValues()

        //리스너 지정
        setListeners()
    }

    override fun onPause() {
        super.onPause()
        saveInfoData()
        Log.d("QuestionnaireThirdFragment onPause", "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d("QuestionnaireThirdFragment onStop", "onStop")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("QuestionnaireThirdFragment onDestroyView", "onDestroyView")
        _binding = null
    }

    private fun saveInfoData() {
        if (binding.medicineAddLayout.childCount > 0) {
            saveNameAndDate(
                parentLinearLayout = binding.medicineAddLayout,
                viewModelSetter = { viewModel.setMedicine(it) }
            )
        }

        if (binding.surgeryAddLayout.childCount > 0) {
            saveNameAndDate(
                parentLinearLayout = binding.surgeryAddLayout,
                viewModelSetter = { viewModel.setSurgery(it) }
            )
        }

        if (binding.diseaseAddLayout.childCount > 0) {
            saveNameAndDate(
                parentLinearLayout = binding.diseaseAddLayout,
                viewModelSetter = { viewModel.setDisease(it) }
            )
        }
    }

    private fun saveNameAndDate(
        parentLinearLayout: LinearLayout,
        viewModelSetter: (String) -> Unit
    ) {
        val childCount: Int = parentLinearLayout.childCount

        val builder = StringBuilder()

        for (i in 0 until childCount) {
            val childLinearLayout: LinearLayout = parentLinearLayout.getChildAt(i) as LinearLayout

            val nameEdittext: EditText = childLinearLayout.getChildAt(0) as EditText
            val name = nameEdittext.text.toString()

            val dateEdittext: EditText = childLinearLayout.getChildAt(1) as EditText
            val date = dateEdittext.text.toString()

            builder.append(name).append(" ").append(date)

            if (i < childCount - 1) {
                builder.append("\n")
            }
        }

        viewModelSetter(builder.toString())
    }

    private fun updateByViewModelValues() {
        //viewmodel 약값에 따른 UI 갱신
        updateUIByValue(
            value = viewModel.medicine.value,
            yesBtn = binding.medicineYesBtn,
            noBtn = binding.medicineNoBtn,
            parent = binding.medicineAddLayout,
            context = requireContext(),
            fragmentManager = childFragmentManager,
            inVisibleType = View.INVISIBLE,
            addInfoBtn = binding.addMedicineInfoBtn
        )

        updateUIByValue(
            value = viewModel.surgery.value,
            yesBtn = binding.surgeryYesBtn,
            noBtn = binding.surgeryNoBtn,
            parent = binding.surgeryAddLayout,
            context = requireContext(),
            fragmentManager = childFragmentManager,
            inVisibleType = View.INVISIBLE,
            addInfoBtn = binding.addSurgeryInfoBtn
        )

        updateUIByValue(
            value = viewModel.disease.value,
            yesBtn = binding.diseaseYesBtn,
            noBtn = binding.diseaseNoBtn,
            parent = binding.diseaseAddLayout,
            context = requireContext(),
            fragmentManager = childFragmentManager,
            inVisibleType = View.INVISIBLE,
            addInfoBtn = binding.addDiseaseInfoBtn
        )
    }

    //value값에 따른 라디오 그룹과 linearlayout에 뿌린다.
    private fun updateUIByValue(
        value: String?,
        yesBtn: RadioButton,
        noBtn: RadioButton,
        parent: LinearLayout,
        context: Context,
        fragmentManager: FragmentManager,
        inVisibleType: Int,
        addInfoBtn: ImageView? = null) {
        when (value) {
            getString(R.string.condition_absent) -> {
                noBtn.isChecked = true
                yesBtn.isChecked = false
                addInfoBtn?.visibility = inVisibleType
            }
            getString(R.string.condition_present) -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                addInfoBtn?.visibility = View.VISIBLE
            }
            getString(R.string.empty_string)-> {
                noBtn.isChecked = false
                yesBtn.isChecked = false
                addInfoBtn?.visibility = inVisibleType
            }
            else -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                updateNameAndData(
                    parent = parent,
                    context = context,
                    fragmentManager = fragmentManager,
                    value = value!!
                )
                addInfoBtn?.visibility = View.VISIBLE
            }
        }
    }

    private fun updateNameAndData(
        parent: LinearLayout,
        context: Context,
        fragmentManager: FragmentManager,
        value: String
    ) {
        val entries = value.trim().split("\n")

        for(entry in entries) {
            val parts = entry.split(" ", limit = 2)

            if (parts.size == 2) {
                val name = parts[0]
                val date = parts[1]

                addInputView(
                    parent = parent,
                    context = context,
                    fragmentManager = fragmentManager,
                    name = name,
                    date = date
                )
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
            navController.navigateUp()
        }

        binding.qnThirdNextBtn.setOnClickListener {
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
        setRadioGroupNameDataUIListener(
            context = requireContext(),
            radioGroup = binding.medicineRadioGroup,
            yesButton = binding.medicineYesBtn,
            noButton = binding.medicineNoBtn,
            addViewBtn = binding.addMedicineInfoBtn,
            parentLinearLayout = binding.medicineAddLayout,
            viewModelSetter = { viewModel.setMedicine(it) }
        )

        setRadioGroupNameDataUIListener(
            context = requireContext(),
            radioGroup = binding.surgeryRadioGroup,
            yesButton = binding.surgeryYesBtn,
            noButton = binding.surgeryNoBtn,
            addViewBtn = binding.addSurgeryInfoBtn,
            parentLinearLayout = binding.surgeryAddLayout,
            viewModelSetter = { viewModel.setSurgery(it) }
        )

        setRadioGroupNameDataUIListener(
            context = requireContext(),
            radioGroup = binding.diseaseRadioGroup,
            yesButton = binding.diseaseYesBtn,
            noButton = binding.diseaseNoBtn,
            addViewBtn = binding.addDiseaseInfoBtn,
            parentLinearLayout = binding.diseaseAddLayout,
            viewModelSetter = { viewModel.setDisease(it) }
        )
    }

    private fun setAddViewListener() {
        binding.addMedicineInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.medicineAddLayout,
                context = requireContext(),
                fragmentManager = childFragmentManager,
                nameHint = getString(R.string.medicine_edittext_name),
                dateHint = getString(R.string.medicine_edittext_date)
            )
        }

        binding.addSurgeryInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.surgeryAddLayout,
                context = requireContext(),
                fragmentManager = childFragmentManager,
                nameHint = getString(R.string.surgery_edittext_name),
                dateHint = getString(R.string.surgery_edittext_date)
            )
        }

        binding.addDiseaseInfoBtn.setOnClickListener {
            addInputView(
                parent = binding.diseaseAddLayout,
                context = requireContext(),
                fragmentManager = childFragmentManager,
                nameHint = getString(R.string.disease_edittext_name),
                dateHint = getString(R.string.disease_edittext_date)
            )
        }
    }
}