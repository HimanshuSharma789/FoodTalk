package com.example.foodtalk

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class NewPostViewModel : ViewModel() {

    enum class PostUploadStatus{ LOADING, DONE, ERROR}
    private var database: DatabaseReference = Firebase.database("https://food-talk-e7599-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    private var storageRef: StorageReference = Firebase.storage.reference
    private var userId = Firebase.auth.currentUser!!.uid
    var currentPhotoPath: String? = null
    var navigateToHome = MutableLiveData(false)
    var uploadStatus = MutableLiveData<PostUploadStatus>()

    fun uploadPost(title: String) {
        uploadStatus.value = PostUploadStatus.LOADING
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
        val uniquePostId = userId + "_" + timeStamp

        val uploadPath = storageRef.child("image/$uniquePostId.jpg")
        val uploadTask = uploadPath.putFile(Uri.fromFile(File(currentPhotoPath!!)))
        uploadTask.addOnFailureListener {
            Log.v("firebase storage", it.message.toString())
            uploadStatus.value = PostUploadStatus.ERROR

        }.addOnSuccessListener {
            // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
            val post = Post("$uniquePostId.jpg", userId, timeStamp, title)
            database.child("posts").child(uniquePostId).setValue(post)
                .addOnSuccessListener {
                    uploadStatus.value = PostUploadStatus.DONE
//                    navigateToHome.value = true
                }
                .addOnFailureListener {
                    Log.v("firebase database", it.message.toString())
                    uploadStatus.value = PostUploadStatus.ERROR
                }

        }
    }


}