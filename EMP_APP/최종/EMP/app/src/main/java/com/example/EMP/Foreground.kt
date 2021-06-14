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

    //안드로이드 오레오버전부터 채널 사용 (Notification)
    val CHANNEL_ID = "FGS153"
    //startForeground를 위한 ID 생성
    val NOTI_ID = 153

    //Notification 채널 함수 생성
    fun createNotificationChannel() {
        //만약 버전이 오레오 이상일 때
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //서비스 채널 생성 (채널 ID: 채널 이름, 알림의 강도설정)
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "FOREGROUND",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            //Notification 메니저를 시스템 서비스를 통해 생성
            val manager = getSystemService(NotificationManager::class.java)
            //
            manager.createNotificationChannel(serviceChannel)
        }
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        createNotificationChannel()
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("FOREGROUND SERVICE")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .build()

        startForeground(NOTI_ID, notification)


        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
}
