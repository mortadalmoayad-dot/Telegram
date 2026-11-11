package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.GroupAdd
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.model.ChatType
import com.example.ui.components.TelegramAvatar
import com.example.ui.theme.LocalTelegramColors

@Composable
fun TelegramDrawer(
    isDarkTheme: Boolean,
    onToggleDarkTheme: () -> Unit,
    onNewGroupClick: () -> Unit,
    onContactsClick: () -> Unit,
    onCallsClick: () -> Unit,
    onSavedMessagesClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onTelegramFeaturesClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val colors = LocalTelegramColors.current

    Column(
        modifier = modifier
            .fillMaxHeight()
            .width(300.dp)
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .testTag("telegram_drawer")
    ) {
        // Drawer Header with Telegram Blue Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (colors.isDark) {
                        Brush.verticalGradient(listOf(Color(0xFF1E2C3A), Color(0xFF17212B)))
                    } else {
                        Brush.verticalGradient(listOf(Color(0xFF5288C1), Color(0xFF2481CC)))
                    }
                )
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Profile Avatar
                    TelegramAvatar(
                        title = "مرتضى المؤيد",
                        chatType = ChatType.DIRECT,
                        colorIndex = 0,
                        size = 64.dp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    // Dark / Light toggle button
                    IconButton(
                        onClick = onToggleDarkTheme,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "تبديل المظهر",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "مرتضى المؤيد",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "+964 770 123 4567",
                            color = Color.White.copy(alpha = 0.75f),
                            fontSize = 13.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "الحسابات",
                        tint = Color.White.copy(alpha = 0.75f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Menu Items
        DrawerMenuItem(
            icon = Icons.Default.GroupAdd,
            title = "مجموعة جديدة",
            onClick = onNewGroupClick,
            testTag = "drawer_item_new_group"
        )

        DrawerMenuItem(
            icon = Icons.Default.Person,
            title = "جهات الاتصال",
            onClick = onContactsClick,
            testTag = "drawer_item_contacts"
        )

        DrawerMenuItem(
            icon = Icons.Default.Call,
            title = "المكالمات",
            onClick = onCallsClick,
            testTag = "drawer_item_calls"
        )

        DrawerMenuItem(
            icon = Icons.Default.LocationOn,
            title = "الأشخاص القريبون",
            onClick = onTelegramFeaturesClick,
            testTag = "drawer_item_nearby"
        )

        DrawerMenuItem(
            icon = Icons.Default.Bookmark,
            title = "الرسائل المحفوظة",
            onClick = onSavedMessagesClick,
            testTag = "drawer_item_saved_messages"
        )

        DrawerMenuItem(
            icon = Icons.Default.Settings,
            title = "الإعدادات",
            onClick = onSettingsClick,
            testTag = "drawer_item_settings"
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.25f)
        )

        DrawerMenuItem(
            icon = Icons.Default.PersonAdd,
            title = "دعوة الأصدقاء",
            onClick = onTelegramFeaturesClick,
            testTag = "drawer_item_invite"
        )

        DrawerMenuItem(
            icon = Icons.Default.HelpOutline,
            title = "ميزات تلغرام",
            onClick = onTelegramFeaturesClick,
            testTag = "drawer_item_features"
        )

        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Telegram for Android v10.9.1",
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            fontSize = 11.sp,
            modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 16.dp)
        )
    }
}

@Composable
private fun DrawerMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 12.dp)
            .testTag(testTag),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = title,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
