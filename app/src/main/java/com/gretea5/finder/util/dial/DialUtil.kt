package com.gretea5.finder.util.dial

import android.content.Context
import android.content.Intent
import android.net.Uri

object DialUtil {
    fun openDial(context: Context, tel: String) {
        val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$tel"))

        context.startActivity(intent)
    }
}