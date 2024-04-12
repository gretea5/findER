package com.gretea5.finder.ui.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.gretea5.finder.data.ApiService
import com.gretea5.finder.R
import com.gretea5.finder.data.model.SignupModel
import com.gretea5.finder.databinding.ActivitySignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    companion object {
        const val PHONE_NUMBER_SIZE = 11
        const val RESIDENCE_NUMBER_SIZE = 13
    }
    private lateinit var binding: ActivitySignupBinding

    private val phoneEditText: EditText by lazy { binding.phoneEditText }
    private val residenceNumberEditText: EditText by lazy { binding.residenceNumberEditText }
    private val editText: EditText by lazy { binding.editText }

    private val btnSignup: Button by lazy { binding.btnSignup }

    private val residenceNumberTitle: TextView by lazy { binding.residenceNumberTitle }
    private val residenceNumberReInputTitle: TextView by lazy { binding.residenceNumberReInputTitle }

    private val phoneNumberSize = 11
    private val residenceNumberSize = 13

    private val api = ApiService.create()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        residenceNumberEditText.isEnabled = false
        editText.isEnabled = false
        btnSignup.isEnabled = false

        phoneEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEditTextStatus(
                    residenceNumberEditText,
                    residenceNumberTitle,
                    s?.length == phoneNumberSize
                )
            }
        })

        residenceNumberEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(editable: Editable?) {}
            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setEditTextStatus(
                    editText,
                    residenceNumberReInputTitle,
                    s?.length == residenceNumberSize
                )
            }
        })

        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {}

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (s?.length == residenceNumberSize && s.toString() == residenceNumberEditText.text.toString()
                ) {
                    btnSignup.setBackgroundResource(R.drawable.button_activate)
                    btnSignup.isEnabled = true
                } else {
                    btnSignup.setBackgroundResource(R.drawable.button_deactivate)
                    btnSignup.isEnabled = false
                }
            }
        })

        btnSignup.setOnClickListener {
            showSignupDialog()
        }
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

    private fun showSignupDialog() {
        val signupDialogView = layoutInflater.inflate(R.layout.dialog_signup, null)

        val dialog = AlertDialog.Builder(this)
            .setView(signupDialogView)
            .create()

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        dialog.show()

        dialog.findViewById<TextView>(R.id.signUpDialogContent)?.text = "입력하신 주민번호는\n${binding.residenceNumberEditText.text}입니다.\n회원가입 하시겠습니까?"


        dialog.findViewById<TextView>(R.id.signUpDialogYesBtn)?.setOnClickListener {
            requestSignup()

            dialog.dismiss()
        }

        dialog.findViewById<TextView>(R.id.signUpDialogNoBtn)?.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun backToLoginActivity() {
        finish()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun requestSignup() {
        val phoneNumber = binding.phoneEditText.text.toString()
        val rrn = binding.residenceNumberEditText.text.toString()

        Log.d("SignupActivity", phoneNumber)
        Log.d("SignupActivity", rrn)

        val signupModel = SignupModel(phoneNumber, rrn)

        api.signUp(signupModel).enqueue(object : Callback<String> {
            override fun onResponse(call: Call<String>, response: Response<String>) {
                if(response.isSuccessful) {
                    backToLoginActivity()
                }
                /*로그인 실패시 처리해야할 로직
                else {
                    Log.d("requestSignup",  "request fail!")
                    Log.d("requestSignup",  response.code().toString())
                    Log.d("requestSignup", response.body().toString())
                }
                 */
            }

            override fun onFailure(call: Call<String>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}