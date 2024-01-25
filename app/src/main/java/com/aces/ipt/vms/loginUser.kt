package com.aces.ipt.vms

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aces.ipt.vms.databinding.ActivityLoginUserBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class loginUser : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var binding: ActivityLoginUserBinding
    private lateinit var editTextPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var sharedPreferences: SharedPreferences




    private lateinit var credential: AuthCredential
    private lateinit var gmail: String


    private lateinit var googleSignInClient: GoogleSignInClient
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private var firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://visitor-management-syste-f5ddc-default-rtdb.firebaseio.com/")

    private lateinit var userName: String
    private var userType = "UNKNOWN"


    companion object {
        private const val TAG = "GoogleActivity"

        //9001
        private const val RC_SIGN_IN = 9001

    }


    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = Firebase.auth

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.app_client_id))
//            .requestIdToken(BuildConfig.WEB_CLIENT_ID)
            .requestEmail()
            .build()


        googleSignInClient = GoogleSignIn.getClient(this, gso)


        binding.btnAdmin2.setOnClickListener {
            val intent = Intent(this@loginUser, LogIn::class.java)
            startActivity(intent)
        }

        binding.btnLoginGoogle.setOnClickListener {

            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)

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

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode==RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
//                Toast.makeText(this, "GOOGLE_SIGN_IN_SUCCESS ${account.id}",Toast.LENGTH_SHORT).show()
                Log.d("onActivityResult", "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(this, "ApiException ${e.message}", Toast.LENGTH_SHORT).show()
                Log.d("onActivityResult", "Google sign in failed", e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener { _ ->
                // Sign in success
                Log.d(TAG, "firebaseAuthWithGoogle: LoggedIN")
                // Sign in success, update UI with the signed-in user's information
                val user = firebaseAuth.currentUser
                Log.d(TAG, "firebaseAuthWithGoogle: LoggedIN ${user!=null}")
                if (user!=null) {
                    val uid = user.uid
                    val email = user.email

//                    if(authResult.additionalUserInfo!!.isNewUser){} //Check if LoggedIn User is new
                    setGmail(email!!)
                    /*Toast.makeText(
                        this,
                        "GOOGLE_SIGN_IN_SUCCESS: ${user.displayName}",
                        Toast.LENGTH_SHORT
                    ).show()*/
                    Log.d(TAG, "GOOGLE_SIGN_IN_SUCCESS: ${user.displayName}")

                    checkUserAccount()
                }else{
                    Log.d(TAG, "firebaseAuthWithGoogle: NULL")
                }

            }
            .addOnFailureListener { authResult ->

                // If sign in fails, display a message to the user.
                Toast.makeText(
                    this,
                    "GOOGLE_SIGN_IN_FAIL ${authResult.message}",
                    Toast.LENGTH_SHORT
                ).show()
                Log.d(TAG, "firebaseAuthWithGoogle:failure ${authResult.message}")

            }
    }

    private fun setGmail(gmail: String) {
        this.gmail = gmail
    }
    private fun checkUserAccount() {
        binding.loading.visibility = View.VISIBLE
        Toast.makeText(this, "Checking Account...", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "firebaseAuthWithGoogle: Checking User Account!")
        firebaseDatabaseReference.child("User")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d(TAG, "firebaseAuthWithGoogle: Checking User if ${firebaseAuth.currentUser!!.uid} Exist!")
                    if (snapshot.hasChild(firebaseAuth.currentUser!!.uid)) {

                        Log.d(TAG, "firebaseAuthWithGoogle: Retrieving User Details")
                        this@loginUser.userType =
                            snapshot.child(firebaseAuth.currentUser!!.uid).child("usertype")
                                .getValue(String::class.java).toString()
                        this@loginUser.userName =
                            snapshot.child(firebaseAuth.currentUser!!.uid).child("Firstname")
                                .getValue(String::class.java).toString() +" "+ snapshot.child(firebaseAuth.currentUser!!.uid).child("Lastname").getValue(String::class.java).toString()

                        Log.d(TAG, "firebaseAuthWithGoogle:User Details ${"$userType - $userName"} Exist!")
                        logged()

//                        val isVerified: Boolean =
//                            snapshot.child(firebaseAuth.currentUser!!.uid).child("verified")
//                                .getValue(Boolean::class.java)!!
//                        checkIfVerified(isVerified)
                    } else {
                        Log.d(TAG, "firebaseAuthWithGoogle: User not Registered")
                        binding.loading.visibility = View.GONE
                        Toast.makeText(this@loginUser, "User not registered", Toast.LENGTH_SHORT);
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    binding.loading.visibility = View.GONE
                    Log.d(TAG, "firebaseAuthWithGoogle: Error Checking User due to ${error.message}")
                    Toast.makeText(
                        this@loginUser,
                        "onCancelled due to : "+ error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

    }

    private fun logged() {
        Handler(Looper.getMainLooper()).postDelayed({

            binding.loading.visibility = View.GONE

            if(userType == "User"){

                Log.d(TAG, "firebaseAuthWithGoogle: Hi $userName you Logged In as User Staff")
                Toast.makeText(this, "Logged In as User Staff", Toast.LENGTH_LONG);

                val intent = Intent(this, Dashboard::class.java)
                startActivity(intent)
                finish()


            }else if(userType == "Staff"){

                Toast.makeText(this, "Logged In as User Staff", Toast.LENGTH_LONG);
            }else{
                Toast.makeText(this, "Logged In as User Admin", Toast.LENGTH_LONG);
            }

        }, 3000) // 3000 is the delayed time in milliseconds.
    }

}
