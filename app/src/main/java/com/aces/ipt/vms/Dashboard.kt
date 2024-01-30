package com.aces.ipt.vms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.aces.ipt.vms.databinding.ActivityDashboardBinding
import com.aces.ipt.vms.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private var firebaseDatabaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://visitor-management-syste-f5ddc-default-rtdb.firebaseio.com/")
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private lateinit var userType: String
    private lateinit var user: User
    private lateinit var bundle: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        Log.d("DASHBOARD", "GET EXTRAS")
        val bundle = intent.extras

// getting the string back
        userType = bundle!!.getString("user").toString()
        binding.userLoggedType.text = "Logged as $userType"
        Log.d("DASHBOARD", "FETCH EXTRA USER TYPE")

        retrieveUserDetails()

        binding.btnAppointments.setOnClickListener {
            val intent = Intent(this@Dashboard, TimeandDate::class.java)
            startActivity(intent)
        }

        binding.btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this@Dashboard, IndexActivity::class.java)
            startActivity(intent)

        }
    }
    private fun retrieveUserDetails() {

        Log.d("DASHBOARD", "CHECKING USER RECORD FROM FIREBASE")

        val databaseRef = firebaseDatabase.reference.child("user")
            .child(firebaseAuth.currentUser!!.uid)

        databaseRef.get().addOnCompleteListener { dataSnapshot ->
            if (dataSnapshot.isSuccessful) {
                user = dataSnapshot.result.getValue(User::class.java)!!
                if (user!=null) {
                    Log.d("DASHBOARD", " USER FOUND \n ${user.toString()}")
                    //bundle = Bundle()
                    //bundle.putParcelable("user", user)
                    binding.userFirstName.text = "Welcome ${user.firstname}"

                }
            } else {
                Log.d("USER_DETAILS_NOT_FOUND", "USER NOT FOUND")
            }
        }
    }
}