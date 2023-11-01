package com.github.bowlbird.sig_appdev_firebase

class FirebaseRepository(private val firebaseDataSource: FirebaseDataSource) {
    fun get(callback: (List<String>?) -> Unit) = firebaseDataSource.get(callback)
    fun push(message: String, index: Int) {
        firebaseDataSource.push(message, index)
    }
}