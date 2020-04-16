package com.mfanir.mocapps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_edit_profile.*

class EditProfileActivity : AppCompatActivity() {

    private lateinit var preferences: Preferences

    lateinit var eUsername :String
    lateinit var ePassword :String
    lateinit var eNama :String
    lateinit var eEmail :String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        preferences = Preferences(this)


        et_username.setText(preferences.getValues("username"))
        et_password.setText(preferences.getValues("password"))
        et_email.setText(preferences.getValues("email"))
        et_nama.setText(preferences.getValues("nama"))

        btn_daftar.setOnClickListener {
            eUsername = et_username.text.toString().trim()
            ePassword = et_password.text.toString().trim()
            eNama = et_nama.text.toString().trim()
            eEmail = et_email.text.toString().trim()

            if (eUsername.equals("")) {
                et_username.error = "Silahkan isi Username"
                et_username.requestFocus()
            } else if (ePassword.equals("")) {
                et_password.error = "Silahkan isi Password"
                et_password.requestFocus()
            } else if (eNama.equals("")) {
                et_nama.error = "Silahkan isi Nama"
                et_nama.requestFocus()
            } else if (eEmail.equals("")) {
                et_email.error = "Silahkan isi Email"
                et_email.requestFocus()
            } else {

                if (eUsername.indexOf(".") >=0) {
                    et_username.error = "Silahkan tulis Username Anda tanpa ."
                    et_username.requestFocus()
                } else {
                    //method simpan data
                    saveUpdate(eUsername, ePassword, eNama, eEmail)
                }

            }
        }

        iv_back.setOnClickListener {
            finish()
        }
    }

    private fun saveUpdate(eUsername: String, ePassword: String, eNama: String, eEmail: String)
    {

    }
}
