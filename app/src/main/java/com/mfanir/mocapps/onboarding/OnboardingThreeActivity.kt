package com.mfanir.mocapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)

        btn_next.setOnClickListener{
            finishAffinity()

            val intent = Intent( this@OnboardingThreeActivity, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
