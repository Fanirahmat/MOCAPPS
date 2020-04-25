package com.mfanir.mocapps.home.setting

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.User
import com.mfanir.mocapps.home.setting.editemail.EditEmailActivity
import com.mfanir.mocapps.home.setting.editpassword.EditPasswordActivity
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.iv_add
import kotlinx.android.synthetic.main.activity_edit_profile.iv_back
import kotlinx.android.synthetic.main.activity_edit_profile.iv_profile
import java.util.*


class EditProfileActivity : AppCompatActivity() , PermissionListener {

    private lateinit var preferences: Preferences

    private var statusAdd:Boolean = false
    private lateinit var eUsername :String
    private lateinit var ePassword :String
    private lateinit var eNama :String
    private lateinit var eEmail :String

    //private lateinit var auth: FirebaseAuth
    private lateinit var filePath: Uri

    private lateinit var mFirebaseDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        preferences = Preferences(this)
        //auth = FirebaseAuth.getInstance()
        mFirebaseDatabase = FirebaseDatabase.getInstance().getReference("User")
        val user = FirebaseAuth.getInstance().currentUser

        et_username.setText(preferences.getValues("username").toString())
        //tv_password.setText(preferences.getValues("password").toString())
        tv_email.setText(user?.email)
        et_nama.setText(preferences.getValues("name").toString())


        if(user?.photoUrl == null)
        {
            statusAdd = false
            iv_add.setImageResource(R.drawable.ic_btn_upload)
            iv_profile.setImageResource(R.drawable.user_pic)
        }
        else
        {
            statusAdd = true
            Glide.with(this)
                .load(user.photoUrl.toString())
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }

        iv_add.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .start()
        }

        tv_email.setOnClickListener {
            startActivity(Intent(this, EditEmailActivity::class.java))
        }

        tv_password.setOnClickListener {
            startActivity(Intent(this, EditPasswordActivity::class.java))
        }

        btn_daftar.setOnClickListener {
            eUsername = et_username.text.toString().trim()
           // ePassword = et_password.text.toString().trim()
            eNama = et_nama.text.toString().trim()
           // eEmail = et_email.text.toString().trim()

            if (eUsername == "") {
                et_username.error = "Silahkan isi Username"
                et_username.requestFocus()
            } else if (eNama == "") {
                et_nama.error = "Silahkan isi Nama"
                et_nama.requestFocus()
            }
            else {

                if (eUsername.indexOf(".") >=0) {
                    et_username.error = "Silahkan tulis Username Anda tanpa ."
                    et_username.requestFocus()
                } else {
                    //method simpan data
                    saveUpdate(eUsername,  eNama,  filePath)
                }

            }
        }

        iv_back.setOnClickListener {
            //startActivity(Intent(this, ))
            finish()
        }
    }

    private fun saveUpdate(eUsername: String,  eNama: String, filePath: Uri)
    {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Uploading new photo...")
        progressDialog.show()

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename");

        ref.putFile(filePath)
            .addOnSuccessListener {
                progressDialog.dismiss()


                ref.downloadUrl.addOnSuccessListener {
                    val userNew = User()
                    val user = FirebaseAuth.getInstance().currentUser
                    userNew.username = eUsername
                    //userNew.password = preferences.getValues("password").toString()
                    userNew.name = eNama
                    userNew.email = user?.email
                    userNew.photoUrl = it.toString()
                    userNew.saldo = ""

                    FirebaseAuth.getInstance().currentUser?.uid?.let { mFirebaseDatabase.child(it).setValue(userNew) }

                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(eUsername)
                        .setPhotoUri(Uri.parse(it.toString()))
                    .build()

                    user?.updateProfile(profileUpdates)
                        ?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Updated" , Toast.LENGTH_SHORT).show()
                                //finish()
                                startActivity(Intent())
                            }
                        }


                }
            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(this, "Failed" + e.message, Toast.LENGTH_SHORT).show()
            }
            .addOnProgressListener {  taskSnapshot ->
                val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                    .totalByteCount
                progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
            }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .galleryOnly()
            .compress(1024)
            .start()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG ).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            statusAdd = true
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)


            iv_add.setImageResource(R.drawable.ic_btn_delete)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
