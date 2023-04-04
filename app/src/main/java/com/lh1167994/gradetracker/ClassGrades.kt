package com.lh1167994.gradetracker

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.ktx.Firebase
import com.lh1167994.gradetracker.databinding.ActivityClassGradesBinding


class ClassGrades : AppCompatActivity(), GradeAdapter.GradeItemListener
{
    private lateinit var binding: ActivityClassGradesBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityClassGradesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        // get data from previous activity
        var classID = intent.getStringExtra("documentID")
        var className = intent.getStringExtra("className")
        binding.titleTextView.text = className

        // on add class button press
        binding.addGradeButton.setOnClickListener{
            // get string from the user input text field
            var gradeName = binding.addGradeNameEditText.text.toString()
            var grade = binding.addGradeEditText.text.toString()
            var weight = binding.addGradeWeightEditText.text.toString()

            // if the text field is not empty
            if (gradeName.isNotEmpty() && grade.isNotEmpty() && weight.isNotEmpty())
            {
                // make sure grade and weight is between 0-100
                if (grade.toInt() >= 0 && grade.toInt() <= 100 && weight.toInt() >= 0 && weight.toInt() <= 100)
                {
                    var uID = auth.currentUser.uid

                    // create and send grade to firestore
                    var newGrade = Grade(gradeName, className, grade.toInt(), weight.toInt(), uID)

                    // connect
                    val db = FirebaseFirestore.getInstance().collection("grades")

                    // give a better name so we can tell what it is on the website
                    var documentID = gradeName + "-" + className + "-" + uID

                    // add to the collection
                    db.document(documentID).set(newGrade)
                        .addOnSuccessListener {
                            // display result
                            Toast.makeText(this,"Grade Added!",Toast.LENGTH_LONG).show()

                            // clear the edit text fields
                            binding.addGradeNameEditText.text.clear()
                            binding.addGradeEditText.text.clear()
                            binding.addGradeWeightEditText.text.clear()

                            db.get().addOnSuccessListener { collection ->
                                for (document in collection)
                                {
                                    Log.i("Firestore","${document.id}=>${document.data}")
                                }
                            }
                                .addOnFailureListener{}

                        }
                        .addOnFailureListener { exception -> Log.w("DB_Issue",exception.localizedMessage)
                            Toast.makeText(this,"Class failed.",Toast.LENGTH_LONG).show()
                        }
                }
                else
                {
                    Toast.makeText(this, "Grade and weight must be between 0-100",Toast.LENGTH_LONG).show()
                }
            }
            // send error message back if it is empty
            else
            {
                Toast.makeText(this, "Grade name, mark, and weight required.",Toast.LENGTH_LONG).show()
            }


        }

        /**
         * back to home button
         */
        binding.backButton.setOnClickListener {
            finish()
        }

        // connects recycler, adapter, and view model together
        val viewModel : GradeViewModel by viewModels()
        viewModel.getGrades().observe(this, {
            binding.recyclerView.adapter = GradeAdapter(this, it, this)
        })

        if (className != null) {
            calculate(className)
        }
    }

    /**
     * grade selected function
     */
    override fun gradeSelected(selectedGrade: Grade) {
        Toast.makeText(this, "Grade: ${selectedGrade.percentage}% Weight: ${selectedGrade.weight}%",Toast.LENGTH_LONG).show()
    }

    /**
     * this function will calculate all the grade info
     */
    fun calculate(className: String)
    {
        val grades : MutableList<Grade?> = ArrayList()
        // variables
        var totalGrades : Int = 0
        var totalPercentages : Int = 0
        var totalWeights : Int = 0
        var remaining : Int = 0
        var gradePercentage : Int = 0

        // get text from xml
        val gradeText = binding.gradeTextView.text.toString()
        var remainingText = binding.remainingTextView.text.toString()
        val passAverageText = binding.passAverageTextView.text.toString()
        val honoursAverageText = binding.honoursAverageTextView.text.toString()

        val db = FirebaseFirestore.getInstance().collection("grades")

        // get total percentages
        db.whereEqualTo("className", className)
            .get()
            .addOnSuccessListener{
                for (document in it)
                {
                    grades.add(document.toObject(Grade::class.java))
                }
            }

//        for (x in 0..grades.size)
//        {
//            totalPercentages = totalPercentages + grades.get(x)?.percentage!!
//            totalWeights = totalWeights + grades.get(x)?.weight!!
//        }

        // calculate grade and weight
        remaining = 100 - totalWeights!!
        totalPercentages = totalPercentages / 100
        totalWeights = totalWeights / 100

        gradePercentage = totalPercentages * totalWeights

        totalPercentages = totalPercentages * 100
        totalWeights = totalWeights * 100

        // display results
        binding.gradeTextView.text = gradeText + gradePercentage.toString() + "%"
        binding.remainingTextView.text = remainingText + remaining.toString() + "%"
        binding.passAverageTextView.text = passAverageText + "50%"
        binding.honoursAverageTextView.text = honoursAverageText + "80%"
    }
}
