
package com.example.weka2
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

import weka.classifiers.Classifier
import weka.classifiers.Evaluation
import weka.classifiers.bayes.BayesNet
import weka.classifiers.bayes.NaiveBayes
import weka.classifiers.functions.SMOreg
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.core.SerializationHelper
import java.io.BufferedReader
import java.io.FileReader
import java.io.ObjectInputStream
import java.util.*


class MainActivity : AppCompatActivity() {

    //dataset 분석한 것과 실제 값들을 나타내기 위한 list
    var list= mutableListOf<Any>()

    //모델을 한번 만 만들기 위한 변수
    var first=true

    //train, test, cross validate setting 에 관련된 변수들
    val numfolds = 10
    val numfold = 0
    val seed = 1

    // data loader (훈련 데이터와 텍스트 데이터를 기본 8:2 로 분리한다.)

    var data: Instances = Instances(
        BufferedReader(
            FileReader("/data/data/com.example.weka2/files/activity_recognition.arff")
        )
    )

    //train,test를 만듦
    val train = data.trainCV(numfolds, numfold)
    val test = data.testCV(numfolds, numfold)

    // evaluate
    val eval: Evaluation by lazy {
    Evaluation(test)
}
    // BayesNet
    val model: Classifier = BayesNet()

    //결과를 보여주는 곳
    private val textview1: EditText by lazy {
        findViewById(R.id.textview1)
    }
    //누르면 결과가 나옴
    private val button1: Button by lazy {
        findViewById<Button>(R.id.button1)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //누르면 분석 결과가 뜸
        button1.setOnClickListener {
            textview1.append(onCreateClassifier())
        }


    }

    //분석 + 분석 결과 + return 함수
    private fun onCreateClassifier(): String {


        if (first){

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
            first=false
        }

        //data에 있는 모든 인덱스와 인스턴스를 사용
        for ((k, instance) in data.withIndex()) {

            //각 인스턴스의 실제값
            val actual: Double = instance.classValue()
            //각 인스턴스의 예측값
            val prediction: Double = eval.evaluateModelOnce(model, instance)

            //예측이 틀린 경우와 맞은 경우로 나누어서
            //다르게 list에 넣음
            if (prediction != actual) {
                val list1 = mutableListOf(k,actual, "$prediction *")
                list.add(k, list1)
            } else {
                val list1 = mutableListOf(k,actual, prediction)
                list.add(k, list1)
            }


        }


        //임의의 인스턴스를 고르고 예측함
        var a : Instance =data.instance(233)
        val prediction =eval.evaluateModelOnce(model,a)

        //list에 있는 내용과   prediction을 같이 출력
        return "\n"+list.toString() + "\n" +prediction
    }




}



