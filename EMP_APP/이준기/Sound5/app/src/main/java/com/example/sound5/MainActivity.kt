package com.example.sound5

import android.app.Activity
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.sound5.databinding.ActivityMainBinding

class MainActivity : Activity() {
    private  var mediaPlayer: MediaPlayer? = null

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


//        mediaPlayer = MediaPlayer.create(this, R.raw.sound1)
//        mediaPlayer?.start()

        val playButton : Button = findViewById(R.id.playButton)
        val pauseButton : Button = findViewById(R.id.pauseButton)
        val stopButton : Button = findViewById(R.id.stopButton)


        playButton.setOnClickListener {
            if (mediaPlayer == null) {  // 재생음악 중복 방지 코드, null일때만 초기화
                mediaPlayer = MediaPlayer.create(this, R.raw.sound1)  // play버튼 클릭시 미디어플레이어 초기화 및 재생
                Toast.makeText(this@MainActivity, "버튼이 클릭 되었습니다.", Toast.LENGTH_LONG).show()
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


    }

    override fun onStop() {
        super.onStop()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}