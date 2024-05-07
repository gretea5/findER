package com.gretea5.finder.conf

import android.app.Application
import com.gretea5.finder.BuildConfig
import com.kakao.vectormap.KakaoMapSdk

class KakaoMapConf : Application() {
    override fun onCreate() {
        super.onCreate()
        KakaoMapSdk.init(this, BuildConfig.KAKAO_MAP_KEY)
    }
}