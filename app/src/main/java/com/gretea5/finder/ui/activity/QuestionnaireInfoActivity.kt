package com.gretea5.finder.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.gretea5.finder.R
import com.gretea5.finder.data.model.QuestionnaireModel
import com.gretea5.finder.databinding.ActivityQuestionnaireInfoBinding

class QuestionnaireInfoActivity : AppCompatActivity() {
    private lateinit var binding : ActivityQuestionnaireInfoBinding

    private val TAG = "QuestionnaireInfoActivity"
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

        Log.d("TAG", qnData.toString())
    }

    override fun onBackPressed() {
        super.onBackPressed()

        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun getPhoneNumber(): String {
        val pref = getSharedPreferences("pref", 0)

        return pref.getString(getString(R.string.phonenumber_key), "") ?: ""
    }

}