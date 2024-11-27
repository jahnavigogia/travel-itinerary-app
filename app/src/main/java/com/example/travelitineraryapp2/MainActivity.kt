package com.example.travelitineraryapp2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerViewDestinations: RecyclerView
    private lateinit var destinationAdapter: DestinationAdapter
    private lateinit var destinations: MutableList<Destination>
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Check if user is authenticated
        if (auth.currentUser == null) {
            // If not logged in, navigate to LoginActivity
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
            finish() // Prevent going back to MainActivity without logging in
            return
        }

        // Initialize RecyclerView
        recyclerViewDestinations = findViewById(R.id.recyclerViewDestinations)
        recyclerViewDestinations.layoutManager = LinearLayoutManager(this)

        // Initialize destinations list and adapter
        destinations = mutableListOf()
        destinationAdapter = DestinationAdapter(destinations) { destination ->
            navigateToDestinationDetail(destination.id)
        }
        recyclerViewDestinations.adapter = destinationAdapter

        // Fetch destinations from Firestore
        fetchDestinations()

        // Set click listener for adding a new destination
        findViewById<FloatingActionButton>(R.id.btnAddDestination).setOnClickListener {
            startActivity(Intent(this, NewDestinationPage::class.java))
        }

        // Set click listener for logging out
        findViewById<Button>(R.id.btnLogout).setOnClickListener {
            logoutUser()
        }
    }

    private fun fetchDestinations() {
        FirebaseFirestore.getInstance().collection("destinations")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val destination = document.toObject(Destination::class.java)
                    destinations.add(destination)
                }
                destinationAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Error fetching destinations: ", exception)
                Toast.makeText(this, "Error fetching destinations", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navigateToDestinationDetail(destinationId: String) {
        val intent = Intent(this, DestinationDetailActivity::class.java).apply {
            putExtra("destinationId", destinationId)
        }
        startActivity(intent)
    }

    private fun logoutUser() {
        auth.signOut() // Sign out from Firebase
        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Redirect to LoginActivity
        val loginIntent = Intent(this, LoginActivity::class.java)
        startActivity(loginIntent)
        finish() // Prevent going back to MainActivity
    }
}
