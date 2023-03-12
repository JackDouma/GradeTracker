package com.lh1167994.gradetracker

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

class ClassViewModel : ViewModel()
{
    private var classes = MutableLiveData<List<Class>>()

    // connect to the firestore and update the classes
    init{
        val userID = Firebase.auth.currentUser.uid

        val db = FirebaseFirestore.getInstance().collection("classes")
            .whereEqualTo("uid", userID)
            .orderBy("className")
            .addSnapshotListener{ documents, exception ->
                if (exception != null)
                {
                    Log.w("DB_Response","Listen failed ${exception.code}")
                    return@addSnapshotListener
                }

                documents?.let { documents
                    val classList = ArrayList<Class>()

                    for (document in documents)
                    {
                        Log.i("DB_Response","${document.data}")
                        val selectedClass = document.toObject(Class::class.java)
                        classList.add(selectedClass)
                    }

                    classes.value = classList
                }
            }
    }

    // returns list of classes
    fun getClasses() : LiveData<List<Class>>
    {
        return classes
    }
}