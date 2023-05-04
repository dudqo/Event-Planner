package com.dudqo.eventplanner.domain.repository

import android.content.res.Resources
import com.dudqo.eventplanner.domain.model.Event
import com.dudqo.eventplanner.util.Resource
import com.google.firebase.Timestamp
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

const val EVENTS_COLLECTION_REF = "events"

class StorageRepository {

    fun user() = Firebase.auth.currentUser

    fun getUserId(): String = Firebase.auth.currentUser?.uid.orEmpty()

    private val eventsRef: CollectionReference = Firebase
        .firestore.collection(EVENTS_COLLECTION_REF)

    fun getUserEvents(
        userId: String,
    ): Flow<Resource<List<Event>>> = callbackFlow {
        var snapshotStateListener: ListenerRegistration? = null

        try {
            snapshotStateListener = eventsRef
                .orderBy("timestamp")
                .whereEqualTo("userId", userId)
                .addSnapshotListener { snapshot, e ->
                    val response = if (snapshot != null) {
                        val events = snapshot.toObjects(Event::class.java)
                        Resource.Success(data = events)
                    } else {
                        Resource.Error(message = e?.toString()!!)
                    }
                    trySend(response)

                }


        } catch (e: Exception) {
            trySend(Resource.Error(message = e.toString()))
            e.printStackTrace()
        }

        awaitClose {
            snapshotStateListener?.remove()
        }


    }

    fun getEventById(
        eventId: String,
        onError: (Throwable?) -> Unit,
        onSuccess: (Event?) -> Unit
    ) {
        eventsRef
            .document(eventId)
            .get()
            .addOnSuccessListener {
                onSuccess.invoke(it?.toObject(Event::class.java))
            }
            .addOnFailureListener { result ->
                onError.invoke(result.cause)
            }


    }

    fun addEvent(
        event: Event,
        onComplete: (Boolean) -> Unit,
    ) {
        val documentId = eventsRef.document().id
        eventsRef
            .document(documentId)
            .set(event)
            .addOnCompleteListener { result ->
                onComplete.invoke(result.isSuccessful)
            }


    }

    fun deleteEvent(eventId: String, onComplete: (Boolean) -> Unit) {
        eventsRef.document(eventId)
            .delete()
            .addOnCompleteListener {
                onComplete.invoke(it.isSuccessful)
            }
    }

/*    fun updateEvent(
        title: String,
        note: String,
        color: Int,
        noteId: String,
        onResult: (Boolean) -> Unit
    ) {
        val updateData = hashMapOf<String, Any>(
            "colorIndex" to color,
            "description" to note,
            "title" to title,
        )

        eventsRef.document(noteId)
            .update(updateData)
            .addOnCompleteListener {
                onResult(it.isSuccessful)
            }


    }*/
}