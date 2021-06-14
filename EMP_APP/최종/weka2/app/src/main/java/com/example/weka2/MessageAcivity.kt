package com.example.weka2

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.provider.Telephony
import android.telephony.SmsManager
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MessageAcivity : AppCompatActivity() {
    private var mediaPlayer: MediaPlayer? = null
    fun serviceStart(view: Unit){
        //서비스 시작 버튼 함수 설정-> 실행시 포그라운드 실행
        val intent = Intent(this, Foreground::class.java)
        ContextCompat.startForegroundService(this, intent)
    }

    fun serviceStop(view: View) {
        // 서비스 종료 버튼 함수 설정 -> 포그라운드 종료
        val intent = Intent(this, Foreground::class.java)
        stopService(intent)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message)
        val stopButton : Button = findViewById(R.id.stopButton)


        val intent = intent

        val a = intent.extras?.get("상태")

        if (a==17.0){
            if (mediaPlayer == null) {  // 재생음악 중복 방지 코드, null일때만 초기화
                mediaPlayer = MediaPlayer.create(this, R.raw.sound1)  // play버튼 클릭시 미디어플레이어 초기화 및 재생
                Toast.makeText(this@MessageAcivity, "***위급상황입니다***", Toast.LENGTH_LONG).show()
            }
            mediaPlayer?.start()

            if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS),
                    111)
            }
            else
                receiveMsg()

            val button:Button=findViewById(R.id.button)
            val editTextPhone : EditText =findViewById(R.id.editTextPhone)
            editTextPhone.text.append("+15555215554")

            val editTextTextMultiLine : EditText =findViewById(R.id.editTextTextMultiLine)
            editTextTextMultiLine.text.append("위급상황입니다")


            var sms = SmsManager.getDefault()
            sms.sendTextMessage(editTextPhone.text.toString(), "ME", editTextTextMultiLine.text.toString(), null, null)

        }



        stopButton.setOnClickListener {
            mediaPlayer?.stop()    // 미디어플레이어 종료
            mediaPlayer = null
            var alarm_intent = Intent(this, MainActivity::class.java)
            startActivity(alarm_intent)

        }
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.RECEIVE_SMS, android.Manifest.permission.SEND_SMS),
                111)
        }
        else
            receiveMsg()

        val button:Button=findViewById(R.id.button)
        val editTextPhone : EditText =findViewById(R.id.editTextPhone)
        val editTextTextMultiLine : EditText =findViewById(R.id.editTextTextMultiLine)

        button.setOnClickListener {
            var sms = SmsManager.getDefault()
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




    private fun receiveMsg(){

        val editTextPhone : EditText =findViewById(R.id.editTextPhone)
        val editTextTextMultiLine : EditText =findViewById(R.id.editTextTextMultiLine)

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

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null

    }





}