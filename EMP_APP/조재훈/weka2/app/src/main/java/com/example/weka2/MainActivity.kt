
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
    var list= mutableListOf<Any>()

    var first=true

    val numfolds = 10
    val numfold = 0
    val seed = 1

    var data: Instances = Instances(
        BufferedReader(
            FileReader("/data/data/com.example.weka2/files/activity_recognition.arff")
        )
    )



    val train = data.trainCV(numfolds, numfold)
    val test = data.testCV(numfolds, numfold)

    val eval: Evaluation by lazy {
    Evaluation(test)
}
    val model: Classifier = BayesNet()

    private val textview1: EditText by lazy {
        findViewById(R.id.textview1)
    }
    private val button1: Button by lazy {
        findViewById<Button>(R.id.button1)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button1.setOnClickListener {
            textview1.append(onCreateClassifier())
        }


    }

    private fun onCreateClassifier(): String {




        if (first){

            data.setClassIndex(data.numAttributes() - 1)

            train.setClassIndex(train.numAttributes() - 1)
            test.setClassIndex(test.numAttributes() - 1)

            eval.crossValidateModel(model, train, numfolds, Random(seed.toLong()))
            model.buildClassifier(train)
            eval.evaluateModel(model, test)
            first=false
        }

        
        for ((k, instance) in data.withIndex()) {

            val actual: Double = instance.classValue()
            val prediction: Double = eval.evaluateModelOnce(model, instance)


            if (prediction != actual) {
                val list1 = mutableListOf(k,actual, "$prediction *")
                list.add(k, list1)
            } else {
                val list1 = mutableListOf(k,actual, prediction)
                list.add(k, list1)
            }


        }



        var a : Instance =data.instance(233)
        val prediction =eval.evaluateModelOnce(model,a)


        return "\n"+list.toString() + "\n" +prediction
    }




}



