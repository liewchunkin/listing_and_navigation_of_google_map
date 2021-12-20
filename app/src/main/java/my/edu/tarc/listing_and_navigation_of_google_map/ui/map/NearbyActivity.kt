package my.edu.tarc.listing_and_navigation_of_google_map.ui.map

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.madassignment3.ui.database.FacilityDatabase
import com.google.android.material.floatingactionbutton.FloatingActionButton
import my.edu.tarc.listing_and_navigation_of_google_map.R

const val EXTRA_USER_MAP = "EXTRA_USER_MAP"
const val EXTRA_MAP_TITLE = "EXTRA_MAP_TITLE"
private const val TAG = "NearbyActivity"
class NearbyActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby)

        val fabCreateMap = findViewById<FloatingActionButton>(R.id.fabCreateMap)


        Toast.makeText(this, "Insert a facility and tap to navigation of map", Toast.LENGTH_SHORT).show()

        val myRecyclerView : RecyclerView = findViewById(R.id.productRecycleView)

        //Build Room Database
        val database = Room.databaseBuilder(
            this, FacilityDatabase::class.java,"facility_database"
        )
            .allowMainThreadQueries()
            .build()





        //Get data and store in serviceList, tap and go to Map
        val facilityList = database.facilityDao().getAllFacility()




        myRecyclerView.adapter = FacilityAdapter(facilityList, object : FacilityAdapter.OnClickListener{
            override fun onItemClick(position: Int) {
                Log.i(TAG, "onItemClick $position")
                //When user taps on view in RV, navigate to new activity
                val intent = Intent(this@NearbyActivity, MapsActivity::class.java)
                intent.putExtra(EXTRA_USER_MAP, facilityList[position])
                startActivity(intent)
            }


        })
        myRecyclerView.setHasFixedSize(true)


        fabCreateMap.setOnClickListener {
            Log.i(TAG, "Tap on FAB")
            val intent = Intent(this@NearbyActivity, InsertFacility::class.java)
            startActivity(intent)
        }

    }


    override fun onRestart() {
        Log.i(TAG, "NearbyActivity onRestart")
        super.onRestart()
        finish()
        startActivity(intent)
    }


}