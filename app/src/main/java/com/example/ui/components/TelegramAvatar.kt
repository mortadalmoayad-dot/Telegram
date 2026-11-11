package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatType

// Telegram iconic avatar gradient pairs
val AvatarGradients = listOf(
    listOf(Color(0xFF2AABEE), Color(0xFF229ED9)), // Blue
    listOf(Color(0xFFFF885E), Color(0xFFFF516A)), // Coral/Red
    listOf(Color(0xFFA667FF), Color(0xFF7E3BFF)), // Violet
    listOf(Color(0xFF54CA68), Color(0xFF2EA846)), // Green
    listOf(Color(0xFF24C3D2), Color(0xFF1DA4B3)), // Cyan
    listOf(Color(0xFFFFA834), Color(0xFFFF8800)), // Amber
    listOf(Color(0xFFFF6B8B), Color(0xFFFF4365)), // Pink
    listOf(Color(0xFF5C7C99), Color(0xFF415B73))  // Slate
)

@Composable
fun TelegramAvatar(
    title: String,
    chatType: ChatType,
    colorIndex: Int = 0,
    size: Dp = 52.dp,
    isOnline: Boolean = false,
    modifier: Modifier = Modifier
) {
    val gradientColors = AvatarGradients[colorIndex.coerceIn(0, AvatarGradients.size - 1)]

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(size)
                .clip(CircleShape)
                .background(Brush.verticalGradient(gradientColors)),
            contentAlignment = Alignment.Center
        ) {
            when (chatType) {
                ChatType.SAVED -> {
                    Icon(
                        imageVector = Icons.Default.Bookmark,
                        contentDescription = "الرسائل المحفوظة",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.52f)
                    )
                }
                ChatType.BOT -> {
                    Icon(
                        imageVector = Icons.Default.SmartToy,
                        contentDescription = "بوت",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.52f)
                    )
                }
                ChatType.CHANNEL -> {
                    Icon(
                        imageVector = Icons.Default.Campaign,
                        contentDescription = "قناة",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.52f)
                    )
                }
                ChatType.GROUP -> {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "مجموعة",
                        tint = Color.White,
                        modifier = Modifier.size(size * 0.52f)
                    )
                }
                ChatType.DIRECT -> {
                    val initial = title.trim().firstOrNull()?.toString()?.uppercase() ?: "T"
                    Text(
                        text = initial,
                        color = Color.White,
                        fontSize = (size.value * 0.42f).sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Online status dot
        if (isOnline && chatType == ChatType.DIRECT) {
            Box(
                modifier = Modifier
                    .size(size * 0.28f)
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
                    .clip(CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .background(Color(0xFF4EAE4E))
            )
        }
    }
}
