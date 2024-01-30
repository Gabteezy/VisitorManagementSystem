package com.aces.ipt.vms

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aces.ipt.vms.databinding.ActivityRegistrationBinding
import com.aces.ipt.vms.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Registration() : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    private var firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://visitor-management-syste-f5ddc-default-rtdb.firebaseio.com/")
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var user: User
    private lateinit var userType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle = intent.extras

// getting the string back
        userType = bundle!!.getString("user").toString()

        binding.title.text = "Create an $userType Account"


        binding.btnSignup.setOnClickListener {

        }

        binding.btnRegister.setOnClickListener {

            val email = binding.email.text.toString()
            val passwordConfirm = binding.passwordConfirm.text.toString()
            val password = binding.password.text.toString()
            val firstName = binding.firstName.text.toString()
            val lastName = binding.LastName.text.toString()
            val middleName = binding.middleName.text.toString()
            val phone = binding.phone.text.toString()

            if (firstName.isEmpty() || lastName.isEmpty() || middleName.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty() || passwordConfirm.isEmpty()) {
                if (firstName.isEmpty()) {
                    binding.firstName.error = "Please enter your Name first!"
                }
                if (lastName.isEmpty()) {
                    binding.LastName.error = "Please enter your Name Last!"
                }
                if (middleName.isEmpty()) {
                    binding.middleName.error = "Please enter your Name Middle!"
                }
                if (email.isEmpty()) {
                    binding.email.error = "Please enter your Email!"
                }
                if (phone.isEmpty()) {
                    binding.phone.error = "Please enter your Phone!"
                }
                if (password.isEmpty()) {
                    binding.password.error = "Please enter your Password!"
                }
                if (passwordConfirm.isEmpty()) {
                    binding.passwordConfirm.error = "Please confirm your Password!"
                }

            } else {
                if (!passwordConfirm.equals(password, false)) {
                    binding.passwordConfirm.error = "Password not match!"
                    Toast.makeText(
                        this@Registration,
                        "Password did not match!",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            firebaseDatabaseReference.child("user")
                                .addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.hasChild(firebaseAuth.currentUser!!.uid)) {
                                            Toast.makeText(
                                                this@Registration,
                                                "User is already Registered!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            val databaseRef =
                                                firebaseDatabase.reference.child("user")
                                                    .child(firebaseAuth.currentUser!!.uid)

                                            user = User(
                                                firebaseAuth.currentUser!!.uid, null, null,
                                                email,
                                                firstName,
                                                middleName,
                                                lastName,
                                                phone, userType, false
                                            )

                                            databaseRef.setValue(user)
                                                .addOnCompleteListener { task ->
                                                    if (task.isSuccessful) {
                                                        Log.d(
                                                            "REGISTER",
                                                            "You are now Registered! \nPlease with for the verification of our Admin for Approval!"
                                                        )
                                                        Toast.makeText(
                                                            this@Registration,
                                                            "User has been successfully Registered!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()


                                                        firebaseAuth.signOut()
                                                        val intent = Intent(
                                                            this@Registration,
                                                            IndexActivity::class.java
                                                        )
                                                        startActivity(intent)
                                                    }
                                                }


                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Log.d(
                                            "REGISTER",
                                            "Error in Registering due to ${error.message}!"
                                        )
                                        Toast.makeText(
                                            this@Registration,
                                            "Error in Registering due to ${error.message}!",
                                            Toast.LENGTH_LONG
                                        )
                                    }

                                })
                        } else {
                            Log.d(
                                "REGISTER", it.exception!!.message.toString()
                            )
                            binding.password.error = it.exception!!.message.toString()
                            Toast.makeText(
                                this@Registration,
                                it.exception!!.message,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

    }
}
