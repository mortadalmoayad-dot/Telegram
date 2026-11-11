package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Audiotrack
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Poll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.MessageType

data class AttachmentItem(
    val title: String,
    val icon: ImageVector,
    val gradient: List<Color>,
    val type: MessageType
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentSheet(
    onDismiss: () -> Unit,
    onSelectAttachment: (MessageType, String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()

    val items = listOf(
        AttachmentItem("المعرض", Icons.Default.Image, listOf(Color(0xFF00C6FF), Color(0xFF0072FF)), MessageType.PHOTO),
        AttachmentItem("ملف", Icons.Default.Description, listOf(Color(0xFF38EF7D), Color(0xFF11998E)), MessageType.FILE),
        AttachmentItem("الموقع", Icons.Default.LocationOn, listOf(Color(0xFFFF5E62), Color(0xFFFF9966)), MessageType.TEXT),
        AttachmentItem("جهة اتصال", Icons.Default.Person, listOf(Color(0xFFF7971E), Color(0xFFFFD200)), MessageType.TEXT),
        AttachmentItem("استطلاع", Icons.Default.Poll, listOf(Color(0xFF8E2DE2), Color(0xFF4A00E0)), MessageType.TEXT),
        AttachmentItem("صوت", Icons.Default.Audiotrack, listOf(Color(0xFFFF416C), Color(0xFFFF4B2B)), MessageType.VOICE)
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        modifier = Modifier.testTag("attachment_bottom_sheet")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "مشاركة محتوى",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp, start = 8.dp)
            )

            // Grid 3 x 2
            for (row in items.chunked(3)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    for (item in row) {
                        AttachmentButton(item = item) {
                            val textPayload = when (item.type) {
                                MessageType.PHOTO -> "لقطة شاشة للتطبيق الجديد"
                                MessageType.FILE -> "تقرير_المشروع_النهائي.pdf"
                                MessageType.VOICE -> "تسجيل صوتي قصير"
                                else -> "📍 مشاركة موقع جغرافي"
                            }
                            onSelectAttachment(item.type, textPayload)
                            onDismiss()
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun AttachmentButton(
    item: AttachmentItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
            .testTag("attachment_${item.title}")
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(Brush.linearGradient(item.gradient)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = item.title,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
