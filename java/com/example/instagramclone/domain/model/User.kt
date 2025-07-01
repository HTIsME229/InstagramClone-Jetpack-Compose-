package com.example.instagramclone.domain.model

import com.google.gson.annotations.SerializedName


data class User(
    var tokenId: String = "",
    @SerializedName("uid")
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

}
