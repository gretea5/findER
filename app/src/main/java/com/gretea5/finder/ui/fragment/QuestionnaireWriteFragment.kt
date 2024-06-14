package com.gretea5.finder.ui.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.databinding.FragmentQuestionnaireWriteBinding
import com.gretea5.finder.util.view.ViewEventUtils.setRadioGroupNameDataUIListener
import com.gretea5.finder.ui.activity.SearchAddressActivity
import com.gretea5.finder.ui.viewmodel.QuestionnaireViewModel
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil
import com.gretea5.finder.util.view.ViewAdditionUtils
import com.gretea5.finder.util.view.ViewEventUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class QuestionnaireWriteFragment : Fragment() {
    private var _binding: FragmentQuestionnaireWriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: QuestionnaireViewModel by activityViewModels()

    private val api = ApiService.create()

    private var resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            //Search Activity로 부터의 결과값을 전달받으면,
            if (result.resultCode == Activity.RESULT_OK) {
                if (result.data != null) {
                    val data = result.data!!.getStringExtra(getString(R.string.address))
                    binding.qnAddress.setText(data)
                }
            }
        }

    private lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionnaireWriteBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()

        //third

        //viewModel값에 따른 UI 갱신
        updateByViewModelValues()

        //리스너 지정
        setListeners()

        //first
        //viewModel 값 설정
        binding.qnName.setText(viewModel.name.value.toString())
        binding.qnAge.setText(viewModel.age.value.toString())
        binding.qnAddress.setText(viewModel.address.value.toString())

        when(viewModel.gender.value) {
            getString(R.string.male) -> {
                binding.qnMale.isChecked = true
                binding.qnFemale.isChecked = false
            }
            getString(R.string.female) -> {
                binding.qnMale.isChecked = false
                binding.qnFemale.isChecked = true
            }
            else -> {
                binding.qnMale.isChecked = false
                binding.qnFemale.isChecked = false
            }
        }

        //edittext변경시 viewModel에 저장
        binding.qnName.addTextChangedListener {
            viewModel.setName(it.toString())
        }

        binding.qnAddress.addTextChangedListener {
            viewModel.setAddress(it.toString())
        }

        binding.qnAge.addTextChangedListener {
            viewModel.setAge(it.toString())
        }

        binding.genderRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            viewModel.setGender(when (checkedId) {
                R.id.qnMale -> getString(R.string.male)
                R.id.qnFemale -> getString(R.string.female)
                else -> getString(R.string.empty_string)
            })
        }

        binding.qnAddress.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val intent = Intent(requireContext(), SearchAddressActivity::class.java)

                resultLauncher.launch(intent)
            }
        }

        //second
        if(viewModel.bloodType.value?.contains(getString(R.string.blood_rh_plus)) == true) {
            binding.rhRadioGroup.check(R.id.rhPlusBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_rh_minus)) == true) {
            binding.rhRadioGroup.check(R.id.rhMinusBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_a)) == true) {
            binding.bloodRadioGroup.check(R.id.aBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_b)) == true) {
            binding.bloodRadioGroup.check(R.id.bBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_o)) == true) {
            binding.bloodRadioGroup.check(R.id.oBtn)
        }

        if(viewModel.bloodType.value?.contains(getString(R.string.blood_ab)) == true) {
            binding.bloodRadioGroup.check(R.id.abBtn)
        }

        binding.rhRadioGroup.setOnCheckedChangeListener { _, checkedId  ->
            val rhType = when (checkedId) {
                R.id.rhPlusBtn -> getString(R.string.blood_rh_plus)
                R.id.rhMinusBtn -> getString(R.string.blood_rh_minus)
                else -> getString(R.string.empty_string)
            }
            val bloodType = when (binding.bloodRadioGroup.checkedRadioButtonId) {
                R.id.aBtn -> getString(R.string.blood_a)
                R.id.bBtn -> getString(R.string.blood_b)
                R.id.abBtn -> getString(R.string.blood_ab)
                R.id.oBtn -> getString(R.string.blood_o)
                else -> getString(R.string.empty_string)
            }
            viewModel.onBloodTypeSelected(rhType, bloodType)
        }

        binding.bloodRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            val rhType = when (binding.rhRadioGroup.checkedRadioButtonId) {
                R.id.rhPlusBtn -> getString(R.string.blood_rh_plus)
                R.id.rhMinusBtn -> getString(R.string.blood_rh_minus)
                else -> getString(R.string.empty_string)
            }
            val bloodType = when (checkedId) {
                R.id.aBtn -> getString(R.string.blood_a)
                R.id.bBtn -> getString(R.string.blood_b)
                R.id.abBtn -> getString(R.string.blood_ab)
                R.id.oBtn -> getString(R.string.blood_o)
                else -> getString(R.string.empty_string)
            }
            viewModel.onBloodTypeSelected(rhType, bloodType)
        }

        when(viewModel.allergy.value) {
            //해당 없음인 값인 경우,
            getString(R.string.condition_absent) -> {
                binding.noAllergy.isChecked = true
                binding.yesAllergy.isChecked = false
                binding.allergyInfo.visibility = View.GONE
            }
            //해당 있음인 값인 경우,
            getString(R.string.condition_present) -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = true
                binding.allergyInfo.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            getString(R.string.empty_string) -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = false
                binding.allergyInfo.visibility = View.GONE
            }
            else -> {
                binding.noAllergy.isChecked = false
                binding.yesAllergy.isChecked = true
                binding.allergyInfo.visibility = View.VISIBLE
                binding.allergyInfo.setText(viewModel.allergy.value)
            }
        }

        binding.allergyRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.yesAllergy -> {
                    viewModel.setAllergy(getString(R.string.condition_present))
                    binding.allergyInfo.visibility = View.VISIBLE
                }
                R.id.noAllergy -> {
                    viewModel.setAllergy(getString(R.string.condition_absent))
                    binding.allergyInfo.setText(getString(R.string.empty_string))
                    binding.allergyInfo.visibility = View.GONE
                }
            }
        }

        binding.allergyInfo.addTextChangedListener {
            if(!it.isNullOrBlank()) {
                viewModel.setAllergy(it.toString())
            }
        }

        //백 버튼 클릭시 이전 fragment 돌아가기
        requireActivity().onBackPressedDispatcher
            .addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    navController.navigateUp()
                    if(!SharedPreferenceUtil.getUpdateMode(requireActivity())) {
                        viewModel.resetViewModelData()
                    }
                }
            })
    }

    override fun onPause() {
        super.onPause()
        saveInfoData()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
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

        updateUIByValue1(
            value = viewModel.smoke.value,
            yesBtn = binding.smokeYesBtn,
            noBtn = binding.smokeNoBtn,
            infoView = binding.smokeInfo,
            inVisibleType = View.GONE
        )

        updateUIByValue1(
            value = viewModel.drink.value,
            yesBtn = binding.drinkYesBtn,
            noBtn = binding.drinkNoBtn,
            infoView = binding.drinkInfo,
            inVisibleType = View.GONE
        )

        updateUIByValue1(
            value = viewModel.etc.value,
            yesBtn = binding.etcYesBtn,
            noBtn = binding.etcNoBtn,
            infoView = binding.etcInfo,
            inVisibleType = View.GONE
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

    private fun updateUIByValue1(
        value: String?,
        yesBtn: RadioButton,
        noBtn: RadioButton,
        infoView: EditText,
        inVisibleType: Int) {
        when (value) {
            getString(R.string.condition_absent) -> {
                noBtn.isChecked = true
                yesBtn.isChecked = false
                infoView.visibility = inVisibleType
            }
            getString(R.string.condition_present) -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.visibility = View.VISIBLE
            }
            //라디오 버튼을 클릭하지 않았을 경우,
            getString(R.string.empty_string) -> {
                noBtn.isChecked = false
                yesBtn.isChecked = false
                infoView.visibility = inVisibleType
            }
            else -> {
                noBtn.isChecked = false
                yesBtn.isChecked = true
                infoView.setText(value)
                infoView.visibility = View.VISIBLE
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

                ViewAdditionUtils.addInputView(
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
        setInputListener()
        setAddViewListener()
    }

    private fun setToolBarListener() {
        //이전 버튼 클릭시
        binding.qnWriteCancelBtn.setOnClickListener {
            navController.navigateUp()
            if(!SharedPreferenceUtil.getUpdateMode(requireActivity())) {
                viewModel.resetViewModelData()
            }
        }

        binding.qnWriteAcceptBtn.text = if (SharedPreferenceUtil.getUpdateMode(requireActivity()))
            getString(R.string.update)
        else
            getString(R.string.complete)

        //수정/완료 버튼 클릭시
        binding.qnWriteAcceptBtn.setOnClickListener {
            if(SharedPreferenceUtil.getUpdateMode(requireActivity()))  {
                updateQuestionnaire()
            }
            else {
                postQuestionnaire()
            }
        }
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

        //final
        ViewEventUtils.setRadioGroupListener(
            context = requireContext(),
            radioGroup = binding.smokeRadioGroup,
            yesButton = binding.smokeYesBtn,
            noButton = binding.smokeNoBtn,
            infoEditText = binding.smokeInfo,
            visibleView = binding.smokeInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setSmoke(it) }
        )

        ViewEventUtils.setEditTextListener(
            editText = binding.smokeInfo,
            viewModelSetter = { viewModel.setSmoke(it) }
        )

        ViewEventUtils.setRadioGroupListener(
            context = requireContext(),
            radioGroup = binding.drinkRadioGroup,
            yesButton = binding.drinkYesBtn,
            noButton = binding.drinkNoBtn,
            infoEditText = binding.drinkInfo,
            visibleView = binding.drinkInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setDrink(it) }
        )

        ViewEventUtils.setEditTextListener(
            editText = binding.drinkInfo,
            viewModelSetter = { viewModel.setDrink(it) }
        )

        ViewEventUtils.setRadioGroupListener(
            context = requireContext(),
            radioGroup = binding.etcRadioGroup,
            yesButton = binding.etcYesBtn,
            noButton = binding.etcNoBtn,
            infoEditText = binding.etcInfo,
            visibleView = binding.etcInfo,
            visibleType = View.GONE,
            viewModelSetter = { viewModel.setEtc(it) }
        )

        ViewEventUtils.setEditTextListener(
            editText = binding.etcInfo,
            viewModelSetter = { viewModel.setEtc(it) }
        )
    }

    private fun setAddViewListener() {
        binding.addMedicineInfoBtn.setOnClickListener {
            ViewAdditionUtils.addInputView(
                parent = binding.medicineAddLayout,
                context = requireContext(),
                fragmentManager = childFragmentManager,
                nameHint = getString(R.string.medicine_edittext_name),
                dateHint = getString(R.string.medicine_edittext_date)
            )
        }

        binding.addSurgeryInfoBtn.setOnClickListener {
            ViewAdditionUtils.addInputView(
                parent = binding.surgeryAddLayout,
                context = requireContext(),
                fragmentManager = childFragmentManager,
                nameHint = getString(R.string.surgery_edittext_name),
                dateHint = getString(R.string.surgery_edittext_date)
            )
        }

        binding.addDiseaseInfoBtn.setOnClickListener {
            ViewAdditionUtils.addInputView(
                parent = binding.diseaseAddLayout,
                context = requireContext(),
                fragmentManager = childFragmentManager,
                nameHint = getString(R.string.disease_edittext_name),
                dateHint = getString(R.string.disease_edittext_date)
            )
        }
    }

    private fun updateQuestionnaire() {
        val questionnaireModel = viewModel.getQuestionnaireModel()

        api.updateQuestionnaire(questionnaireModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                navController.navigate(R.id.action_questionnaireWriteFragment_to_questionnaireFragment)

                viewModel.resetViewModelData()

                SharedPreferenceUtil.offUpdateMode(requireActivity())
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun postQuestionnaire() {
        viewModel.setPhoneNumber(SharedPreferenceUtil.getPhoneNumber(requireActivity()))

        val questionnaireModel = viewModel.getQuestionnaireModel()

        api.writeQuestionnaire(questionnaireModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    val result = response.body()

                    navController.navigate(R.id.action_questionnaireWriteFragment_to_questionnaireFragment)

                    viewModel.resetViewModelData()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}