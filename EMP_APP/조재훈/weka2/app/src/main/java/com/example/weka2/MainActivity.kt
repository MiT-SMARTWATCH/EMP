package com.example.weka2

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

    var list = mutableListOf<Any>()


    var first = true


    val numfolds = 10
    val numfold = 0
    val seed = 1


    var data: Instances = Instances(
        BufferedReader(
            FileReader("/data/data/com.example.weka2/files/activity_recognition.arff")
        )
    )


    private val train = data.trainCV(numfolds, numfold)
    private val test = data.testCV(numfolds, numfold)

    private val eval: Evaluation by lazy {
        Evaluation(test)
    }
    private val model: Classifier = BayesNet()

    private val textview1: EditText by lazy {
        findViewById(R.id.textview1)
    }
    private val button1: Button by lazy {
        findViewById<Button>(R.id.button1)
    }

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
        serviceStart(setContentView(R.layout.activity_main))


        Handler().postDelayed(Runnable {
            onCreateClassifier()
            //딜레이 후 시작할 코드 작성
        }, 6000) // 6초 정도 딜레이를 준 후 시작



    }

    private fun onCreateClassifier(): String {


        if (first) {

            data.setClassIndex(data.numAttributes() - 1)

            train.setClassIndex(train.numAttributes() - 1)
            test.setClassIndex(test.numAttributes() - 1)

            eval.crossValidateModel(model, train, numfolds, Random(seed.toLong()))
            model.buildClassifier(train)
            eval.evaluateModel(model, test)
            first = false
        }


        for ((k, instance) in data.withIndex()) {

            val actual: Double = instance.classValue()
            val prediction: Double = eval.evaluateModelOnce(model, instance)


            if (prediction != actual) {
                val list1 = mutableListOf(k, actual, "$prediction *")
                list.add(k, list1)
            } else {
                val list1 = mutableListOf(k, actual, prediction)
                list.add(k, list1)
            }


        }

        val list_prediction = mutableListOf<Any>()
        var prediction: Double = 3.0

        for (i in 0 until data.size) {
            var a: Instance = data.instance(i)
            prediction = eval.evaluateModelOnce(model, a)

            if (prediction == 3.0) {
                list_prediction.add(i)


            }
        }


        if (list_prediction.size > 5) {

            var alarm_intent = Intent(this, MessageAcivity::class.java)
            alarm_intent.putExtra("상태", prediction)

            startActivity(alarm_intent)
            // 5개가 넘어가면 그 파이
        }
        return list_prediction.toString()
    }



}



