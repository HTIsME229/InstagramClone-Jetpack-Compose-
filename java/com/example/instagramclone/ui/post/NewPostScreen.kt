package com.example.instagramclone.ui.post

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.instagramclone.data.model.Visibility

@Composable
fun NewPostScreen(uri: String,onPostSelected: (uri:String,caption:String,
                                               selectedVisibility:Visibility) -> Unit) {
    var caption by remember { mutableStateOf("") }
    var isAiTagEnabled by remember { mutableStateOf(false) }
    var selectedVisibility by remember { mutableStateOf(Visibility.PUBLIC) }


    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(20.dp)
        .padding(top = 25.dp)
        .verticalScroll(rememberScrollState())
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(end = 8.dp),
                    horizontalArrangement = Arrangement.Center
        ) {

            Image(
                painter = rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(1f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        TextField(
            value = caption,
            onValueChange = { caption = it },
            placeholder = { Text("Thêm chú thích...") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingItem(icon = Icons.Default.Person, label = "Gắn thẻ người khác")
        SettingItem(icon = Icons.Default.LocationOn, label = "Thêm vị trí")
        SettingItem(icon = Icons.Default.Add, label = "Thêm nhạc")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Thêm nhãn AI", fontWeight = FontWeight.Medium)
            Switch(checked = isAiTagEnabled, onCheckedChange = { isAiTagEnabled = it })
        }

        HorizontalDivider()
        VisibilitySelector(
            selectedVisibility,
            onVisibilitySelected = { it ->
                selectedVisibility = it


            }
        )
        SettingItem(label = "Cũng chia sẻ trên...", trailing = { Text("Facebook") })
        SettingItem(label = "Lựa chọn khác")

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {onPostSelected(uri,caption,selectedVisibility)},
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp)
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Chia sẻ")
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector? = null,
    label: String,
    trailing: @Composable (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (icon != null) {
            Icon(icon, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(label, modifier = Modifier.weight(1f))
        trailing?.invoke()
    }
}
@Composable
fun VisibilitySelector(
    selectedVisibility: Visibility,
    onVisibilitySelected: (Visibility) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    // Hiển thị tên dựa trên giá trị enum hiện tại
    var displayName = when (selectedVisibility) {
        Visibility.PUBLIC -> "Công khai"
        Visibility.PRIVATE -> "Riêng tư"
        Visibility.FOLLOWERS -> "Người theo dõi"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = true }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Đối tượng")
Box(){
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = displayName)
        Icon(
            imageVector = Icons.Default.ArrowDropDown,
            contentDescription = "Chọn đối tượng"
        )
        DropdownMenu(
            expanded = expanded,

            onDismissRequest = { expanded = false },
            offset = DpOffset((-10).dp, 230.dp)

        ) {
            DropdownMenuItem(

                text = { Text("Công khai") },
                onClick = {
                    onVisibilitySelected(Visibility.PUBLIC)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Riêng tư") },
                onClick = {
                    onVisibilitySelected(Visibility.PRIVATE)
                    expanded = false
                }
            )

            DropdownMenuItem(
                text = { Text("Người theo dõi") },
                onClick = {
                    onVisibilitySelected(Visibility.FOLLOWERS)
                    expanded = false
                }
            )
        }

    }
}



    }

}
