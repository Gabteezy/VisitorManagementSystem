package com.aces.ipt.vms

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aces.ipt.vms.databinding.ActivityDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class Dashboard : AppCompatActivity() {

    private lateinit var binding: ActivityDashboardBinding

    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
}