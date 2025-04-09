package com.example.instagramclone.data.model

data class User(
    var userId: String = "",
    var email: String = "",
    var  userName: String = "",
    var name: String = "",
    var imageUrl: String = "",
    var bio : String = "",
    var password: String = "",
    var followers: List<String> = emptyList(),
    var following: List<String> = emptyList(),

)
