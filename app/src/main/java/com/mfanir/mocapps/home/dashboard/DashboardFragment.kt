package com.mfanir.mocapps.home.dashboard

import android.content.Intent
import android.graphics.Movie
import android.os.Bundle
import android.util.Log
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
import com.mfanir.mocapps.auth.signin.User
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*

class DashboardFragment : Fragment() {

    private lateinit var preferences: Preferences
    lateinit var mDatabase: DatabaseReference
    val user = FirebaseAuth.getInstance().currentUser
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

        preferences = Preferences(activity!!.applicationContext)
        mDatabase = FirebaseDatabase.getInstance().getReference("Film")

        tv_nama.text = preferences.getValues("name")

        if (!preferences.getValues("saldo").equals(""))
        {
            currecy(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
        }

        if (user?.photoUrl == null)
        {
            iv_profile.setImageResource(R.drawable.user_pic)
        }
        else
        {

            Glide.with(this)
                .load(user?.photoUrl.toString())
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)
        }

        //menampilkan data film dengan recycle view
        rv_now_playing.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rv_comming_soon.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getData()

    }


    private fun getData() {
        mDatabase.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()
                dataSnapshot.children.forEach { getdataSnapshot ->
                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    dataList.add(film!!)
                }

                if (dataList.isNotEmpty()) {
                    rv_now_playing.adapter = NowPlayingAdapter(dataList) {
                        val intent = Intent(context, MovieDetailActivity::class.java).putExtra("data", it)
                        startActivity(intent)
                    }

                    rv_comming_soon.adapter = CommingSoonAdapter(dataList) {
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



    private fun currecy(harga:Double, textView: TextView) {
        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga))

    }

}
