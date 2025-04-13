package com.example.instagramclone.ui.post

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import coil.compose.rememberAsyncImagePainter

@Composable
fun InstagramStylePicker(
    onNextClicked:  (Uri) -> Unit
) {
    val context = LocalContext.current
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedImage by remember { mutableStateOf<Uri?>(null) }
    var hasPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.READ_MEDIA_IMAGES
                    ) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
        if (isGranted) {
            images = loadGalleryImages(context)
            if (images.isNotEmpty()) {
                selectedImage = images.first()
            }
        }
    }

    // Biến để kiểm soát việc hiển thị AlertDialog
    var showPermissionDialog by remember { mutableStateOf(!hasPermission) }

    // Request permission and load images if permission is granted
    LaunchedEffect(hasPermission) {
        if (hasPermission) {
            showPermissionDialog = false
            images = loadGalleryImages(context)
            if (images.isNotEmpty()) {
                selectedImage = images.first()
            }
        }
    }
Box(   modifier = Modifier
    .fillMaxSize()
    .padding(top = 20.dp)
    ){
    Column(modifier = Modifier.fillMaxSize()) {
        // Header: nút Tiếp
        Row(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "Bài viết mới", fontWeight = FontWeight.Bold)
            if (hasPermission && selectedImage != null) {
                Text(
                    text = "Tiếp",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        selectedImage?.let(onNextClicked)
                    }
                )
            }
        }

        if (!hasPermission) {
            // Show placeholder UI when no permission
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Không có quyền truy cập thư viện ảnh")
                Button(
                    onClick = { showPermissionDialog = true },
                    modifier = Modifier.padding(top = 16.dp)
                ) {
                    Text("Cấp quyền ngay")
                }
            }

            if (showPermissionDialog) {
                AlertDialog(
                    onDismissRequest = { /* Không làm gì khi bấm ra ngoài */ },
                    title = { Text("Cần cấp quyền") },
                    text = { Text("Ứng dụng cần quyền truy cập vào thư viện ảnh để hiển thị và cho phép bạn chọn ảnh. Vui lòng cấp quyền để tiếp tục.") },
                    confirmButton = {
                        androidx.compose.material3.TextButton(
                            onClick = {
                                val permission = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    Manifest.permission.READ_MEDIA_IMAGES
                                } else {
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                                }
                                permissionLauncher.launch(permission)
                            }
                        ) {
                            Text("Cấp quyền")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                // Đóng AlertDialog
                                showPermissionDialog = false
                            }
                        ) {
                            Text("Để sau")
                        }
                    }
                )
            }
        } else {
            // Ảnh được chọn
            selectedImage?.let {
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                )
            }

            // Grid ảnh
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f)
            ) {
                items(images) { imageUri ->
                    Image(
                        painter = rememberAsyncImagePainter(imageUri),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable {
                                selectedImage = imageUri
                            }
                            .padding(1.dp)
                    )
                }
            }
        }
    }
}
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