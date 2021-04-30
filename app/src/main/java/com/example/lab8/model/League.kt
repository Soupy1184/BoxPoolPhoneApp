package com.example.lab8.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize
import java.security.Key

@Parcelize
@IgnoreExtraProperties
data class League(
    val leagueId: String = "",
    val leagueName: String = "",
    val teamName: String = "",
    val user: String = ""
) : Parcelable

//
//val content: String = "",
//val author: String = "",
//val timestamp: Long = 0L