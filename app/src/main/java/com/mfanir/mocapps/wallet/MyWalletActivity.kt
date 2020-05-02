package com.mfanir.mocapps.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfanir.mocapps.R
import com.mfanir.mocapps.utils.Preferences
import com.mfanir.mocapps.wallet.adapter.WalletAdapter
import com.mfanir.mocapps.wallet.model.Wallet
import kotlinx.android.synthetic.main.activity_my_wallet.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class MyWalletActivity : AppCompatActivity() {

    private var dataList = ArrayList<Wallet>()
    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        preferences = Preferences(this)

        if (!preferences.getValues("saldo").equals(""))
        {
            currecy(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
        }

        //no database
        dataList.add(
            Wallet(
                "Avengers Returns",
                "Sabtu, 12 Jan 2020",
                70000.0,
                "0"
            )
        )

        dataList.add(
            Wallet(
                "Top Up",
                "Sabtu, 12 Jan 2020",
                50000.0,
                "1"
            )
        )

        dataList.add(
            Wallet(
                "Top Up",
                "Sabtu, 12 Jan 2020",
                50000.0,
                "1"
            )
        )

        dataList.add(
            Wallet(
                "Alitta Battle",
                "Sabtu, 12 Jan 2020",
                50000.0,
                "0"
            )
        )

        rv_transaksi.layoutManager = LinearLayoutManager(this)
        rv_transaksi.adapter = WalletAdapter(dataList){

        }

        btn_top_up.setOnClickListener {
            startActivity(Intent(this,MyWalletTopUpActivity::class.java))
        }

    }

    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga))

    }
}
