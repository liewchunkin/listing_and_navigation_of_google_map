package my.edu.tarc.listing_and_navigation_of_google_map.ui.map

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.madassignment3.ui.map.Facility
import my.edu.tarc.listing_and_navigation_of_google_map.R

private const val TAG = "FacilityAdapter"
class FacilityAdapter(
    private val facilityList: List<Facility>,
    val onClickListener: OnClickListener

) : RecyclerView.Adapter<FacilityAdapter.myViewHolder>(){

    interface OnClickListener {
        fun onItemClick(position: Int)
    }

    class myViewHolder (itemView: View): RecyclerView.ViewHolder(itemView){
        val serviceName:TextView = itemView.findViewById(R.id.name)
        val serviceImage:ImageView =  itemView.findViewById(R.id.image)
        val serviceHours:TextView =  itemView.findViewById(R.id.hours)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.nearby_item, parent, false)

        return myViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {

        val currentFacility = facilityList[position]

        holder.itemView.setOnClickListener {
            Log.i(TAG, "Tapped on position $position")
            onClickListener.onItemClick(position)
        }

        holder.serviceName.text = currentFacility.facilityName

        holder.serviceImage.setImageResource(currentFacility.facilityImage)
        holder.serviceHours.text = currentFacility.facilityService
    }

    override fun getItemCount(): Int {
        return  facilityList.size
    }



}