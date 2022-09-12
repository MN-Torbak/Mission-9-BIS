package com.openclassrooms.firebaseREM.model

import android.location.Location
import com.google.type.TimeOfDay
import java.io.Serializable
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

data class Property(
    val type: String = "",
    val price: Int = 0,
    val avatar1: String = "",
    val description: String = "",
    val surface: Int = 0,
    val numberOfRooms: Int = 0,
    val numberOfBathrooms: Int = 0,
    val numberOfBedrooms: Int = 0,
    val city: String = "",
    val address: String = "",
    val createDate: String = "",
    val saleDate: String = "",
    val closeToShops: Boolean = false,
    val closeToSchools: Boolean = false,
    val closeToParc: Boolean = false,
    val agentWhoAdd: String = "",
    val agentWhoSells: String = "",
    var numberOfPhotos: Int = 0,
    var id: String? = "",
) : Serializable

