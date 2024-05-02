package com.gretea5.finder.ui.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import com.gretea5.finder.R

class SearchAddressActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_address)

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(BridgeInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            //안드로이드에서 javascript를 실행시킬 수 있다.(콜벡 형태)
            //웹 뷰 페이지가 모두 로딩이 끝났을때, 호출된다.(2)
            override fun onPageFinished(view: WebView?, url: String?) {
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        //최초 웹뷰 로드(1)
        webView.loadUrl("https://finder-4cb5f.web.app")
    }

    inner class BridgeInterface {
        //html 코드와 android를 연결해주는 코드, data를 받아온다.
        @JavascriptInterface
        fun processDATA(data: String) {
            //다음(카카오) 주소 검색 API 결과 값 브릿지 통로를 통해 전달 받는다.
            val intent = Intent()
            intent.putExtra(getString(R.string.address), data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }
}