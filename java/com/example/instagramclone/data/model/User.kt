package com.example.instagramclone.data.model


data class User(
    var tokenId: String = "",
    var userId: String = "",
    var email: String = "",
    var  userName: String = "",
    var name: String = "",
    var imageUrl: String = "",
    var phone: String = "",
    var bio : String = "",
    var password: String = "",
    var followers: List<String> = emptyList(),
    var following: List<String> = emptyList(),

)
{
    constructor(
        tokenId: String,
        currentUser: User?,
        email: String,
        username: String,
        name: String,
        imageUrl: String,
        phone: String
    ) : this()
}
