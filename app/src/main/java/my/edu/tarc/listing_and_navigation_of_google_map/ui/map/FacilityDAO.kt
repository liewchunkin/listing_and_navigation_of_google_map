package com.example.madassignment3.ui.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.madassignment3.ui.map.Facility

@Dao
interface FacilityDAO {

    @Query("SELECT * FROM facility ORDER BY facilityName ASC")
    fun getAllFacility(): List<Facility>

    @Insert
    fun insertFacility(facility: Facility)

    @Delete
    fun delFacility(facility: Facility)
}