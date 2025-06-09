package com.example.instagramclone.util

import android.net.Uri


fun isValidUri(uriString: String?): Boolean {
    if (uriString.isNullOrBlank()) return false

    return try {
        val uri = Uri.parse(uriString)
        uri.scheme != null
    } catch (e: Exception) {
        false
    }
}