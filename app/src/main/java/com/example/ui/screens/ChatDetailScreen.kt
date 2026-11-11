package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.Chat
import com.example.model.ChatType
import com.example.model.Message
import com.example.model.MessageType
import com.example.ui.components.AttachmentSheet
import com.example.ui.components.DateChip
import com.example.ui.components.EmojiPickerSheet
import com.example.ui.components.MessageBubble
import com.example.ui.components.TelegramAvatar
import com.example.ui.theme.LocalTelegramColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatDetailScreen(
    chat: Chat,
    messages: List<Message>,
    isTyping: Boolean,
    onBack: () -> Unit,
    onSendMessage: (String, MessageType, String?) -> Unit,
    onClearHistory: () -> Unit,
    onToggleMute: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)

    val colors = LocalTelegramColors.current
    var inputText by remember { mutableStateOf("") }
    var showMenu by remember { mutableStateOf(false) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    var showEmojiSheet by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Auto scroll to bottom when new messages arrive
    LaunchedEffect(messages.size, isTyping) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colors.chatWallpaperColor)
    ) {
        // Wallpaper Doodle Background
        Image(
            painter = painterResource(id = R.drawable.bg_telegram_doodle),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .alpha(if (colors.isDark) 0.08f else 0.45f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .navigationBarsPadding()
        ) {
            // Telegram TopAppBar for Chat
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { showProfileDialog = true }
                            .padding(vertical = 4.dp)
                            .testTag("chat_title_bar")
                    ) {
                        TelegramAvatar(
                            title = chat.title,
                            chatType = chat.chatType,
                            colorIndex = chat.avatarColorIndex,
                            size = 40.dp,
                            isOnline = chat.isOnline
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = chat.title,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = colors.topBarContent,
                                    maxLines = 1
                                )
                                if (chat.isVerified) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.Verified,
                                        contentDescription = "موثق",
                                        tint = Color.White,
                                        modifier = Modifier.size(15.dp)
                                    )
                                }
                            }

                            val subtitleText = when {
                                isTyping -> "يكتب الآن…"
                                chat.chatType == ChatType.SAVED -> "سحابة التخزين"
                                chat.chatType == ChatType.BOT -> "بوت"
                                chat.isOnline -> "متصل الآن"
                                chat.subtitle.isNotBlank() -> chat.subtitle
                                else -> "آخر ظهور كان قريباً"
                            }

                            Text(
                                text = subtitleText,
                                fontSize = 12.sp,
                                color = if (isTyping) Color(0xFF8CE071) else colors.topBarContent.copy(alpha = 0.8f)
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "رجوع",
                            tint = colors.topBarContent
                        )
                    }
                },
                actions = {
                    if (chat.chatType == ChatType.DIRECT) {
                        IconButton(
                            onClick = {},
                            modifier = Modifier.testTag("call_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = "اتصال",
                                tint = colors.topBarContent
                            )
                        }
                    }

                    IconButton(
                        onClick = { showMenu = !showMenu },
                        modifier = Modifier.testTag("chat_options_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "خيارات",
                            tint = colors.topBarContent
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("معلومات ${chat.title}") },
                            onClick = {
                                showMenu = false
                                showProfileDialog = true
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(if (chat.isMuted) "إلغاء كتم الإشعارات" else "كتم الإشعارات") },
                            onClick = {
                                showMenu = false
                                onToggleMute()
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("مسح السجل") },
                            onClick = {
                                showMenu = false
                                onClearHistory()
                            }
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colors.topBarBackground
                )
            )

            // Messages Container
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .testTag("messages_lazy_column")
            ) {
                item {
                    DateChip(dateText = "اليوم")
                }

                items(messages, key = { it.id }) { msg ->
                    MessageBubble(
                        message = msg,
                        isGroup = chat.chatType == ChatType.GROUP
                    )
                }

                if (isTyping) {
                    item {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = colors.incomingBubble,
                            modifier = Modifier
                                .padding(horizontal = 12.dp, vertical = 4.dp)
                                .testTag("typing_indicator")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "✍️ يكتب الآن…",
                                    fontSize = 13.sp,
                                    color = colors.incomingTime
                                )
                            }
                        }
                    }
                }
            }

            // Message Composer / Bottom Input Bar
            Surface(
                color = MaterialTheme.colorScheme.surface,
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Emoji / Sticker Button
                    IconButton(
                        onClick = { showEmojiSheet = true },
                        modifier = Modifier.testTag("emoji_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SentimentSatisfied,
                            contentDescription = "رموز وملصقات",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Text Field
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                text = "الرسالة…",
                                fontSize = 15.sp,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.45f)
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("message_input_field"),
                        maxLines = 4
                    )

                    // Attachment Paperclip Button
                    IconButton(
                        onClick = { showAttachmentSheet = true },
                        modifier = Modifier.testTag("attach_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "إرفاق",
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }

                    // Send or Mic Button (Telegram Behavior)
                    if (inputText.isNotBlank()) {
                        IconButton(
                            onClick = {
                                onSendMessage(inputText.trim(), MessageType.TEXT, null)
                                inputText = ""
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(colors.fabColor)
                                .testTag("send_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "إرسال",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    } else {
                        // Simulated Hold/Tap to send Voice Note
                        IconButton(
                            onClick = {
                                onSendMessage("تسجيل صوتي", MessageType.VOICE, "0:18")
                            },
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(colors.fabColor)
                                .testTag("mic_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Mic,
                                contentDescription = "تسجيل رسالة صوتية",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        }

        // Attachments Bottom Sheet
        if (showAttachmentSheet) {
            AttachmentSheet(
                onDismiss = { showAttachmentSheet = false },
                onSelectAttachment = { type, text ->
                    onSendMessage(text, type, if (type == MessageType.VOICE) "0:25" else null)
                }
            )
        }

        // Emoji & Stickers Picker Sheet
        if (showEmojiSheet) {
            EmojiPickerSheet(
                onDismiss = { showEmojiSheet = false },
                onEmojiSelected = { emoji ->
                    inputText += emoji
                },
                onStickerSelected = { sticker ->
                    onSendMessage(sticker, MessageType.STICKER, null)
                }
            )
        }

        // Contact Profile Details Dialog
        if (showProfileDialog) {
            AlertDialog(
                onDismissRequest = { showProfileDialog = false },
                title = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        TelegramAvatar(
                            title = chat.title,
                            chatType = chat.chatType,
                            colorIndex = chat.avatarColorIndex,
                            size = 72.dp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = chat.title,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        if (!chat.username.isNullOrBlank()) {
                            Text(
                                text = "@${chat.username}",
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                text = {
                    Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                        Text(
                            text = "النبذة التعريفية:",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Text(
                            text = if (chat.bio.isNotBlank()) chat.bio else chat.subtitle,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(top = 2.dp, bottom = 8.dp)
                        )
                        if (chat.isOnline) {
                            Text(
                                text = "الحالة: متصل الآن 🟢",
                                fontSize = 13.sp,
                                color = Color(0xFF4EAE4E)
                            )
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showProfileDialog = false }) {
                        Text("إغلاق")
                    }
                }
            )
        }
    }
}
