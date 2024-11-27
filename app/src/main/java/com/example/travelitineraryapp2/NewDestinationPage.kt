package com.example.travelitineraryapp2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log

class NewDestinationPage : AppCompatActivity() {

    private lateinit var editTextDestinationName: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_destination)

        editTextDestinationName = findViewById(R.id.TextDestinationName)

        findViewById<Button>(R.id.buttonSaveDestination).setOnClickListener {
            saveDestination()
        }
    }

    private fun saveDestination() {
        val name = editTextDestinationName.text.toString()

        val destination = Destination(destinationName  = name)
        FirebaseFirestore.getInstance().collection("destinations")
            .document(destination.id)
            .set(destination)
            .addOnSuccessListener {
                Toast.makeText(this, "Destination saved!", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception ->
                Log.e("NewDestinationPage", "Error saving destination: ", exception)
            }
    }
}
