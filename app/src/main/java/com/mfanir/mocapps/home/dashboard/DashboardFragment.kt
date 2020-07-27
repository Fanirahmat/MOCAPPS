package com.mfanir.mocapps.home.dashboard

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.mfanir.mocapps.home.model.Film
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import com.mfanir.mocapps.MovieDetailActivity
import com.mfanir.mocapps.utils.Preferences
import com.mfanir.mocapps.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mfanir.mocapps.auth.signin.User
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var mDatabase: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance().reference
        auth = FirebaseAuth.getInstance()

        showUserInfo(auth.currentUser)

        //menampilkan data film dengan recycle view
        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_comming_soon.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getDataNowPlaying()
        getDataCommingSoon()

    }

    private fun showUserInfo(currentUser: FirebaseUser?) {
        mDatabase.child("User").child(currentUser.toString()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(data: DataSnapshot) {
                if (data.exists())
                {
                    val user: User? = data.getValue(User::class.java)
                    tv_nama.text = user?.name
                    context?.let {
                        Glide.with(it)
                            .load(user?.image)
                            .apply(RequestOptions.circleCropTransform())
                            .into(iv_profile)
                    }

                    user?.saldo?.toDouble()?.let { currecy(it, tv_saldo) }
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    // mengambil data dari firebase
    private fun getDataNowPlaying() {
        mDatabase.child("Film").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (dataFilm in dataSnapshot.children) {
                    val film = dataFilm.getValue(Film::class.java)
                    val status = dataFilm.child("status").value.toString()
                    if (film != null) {
                        if (status == "now playing")
                        {
                            dataList.add(film)
                        }
                    }
                }

                 /*
                dataSnapshot.children.forEach { getdataSnapshot ->
                    val film = getdataSnapshot.getValue(Film::class.java)
                    dataList.add(film!!)
                }
                  */

                if (dataList.isNotEmpty()) {
                    this@DashboardFragment.rv_now_playing.adapter = NowPlayingAdapter(dataList) {
                        val intent = Intent(context, MovieDetailActivity::class.java).putExtra("data", it)
                        startActivity(intent)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun getDataCommingSoon() {
        mDatabase.child("Film").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                for (dataFilm in dataSnapshot.children) {
                    val film = dataFilm.getValue(Film::class.java)
                    val status = dataFilm.child("status").value.toString()
                    if (film != null) {
                        if (status == "comming soon")
                        {
                            dataList.add(film)
                        }
                    }
                }

                /*
               dataSnapshot.children.forEach { getdataSnapshot ->
                   val film = getdataSnapshot.getValue(Film::class.java)
                   dataList.add(film!!)
               }
                 */

                if (dataList.isNotEmpty()) {
                    this@DashboardFragment.rv_comming_soon.adapter = CommingSoonAdapter(dataList) {
                        val intent = Intent(context, MovieDetailActivity::class.java).putExtra("data", it)
                        startActivity(intent)
                    }
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }


    // konversi nilai saldo menjadi format angka
    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.text = formatRupiah.format(harga)

    }

}
