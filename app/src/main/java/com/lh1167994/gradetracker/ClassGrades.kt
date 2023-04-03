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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
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


        binding.backButton.setOnClickListener {
            finish()
        }

        // connects recycler, adapter, and view model together
        val viewModel : GradeViewModel by viewModels()
        viewModel.getGrades().observe(this, {
            binding.recyclerView.adapter = GradeAdapter(this, it, this)
        })
    }

    override fun gradeSelected(selectedGrade: Grade) {
        TODO("Not yet implemented")
    }

    fun calculate()
    {
        val gradeText = binding.gradeTextView.text.toString()
    }
}
