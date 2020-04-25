package com.mfanir.mocapps.auth.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import com.mfanir.mocapps.home.HomeActivity
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.User
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_sign_up_photoscreen.*
import java.util.*

class SignUpPhotoscreenActivity : AppCompatActivity(), PermissionListener {


    var statusAdd:Boolean = false
    lateinit var filePath: Uri
    lateinit var preferences: Preferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photoscreen)


        preferences = Preferences(this)
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename");
        tv_hello.text = "Welcome, \n"+intent.getStringExtra("nama")

        iv_add.setOnClickListener {
            if (statusAdd) {
                statusAdd = false
                btn_save.visibility = View.INVISIBLE
                iv_add.setImageResource(R.drawable.ic_btn_upload)
                iv_profile.setImageResource(R.drawable.user_pic)

            } else {
                ImagePicker.with(this)
                    .galleryOnly()
                    .compress(1024)//User can only capture image using Camera
                    .start()
            }
        }

        btn_upload_nanti.setOnClickListener {

            finishAffinity()

            val intent = Intent(this@SignUpPhotoscreenActivity,
                HomeActivity::class.java)
            startActivity(intent)
        }

        btn_save.setOnClickListener {
            if (filePath != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading...")
                progressDialog.show()

                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoscreenActivity, "Uploaded", Toast.LENGTH_SHORT).show()

                        //mengedit data user (menambahkan data url(foto))
                        ref.downloadUrl.addOnSuccessListener {
                            //val username = preferences.getValues("username")
                            val ref = FirebaseDatabase.getInstance().getReference("User")
                            val user = User()
                           // user.url        = it.toString()
                            user.email      = preferences.getValues("email")
                            user.username   = preferences.getValues("username")
                            //user.nama       = preferences.getValues("nama")
                            //user.password   = preferences.getValues("password")
                            //ref.child(user.id.toString()).setValue(user)

                            //preferences.setValues("id", user.id.toString())
                            preferences.setValues("url", it.toString())
                           // preferences.setValues("nama", user.nama.toString())
                            preferences.setValues("username", user.username.toString())
                            //preferences.setValues("password", user.password.toString())
                            preferences.setValues("saldo", "")
                            preferences.setValues("email", user.email.toString())
                            preferences.setValues("status", "1")

                            finishAffinity()
                            val intent = Intent(this@SignUpPhotoscreenActivity,
                                HomeActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    .addOnFailureListener { e ->
                        progressDialog.dismiss()
                        Toast.makeText(this@SignUpPhotoscreenActivity, "Failed " + e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                            .totalByteCount
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            }

        }
    }



    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

        ImagePicker.with(this)
            .cameraOnly()
            .compress(1024)//User can only capture image using Camera
            .start()

    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    ) {
        //To change body of created functions use File | Settings | File Templates.
    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(this, "Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG ).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this, "Tergesah? Klik tombol Upload Nanti aja", Toast.LENGTH_LONG ).show()
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

            btn_save.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_delete)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    }

