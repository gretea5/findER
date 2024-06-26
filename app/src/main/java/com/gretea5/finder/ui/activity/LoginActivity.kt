package com.gretea5.finder.ui.activity

import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.R
import com.gretea5.finder.data.model.AuthData
import com.gretea5.finder.ui.activity.SignupActivity.Companion.PHONE_NUMBER_SIZE
import com.gretea5.finder.ui.activity.SignupActivity.Companion.RESIDENCE_NUMBER_SIZE
import com.gretea5.finder.databinding.ActivityLoginBinding
import com.gretea5.finder.util.sharedpreference.SharedPreferenceUtil.savePhoneNumber
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    private val txtSignup: TextView by lazy { binding.txtSignup }
    private val residenceNumberTextView : TextView by lazy { binding.residenceNumberTextView }

    private val phoneEditText : EditText by lazy { binding.phoneEditText }
    private val residenceNumberEditText : EditText by lazy { binding.residenceNumberEditText }

    private val btnSignin : Button by lazy { binding.btnSignin }

    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        //회원가입 textView 밑줄 지정
        txtSignup.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        //회원가입 클릭시 회원가입 페이지 이동
        txtSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        //전화번호 형식 - 지정
        //phoneEditText.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        btnSignin.setOnClickListener {
            requestLogin()
        }

        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEditTextStatus(
                    residenceNumberEditText,
                    residenceNumberTextView,
                    s?.length == PHONE_NUMBER_SIZE
                )
            }
        })

        residenceNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setButtonStatus(btnSignin, s?.length == RESIDENCE_NUMBER_SIZE)
            }
        })
    }

    private fun setButtonStatus(button : Button, enabled : Boolean) {
        button.setBackgroundResource(if(enabled) R.drawable.button_activate else R.drawable.button_deactivate)
        button.isEnabled = enabled
    }

    private fun setEditTextStatus(
        editText: EditText,
        textView: TextView,
        isEnabled: Boolean
    ) {
        val buttonBorder =
            if (isEnabled) R.drawable.edittext_accessible_border else R.drawable.edittext_unaccessible_border
        val textColor = if (isEnabled) R.color.main_color else R.color.grey

        editText.isEnabled = isEnabled
        editText.setBackgroundResource(buttonBorder)
        textView.setTextColor(ContextCompat.getColor(this, textColor))
    }

    private fun requestLogin() {
        val phoneNumber = binding.phoneEditText.text.toString()
        val rrn = binding.residenceNumberEditText.text.toString()

        val loginModel = AuthData(phoneNumber, rrn)

        api.login(loginModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    savePhoneNumber(baseContext, phoneNumber)

                    val intent = Intent(applicationContext, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                }
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                //TODO("Not yet implemented")
            }
        })
    }
}