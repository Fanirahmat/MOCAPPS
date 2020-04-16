package com.mfanir.mocapps.auth.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.*
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.User
import kotlinx.android.synthetic.main.activity_sign_up.*
import com.mfanir.mocapps.utils.Preferences

class SignUpActivity : AppCompatActivity() {

    lateinit var iUsername :String
    lateinit var iPassword :String
    lateinit var iNama :String
    lateinit var iEmail :String

    private lateinit var mFirebaseDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

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

    private fun saveUser(iUsername: String, iPassword: String, iNama: String, iEmail: String)
    {

        val user = User()
        user.email = iEmail
        user.username = iUsername
        user.nama = iNama
        user.password = iPassword

        //mengecek username
        checkingUsername(iUsername, user)
    }

    private fun checkingUsername(iUsername: String, data: User) {
        mFirebaseDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)
                if (user == null)
                {
                    mFirebaseDatabase.child(iUsername).setValue(data)

                    preferences.setValues("nama", data.nama.toString())
                    preferences.setValues("user", data.username.toString())
                    preferences.setValues("saldo", "")
                    preferences.setValues("url", "")
                    preferences.setValues("email", data.email.toString())
                    preferences.setValues("status", "1")

                    val intent = Intent(this@SignUpActivity,
                        SignUpPhotoscreenActivity::class.java).putExtra("username", data.username)
                    startActivity(intent)

                }
                else
                {
                    Toast.makeText(this@SignUpActivity, "User sudah digunakan", Toast.LENGTH_LONG).show()

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

}
