package com.github.bowlbird.sig_appdev_firebase

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

class FirebaseDataSource(context: Context) {
    init { FirebaseApp.initializeApp(context)}
    private val database = Firebase.database
    private val ref = database.getReference("")
    init {
        ref.orderByKey()
    }

    fun push(message: String, index: Int) {
        //get local reference to root of firebase
        val childRef = ref.child(index.toString())
        childRef.setValue(message)
    }

    fun get(callback: (List<String>?) -> Unit) {
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                with(dataSnapshot.value) {
                    if (this != null) this::class.simpleName?.let { Log.d("Type", it) }
                    else Log.d("Type", "null")
                    callback(if(dataSnapshot.value != null)  dataSnapshot.value as List<String> else null)
                }

            }
            override fun onCancelled(error: DatabaseError) {
                // Failed to read value
                Log.w("ERROR", "Failed to read value.", error.toException())
            }
        })
    }
}