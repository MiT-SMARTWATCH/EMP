package com.example.foreground

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun serviceStart(view: View){  //서비스 시작 버튼 함수 설정-> 실행시 포그라운드 실행
        val intent = Intent(this, ForegroundService::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    fun serviceStop(view: View) {   // 서비스 종료 버튼 함수 설정 -> 포그라운드 종료
        val intent = Intent(this, ForegroundService::class.java)
        stopService(intent)

    }
}