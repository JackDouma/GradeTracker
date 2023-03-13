package com.lh1167994.gradetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.lh1167994.gradetracker.databinding.ActivityClassGradesBinding

class ClassGrades : AppCompatActivity() {
    private lateinit var binding: ActivityClassGradesBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassGradesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // get data from previous activity
        var documentID = intent.getStringExtra("documentID")
        var className = intent.getStringExtra("className")

        val titleTextView: TextView = findViewById(R.id.titleTextView)
        titleTextView.text = className

        binding.backButton.setOnClickListener {
            finish()
        }
    }
}