package com.aces.ipt.vms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aces.ipt.vms.databinding.ActivityIndexBinding


class IndexActivity : AppCompatActivity() {

    lateinit var binding: ActivityIndexBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.started.setOnClickListener {

            println("Button clicked!")
        }
    }
}

