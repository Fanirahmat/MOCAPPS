package com.mfanir.mocapps.home.setting

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.auth.FirebaseAuth
import com.mfanir.mocapps.MyWalletActivity

import com.mfanir.mocapps.R
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.fragment_setting.*

/**
 * A simple [Fragment] subclass.
 */
class SettingFragment : Fragment() {

    lateinit var preferences: Preferences
    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(context!!.applicationContext)

        tv_nama.text = preferences.getValues("name")
        tv_email.text = user?.email


        if(user?.photoUrl == null)
        {
            iv_profile.setImageResource(R.drawable.user_pic)
        }
        else
        {
            Glide.with(this)
                .load(user?.photoUrl.toString())
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }



        tv_myWallet.setOnClickListener {
            startActivity(Intent(activity, MyWalletActivity::class.java))
        }

        tv_editProfil.setOnClickListener {
            startActivity(Intent(activity, EditProfileActivity::class.java))
        }
    }

}
