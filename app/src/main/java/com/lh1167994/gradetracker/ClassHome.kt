package com.lh1167994.gradetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lh1167994.gradetracker.databinding.ActivityClassHomeBinding

class ClassHome : AppCompatActivity() {
    private lateinit var binding: ActivityClassHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}