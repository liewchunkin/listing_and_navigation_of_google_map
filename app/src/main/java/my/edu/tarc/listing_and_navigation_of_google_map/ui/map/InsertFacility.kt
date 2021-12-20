package my.edu.tarc.listing_and_navigation_of_google_map.ui.map

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.madassignment3.ui.database.FacilityDatabase
import com.example.madassignment3.ui.map.Facility
import my.edu.tarc.listing_and_navigation_of_google_map.R
import my.edu.tarc.listing_and_navigation_of_google_map.databinding.ActivityInsertFacilityBinding

class InsertFacility : AppCompatActivity() {

    private lateinit var binding: ActivityInsertFacilityBinding
    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInsertFacilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.buttonGetLaLong.setOnClickListener {
            val intent = Intent(this@InsertFacility, CreateMapActivity::class.java)

            intent.putExtra("Name", binding.editFacilityName.text.toString())
            intent.putExtra("Desc", binding.editFacilityDesc.text.toString())
            intent.putExtra("Service", binding.editServiceList.text.toString())
            finish()
            startActivity(intent)
        }

        val intent1 = intent
        val latitude = intent1.getDoubleExtra("La", 0.00)
        val longitude = intent1.getDoubleExtra("Long", 0.00)
        val nameBefore = intent1.getStringExtra("Name")
        val descBefore = intent1.getStringExtra("Desc")
        val serviceBefore = intent1.getStringExtra("Service")

        binding.editFacilityName.setText(nameBefore)
        binding.editFacilityDesc.setText(descBefore)
        binding.editServiceList.setText(serviceBefore)

        val latitude1 = latitude.toString()
        binding.tvLatitude.text = latitude1

        val longitude1 = longitude.toString()
        binding.tvLongitude.text = longitude1

        binding.buttonUploadFacilityImage.setOnClickListener {
            selectImage()
        }

        binding.buttonPostFacility.setOnClickListener {
            //check user input
            if (binding.editFacilityName.text.isEmpty()) {
                binding.editFacilityName.error = getString(R.string.error_input)
                return@setOnClickListener
            }
            if (binding.editFacilityDesc.text.isEmpty()) {
                binding.editFacilityDesc.error = getString(R.string.error_input)
                return@setOnClickListener
            }
            if (binding.editServiceList.text.isEmpty()) {
                binding.editServiceList.error = getString(R.string.error_input)
                return@setOnClickListener
            }
            if (binding.tvLatitude.text.toString() == "0.0") {
                Toast.makeText(this, "Must choose latitude and longitude", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

//            val fileName = binding.editFacilityName.text
//            val storageReference =
//                FirebaseStorage.getInstance().getReference("facilityImages/$fileName")
//
//            storageReference.putFile(imageUri)

            val facilityName = binding.editFacilityName.text.toString()
            val facilityService = binding.editServiceList.text.toString()
            val facilityDesc = binding.editFacilityDesc.text.toString()


            val latitude2 = latitude.toString().toDouble()
            val longitude2 = longitude.toString().toDouble()


            val database = Room.databaseBuilder(
                this, FacilityDatabase::class.java, "facility_database"
            )
                .allowMainThreadQueries()
                .build()

            database.facilityDao().insertFacility(
                Facility(
                    facilityName = facilityName,
                    facilityDesc = facilityDesc,
                    facilityImage = R.drawable.aspira,
                    facilityService = facilityService,
                    latitude = latitude2, longitude = longitude2
                )
            )

//            val databaseF: DatabaseReference = Firebase.database.reference
//            databaseF.child("Facility").child(facilityName).child("facilityName")
//                .setValue(facilityName)
//            databaseF.child("Facility").child(facilityName).child("facilityDesc")
//                .setValue(facilityDesc)
//            databaseF.child("Facility").child(facilityName).child("latitude")
//                .setValue(latitude2)
//            databaseF.child("Facility").child(facilityName).child("longitude")
//                .setValue(longitude2)
//            databaseF.child("Facility").child(facilityName).child("facilityService")
//                .setValue(facilityService)




            Toast.makeText(this, "Added Facility into database", Toast.LENGTH_SHORT).show()

            finish()


        }
    }

    private fun selectImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT

        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK) {

            imageUri = data?.data!!
            binding.facilityImageView.setImageURI(imageUri)

        }
    }
}