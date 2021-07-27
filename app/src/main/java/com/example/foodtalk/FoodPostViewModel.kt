package com.example.foodtalk

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class FoodPostViewModel: ViewModel() {

    private var database: DatabaseReference = Firebase.database("https://food-talk-e7599-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    private var storageRef: StorageReference = Firebase.storage.reference

    var posts: MutableLiveData<List<Post?>>? = null

    fun getPosts(): LiveData<List<Post?>>  {
        if(posts==null) {
            posts = MutableLiveData()
            fetchLatestPosts()
        }
        return posts as MutableLiveData<List<Post?>>
    }

    private fun fetchLatestPosts() {
        val postsList: MutableList<Post?> = mutableListOf()
        val query = database.child("posts").orderByChild("timeStamp")
        val postListener = object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children) {
                    val post = snap.getValue<Post?>()
                    postsList.add(post)
                    Log.v("snap:", post.toString())
                }
                posts?.value = postsList.toList().reversed()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.v("snap error details:", error.details)
                Log.v("snap error message:", error.message)
            }

        }
        query.addListenerForSingleValueEvent(postListener)
    }

}