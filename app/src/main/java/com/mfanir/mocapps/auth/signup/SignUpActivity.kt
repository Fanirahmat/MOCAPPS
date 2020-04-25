package com.mfanir.mocapps.auth.signup

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.SignInActivity
import com.mfanir.mocapps.auth.signin.User
import com.mfanir.mocapps.home.HomeActivity
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUpActivity : AppCompatActivity() {

    lateinit var iUsername :String
    lateinit var iPassword :String
    lateinit var iNama :String
    lateinit var iEmail :String

    private lateinit var auth: FirebaseAuth
    private lateinit var mFirebaseDatabase: DatabaseReference

    private lateinit var preferences: Preferences




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("User")
        preferences = Preferences(this)

        btn_daftar.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()
            iNama = et_nama.text.toString()
            iEmail = et_email.text.toString()

            if (iUsername.equals("")) {
                et_username.error = "Silahkan isi Username"
                et_username.requestFocus()
            } else if (iPassword.equals("")) {
                et_password.error = "Silahkan isi Password"
                et_password.requestFocus()
            } else if (iNama.equals("")) {
                et_nama.error = "Silahkan isi Nama"
                et_nama.requestFocus()
            } else if (iEmail.equals("")) {
                et_email.error = "Silahkan isi Email"
                et_email.requestFocus()
            } else {

                if (iUsername.indexOf(".") >=0) {
                    et_username.error = "Silahkan tulis Username Anda tanpa ."
                    et_username.requestFocus()
                } else {
                    //method simpan data
                    saveUser(iUsername, iPassword, iNama, iEmail)
                }

            }
        }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if (currentUser != null)
        {
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    private fun saveUser(iUsername: String, iPassword: String, iNama: String, iEmail: String)
    {

        auth.createUserWithEmailAndPassword(iEmail, iPassword)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val currentUser = auth.currentUser
                    //send email verification
                    currentUser?.sendEmailVerification()
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                                val user = User()
                                user.email = iEmail
                                user.username = iUsername
                                user.name = iNama
                                //user.password = iPassword
                                user.photoUrl = ""
                                user.saldo = ""


                                FirebaseAuth.getInstance().currentUser?.uid?.let { mFirebaseDatabase.child(it).setValue(user) }
                                Toast.makeText(this@SignUpActivity, "Registration complete, Check your email for verification", Toast.LENGTH_LONG)
                                    .show()
                                val intent = Intent(this@SignUpActivity,
                                    SignInActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                        }

                } else {
                    // If sign in fails, display a message to the user.

                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()

                }

                // ...
            }


    }



}
