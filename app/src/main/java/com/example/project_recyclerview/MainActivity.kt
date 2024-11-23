package com.example.project_recyclerview

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.buildtools.reloc.com.google.common.reflect.TypeToken
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
    private var _nama: MutableList<String> = emptyList<String>().toMutableList()
    private var _karakter: MutableList<String> = emptyList<String>().toMutableList()
    private var _deskripsi: MutableList<String> = emptyList<String>().toMutableList()
    private var _gambar: MutableList<String> = emptyList<String>().toMutableList()
    private lateinit var _rvWayang: RecyclerView
    lateinit var sp : SharedPreferences
    private var arWayang = arrayListOf<Wayang>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences("dataSP", MODE_PRIVATE)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val gson = Gson()
        val isiSP = sp.getString("spWayang",null)
        val type = object : TypeToken<ArrayList<Wayang>>(){}.type
        if (isiSP!=null)
            arWayang = gson.fromJson(isiSP, type)


        _rvWayang = findViewById<RecyclerView>(R.id.rvWayang)
        if(arWayang.size==0){
            SiapkanData()
        } else{
            arWayang.forEach{
                _nama.add(it.nama)
                _gambar.add(it.foto)
                _deskripsi.add(it.deskripsi)
                _karakter.add(it.karakter)
            }
        }
        TambahData()
        TampilkanData()

    }

    fun SiapkanData() {
        _nama = resources.getStringArray(R.array.namaWayang).toMutableList()
        _deskripsi = resources.getStringArray(R.array.deskripsiWayang).toMutableList()
        _karakter = resources.getStringArray(R.array.karakterUtamaWayang).toMutableList()
        _gambar = resources.getStringArray(R.array.gambarWayang).toMutableList()
    }

    fun TambahData() {
        val gson = Gson()
        val editor = sp.edit()
        arWayang.clear()
        for (posititon in _nama.indices) {
            val data = Wayang(
                _gambar[posititon],
                _nama[posititon],
                _karakter[posititon],
                _deskripsi[posititon]
            )
            arWayang.add(data)
        }
        val json = gson.toJson(arWayang)
        editor.putString("spWayang",json)
        editor.apply()
    }

    fun TampilkanData() {
        _rvWayang.layoutManager = LinearLayoutManager(this)
//        _rvWayang.layoutManager = GridLayoutManager(this,2)
//        _rvWayang.layoutManager = StaggeredGridLayoutManager(2,LinearLayoutManager.VERTICAL) //mengikuti panjang deksripsi jadi semakin turun kebawah
//        _rvWayang.adapter = adapterRecView(arWayag)

        // adapter wayang
        val adapterWayang = adapterRecView(arWayang)
        _rvWayang.adapter = adapterWayang

        adapterWayang.setOnItemClickCallback(object : adapterRecView.OnItemClickCallback {
            override fun onItemClicked(data: Wayang) {
                Toast.makeText(this@MainActivity, data.nama, Toast.LENGTH_LONG).show()
                val intent = Intent(this@MainActivity,detWayang::class.java)
                intent.putExtra("kirimData", data)
                startActivity(intent)

            }
            override fun delData(pos: Int) {
                AlertDialog.Builder(this@MainActivity)
                    .setTitle("HAPUS DATA")
                    .setMessage("Apakah Benar Data " + _nama[pos] + " akan dihapus ?")
                    .setPositiveButton(
                        "HAPUS", DialogInterface.OnClickListener{
                                dialog, which -> _gambar.removeAt(pos)
                            _nama.removeAt(pos)
                            _deskripsi.removeAt(pos)
                            _karakter.removeAt(pos)
                            TambahData()
                            TampilkanData()
                        }
                    )
                    .setNegativeButton(
                        "BATAL", DialogInterface.OnClickListener{
                                dialog, which ->
                            Toast.makeText(this@MainActivity, "Data Batal Dihapus", Toast.LENGTH_LONG)
                                .show()
                        }
                    ).show()
            }

        })

    }
}