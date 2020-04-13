package com.mfanir.mocapps.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mfanir.mocapps.R
import com.mfanir.mocapps.home.HomeActivity
import kotlinx.android.synthetic.main.activity_checkout_success.*

class CheckoutSuccessActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout_success)

        btn_skip.setOnClickListener {
            finishAffinity()

            val intent = Intent( this@CheckoutSuccessActivity,
                HomeActivity::class.java)
            startActivity(intent)

        }

    }
}
