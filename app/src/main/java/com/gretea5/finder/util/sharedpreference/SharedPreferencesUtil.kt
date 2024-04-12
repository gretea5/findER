package com.gretea5.finder.util.sharedpreference

import android.content.Context
import com.gretea5.finder.R

object SharedPreferenceUtil {
    enum class SharedPreferenceMode(val value: Int) {
        //sharedPreference는 private mode 0번 => 앱에서 액티비티나 서비스에서 엑세스 가능
        //다른 앱에서는 엑세스가 불가하다.
        MODE_PRIVATE(Context.MODE_PRIVATE)
    }

    fun getPhoneNumber(context: Context): String {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.pref_name),
            SharedPreferenceMode.MODE_PRIVATE.value
        )
        val emptyString = context.getString(R.string.empty_string)
        return sharedPref.getString(context.getString(R.string.phonenumber_key), emptyString) ?: emptyString
    }

    fun setUpdateMode(context: Context) {
        //전화번호 getPreferences 저장
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.pref_name),
            SharedPreferenceMode.MODE_PRIVATE.value
        )
        val sharedEditor = sharedPref.edit()
        sharedEditor.putBoolean(context.getString(R.string.edit_mode), true)
        sharedEditor.apply()
    }

    fun getUpdateMode(context: Context): Boolean {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.pref_name),
            SharedPreferenceMode.MODE_PRIVATE.value
        )
        return sharedPref.getBoolean(context.getString(R.string.edit_mode), false)
    }

    fun offUpdateMode(context: Context) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.pref_name),
            SharedPreferenceMode.MODE_PRIVATE.value
        )
        val sharedEditor = sharedPref.edit()
        sharedEditor.putBoolean(context.getString(R.string.edit_mode), false)
        sharedEditor.apply()
    }

    fun savePhoneNumber(context: Context, phoneNumber: String) {
        //전화번호 getPreferences 저장
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.pref_name),
            SharedPreferenceMode.MODE_PRIVATE.value
        )
        val sharedEditor = sharedPref.edit()
        sharedEditor.putString(context.getString(R.string.phonenumber_key), phoneNumber)
        sharedEditor.apply()
    }
}