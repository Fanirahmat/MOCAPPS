package com.mfanir.mocapps.checkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.mfanir.mocapps.R
import com.mfanir.mocapps.auth.signin.User
import com.mfanir.mocapps.checkout.adapter.CheckoutAdapter
import com.mfanir.mocapps.checkout.model.Checkout
import com.mfanir.mocapps.home.model.Film
import com.mfanir.mocapps.home.tiket.TiketActivity
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0
    private lateinit var auth: FirebaseAuth
    private lateinit var saldo: String
    private lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        auth = FirebaseAuth.getInstance()
        dataList = intent.getSerializableExtra("dataCart") as ArrayList<Checkout>
        val data = intent.getParcelableExtra<Film>("dataFilm")

        ref = FirebaseDatabase.getInstance().reference
        ref.child("User").child(auth.currentUser.toString()).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(data: DataSnapshot) {
                if (data.exists())
                {
                    val user: User? = data.getValue(User::class.java)
                   saldo = user?.saldo.toString()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

        tampilTotal(dataList)

        iv_back.setOnClickListener{
            finish()
        }
        btn_tiket.setOnClickListener {

            saveTransaction(data, dataList, ref)

            val intent = Intent(this@CheckoutActivity,
                CheckoutSuccessActivity::class.java)
            startActivity(intent)

            showNotif(data)
        }
        btn_daftar.setOnClickListener {
            finish()
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        rc_checkout.adapter = CheckoutAdapter(dataList) {

        }

        if(saldo.isNotEmpty() || saldo != "0")
        {
            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
            tv_saldo.setText(formatRupiah.format(saldo.toDouble()))
            btn_tiket.visibility = View.VISIBLE
            //tv_balance.visibility = View.INVISIBLE
        } else {
            tv_saldo.setText("IDR 0")
            btn_tiket.visibility = View.INVISIBLE
            tv_balance.visibility = View.VISIBLE
            tv_balance.text = "Saldo pada e-wallet kamu tidak mencukupi\n" + "untuk melakukan transaksi"
        }
    }

    private fun saveTransaction(data: Film?, dataList: ArrayList<Checkout>, ref: DatabaseReference) {
        val map = HashMap<String, Any>()
        map["username"] = ""
    }

    private fun tampilTotal(dataList: ArrayList<Checkout>) {
        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }
        dataList.add(Checkout("Total Harus Dibayar", total.toString()))
    }


    private fun showNotif(datas: Film)
   {
       val NOTIFICATION_CHANNEL_ID = "channel_mocapps_notif"
       val context = this.applicationContext
       var notificationManager =
           context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

       if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
           val channelName = "MOCAPPS Notif Channel"
           val importance = NotificationManager.IMPORTANCE_HIGH

           val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
           notificationManager.createNotificationChannel(mChannel)
       }

       val mIntent = Intent(this, TiketActivity::class.java)
       val bundle = Bundle()
       bundle.putParcelable("data", datas)
       mIntent.putExtras(bundle)

       val pendingIntent =
           PendingIntent.getActivity(this, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)

       val builder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
       builder.setContentIntent(pendingIntent)
           .setSmallIcon(R.drawable.logo_moc2)
           .setLargeIcon(
               BitmapFactory.decodeResource(
                   this.resources,
                   R.drawable.logo_notification
               )
           )
           .setTicker("notif mocapps starting")
           .setAutoCancel(true)
           .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
           .setLights(Color.RED, 3000, 3000)
           .setDefaults(Notification.DEFAULT_SOUND)
           .setContentTitle("Sukses Terbeli")
           .setContentText("Tiket "+datas.judul+" berhasil kamu dapatkan. Enjoy the movie!")

       notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
       notificationManager.notify(115, builder.build())
   }
}
