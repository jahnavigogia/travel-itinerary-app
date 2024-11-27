package com.example.travelitineraryapp2

import android.os.Parcelable
import com.google.firebase.firestore.DocumentId
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination(
    @DocumentId val id: String = "",
    val destinationName: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val numberOfPeople: Int = 0,
    val activities: MutableList<Activity> = mutableListOf()
) : Parcelable
