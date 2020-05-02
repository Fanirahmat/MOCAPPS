package com.mfanir.mocapps.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfanir.mocapps.R
import com.mfanir.mocapps.home.HomeActivity
import kotlinx.android.synthetic.main.activity_success_top_up.*

class SuccessTopUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_success_top_up)

        btn_skip.setOnClickListener {
            finishAffinity()

            val intent = Intent( this,
                HomeActivity::class.java)
            startActivity(intent)

        }
    }
}
