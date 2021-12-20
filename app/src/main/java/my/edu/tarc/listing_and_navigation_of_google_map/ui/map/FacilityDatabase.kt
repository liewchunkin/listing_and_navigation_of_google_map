package com.example.madassignment3.ui.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.madassignment3.ui.dao.FacilityDAO
import com.example.madassignment3.ui.map.Facility

@Database(entities = [Facility::class], version = 1)
abstract class FacilityDatabase : RoomDatabase(){

    abstract fun facilityDao(): FacilityDAO
}