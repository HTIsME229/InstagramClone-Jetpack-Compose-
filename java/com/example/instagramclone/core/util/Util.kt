package com.example.instagramclone.core.util

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast


fun isValidUri(uriString: String?): Boolean {
    if (uriString.isNullOrBlank()) return false

    return try {
        val uri = Uri.parse(uriString)
        uri.scheme != null
    } catch (e: Exception) {
        false
    }
}
fun isVideoUrl(url: String?): Boolean {
    if (url.isNullOrBlank()) return false
    val lowerCaseUrl = url.lowercase()
    return lowerCaseUrl.endsWith(".mp4") || lowerCaseUrl.endsWith(".mov") ||
            lowerCaseUrl.endsWith(".avi") || lowerCaseUrl.endsWith(".mkv") ||
            lowerCaseUrl.endsWith(".flv") || lowerCaseUrl.endsWith(".wmv")
}
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
fun loadGalleryVideos(context: Context): List<Uri> {
    val videoList = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Video.Media._ID)
    val sortOrder = "${MediaStore.Video.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Video.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id
            )
            videoList.add(contentUri)
        }
    }

    return videoList
}

fun loadGalleryImages(context: Context): List<Uri> {
    val imageList = mutableListOf<Uri>()
    val projection = arrayOf(MediaStore.Images.Media._ID)
    val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"

    context.contentResolver.query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        projection,
        null,
        null,
        sortOrder
    )?.use { cursor ->
        val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
        while (cursor.moveToNext()) {
            val id = cursor.getLong(idColumn)
            val contentUri = ContentUris.withAppendedId(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
            )
            imageList.add(contentUri)
        }
    }

    return imageList

}
fun isVideoUri(context: Context, uri: Uri): Boolean {
    val type = context.contentResolver.getType(uri)
    return type?.startsWith("video") == true
}
fun extractHashtags(caption: String): List<String> {
    val pattern = "#\\w+".toRegex()
    return pattern.findAll(caption)
        .map { it.value }
        .toList()
}
