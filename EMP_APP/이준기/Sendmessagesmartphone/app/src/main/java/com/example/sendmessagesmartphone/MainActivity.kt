package com.example.sendmessagesmartphone

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Telephony
import android.telephony.SmsManager
import android.widget.Button
import android.widget.EditText
import android.widget.MultiAutoCompleteTextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.util.jar.Manifest

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
        if(requestCode==111 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
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
}

