package com.lh1167994.gradetracker

import android.text.Spannable.Factory
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class GradeViewModel : ViewModel()
{
    private var grades = MutableLiveData<List<Grade>>()
    // connect to the firestore and update the gradess
    init{
        val userID = Firebase.auth.currentUser.uid

        val db = FirebaseFirestore.getInstance().collection("grades")
            .whereEqualTo("uid", userID)
            .whereEqualTo("className", "Java")
            .orderBy("gradeName")
            .addSnapshotListener{ documents, exception ->
                if (exception != null)
                {
                    Log.w("DB_Response","Listen failed ${exception.code}")
                    return@addSnapshotListener
                }

                documents?.let { documents
                    val gradeList = ArrayList<Grade>()

                    for (document in documents)
                    {
                        Log.i("DB_Response","${document.data}")
                        val selectedGrade = document.toObject(Grade::class.java)
                        gradeList.add(selectedGrade)
                    }

                    grades.value = gradeList
                }
            }
    }

    // returns list of grades
    fun getGrades() : LiveData<List<Grade>>
    {
        return grades
    }
}