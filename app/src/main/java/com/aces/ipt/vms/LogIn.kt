package com.aces.ipt.vms

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import com.aces.ipt.vms.databinding.ActivityLogInBinding

class LogIn : AppCompatActivity() {

    private lateinit var binding: ActivityLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin1.setOnClickListener {
            val intent = Intent(this@LogIn, Dashboard::class.java)
            startActivity(intent)
        }

        binding.btnUser.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    binding.btnUser.setBackgroundColor(Color.RED)
                    true
                }
                MotionEvent.ACTION_UP -> {

                    val intent = Intent(this@LogIn, loginUser::class.java)
                    startActivity(intent)


                    binding.btnUser.setBackgroundColor(Color.GRAY)

                    true
                }
                else -> false
            }
        }
    }

    private fun showToast(message: String) {
        // Implement your showToast logic here
    }
}
