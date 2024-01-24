package com.aces.ipt.vms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import com.aces.ipt.vms.databinding.ActivityIndexBinding

class IndexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndexBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnstarted.setOnClickListener {
            val intent = Intent(this@IndexActivity, loginUser::class.java)
            startActivity(intent)
        }
    }
}
