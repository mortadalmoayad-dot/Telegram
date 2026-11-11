package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Chat
import com.example.model.ChatFolder
import com.example.ui.components.TelegramAvatar
import com.example.ui.theme.LocalTelegramColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatListScreen(
    chats: List<Chat>,
    selectedFolder: ChatFolder,
    onSelectFolder: (ChatFolder) -> Unit,
    onChatClick: (Chat) -> Unit,
    onMenuClick: () -> Unit,
    onSearchClick: () -> Unit,
    onNewChatClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalTelegramColors.current
    var showMenu by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Telegram TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Telegram",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = colors.topBarContent
                )
            },
            navigationIcon = {
                IconButton(
                    onClick = onMenuClick,
                    modifier = Modifier.testTag("menu_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "القائمة الرئيسية",
                        tint = colors.topBarContent
                    )
                }
            },
            actions = {
                IconButton(
                    onClick = onSearchClick,
                    modifier = Modifier.testTag("search_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "بحث",
                        tint = colors.topBarContent
                    )
                }

                IconButton(
                    onClick = { showMenu = !showMenu },
                    modifier = Modifier.testTag("more_options_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "المزيد",
                        tint = colors.topBarContent
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("إنشاء قناة") },
                        onClick = {
                            showMenu = false
                            onNewChatClick()
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("قراءة الكل") },
                        onClick = { showMenu = false }
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colors.topBarBackground
            )
        )

        // Telegram Folder Tabs (الكل، خاص، مجموعات، قنوات، بوتات)
        val folders = ChatFolder.values()
        ScrollableTabRow(
            selectedTabIndex = selectedFolder.ordinal,
            containerColor = colors.topBarBackground,
            contentColor = colors.topBarContent,
            edgePadding = 12.dp,
            indicator = { tabPositions ->
                TabRowDefaults.SecondaryIndicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedFolder.ordinal]),
                    height = 3.dp,
                    color = Color.White
                )
            },
            divider = {}
        ) {
            folders.forEach { folder ->
                val isSelected = selectedFolder == folder
                Tab(
                    selected = isSelected,
                    onClick = { onSelectFolder(folder) },
                    modifier = Modifier.testTag("tab_${folder.name}"),
                    text = {
                        Text(
                            text = folder.titleAr,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
                        )
                    }
                )
            }
        }

        // Chat List
        Box(modifier = Modifier.fillMaxSize()) {
            if (chats.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "لا توجد محادثات في هذا المجلد",
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                        fontSize = 15.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .testTag("chats_lazy_column")
                ) {
                    items(chats, key = { it.id }) { chat ->
                        ChatItemRow(
                            chat = chat,
                            onClick = { onChatClick(chat) }
                        )
                        HorizontalDivider(
                            modifier = Modifier.padding(start = 72.dp),
                            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                            thickness = 0.5.dp
                        )
                    }
                }
            }

            // Telegram Signature Floating Action Button
            FloatingActionButton(
                onClick = onNewChatClick,
                containerColor = colors.fabColor,
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
                    .testTag("new_chat_fab")
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "محادثة جديدة",
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun ChatItemRow(
    chat: Chat,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colors = LocalTelegramColors.current

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .testTag("chat_item_${chat.id}"),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        TelegramAvatar(
            title = chat.title,
            chatType = chat.chatType,
            colorIndex = chat.avatarColorIndex,
            size = 52.dp,
            isOnline = chat.isOnline
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Center Content (Title & Last Message)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = chat.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f, fill = false)
                )

                if (chat.isVerified) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Verified,
                        contentDescription = "موثق",
                        tint = Color(0xFF2481CC),
                        modifier = Modifier.size(16.dp)
                    )
                }

                if (chat.isMuted) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.VolumeOff,
                        contentDescription = "كتم",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                        modifier = Modifier.size(15.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Time
                Text(
                    text = chat.time,
                    fontSize = 12.sp,
                    color = if (chat.unreadCount > 0 && !chat.isMuted) colors.unreadBadge else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                    fontWeight = if (chat.unreadCount > 0) FontWeight.Bold else FontWeight.Normal
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = chat.lastMessage,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                // Pin icon
                if (chat.isPinned) {
                    Icon(
                        imageVector = Icons.Default.PushPin,
                        contentDescription = "مثبت",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // Unread Count Badge
                if (chat.unreadCount > 0) {
                    Surface(
                        color = if (chat.isMuted) colors.mutedBadge else colors.unreadBadge,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("unread_badge_${chat.id}")
                    ) {
                        Text(
                            text = if (chat.unreadCount > 99) "99+" else chat.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}
