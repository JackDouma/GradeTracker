package com.lh1167994.gradetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.lh1167994.gradetracker.databinding.ActivityClassHomeBinding


class ClassHome : AppCompatActivity(), ClassAdapter.ClassItemListener{
    private lateinit var binding: ActivityClassHomeBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClassHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        // on add class button press
        binding.addClassButton.setOnClickListener{
            // get string from the user input text field
            var className = binding.addClassEditText.text.toString()

            // if the text field is not empty
            if (className.isNotEmpty())
            {
                var uID = auth.currentUser.uid

                // create and send class to firestore
                var newClass = Class(className, null, uID, ArrayList())

                // connect
                val db = FirebaseFirestore.getInstance().collection("classes")

                // give a better name so we can tell what it is on the website
                var documentID = className + "-" + uID

                // add to the collection
                db.document(documentID).set(newClass)
                    .addOnSuccessListener {
                        // display result
                        Toast.makeText(this,"Class Added!",Toast.LENGTH_LONG).show()

                        // clear the text
                        binding.addClassEditText.text.clear()

                        // log project
                        db.get().addOnSuccessListener { collection ->
                            for (document in collection)
                            {
                                Log.i("Firestore","${document.id}=>${document.data}")
                            }
                        }
                    }
                    .addOnFailureListener { exception -> Log.w("DB_Issue",exception.localizedMessage)
                        Toast.makeText(this,"Class failed.",Toast.LENGTH_LONG).show()
                    }
            }
            // send error message back if it is empty
            else
            {
                Toast.makeText(this, "Class name is required",Toast.LENGTH_LONG).show()
            }

            //This will be used to connect the RecyclerView, adapeter and viewModel together
            val viewModel : ClassViewModel by viewModels()
            viewModel.getClasses().observe(this, {
                binding.recyclerView.adapter = ClassAdapter(this,it,this)
            })

        }
    }

    override fun classSelected(selectedClass: Class) {
        TODO("Not yet implemented")
    }

}