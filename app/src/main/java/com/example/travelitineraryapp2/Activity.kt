package com.example.travelitineraryapp2

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Activity(
    val id: String = "",
    val activityName: String = "",
    val date: String = "",
    val time: String = ""
) : Parcelable
