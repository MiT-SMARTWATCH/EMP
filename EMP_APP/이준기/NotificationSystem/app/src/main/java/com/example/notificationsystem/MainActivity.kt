package com.example.notificationsystem

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val playButton : Button = findViewById(R.id.playButton)
        val pauseButton : Button = findViewById(R.id.pauseButton)
        val stopButton : Button = findViewById(R.id.stopButton)


        playButton.setOnClickListener {
            if (mediaPlayer == null) {  // 재생음악 중복 방지 코드, null일때만 초기화
                mediaPlayer = MediaPlayer.create(this, R.raw.sound1)  // play버튼 클릭시 미디어플레이어 초기화 및 재생
                Toast.makeText(this@MainActivity, "***위급상황입니다***", Toast.LENGTH_LONG).show()
            }
            mediaPlayer?.start()
        }

        pauseButton.setOnClickListener {
            if (mediaPlayer?.isPlaying == true) { // 일시정지후 다시 재생시키는 코드
                mediaPlayer?.pause()  // 미디어플레이어 일시정지
            }else {
                mediaPlayer?.start()     // 미디어플레이어 다시 시작
            }

        }

        stopButton.setOnClickListener {
            mediaPlayer?.stop()    // 미디어플레이어 종료
            mediaPlayer = null

        }
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)  // 퍼미년 sms 부여 조건문
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS),
                111)   //requestcode 111 생성
        }
        else
            receiveMsg()           // 그밖에 receiveMessage

        val button:Button=findViewById(R.id.button)
        val editTextPhone : EditText=findViewById(R.id.editTextPhone)
        val editTextTextMultiLine : EditText=findViewById(R.id.editTextTextMultiLine)

        button.setOnClickListener {     // 버튼 클릭시
            var sms = SmsManager.getDefault()   // sms 변서값 지정
            sms.sendTextMessage(editTextPhone.text.toString(), "ME", editTextTextMultiLine.text.toString(), null, null)
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode==111 && grantResults[0]== PackageManager.PERMISSION_GRANTED)
            receiveMsg()
    }




    private fun receiveMsg(){  // receiveMessage 함수 생성

        val editTextPhone : EditText=findViewById(R.id.editTextPhone)
        val editTextTextMultiLine : EditText=findViewById(R.id.editTextTextMultiLine)

        var br = object: BroadcastReceiver(){
            override fun onReceive(p0: Context?, p1: Intent?) {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    for(sms in Telephony.Sms.Intents.getMessagesFromIntent(p1)){
                        editTextPhone.setText(sms.originatingAddress)
                        editTextTextMultiLine.setText(sms.displayMessageBody)





                    }

                }
            }


        }
        registerReceiver(br, IntentFilter("andoid.provider.Telepony.SMS_RECEIVED"))


    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null

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