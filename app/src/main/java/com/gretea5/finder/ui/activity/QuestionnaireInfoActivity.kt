package com.gretea5.finder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.gretea5.finder.R
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.data.model.QuestionnaireUnlinkModel
import com.gretea5.finder.databinding.ActivityQuestionnaireInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class QuestionnaireInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityQuestionnaireInfoBinding

    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionnaireInfoBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val qnData = intent.getSerializableExtra("qnInfo") as QuestionnaireModel

        binding.qnInfoName.text = qnData.name
        binding.qnInfoAge.text = "${qnData.age}세"
        binding.qnInfoGender.text = "(${qnData.gender})"

        if (qnData.phoneNumber.equals(getPhoneNumber())) {
            binding.qnInfoDeleteBtn.text = getString(R.string.delete)
        }
        else {
            binding.qnInfoDeleteBtn.text = getString(R.string.linkDelete)
            binding.qnInfoUpdateBtn.visibility = View.INVISIBLE
        }

        val phoneNumber = getPhoneNumber()

        binding.qnInfoDeleteBtn.setOnClickListener {
            if (qnData.phoneNumber.equals(getPhoneNumber())) {
                deleteQuestionnaire(phoneNumber)
            }
            else {
                unLinkQuestionnaire(phoneNumber, qnData.phoneNumber)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finishActivity()
    }

    private fun finishActivity() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun getPhoneNumber(): String {
        val pref = getSharedPreferences("pref", 0)

        return pref.getString(getString(R.string.phonenumber_key), "") ?: ""
    }

    private fun deleteQuestionnaire(phoneNumber: String) {
        api.deleteQuestionnaire(phoneNumber).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    finishActivity()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })
    }

    private fun unLinkQuestionnaire(phoneNumber1: String, phoneNumber2: String) {
        val data = QuestionnaireUnlinkModel(phoneNumber1, phoneNumber2)

        api.unLinkQuestionnaire(data).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    finishActivity()
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {}
        })
    }
}