package com.example.madassignment3.ui.map

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class Facility(
    @PrimaryKey val facilityName:String,
    val facilityImage:Int,
    val facilityService:String,
    val facilityDesc:String,
    val latitude: Double,
    val longitude: Double
 ) : Serializable
