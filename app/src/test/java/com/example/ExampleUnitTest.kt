package com.example

import com.example.data.TelegramRepository
import com.example.model.ChatFolder
import com.example.model.ChatType
import com.example.model.MessageType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun testRepositoryInitialization() {
        val repo = TelegramRepository()
        val chats = repo.chats.value
        assertTrue("Chats list should not be empty", chats.isNotEmpty())
        
        val savedChat = chats.find { it.chatType == ChatType.SAVED }
        assertNotNull("Saved messages chat should exist", savedChat)
        assertEquals("الرسائل المحفوظة", savedChat?.title)
    }

    @Test
    fun testFilterFolders() {
        val repo = TelegramRepository()
        val channelChats = repo.filterChats(ChatFolder.CHANNELS)
        assertTrue(channelChats.all { it.chatType == ChatType.CHANNEL })

        val botChats = repo.filterChats(ChatFolder.BOTS)
        assertTrue(botChats.all { it.chatType == ChatType.BOT })

        val groupChats = repo.filterChats(ChatFolder.GROUPS)
        assertTrue(groupChats.all { it.chatType == ChatType.GROUP })
    }

    @Test
    fun testSendMessage() {
        val repo = TelegramRepository()
        val initialCount = repo.messages.value["saved_messages"]?.size ?: 0
        repo.sendMessage("saved_messages", "اختبار إرسال رسالة في تلجرام", MessageType.TEXT)
        
        val updatedMessages = repo.messages.value["saved_messages"]
        assertNotNull(updatedMessages)
        assertEquals(initialCount + 1, updatedMessages?.size)
        assertEquals("اختبار إرسال رسالة في تلجرام", updatedMessages?.last()?.text)
    }
}
