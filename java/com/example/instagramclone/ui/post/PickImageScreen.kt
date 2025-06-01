package com.example.instagramclone.ui.post

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.videoFrameMillis

@Composable
fun InstagramStylePicker(
    onNextClicked:  (Uri) -> Unit
) {
    val context = LocalContext.current
    var images by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var videos by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var allMedia by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var selectedMedia by remember { mutableStateOf<Uri?>(null) }
    var hasPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_MEDIA_VIDEO
                        ) == PackageManager.PERMISSION_GRANTED
            } else {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            }
        )
    }

    // Permission launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        hasPermission = permissions.values.any { it }
        if (hasPermission) {
            images = loadGalleryImages(context)
            videos = loadGalleryVideos(context)
             allMedia = images + videos
            if (images.isNotEmpty()) {
                selectedMedia = images.first()
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
            videos = loadGalleryVideos(context)
            allMedia = images + videos
            if (images.isNotEmpty()) {
                selectedMedia = images.first()
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
            if (hasPermission && selectedMedia != null) {
                Text(
                    text = "Tiếp",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        selectedMedia?.let(onNextClicked)
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
                                val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    arrayOf(
                                        Manifest.permission.READ_MEDIA_IMAGES,
                                        Manifest.permission.READ_MEDIA_VIDEO
                                    )
                                } else {
                                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                                }
                                permissionLauncher.launch(permissions)
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
            selectedMedia?.let {
                if (isVideoUri(context, selectedMedia!!)) {
                    VideoPlayer(uri = selectedMedia!!)
                } else {
                    Image(
                        painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(context)
                                .data(selectedMedia)
                                .size(1000, 1000)
                                .build()
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                    )
                }

            }

            // Grid ảnh
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.weight(1f)
            ) {
                items(allMedia) { uri ->
                    val isVideo = isVideoUri(context, uri)
                    if (!isVideo) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = ImageRequest.Builder(context)
                                    .data(uri)
                                    .size(1000, 1000)  // Giới hạn kích thước ảnh (width, height)
                                    .build()

                            ),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    selectedMedia = uri
                                }
                                .padding(1.dp)
                        )
                    }
                    else {
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clickable {
                                    selectedMedia = uri
                                }
                                .padding(1.dp)
                        ) {
                            // Thumbnail video với cấu hình tốt hơn
                            Image(
                                painter = rememberAsyncImagePainter(
                                    model = ImageRequest.Builder(context)
                                        .data(uri)
                                        .size(300, 300) // Giảm size để load nhanh hơn
                                        .crossfade(true)
                                        .videoFrameMillis(1000) // Lấy frame ở giây thứ 1
                                        .build()
                                ),
                                contentDescription = "Video thumbnail",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )

                            // Icon Play với background để dễ nhìn
                            Box(
                                modifier = Modifier
                                    .align(Alignment.Center)
                                    .size(48.dp)
                                    .background(
                                        Color.Black.copy(alpha = 0.5f),
                                        CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play video",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }

                }

            }
        }
    }
}
}
@Composable
fun VideoPlayer(uri: Uri) {
    val context = LocalContext.current

    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uri))
            prepare()
            playWhenReady = false
        }
    }
    LaunchedEffect(uri) {
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.playWhenReady = false
    }
    DisposableEffect (key1 = player) {
        onDispose {
            player.release()
        }
    }


    AndroidView(
        factory = {
            PlayerView(context).apply {
                this.player = player
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    )
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