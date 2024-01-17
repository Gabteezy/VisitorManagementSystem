package com.aces.ipt.vms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aces.ipt.vms.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this@LogIn, Dashboard::class.java)
            startActivity(intent)
        }

        binding.btnSignup1.setOnClickListener {
            val intent = Intent(this@LogIn, Registration::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {

    }
}
