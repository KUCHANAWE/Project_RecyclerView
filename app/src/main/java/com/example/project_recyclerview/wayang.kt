package com.example.project_recyclerview

import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import kotlinx.parcelize.Parcelize
@Parcelize
data class wayang(
    var foto:String,
    var nama:String,
    var karakter : String,
    var deskripsi : String
):Parcelable
