package com.mfanir.mocapps.auth.signin

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*
import com.mfanir.mocapps.home.HomeActivity

import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signup.SignUpActivity
import kotlinx.android.synthetic.main.activity_sign_in.*
import com.mfanir.mocapps.utils.Preferences


class SignInActivity : AppCompatActivity() {

    lateinit var iUsername :String
    lateinit var iPassword :String

    lateinit var mDatabase:DatabaseReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        //inisialisasi firebase
        mDatabase = FirebaseDatabase.getInstance().getReference("User")
        //inisialisasi preferences
        preferences = Preferences(this)

        preferences.setValues("onboarding", "1")
        if (preferences.getValues("status").equals("1")) {
            finishAffinity()

            val intent = Intent(this@SignInActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        //ketika tekan btn sign in
        btn_signin.setOnClickListener{
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            when {
                iUsername.equals("") -> {
                    et_username.error = "Silahkan Masukkan Username Anda"
                    et_username.requestFocus()
                }
                iPassword.equals("") -> {
                    et_password.error = "Silahkan Masukkan Username Anda"
                    et_password.requestFocus()
                }
                else -> {
                    pushLogin(iUsername, iPassword)
                }
            }
        }

        btn_daftar.setOnClickListener {
            val intent = Intent(this@SignInActivity, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun pushLogin(iUsername: String, iPassword: String) {
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null) {
                    Toast.makeText(this@SignInActivity, "User tidak ditemukan", Toast.LENGTH_LONG).show()

                } else {
                    if (user.password.equals(iPassword)){
                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()

                        preferences.setValues("nama", user.nama.toString())
                        preferences.setValues("user", user.username.toString())
                        preferences.setValues("url", user.url.toString())
                        preferences.setValues("email", user.email.toString())
                        preferences.setValues("saldo", user.saldo.toString())
                        preferences.setValues("status", "1")

                        finishAffinity()

                        val intent = Intent(this@SignInActivity,
                            HomeActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@SignInActivity, "Password Anda Salah", Toast.LENGTH_LONG).show()
                    }

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}

