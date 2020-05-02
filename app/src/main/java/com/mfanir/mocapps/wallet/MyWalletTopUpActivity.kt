package com.mfanir.mocapps.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.mfanir.mocapps.R
import kotlinx.android.synthetic.main.activity_my_wallet_top_up.*

class MyWalletTopUpActivity : AppCompatActivity() {

    private var statusAdd: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet_top_up)

        tv_10k.setOnClickListener {
            if(statusAdd)
            {
                notSelectedMoney(tv_10k)
            }
            else
            {
                selectedMoney(tv_10k)
            }
        }

    }

    private fun selectedMoney(textView: TextView)
    {
        statusAdd = true
        textView.setTextColor(resources.getColor(R.color.colorBlueDark))
        textView.setBackgroundResource(R.drawable.shape_rectangle_yellow2)
        btn_top_up2.visibility = View.VISIBLE
    }

    private fun notSelectedMoney(textView: TextView)
    {
        statusAdd = false
        textView.setTextColor(resources.getColor(R.color.colorWhite))
        textView.setBackgroundResource(R.drawable.shape_line_bluegrey)
        btn_top_up2.visibility = View.INVISIBLE
    }
}
