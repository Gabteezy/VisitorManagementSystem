package com.aces.ipt.vms

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aces.ipt.vms.databinding.ActivityLogInBinding
import com.aces.ipt.vms.databinding.ActivityRegistrationBinding

class Registration : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            val intent = Intent(this@Registration, IndexActivity::class.java)
            startActivity(intent)
        }

        binding.btnSignup.setOnClickListener {
            val intent = Intent(this@Registration, LogIn::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {

    }
}
