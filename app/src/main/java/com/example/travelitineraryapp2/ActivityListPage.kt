package com.example.travelitineraryapp2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore

class ActivityListPage : AppCompatActivity() {

    private lateinit var recyclerViewActivities: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var activities: MutableList<Activity>
    private lateinit var destinationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_activity_list)

        recyclerViewActivities = findViewById(R.id.recyclerViewActivities)
        recyclerViewActivities.layoutManager = LinearLayoutManager(this)

        activities = mutableListOf()
        activityAdapter = ActivityAdapter(activities) { activity -> deleteActivity(activity) }
        recyclerViewActivities.adapter = activityAdapter

        destinationId = intent.getStringExtra("destinationId") ?: ""
        fetchActivities()

        findViewById<FloatingActionButton>(R.id.btnAddActivity).setOnClickListener {
            startActivity(
                Intent(this, NewActivityPage::class.java).putExtra(
                    "destinationId",
                    destinationId
                )
            )
        }
    }

    private fun fetchActivities() {
        activities.clear() // Clear the list before fetching new activities
        FirebaseFirestore.getInstance().collection("destinations")
            .document(destinationId)
            .collection("activities")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val activity = document.toObject(Activity::class.java)
                    activities.add(activity)
                }
                activityAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("ActivityListPage", "Error fetching activities: ", exception)
            }
    }

    private fun deleteActivity(activity: Activity) {
        FirebaseFirestore.getInstance().collection("destinations")
            .document(destinationId)
            .collection("activities")
            .document(activity.id)
            .delete()
            .addOnSuccessListener {
                activities.remove(activity)
                activityAdapter.notifyDataSetChanged()
                Toast.makeText(this, "Activity deleted", Toast.LENGTH_SHORT).show() // Feedback on delete
            }
            .addOnFailureListener { exception ->
                Log.e("ActivityListPage", "Error deleting activity: ", exception)
            }
    }
}
