package com.mfanir.mocapps.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfanir.mocapps.R
import com.mfanir.mocapps.checkout.adapter.CheckoutAdapter
import com.mfanir.mocapps.checkout.model.Checkout
import com.mfanir.mocapps.home.model.Film
import com.mfanir.mocapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0

    private lateinit var preferences:Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>
        //val data = intent.getParcelableExtra<Film>("datas")

        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        btn_tiket.setOnClickListener {
            val intent = Intent(this@CheckoutActivity,
                CheckoutSuccessActivity::class.java)
            startActivity(intent)

            //showNotif(data)
        }

        btn_daftar.setOnClickListener {
            finish()
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckoutAdapter(dataList) {

        }

        //if(preferences.getValues("saldo")!!.isNotEmpty()) {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
           // btn_tiket.visibility = View.VISIBLE
           // tv_balance.visibility = View.INVISIBLE

        //} else {
           // tv_saldo.setText("Rp 0")
           // btn_tiket.visibility = View.INVISIBLE
          //  tv_balance.visibility = View.VISIBLE
          //  tv_balance.text = "Saldo pada e-wallet kamu tidak mencukupi\n" +
          //          "untuk melakukan transaksi"
       // }
    }

   // private fun showNotif(datas: Film) {

   // }
}
