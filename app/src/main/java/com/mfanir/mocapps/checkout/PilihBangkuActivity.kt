package com.mfanir.mocapps.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mfanir.mocapps.R
import com.mfanir.mocapps.checkout.model.Checkout
import com.mfanir.mocapps.home.model.Film
import kotlinx.android.synthetic.main.activity_pilih_bangku.*

class PilihBangkuActivity : AppCompatActivity() {

    var statusA1:Boolean = false
    var statusA2:Boolean = false
    var statusA3:Boolean = false
    var statusA4:Boolean = false
    var total:Int = 0

    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pilih_bangku)

        val data = intent.getParcelableExtra<Film>("data")

        tv_judul.text = data.judul

        // (a1,a2,a3,....) adalah value setiap kursi
        a1.setOnClickListener {
            if (statusA1) {
                a1.setImageResource(R.drawable.shape_line_bluegrey)
                statusA1 = false
                total -=1
                belitiket(total)

                // delete data
                dataList.remove(Checkout("A1", "30000"))

            } else {
                a1.setImageResource(R.drawable.ic_rectangle_selected)
                statusA1 = true
                total +=1
                belitiket(total)
                //addToCart("A1", "30000")

                val data = Checkout("A1", "30000")
                dataList.add(data)
            }
        }

        a2.setOnClickListener {
            if (statusA2) {
                a2.setImageResource(R.drawable.shape_line_bluegrey)
                statusA2 = false
                total -=1
                belitiket(total)

                // delete data
                dataList.remove(Checkout("A2", "30000"))

            } else {
                a2.setImageResource(R.drawable.ic_rectangle_selected)
                statusA2 = true
                total +=1
                belitiket(total)

                val data = Checkout("A2", "30000")
                dataList.add(data)
            }
        }

        a3.setOnClickListener {
            if (statusA3) {
                a3.setImageResource(R.drawable.shape_line_bluegrey)
                statusA3 = false
                total -=1
                belitiket(total)

                // delete data
                dataList.remove(Checkout("A3", "30000"))

            } else {
                a3.setImageResource(R.drawable.ic_rectangle_selected)
                statusA3 = true
                total +=1
                belitiket(total)

                val data = Checkout("A3", "30000")
                dataList.add(data)
            }
        }

        a4.setOnClickListener {
            if (statusA4) {
                a4.setImageResource(R.drawable.shape_line_bluegrey)
                statusA4 = false
                total -=1
                belitiket(total)

                // delete data
                dataList.remove(Checkout("A4", "30000"))

            } else {
                a4.setImageResource(R.drawable.ic_rectangle_selected)
                statusA4 = true
                total +=1
                belitiket(total)

                val data = Checkout("A4", "30000")
                dataList.add(data)
            }
        }

        //a5, .....

        btn_beli.setOnClickListener {

            val intent = Intent(
                this@PilihBangkuActivity,
                CheckoutActivity::class.java
            ).putExtra("dataCart", dataList).putExtra("dataFilm", data)
            startActivity(intent)
        }

        iv_back.setOnClickListener{
            finish()
        }

    }

    private fun addToCart(kursi: String, harga: String){

    }

    private fun removeCart(kursi: String, harga: String){

    }

    private fun belitiket(total:Int) {
        if (total == 0) {
            btn_beli.setText("Beli Tiket")
            btn_beli.visibility = View.INVISIBLE
        } else {
            btn_beli.setText("Beli Tiket ("+total+")")
            btn_beli.visibility = View.VISIBLE
        }

    }
}

