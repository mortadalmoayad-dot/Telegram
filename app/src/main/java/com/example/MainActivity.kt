package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.ChatDetailScreen
import com.example.ui.screens.ChatListScreen
import com.example.ui.screens.ContactsScreen
import com.example.ui.screens.NewChatDialog
import com.example.ui.screens.SearchScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.TelegramDrawer
import com.example.ui.theme.TelegramTheme
import com.example.viewmodel.TelegramScreen
import com.example.viewmodel.TelegramViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: TelegramViewModel = viewModel()
            val isDarkTheme by viewModel.isDarkTheme.collectAsState()

            TelegramTheme(darkTheme = isDarkTheme) {
                TelegramApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun TelegramApp(
    viewModel: TelegramViewModel
) {
    val currentScreen by viewModel.currentScreen.collectAsState()
    val filteredChats by viewModel.filteredChats.collectAsState()
    val allChats by viewModel.allChats.collectAsState()
    val allMessages by viewModel.allMessages.collectAsState()
    val contacts by viewModel.contacts.collectAsState()
    val typingStatus by viewModel.typingStatus.collectAsState()
    val selectedFolder by viewModel.selectedFolder.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var showNewChatDialog by remember { mutableStateOf(false) }

    // Handle back button for drawer or screens
    BackHandler(enabled = drawerState.isOpen || currentScreen !is TelegramScreen.ChatList) {
        if (drawerState.isOpen) {
            coroutineScope.launch { drawerState.close() }
        } else {
            viewModel.navigateBack()
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = currentScreen is TelegramScreen.ChatList,
        drawerContent = {
            TelegramDrawer(
                isDarkTheme = isDarkTheme,
                onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                onNewGroupClick = {
                    coroutineScope.launch { drawerState.close() }
                    showNewChatDialog = true
                },
                onContactsClick = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.navigateTo(TelegramScreen.Contacts)
                },
                onCallsClick = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.navigateTo(TelegramScreen.Contacts)
                },
                onSavedMessagesClick = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.openChat("saved_messages")
                },
                onSettingsClick = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.navigateTo(TelegramScreen.Settings)
                },
                onTelegramFeaturesClick = {
                    coroutineScope.launch { drawerState.close() }
                    viewModel.openChat("telegram_news")
                }
            )
        }
    ) {
        Surface(modifier = Modifier.fillMaxSize()) {
            when (val screen = currentScreen) {
                is TelegramScreen.ChatList -> {
                    ChatListScreen(
                        chats = filteredChats,
                        selectedFolder = selectedFolder,
                        onSelectFolder = { viewModel.selectFolder(it) },
                        onChatClick = { chat -> viewModel.openChat(chat.id) },
                        onMenuClick = {
                            coroutineScope.launch { drawerState.open() }
                        },
                        onSearchClick = {
                            viewModel.navigateTo(TelegramScreen.Search)
                        },
                        onNewChatClick = {
                            showNewChatDialog = true
                        }
                    )
                }

                is TelegramScreen.ChatDetail -> {
                    val chat = allChats.find { it.id == screen.chatId }
                    val messages = allMessages[screen.chatId] ?: emptyList()
                    val isTyping = typingStatus[screen.chatId] ?: false

                    if (chat != null) {
                        ChatDetailScreen(
                            chat = chat,
                            messages = messages,
                            isTyping = isTyping,
                            onBack = { viewModel.navigateBack() },
                            onSendMessage = { text, type, duration ->
                                viewModel.sendMessage(chat.id, text, type, duration)
                            },
                            onClearHistory = { viewModel.clearChatHistory(chat.id) },
                            onToggleMute = { viewModel.toggleMute(chat.id) }
                        )
                    } else {
                        viewModel.navigateBack()
                    }
                }

                is TelegramScreen.Search -> {
                    SearchScreen(
                        chats = allChats,
                        onBack = { viewModel.navigateBack() },
                        onChatClick = { chat -> viewModel.openChat(chat.id) }
                    )
                }

                is TelegramScreen.Contacts -> {
                    ContactsScreen(
                        contacts = contacts,
                        onBack = { viewModel.navigateBack() },
                        onContactClick = { contact ->
                            viewModel.startChatWithContact(contact)
                        }
                    )
                }

                is TelegramScreen.Settings -> {
                    SettingsScreen(
                        isDarkTheme = isDarkTheme,
                        onToggleDarkTheme = { viewModel.toggleDarkTheme() },
                        onBack = { viewModel.navigateBack() }
                    )
                }
            }
        }
    }

    if (showNewChatDialog) {
        NewChatDialog(
            onDismiss = { showNewChatDialog = false },
            onCreateChat = { name, isGroup ->
                viewModel.createChat(name, isGroup)
            }
        )
    }
}
