package com.example.notification

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.example.notification.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val btn_toast:Button=findViewById(R.id.btn_toast)
        val iv_profile:ImageView=findViewById(R.id.iv_profile)


        btn_toast.setOnClickListener {

            iv_profile.setImageResource(R.mipmap.android2) // 이미지 뷰에 새로운 이미지 등록

            Toast.makeText(this@MainActivity, "버튼이 클릭 되었습니다.", Toast.LENGTH_LONG).show() // 토스트메시지 str 지정

        }

    }
}