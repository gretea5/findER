package com.gretea5.finder.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.ui.model.QuestionnaireInfo

class QuestionnaireViewModel : ViewModel() {
    // MutableLiveData로 selectedBloodType 선언
    private val _phoneNumber = MutableLiveData<String>().apply { value = "" }
    private val _name = MutableLiveData<String>().apply { value = "" }
    private val _age = MutableLiveData<String>().apply { value = "" }
    private val _gender = MutableLiveData<String>().apply { value = "" }
    private val _bloodType = MutableLiveData<String>().apply { value = "" }
    private val _address = MutableLiveData<String>().apply { value = "" }
    private val _allergy = MutableLiveData<String>().apply { value = "" }
    private val _disease = MutableLiveData<String>().apply { value = "" }
    private val _medicine = MutableLiveData<String>().apply { value = "" }
    private val _surgery = MutableLiveData<String>().apply { value = "" }
    private val _drink = MutableLiveData<String>().apply { value = "" }
    private val _smoke = MutableLiveData<String>().apply { value = "" }
    private val _etc = MutableLiveData<String>().apply { value = "" }

    val phoneNumber: LiveData<String>
        get() = _phoneNumber
    val name: LiveData<String>
        get() = _name
    val age: LiveData<String>
        get() = _age
    val gender: LiveData<String>
        get() = _gender
    val bloodType: LiveData<String>
        get() = _bloodType
    val address: LiveData<String>
        get() = _address
    val allergy: LiveData<String>
        get() = _allergy
    val disease: LiveData<String>
        get() = _disease
    val medicine: LiveData<String>
        get() = _medicine
    val surgery: LiveData<String>
        get() = _surgery
    val drink: LiveData<String>
        get() = _drink
    val smoke: LiveData<String>
        get() = _smoke
    val etc: LiveData<String>
        get() = _etc

    fun setPhoneNumber(phoneNumber: String) {
        _phoneNumber.value = phoneNumber
    }

    fun setName(name: String) {
        _name.value = name
    }

    fun setAge(age: String) {
        _age.value = age
    }

    fun setAddress(address: String) {
        _address.value = address
    }

    fun setGender(gender: String) {
        _gender.value = gender
    }

    fun setBloodType(bloodType: String) {
        _bloodType.value = bloodType
    }

    fun setAllergy(allergy: String) {
        _allergy.value = allergy
    }

    fun setMedicine(medicine: String) {
        _medicine.value = medicine
    }

    fun setSurgery(surgery: String) {
        _surgery.value = surgery
    }

    fun setDisease(disease: String) {
        _disease.value = disease
    }

    fun setSmoke(smoke: String) {
        _smoke.value = smoke
    }

    fun setDrink(drink: String) {
        _drink.value = drink
    }

    fun setEtc(etc: String) {
        _etc.value = etc
    }

    fun onBloodTypeSelected(rhType: String, bloodType: String) {
        _bloodType.value = "$rhType $bloodType"
    }

    fun resetViewModelData() {
        _phoneNumber.value = ""
        _name.value = ""
        _age.value = ""
        _gender.value = ""
        _bloodType.value = ""
        _address.value = ""
        _allergy.value = ""
        _disease.value = ""
        _medicine.value = ""
        _surgery.value = ""
        _drink.value = ""
        _smoke.value = ""
        _etc.value = ""
    }

    fun getQuestionnaireModel() : QuestionnaireModel {
        val questionnaireModel = QuestionnaireModel()

        questionnaireModel.phoneNumber = phoneNumber.value.toString()
        questionnaireModel.name = name.value.toString()
        questionnaireModel.age = age.value.toString().toInt()
        questionnaireModel.gender = gender.value.toString()
        questionnaireModel.bloodType = bloodType.value.toString()
        questionnaireModel.address = address.value.toString()
        questionnaireModel.allergy = allergy.value.toString()
        questionnaireModel.disease = disease.value.toString()
        questionnaireModel.medicine = medicine.value.toString()
        questionnaireModel.surgery = surgery.value.toString()
        questionnaireModel.drink = drink.value.toString()
        questionnaireModel.smoke = smoke.value.toString()
        questionnaireModel.etc = etc.value.toString()

        Log.d("questionnaireModel.name", name.value.toString())
        Log.d("questionnaireModel.age", age.value.toString())
        Log.d("questionnaireModel.gender", gender.value.toString())
        Log.d("questionnaireModel.bloodType", bloodType.value.toString())
        Log.d("questionnaireModel.address", address.value.toString())
        Log.d("questionnaireModel.allergy", allergy.value.toString())
        Log.d("questionnaireModel.disease", disease.value.toString())
        Log.d("questionnaireModel.medicine", medicine.value.toString())
        Log.d("questionnaireModel.surgery", surgery.value.toString())
        Log.d("questionnaireModel.drink", drink.value.toString())
        Log.d("questionnaireModel.smoke", smoke.value.toString())
        Log.d("questionnaireModel.etc", etc.value.toString())

        return questionnaireModel
    }
}