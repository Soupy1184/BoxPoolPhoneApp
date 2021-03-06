package com.example.lab8.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Post(
    val id: String = "",
    val content: String = "",
    val author: String = "",
    val timestamp: Long = 0L
) : Parcelable
