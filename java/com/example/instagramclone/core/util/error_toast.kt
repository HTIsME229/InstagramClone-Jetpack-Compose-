package com.example.instagramclone.core.util

import android.widget.Toast

fun toastError(
    context: android.content.Context,
    message: String,
    duration: Int = Toast.LENGTH_SHORT,
    onDismiss: () -> Unit = {}
) {
Toast.makeText(context, message, duration).apply {
    show()
}}