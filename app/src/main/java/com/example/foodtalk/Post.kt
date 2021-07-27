package com.example.foodtalk

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Post(
    val imageUri: String? = null,
    val userId: String? = null,
    val timeStamp: String? = null,
    val title: String? = null
)