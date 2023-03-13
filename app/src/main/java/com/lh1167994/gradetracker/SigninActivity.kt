package com.lh1167994.gradetracker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth

class SigninActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        // google and email will be the log in options
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build())

        // log in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.apple_logo)
            .build()
        signInLauncher.launch(signInIntent)
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    )
    { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult)
    {
        val response = result.idpResponse

        if (result.resultCode == RESULT_OK)
        {
            // sign in success
            val user = FirebaseAuth.getInstance().currentUser
            val intent = Intent(this, ClassHome::class.java)

            intent.putExtra("user", user)
            startActivity(intent)
        }
        else
        {
            // sign in fail
            Toast.makeText(this, "Sign in Failed.", Toast.LENGTH_LONG).show()
            startActivity(Intent(this, SigninActivity::class.java))
        }
    }
}