package com.example.login2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.login2.databinding.SignupBinding
import com.example.login2.datasource.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SignUp : AppCompatActivity() {

    private lateinit var binding: SignupBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var user: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().getReference("Users")

        binding.regButton.setOnClickListener{
            val pass = binding.signupPass.text.toString()
            val username = binding.signupUser.text.toString()
            val confPass = binding.confPass.text.toString()
            val firstName = binding.firstname.text.toString()
            val lastName = binding.lastname.text.toString()
            val status = if (binding.student.isChecked) {
                "Student"
            } else {
                "Teacher"
            }

            if (pass.isNotEmpty() && username.isNotEmpty() && confPass.isNotEmpty() && (binding.student.isChecked || binding.teacher.isChecked)) {
                if (confPass == pass) {
                    firebaseAuth.createUserWithEmailAndPassword(username, pass)
                        .addOnCompleteListener { it ->
                            if (it.isSuccessful) {
                                firebaseAuth.signInWithEmailAndPassword(username, pass)
                                    .addOnCompleteListener {
                                        if (it.isSuccessful) {
                                            val userId = firebaseAuth.currentUser?.uid
                                            user = User(firstName, lastName, status)
                                            if (userId != null) {
                                                databaseReference.child(userId).setValue(user)
                                                    .addOnCompleteListener {
                                                        Toast.makeText(
                                                            this,
                                                            "Success!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                                    .addOnFailureListener {
                                                        Toast.makeText(
                                                            this,
                                                            "Failed!",
                                                            Toast.LENGTH_SHORT
                                                        ).show()
                                                    }
                                            }
                                            Toast.makeText(
                                                this,
                                                "Login successful!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                            if (userId.toString().isNotEmpty()) {
                                                databaseReference.child(userId.toString())
                                                    .addValueEventListener(object :
                                                        ValueEventListener {
                                                        override fun onDataChange(snapshot: DataSnapshot) {
                                                            user = snapshot.getValue(User::class.java)!!
                                                            if (status == "Teacher") {
                                                                Toast.makeText(
                                                                    this@SignUp,
                                                                    "teacher view!",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                startTeacherActivity()
                                                            } else {
                                                                Toast.makeText(
                                                                    this@SignUp,
                                                                    "student view!",
                                                                    Toast.LENGTH_SHORT
                                                                ).show()
                                                                startStudentActivity()
                                                            }
                                                        }

                                                        override fun onCancelled(error: DatabaseError) {
                                                            Toast.makeText(
                                                                this@SignUp,
                                                                "Failed!",
                                                                Toast.LENGTH_SHORT
                                                            ).show()
                                                        }

                                                    })
                                            }
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Login failed",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                    }
                            } else {
                                Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                } else {
                    Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Fill in all spaces.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backLogin.setOnClickListener{
            //Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LogIn::class.java)
            startActivity(intent)
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
