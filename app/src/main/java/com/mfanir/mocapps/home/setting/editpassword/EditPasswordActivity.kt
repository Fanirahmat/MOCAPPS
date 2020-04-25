package com.mfanir.mocapps.home.setting.editpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mfanir.mocapps.R
import com.mfanir.mocapps.home.setting.EditProfileActivity
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_edit_password.*

class EditPasswordActivity : AppCompatActivity() {

    private lateinit var ePassword:String
    private lateinit var ePassword3:String
    private lateinit var ePassword_confirm:String
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_password)

        preferences = Preferences(this)

        layout_oldPassword.visibility = View.VISIBLE
        layout_newPassword.visibility = View.GONE

        iv_back3.setOnClickListener {
            finish()
        }

        btn_check.setOnClickListener {
            ePassword = et_password.text.toString()

            if (ePassword == "")
            {
                et_password.error = "password required"
                et_password.requestFocus()
            }
            else
            {
                checkPassword(ePassword)
            }

        }

        btn_confirm.setOnClickListener {
            ePassword3 = et_password3.text.toString()
            ePassword_confirm = et_password_confirm.text.toString()

            if (ePassword3 == "" || ePassword.length < 8)
            {
                et_password.error = "aleast 8 char password required"
                et_password.requestFocus()
            }
            else if (ePassword3 != ePassword_confirm)
            {
                et_password_confirm.error = "password did not match"
                et_password_confirm.requestFocus()
            }
            else
            {
                updatePassword(ePassword3)
            }

        }
    }


    private fun checkPassword(ePassword: String)
    {
        val user = FirebaseAuth.getInstance().currentUser

        val credential = user?.email?.let { it1 ->
            EmailAuthProvider
                .getCredential(it1, ePassword)
        }

        credential?.let { it1 ->
            user.reauthenticate(it1)
                .addOnCompleteListener { task ->
                    when {
                        task.isSuccessful -> {
                            layout_oldPassword.visibility = View.GONE
                            layout_newPassword.visibility = View.VISIBLE
                        }
                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                            et_password.error = "Invalid Password"
                            et_password.requestFocus()
                        }
                        else -> {
                            Toast.makeText(baseContext, task.exception?.message!!,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
        }

    }

    private fun updatePassword(ePassword: String)
    {
        val user = FirebaseAuth.getInstance().currentUser

        user?.updatePassword(ePassword3)
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    //Log.d(TAG, "User password updated.")
                    preferences.setValues("password", ePassword3)
                    Toast.makeText(baseContext, "User password updated.",
                        Toast.LENGTH_SHORT).show()
                    //startActivity(Intent(this, EditProfileActivity::class.java))
                    finish()
                }
                else
                {
                    Toast.makeText(baseContext, task.exception?.message!!,
                        Toast.LENGTH_SHORT).show()
                }
            }

    }
}
