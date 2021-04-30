package com.example.lab8.model

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@Parcelize
@IgnoreExtraProperties
data class Team(
    var playerName: String = "",
    var playerId: String = "",
    var points: Int = 0,
    var boxNumber: Int? = null
): Parcelable