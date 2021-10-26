package com.scoto.hadila.helper

import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class FirebaseDbManager @Inject constructor(
    private val db: FirebaseFirestore,
    private val uid: String
) {

    init {
//        val settings = FirebaseFirestoreSettings.Builder()
//            .setPersistenceEnabled(true)
//            .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
//            .build()
//        db.firestoreSettings = settings

    }

    fun dbProblem(): CollectionReference =
        db.collection("root").document(uid).collection(PATH_PROBLEMS)

    fun dbUser(): CollectionReference =
        db.collection("root").document(uid).collection(PATH_USER)

    fun dbUserLogs(): CollectionReference =
        db.collection("root").document(uid).collection(PATH_USER_LOGS)

    fun dbCollections(): CollectionReference =
        db.collection("root").document(uid).collection(PATH_COLLECTIONS)

    companion object {
        private const val PATH_USER = "user"
        private const val PATH_PROBLEMS = "problems"
        private const val PATH_USER_LOGS = "user_logs"
        private const val PATH_COLLECTIONS = "collections"
    }
}