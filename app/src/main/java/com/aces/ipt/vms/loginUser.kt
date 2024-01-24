package com.aces.ipt.vms

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aces.ipt.vms.databinding.ActivityLoginUserBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.common.api.ApiException

class loginUser : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var binding: ActivityLoginUserBinding
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedPreferences: SharedPreferences


    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest


    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)



        oneTapClient = Identity.getSignInClient(this)
        signInRequest = BeginSignInRequest.builder()
            .setPasswordRequestOptions(BeginSignInRequest.PasswordRequestOptions.builder()
                .setSupported(true)
                .build())
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    // Your server's client ID, not your Android client ID.
                    .setServerClientId(getString(R.string.app_client_id))
                    // Only show accounts previously used to sign in.
                    .setFilterByAuthorizedAccounts(false)
                    .build())
            // Automatically sign in when exactly one credential is retrieved.
            .setAutoSelectEnabled(true)
            .build()


        binding.btnAdmin2.setOnClickListener {
            val intent = Intent(this@loginUser, LogIn::class.java)
            startActivity(intent)
        }

        binding.btnLoginGoogle.setOnClickListener {



           /* oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this) { result ->
                    try {
//                        startIntentSenderForResult(
//                            result.pendingIntent.intentSender, 1,
//                            null, 0, 0, 0, null)

                        val intent = Intent(this, Dashboard::class.java)
                        startActivity(intent)
                        finish()

                    } catch (e: IntentSender.SendIntentException) {
                        Log.e("OnSuccess ONE_TAP_LOGIN", "Couldn't start One Tap UI: ${e.localizedMessage}")
                    }
                }
                .addOnFailureListener(this) { e ->
                    // No saved credentials found. Launch the One Tap sign-up flow, or
                    // do nothing and continue presenting the signed-out UI.
                    Log.d("OnFailure ONE_TAP_LOGIN", e.localizedMessage)
                }
            */

        }


        editTextEmail = findViewById(R.id.emailAdd)
        editTextPassword = findViewById(R.id.emailPass)
        btnLogin = findViewById(R.id.btnLogin)

        sharedPreferences = getPreferences(Context.MODE_PRIVATE)

        btnLogin.setOnClickListener {
            val enteredEmail = editTextEmail.text.toString()
            val enteredPassword = editTextPassword.text.toString()

            if (enteredEmail.isNotEmpty() && enteredPassword.isNotEmpty()) {
                val storedEmail = sharedPreferences.getString("email", "user")
                val storedPassword = sharedPreferences.getString("password", "123")

                if (enteredEmail == storedEmail && enteredPassword == storedPassword) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, Dashboard::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Incorrect email or password", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d("REQ_ONE_TAP", "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d("REQ_ONE_TAP", "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d("REQ_ONE_TAP", "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                    Log.d("REQ_ONE_TAP EXCEPTION", e.message!!)
                    // ...
                }
            }
        }
    }
    // ...


}
