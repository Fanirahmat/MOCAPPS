package com.mfanir.mocapps.auth.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mfanir.mocapps.home.HomeActivity
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.mfanir.mocapps.utils.Preferences


class SignInActivity : AppCompatActivity() {

    lateinit var iEmail :String
    lateinit var iPassword :String

    private lateinit var auth: FirebaseAuth
    lateinit var mDatabase:DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        auth = FirebaseAuth.getInstance()
        //inisialisasi firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        //ketika tekan btn sign in
        btn_signin.setOnClickListener{
            iEmail = et_email.text.toString()
            iPassword = et_password.text.toString()

            when {
                iEmail.equals("") -> {
                    et_email.error = "Silahkan Masukkan Username Anda"
                    et_email.requestFocus()
                }
                iPassword.equals("") -> {
                    et_password.error = "Silahkan Masukkan Username Anda"
                    et_password.requestFocus()
                }
                else -> {
                    pushLogin(iEmail, iPassword)
                }
            }
        }

        btn_daftar.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun pushLogin(iEmail: String, iPassword: String) {
        auth.signInWithEmailAndPassword(iEmail, iPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val currentUser = auth.currentUser
                    updateUI(currentUser)
                } else {
                    Toast.makeText(baseContext, task.exception?.message!!,
                        Toast.LENGTH_SHORT).show()

                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null)
        {
            finishAffinity()
            startActivity(Intent(this, HomeActivity::class.java))
        }

    }



}

