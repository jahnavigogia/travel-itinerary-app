package com.example.travelitineraryapp2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.util.Log

class DestinationDetailActivity : AppCompatActivity() {

    private lateinit var recyclerViewActivities: RecyclerView
    private lateinit var activityAdapter: ActivityAdapter
    private lateinit var activities: MutableList<Activity>
    private lateinit var destinationId: String

    // Variables to hold selected date and time
    private var selectedDate: String = ""
    private var selectedTime: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_destination_detail)

        destinationId = intent.getStringExtra("destinationId") ?: ""
        recyclerViewActivities = findViewById(R.id.recyclerViewActivities)
        recyclerViewActivities.layoutManager = LinearLayoutManager(this)

        activities = mutableListOf()
        activityAdapter = ActivityAdapter(activities) { activity -> deleteActivity(activity) }
        recyclerViewActivities.adapter = activityAdapter

        fetchActivities()

        findViewById<FloatingActionButton>(R.id.btnAddActivity).setOnClickListener {
            showAddActivityDialog()
        }
    }
    private fun fetchActivities() {
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
                Log.e("DestinationDetailActivity", "Error fetching activities: ", exception)
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
                Toast.makeText(this, "Activity deleted", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { exception ->
                Log.e("DestinationDetailActivity", "Error deleting activity: ", exception)
            }
    }

    private fun showAddActivityDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Add Activity")

        val view = layoutInflater.inflate(R.layout.dialog_add_activity, null)
        val editTextActivityName = view.findViewById<EditText>(R.id.editTextActivityName)
        val editTextActivityDate = view.findViewById<EditText>(R.id.editTextActivityDate)
        val editTextActivityTime = view.findViewById<EditText>(R.id.editTextActivityTime)

        builder.setView(view)
        builder.setPositiveButton("Add") { _, _ ->
            val activityName = editTextActivityName.text.toString()
            val activityDate = selectedDate.ifEmpty { editTextActivityDate.text.toString() }
            val activityTime = selectedTime.ifEmpty { editTextActivityTime.text.toString() }

            val newActivity = Activity(UUID.randomUUID().toString(), activityName, activityDate, activityTime)
            FirebaseFirestore.getInstance().collection("destinations")
                .document(destinationId)
                .collection("activities")
                .document(newActivity.id)
                .set(newActivity)
                .addOnSuccessListener {
                    activities.add(newActivity)
                    activityAdapter.notifyDataSetChanged()
                    Toast.makeText(this, "Activity added", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener { exception ->
                    Log.e("DestinationDetailActivity", "Error adding activity: ", exception)
                }
        }
        builder.setNegativeButton("Cancel", null)

        builder.create().show()
    }

    // Show DatePickerDialog
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            Toast.makeText(this, "Selected Date: $selectedDate", Toast.LENGTH_SHORT).show()
        }, year, month, day)

        datePickerDialog.show()
    }

    // Show TimePickerDialog
    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            Toast.makeText(this, "Selected Time: $selectedTime", Toast.LENGTH_SHORT).show()
        }, hour, minute, true)

        timePickerDialog.show()
    }
}
