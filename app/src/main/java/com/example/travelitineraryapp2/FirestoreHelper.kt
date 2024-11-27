import com.google.firebase.firestore.FirebaseFirestore

class FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()

    fun addDestination(destination: com.example.travelitineraryapp2.Destination, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("destinations").add(destination)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun updateDestination(destinationId: String, updatedData: Map<String, Any>, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("destinations").document(destinationId).update(updatedData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteDestination(destinationId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("destinations").document(destinationId).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun addActivity(destinationId: String, activity: com.example.travelitineraryapp2.Activity, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("destinations").document(destinationId).collection("activities").add(activity)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    fun deleteActivity(destinationId: String, activityId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("destinations").document(destinationId).collection("activities").document(activityId).delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
