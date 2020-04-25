package com.mfanir.mocapps.home.setting.editemail

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.mfanir.mocapps.R
import com.mfanir.mocapps.home.setting.EditProfileActivity
import kotlinx.android.synthetic.main.activity_edit_email.*

class EditEmailActivity : AppCompatActivity() {
    private lateinit var iPassword:String
    private lateinit var iEmail:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_email)

        layout_enterPassword.visibility = View.VISIBLE
        layout_newEmail.visibility = View.GONE

        iv_back2.setOnClickListener {
            finish()
        }

        btn_verif.setOnClickListener {
            iPassword = et_password2.text.toString()

            if (iPassword == "")
            {
                et_password2.error = "Password required"
                et_password2.requestFocus()
            }
            else
            {
                val user = FirebaseAuth.getInstance().currentUser

                val credential = user?.email?.let { it1 ->
                    EmailAuthProvider
                        .getCredential(it1, iPassword)
                }

                // Prompt the user to re-provide their sign-in credentials
                credential?.let { it1 ->
                    user.reauthenticate(it1)
                        .addOnCompleteListener { task ->
                            when {
                                task.isSuccessful -> {
                                    layout_enterPassword.visibility = View.GONE
                                    layout_newEmail.visibility = View.VISIBLE
                                }
                                task.exception is FirebaseAuthInvalidCredentialsException -> {
                                    et_password2.error = "Invalid Password"
                                    et_password2.requestFocus()
                                }
                                else -> {
                                    Toast.makeText(baseContext, task.exception?.message!!,
                                        Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                }
            }

        }

        btn_change2.setOnClickListener {
            iEmail = et_email2.text.toString()

            if (iEmail == "")
            {
                et_email2.error = "required email"
                et_email2.requestFocus()
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(iEmail).matches())
            {
                et_email2.error = "valid email required"
                et_email2.requestFocus()
            }
            else
            {
                val user = FirebaseAuth.getInstance().currentUser

                user?.updateEmail(iEmail)
                    ?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            //send Email verification
                            user.sendEmailVerification()
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                        Toast.makeText(baseContext, "Your Email Have Been Updated, Check your email for verification",
                                            Toast.LENGTH_SHORT).show()
                                        startActivity(Intent(this,EditProfileActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(baseContext, task.exception?.message!!,
                                            Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                        else
                        {
                            Toast.makeText(baseContext, task.exception?.message!!,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }

        }

    }

}
