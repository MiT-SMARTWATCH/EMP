package com.example.EMP

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat

class Foreground : Service() {
    val CHANNEL_ID = "FGS153"   //안드로이드 오레오버전부터 채널 사용(Notification)
    val NOTI_ID = 153  //startForeground를 위한 ID 생성

    fun createNotificationChannel() {      //Notification 채널 함수 생성
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {    // 만약 버전이 오레오버전 이상일 때
            val serviceChannel = NotificationChannel(CHANNEL_ID, "FOREGROUND", NotificationManager.IMPORTANCE_DEFAULT )    // 서비스 채널 생성(채널ID:채널이름, 알림의 강도 설정)
            val manager = getSystemService(NotificationManager::class.java)  // Notification매니저를 시스템 서비스를 통해 생성
            manager.createNotificationChannel(serviceChannel)  // Notification채널에 서비스채널을 삽입
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {  // 서비스 시작버튼 함수 실행시

        createNotificationChannel()   // 채널 생성
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)  // 띄울 Notification 생성
            .setContentTitle("Foreground Service")  // 추가적인 옵션 -> Notification에 보여주는 제목 Text
            .setSmallIcon(R.mipmap.ic_launcher_round)  // Icon 삽입
            .build()  // build 호출 -> 변수애 저장

        startForeground(NOTI_ID, notification)



        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}
