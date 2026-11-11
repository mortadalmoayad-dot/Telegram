package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.TelegramRepository
import com.example.model.Chat
import com.example.model.ChatFolder
import com.example.model.Contact
import com.example.model.Message
import com.example.model.MessageType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

sealed interface TelegramScreen {
    object ChatList : TelegramScreen
    data class ChatDetail(val chatId: String) : TelegramScreen
    object Search : TelegramScreen
    object Contacts : TelegramScreen
    object Settings : TelegramScreen
}

class TelegramViewModel(
    private val repository: TelegramRepository = TelegramRepository()
) : ViewModel() {

    private val _currentScreen = MutableStateFlow<TelegramScreen>(TelegramScreen.ChatList)
    val currentScreen: StateFlow<TelegramScreen> = _currentScreen.asStateFlow()

    private val _selectedFolder = MutableStateFlow(ChatFolder.ALL)
    val selectedFolder: StateFlow<ChatFolder> = _selectedFolder.asStateFlow()

    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    val contacts: StateFlow<List<Contact>> = repository.contacts
    val typingStatus: StateFlow<Map<String, Boolean>> = repository.typingStatus

    // Filtered chats based on active folder
    val filteredChats: StateFlow<List<Chat>> = combine(
        repository.chats,
        _selectedFolder
    ) { chatsList, folder ->
        when (folder) {
            ChatFolder.ALL -> chatsList
            ChatFolder.PERSONAL -> chatsList.filter { it.chatType == com.example.model.ChatType.DIRECT || it.chatType == com.example.model.ChatType.SAVED }
            ChatFolder.GROUPS -> chatsList.filter { it.chatType == com.example.model.ChatType.GROUP }
            ChatFolder.CHANNELS -> chatsList.filter { it.chatType == com.example.model.ChatType.CHANNEL }
            ChatFolder.BOTS -> chatsList.filter { it.chatType == com.example.model.ChatType.BOT }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allChats: StateFlow<List<Chat>> = repository.chats
    val allMessages: StateFlow<Map<String, List<Message>>> = repository.messages

    fun selectFolder(folder: ChatFolder) {
        _selectedFolder.value = folder
    }

    fun toggleDarkTheme() {
        _isDarkTheme.value = !_isDarkTheme.value
    }

    fun openChat(chatId: String) {
        _currentScreen.value = TelegramScreen.ChatDetail(chatId)
    }

    fun navigateTo(screen: TelegramScreen) {
        _currentScreen.value = screen
    }

    fun navigateBack(): Boolean {
        return if (_currentScreen.value !is TelegramScreen.ChatList) {
            _currentScreen.value = TelegramScreen.ChatList
            true
        } else {
            false
        }
    }

    fun sendMessage(chatId: String, text: String, type: MessageType = MessageType.TEXT, duration: String? = null) {
        repository.sendMessage(chatId, text, type, duration)
    }

    fun createChat(name: String, isGroup: Boolean) {
        if (isGroup) {
            val id = repository.createNewGroup(name, listOf("سارة أحمد", "محمد علي"))
            openChat(id)
        } else {
            val dummyContact = Contact(
                id = "custom_${System.currentTimeMillis()}",
                name = name,
                phone = "+964 770 000 0000",
                status = "آخر ظهور كان قريباً",
                isOnline = true,
                avatarColorIndex = (name.length % 8)
            )
            val id = repository.startChatWithContact(dummyContact)
            openChat(id)
        }
    }

    fun startChatWithContact(contact: Contact) {
        val id = repository.startChatWithContact(contact)
        openChat(id)
    }

    fun clearChatHistory(chatId: String) {
        repository.clearChatHistory(chatId)
    }

    fun toggleMute(chatId: String) {
        repository.toggleMute(chatId)
    }
}
