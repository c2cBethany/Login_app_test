package com.example.login2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.login2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var username: EditText
    lateinit var password: EditText
    lateinit var loginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fun login() {
//            val user = binding.username.text.toString()
//            val pass = binding.password.text.toString()
//            val logButton = binding.loginButton

            binding.loginButton.setOnClickListener(
                View.OnClickListener
                {
                    if (binding.username.text.toString() == "user" && binding.password.text.toString() == "1234") {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                })

        }

    }
}