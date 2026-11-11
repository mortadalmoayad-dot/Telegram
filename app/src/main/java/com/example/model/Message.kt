package com.example.model

enum class MessageType {
    TEXT,
    VOICE,
    PHOTO,
    FILE,
    STICKER
}

enum class MessageStatus {
    SENDING,
    SENT,
    READ
}

data class Message(
    val id: String,
    val chatId: String,
    val senderName: String? = null,
    val text: String,
    val timestamp: String,
    val isOutgoing: Boolean,
    val status: MessageStatus = MessageStatus.READ,
    val type: MessageType = MessageType.TEXT,
    val voiceDuration: String? = null,
    val voiceProgress: Float = 0f,
    val replyToText: String? = null
)
