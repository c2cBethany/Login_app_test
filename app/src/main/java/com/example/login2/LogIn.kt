package com.example.login2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.login2.databinding.LoginBinding
import com.example.login2.datasource.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LogIn : AppCompatActivity() {

    private lateinit var binding: LoginBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = LoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")
        val userId = firebaseAuth.currentUser?.uid

        binding.registerButton.setOnClickListener {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val pass = binding.password.text.toString()
            val username = binding.username.text.toString()

            if (pass.isNotEmpty() && username.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(username, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()
                        if (userId.toString().isNotEmpty()) {
                            databaseReference.child(userId.toString())
                                .addValueEventListener(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        user = snapshot.getValue(User::class.java)!!
                                        if (user.status == "Teacher") {
                                            Toast.makeText(
                                                this@LogIn,
                                                "teacher view!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startTeacherActivity()
                                        } else {
                                            Toast.makeText(
                                                this@LogIn,
                                                "student view!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            startStudentActivity()
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(
                                            this@LogIn,
                                            "Failed!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                })
                        }

                    } else {
                        Toast.makeText(this, "Incorrect username or password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            } else {
                Toast.makeText(this, "Fill in all spaces.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun startTeacherActivity() {
        val intent = Intent(this, TeacherView::class.java)
        startActivity(intent)
    }

    private fun startStudentActivity() {
        val intent = Intent(this, StudentView::class.java)
        startActivity(intent)
    }

}