package com.example.model

enum class ChatType {
    DIRECT,
    GROUP,
    CHANNEL,
    BOT,
    SAVED
}

enum class ChatFolder(val titleAr: String) {
    ALL("الكل"),
    PERSONAL("خاص"),
    GROUPS("مجموعات"),
    CHANNELS("قنوات"),
    BOTS("بوتات")
}

data class Chat(
    val id: String,
    val title: String,
    val lastMessage: String,
    val time: String,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isMuted: Boolean = false,
    val isOnline: Boolean = false,
    val isVerified: Boolean = false,
    val chatType: ChatType = ChatType.DIRECT,
    val username: String? = null,
    val avatarColorIndex: Int = 0,
    val subtitle: String = "",
    val bio: String = ""
)

data class Contact(
    val id: String,
    val name: String,
    val phone: String,
    val status: String,
    val isOnline: Boolean,
    val avatarColorIndex: Int
)
