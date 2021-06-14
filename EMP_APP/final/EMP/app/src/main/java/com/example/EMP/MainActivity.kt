package com.example.EMP

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import weka.classifiers.Classifier
import weka.classifiers.Evaluation
import weka.classifiers.bayes.BayesNet
import weka.core.Instance
import weka.core.Instances
import java.io.BufferedReader
import java.io.FileReader
import java.util.*

class MainActivity : AppCompatActivity() {


    //모델을 한번 만 만들기 위한 변수
    var first = true

    //train, test, cross validate setting 에 관련된 변수들
    val numfolds = 10
    val numfold = 0
    val seed = 1


    // data loader (훈련 데이터와 텍스트 데이터를 기본 8:2 로 분리한다.)
    var data: Instances = Instances(
        BufferedReader(
            FileReader("/data/data/com.example.EMP/files/activity_recognition.arff")
        )
    )


    //train,test를 만듦
    private val train = data.trainCV(numfolds, numfold)
    private val test = data.testCV(numfolds, numfold)

    // evaluate
    private val eval: Evaluation by lazy {
        Evaluation(test)
    }
    // BayesNet
    private val model: Classifier = BayesNet()

    fun serviceStart(view: Unit) {
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
        setContentView(R.layout.activity_main)
        //포그라운드 실행
        serviceStart(setContentView(R.layout.activity_main))


        Handler().postDelayed(Runnable {
            onCreateClassifier()
            //딜레이 후 시작할 코드 작성
        }, 6000) // 6초 정도 딜레이를 준 후 시작


    }

    //분석을 하고 한 후에는 MessageActivity로 전환
    private fun onCreateClassifier(): String {


        if (first) {

            //class assigner
            data.setClassIndex(data.numAttributes() - 1)
            train.setClassIndex(train.numAttributes() - 1)
            test.setClassIndex(test.numAttributes() - 1)

            //cross validate setting
            eval.crossValidateModel(model, train, numfolds, Random(seed.toLong()))
            // BayesNet run
            model.buildClassifier(train)
            // BayesNet evaluate
            eval.evaluateModel(model, test)
            //BayesNet 다시 만들지 않음
            first = false
        }



        val list_prediction = mutableListOf<Any>()
        //임의의 수를 넘어줌
        var prediction: Double = 3.0

        for (i in 0 until data.size) {
            // 하나의 인스턴스를 받고 예측
            var a: Instance = data.instance(i)
            prediction = eval.evaluateModelOnce(model, a)

            //3이 나올경우 저장
            if (prediction == 3.0) {
                list_prediction.add(i)


            }
        }


        // 사이즈가 5를 넘으면 화면 전환하면서
        //Key는 "상태", value는 prediction이라는 데이터도 같이 전송
        if (list_prediction.size > 5) {

            var alarm_intent = Intent(this, MessageAcivity::class.java)
            alarm_intent.putExtra("상태", prediction)

            startActivity(alarm_intent)

        }
        return list_prediction.toString()
    }



}



