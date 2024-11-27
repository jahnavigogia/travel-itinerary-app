package com.example.travelitineraryapp2

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Toast
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import android.util.Log

class NewActivityPage : AppCompatActivity() {

    private lateinit var editTextActivityName: EditText
    private lateinit var editTextActivityDate: EditText
    private lateinit var editTextActivityTime: EditText
    private lateinit var destinationId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_activity)

        editTextActivityName = findViewById(R.id.editTextActivityName)
        editTextActivityDate = findViewById(R.id.editTextActivityDate)
        editTextActivityTime = findViewById(R.id.editTextActivityTime)

        destinationId = intent.getStringExtra("destinationId") ?: ""

        editTextActivityDate.setOnClickListener { showDatePicker(editTextActivityDate) }
        editTextActivityTime.setOnClickListener { showTimePicker(editTextActivityTime) }

        findViewById<Button>(R.id.buttonSaveActivity).setOnClickListener {
            saveActivity()
        }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            editText.setText("$selectedYear/${selectedMonth + 1}/$selectedDay")
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            editText.setText(String.format("%02d:%02d", selectedHour, selectedMinute))
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun saveActivity() {
        val name = editTextActivityName.text.toString()
        val date = editTextActivityDate.text.toString()
        val time = editTextActivityTime.text.toString()

        val activity = Activity(UUID.randomUUID().toString(), name, date, time)
        FirebaseFirestore.getInstance().collection("destinations")
            .document(destinationId)
            .collection("activities")
            .document(activity.id)
            .set(activity)
            .addOnSuccessListener {
                Toast.makeText(this, "Activity saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Log.e("AddActivityPage", "Error saving activity: ", exception)
            }
    }
}
