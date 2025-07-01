package com.example.instagramclone.presentation.ui.post.create_post.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.example.instagramclone.domain.model.Visibility

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
        Box() {
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
