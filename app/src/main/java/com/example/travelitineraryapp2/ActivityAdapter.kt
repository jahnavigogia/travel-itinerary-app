package com.example.travelitineraryapp2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class ActivityAdapter(
    private val activities: MutableList<Activity>,
    private val onDelete: (Activity) -> Unit
) : RecyclerView.Adapter<ActivityAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textViewActivityName: TextView = itemView.findViewById(R.id.textViewActivityName)
        private val textViewActivityDate: TextView = itemView.findViewById(R.id.textViewActivityDate)
        private val textViewActivityTime: TextView = itemView.findViewById(R.id.textViewActivityTime)

        fun bind(activity: Activity) {
            textViewActivityName.text = activity.activityName
            textViewActivityDate.text = activity.date
            textViewActivityTime.text = activity.time

            itemView.setOnClickListener {
                // Implement click functionality if needed
            }

            // Add a delete button listener here if needed
            itemView.findViewById<Button>(R.id.buttonDeleteActivity).setOnClickListener {
                onDelete(activity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_activity, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(activities[position])
    }

    override fun getItemCount(): Int = activities.size
}
