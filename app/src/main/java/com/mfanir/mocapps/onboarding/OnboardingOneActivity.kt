package com.mfanir.mocapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_one.*

class OnboardingOneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        btn_next.setOnClickListener{
            val intent = Intent( this@OnboardingOneActivity, OnboardingTwoActivity::class.java)
            startActivity(intent)
        }

        btn_skip.setOnClickListener{
            finishAffinity()

            val intent = Intent( this@OnboardingOneActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
