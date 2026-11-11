package com.example.data

import com.example.model.Chat
import com.example.model.ChatFolder
import com.example.model.ChatType
import com.example.model.Contact
import com.example.model.Message
import com.example.model.MessageStatus
import com.example.model.MessageType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class TelegramRepository(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Main)
) {
    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<Message>>>(emptyMap())
    val messages: StateFlow<Map<String, List<Message>>> = _messages.asStateFlow()

    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts: StateFlow<List<Contact>> = _contacts.asStateFlow()

    private val _typingStatus = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val typingStatus: StateFlow<Map<String, Boolean>> = _typingStatus.asStateFlow()

    init {
        initInitialData()
    }

    private fun initInitialData() {
        val initialChats = listOf(
            Chat(
                id = "saved_messages",
                title = "الرسائل المحفوظة",
                lastMessage = "ملاحظات هامة لتحديث التطبيق ومميزات تلجرام الجديدة 📌",
                time = "10:45 ص",
                unreadCount = 0,
                isPinned = true,
                isMuted = false,
                isOnline = false,
                isVerified = false,
                chatType = ChatType.SAVED,
                username = null,
                avatarColorIndex = 0,
                subtitle = "سحابتك التخزينية الخاصة",
                bio = "سحابة التخزين الشخصية لحفظ الرسائل والوسائط والملاحظات"
            ),
            Chat(
                id = "telegram_news",
                title = "Telegram News",
                lastMessage = "🎉 تحديث تلغرام الكبير: ميزات الذكاء الاصطناعي ومجلدات المحادثات المتطورة!",
                time = "10:12 ص",
                unreadCount = 3,
                isPinned = true,
                isMuted = false,
                isOnline = false,
                isVerified = true,
                chatType = ChatType.CHANNEL,
                username = "telegram",
                avatarColorIndex = 1,
                subtitle = "8,450,120 مشتركاً",
                bio = "القناة الرسمية لأخبار وتحديثات تطبيق تلغرام حول العالم."
            ),
            Chat(
                id = "sarah_ahmed",
                title = "سارة أحمد",
                lastMessage = "ممتاز جداً! التصميم مطابق تماماً لتطبيق تلجرام الأصلي 🚀",
                time = "09:58 ص",
                unreadCount = 2,
                isPinned = false,
                isMuted = false,
                isOnline = true,
                isVerified = false,
                chatType = ChatType.DIRECT,
                username = "sarah_a",
                avatarColorIndex = 2,
                subtitle = "متصل الآن",
                bio = "مصممة واجهات ومطورة برمجيات | عاشقة للتقنية ✨"
            ),
            Chat(
                id = "ai_bot",
                title = "مساعد تلغرام الذكي",
                lastMessage = "أهلاً بك! أرسل /help لرؤية الأوامر المتاحة أو اسألني أي سؤال 🤖",
                time = "أمس",
                unreadCount = 1,
                isPinned = false,
                isMuted = false,
                isOnline = true,
                isVerified = true,
                chatType = ChatType.BOT,
                username = "AiHelperBot",
                avatarColorIndex = 3,
                subtitle = "بوت",
                bio = "بوت ذكي مبرمج لمساعدتك في الترجمة، التلخيص، والإجابة عن الأسئلة."
            ),
            Chat(
                id = "android_devs",
                title = "مجموعة مطوري الأندرويد",
                lastMessage = "محمد: تم إصدار Jetpack Compose 1.8 مع تحسينات هائلة في الأداء!",
                time = "أمس",
                unreadCount = 14,
                isPinned = false,
                isMuted = true,
                isOnline = false,
                isVerified = false,
                chatType = ChatType.GROUP,
                username = "android_dev_group",
                avatarColorIndex = 4,
                subtitle = "1,420 عضواً، 85 متصل",
                bio = "مجتمع المطورين لمناقشة أحدث تقنيات أندرويد وKotlin وJetpack Compose."
            ),
            Chat(
                id = "tech_today",
                title = "قناة التقنية اليوم",
                lastMessage = "ملخص أهم أخبار التقنية والذكاء الاصطناعي لهذا الأسبوع في ملف PDF 📱",
                time = "الإثنين",
                unreadCount = 0,
                isPinned = false,
                isMuted = false,
                isOnline = false,
                isVerified = false,
                chatType = ChatType.CHANNEL,
                username = "techtoday_ar",
                avatarColorIndex = 5,
                subtitle = "95,400 مشتركاً",
                bio = "كل ما هو جديد في عالم الهواتف الذكية والأجهزة والذكاء الاصطناعي."
            ),
            Chat(
                id = "mohammed_ali",
                title = "محمد علي",
                lastMessage = "رسالة صوتية (0:28)",
                time = "الأحد",
                unreadCount = 0,
                isPinned = false,
                isMuted = false,
                isOnline = false,
                isVerified = false,
                chatType = ChatType.DIRECT,
                username = "m_ali99",
                avatarColorIndex = 6,
                subtitle = "آخر ظهور كان منذ ساعتين",
                bio = "مهندس نظم معلومات | مهتم بأمن المعلومات والشبكات"
            ),
            Chat(
                id = "book_world",
                title = "عالم الكتب والروايات",
                lastMessage = "كتاب الأسبوع: العادات الذرية - ملخص شامل لأهم الأفكار 📚",
                time = "10/11",
                unreadCount = 0,
                isPinned = false,
                isMuted = true,
                isOnline = false,
                isVerified = false,
                chatType = ChatType.CHANNEL,
                username = "books_corner",
                avatarColorIndex = 7,
                subtitle = "42,100 مشتركاً",
                bio = "قناة مخصصة لمشاركة ترشيحات ومراجعات واقتباسات الكتب القيمة."
            )
        )

        _chats.value = initialChats

        val initialMessages = mutableMapOf<String, List<Message>>()

        // Saved Messages
        initialMessages["saved_messages"] = listOf(
            Message(
                id = "sm1",
                chatId = "saved_messages",
                text = "قائمة المهام اليومية:\n1. مراجعة كود تطبيق تلجرام\n2. تجربة الرسائل الصوتية والملصقات\n3. التحقق من السمة الليلية والنهارية",
                timestamp = "09:30 ص",
                isOutgoing = true,
                status = MessageStatus.READ
            ),
            Message(
                id = "sm2",
                chatId = "saved_messages",
                text = "رابط المستودع: https://github.com/telegram/android-compose",
                timestamp = "10:15 ص",
                isOutgoing = true,
                status = MessageStatus.READ
            ),
            Message(
                id = "sm3",
                chatId = "saved_messages",
                text = "ملاحظات هامة لتحديث التطبيق ومميزات تلجرام الجديدة 📌",
                timestamp = "10:45 ص",
                isOutgoing = true,
                status = MessageStatus.READ
            )
        )

        // Sarah
        initialMessages["sarah_ahmed"] = listOf(
            Message(
                id = "s1",
                chatId = "sarah_ahmed",
                senderName = "سارة",
                text = "السلام عليكم، كيف حال مشروع تطبيق تلجرام الجديد؟",
                timestamp = "09:40 ص",
                isOutgoing = false
            ),
            Message(
                id = "s2",
                chatId = "sarah_ahmed",
                text = "وعليكم السلام! يسير بشكل ممتاز جداً، التصميم طبق الأصل مع كل التفاصيل والألوان والسمات",
                timestamp = "09:45 ص",
                isOutgoing = true,
                status = MessageStatus.READ
            ),
            Message(
                id = "s3",
                chatId = "sarah_ahmed",
                senderName = "سارة",
                text = "تسجيل صوتي قصير لشرح فكرة المجلدات والقنوات",
                timestamp = "09:50 ص",
                isOutgoing = false,
                type = MessageType.VOICE,
                voiceDuration = "0:35"
            ),
            Message(
                id = "s4",
                chatId = "sarah_ahmed",
                senderName = "سارة",
                text = "ممتاز جداً! التصميم مطابق تماماً لتطبيق تلجرام الأصلي 🚀",
                timestamp = "09:58 ص",
                isOutgoing = false
            )
        )

        // AI Bot
        initialMessages["ai_bot"] = listOf(
            Message(
                id = "b1",
                chatId = "ai_bot",
                senderName = "مساعد تلغرام الذكي",
                text = "👋 مرحباً بك في بوت تلغرام الذكي!\n\nأنا هنا لمساعدتك في أي استفسار. إليك بعض الأوامر:\n/start - بدء المحادثة\n/help - قائمة المساعدة\n/features - مميزات تطبيق تلجرام\n/clear - مسح المحادثة\n\nأو يمكنك كتابة أي رسالة وسأجيبك فوراً!",
                timestamp = "أمس 04:30 م",
                isOutgoing = false
            ),
            Message(
                id = "b2",
                chatId = "ai_bot",
                text = "أهلاً بك! أرسل /help لرؤية الأوامر المتاحة أو اسألني أي سؤال 🤖",
                timestamp = "أمس 04:31 م",
                isOutgoing = false
            )
        )

        // Android Devs Group
        initialMessages["android_devs"] = listOf(
            Message(
                id = "ad1",
                chatId = "android_devs",
                senderName = "أحمد خالد",
                text = "صباح الخير جميعاً، هل قام أحدكم بتجربة التحديث الأخير لـ Material 3 في أندرويد؟",
                timestamp = "أمس 08:15 ص",
                isOutgoing = false
            ),
            Message(
                id = "ad2",
                chatId = "android_devs",
                senderName = "ليلى حسن",
                text = "نعم، التوافق مع Edge-to-Edge وإيماءات التنبؤ بالرجوع أصبحت سلسة جداً!",
                timestamp = "أمس 08:30 ص",
                isOutgoing = false
            ),
            Message(
                id = "ad3",
                chatId = "android_devs",
                text = "لقد قمنا بتطبيقها بالكامل في تصميم تلجرام هنا مع شريط الحالة الشفاف والقوائم الجانبية",
                timestamp = "أمس 09:00 ص",
                isOutgoing = true,
                status = MessageStatus.READ
            ),
            Message(
                id = "ad4",
                chatId = "android_devs",
                senderName = "محمد",
                text = "محمد: تم إصدار Jetpack Compose 1.8 مع تحسينات هائلة في الأداء!",
                timestamp = "أمس 09:10 ص",
                isOutgoing = false
            )
        )

        // Telegram News
        initialMessages["telegram_news"] = listOf(
            Message(
                id = "tn1",
                chatId = "telegram_news",
                senderName = "Telegram",
                text = "🌟 ميزات تلغرام الجديدة:\n\n• مجلدات المحادثات المخصصة\n• خلفيات محادثة متحركة ورسومات Doodle\n• ردود الفعل السريعة بالإيموجي والملصقات\n• الرسائل المحفوظة المصنفة بالوسوم\n• استهلاك منخفض جداً للبطارية والبيانات",
                timestamp = "10:00 ص",
                isOutgoing = false
            ),
            Message(
                id = "tn2",
                chatId = "telegram_news",
                senderName = "Telegram",
                text = "🎉 تحديث تلغرام الكبير: ميزات الذكاء الاصطناعي ومجلدات المحادثات المتطورة!",
                timestamp = "10:12 ص",
                isOutgoing = false
            )
        )

        // Mohammed
        initialMessages["mohammed_ali"] = listOf(
            Message(
                id = "ma1",
                chatId = "mohammed_ali",
                senderName = "محمد علي",
                text = "أهلاً مرتضى! هل استلمت الملفات التي أرسلتها عبر التلجرام أمس؟",
                timestamp = "الأحد 11:20 ص",
                isOutgoing = false
            ),
            Message(
                id = "ma2",
                chatId = "mohammed_ali",
                text = "نعم اطلعت عليها، شغل رائع ومتقن!",
                timestamp = "الأحد 11:25 ص",
                isOutgoing = true,
                status = MessageStatus.READ
            ),
            Message(
                id = "ma3",
                chatId = "mohammed_ali",
                senderName = "محمد علي",
                text = "رسالة صوتية (0:28)",
                timestamp = "الأحد 11:30 ص",
                isOutgoing = false,
                type = MessageType.VOICE,
                voiceDuration = "0:28"
            )
        )

        _messages.value = initialMessages

        // Contacts list
        _contacts.value = listOf(
            Contact("c1", "سارة أحمد", "+964 771 234 5678", "متصل الآن", true, 2),
            Contact("c2", "محمد علي", "+964 772 345 6789", "آخر ظهور منذ ساعتين", false, 6),
            Contact("c3", "أحمد خالد", "+964 773 456 7890", "متصل الآن", true, 4),
            Contact("c4", "ليلى حسن", "+964 774 567 8901", "آخر ظهور كان قريباً", false, 1),
            Contact("c5", "عمر الفاروق", "+964 775 678 9012", "آخر ظهور اليوم 03:15 م", false, 5),
            Contact("c6", "فاطمة الزهراء", "+964 776 789 0123", "متصل الآن", true, 3),
            Contact("c7", "زيد الرافدين", "+964 777 890 1234", "آخر ظهور أمس", false, 7),
            Contact("c8", "نور الهدى", "+964 778 901 2345", "آخر ظهور كان قريباً", false, 0)
        )
    }

    fun sendMessage(chatId: String, text: String, type: MessageType = MessageType.TEXT, voiceDuration: String? = null) {
        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            .replace("AM", "ص").replace("PM", "م")

        val newMsg = Message(
            id = UUID.randomUUID().toString(),
            chatId = chatId,
            text = text,
            timestamp = now,
            isOutgoing = true,
            status = MessageStatus.READ,
            type = type,
            voiceDuration = voiceDuration
        )

        val currentList = _messages.value[chatId] ?: emptyList()
        _messages.value = _messages.value + (chatId to (currentList + newMsg))

        // Update chat's last message and time
        val updatedChats = _chats.value.map { chat ->
            if (chat.id == chatId) {
                chat.copy(
                    lastMessage = if (type == MessageType.VOICE) "رسالة صوتية ($voiceDuration)" else text,
                    time = now,
                    unreadCount = 0
                )
            } else {
                chat
            }
        }
        // Move active chat near top (preserving pinned chats on top)
        val targetChat = updatedChats.find { it.id == chatId }
        if (targetChat != null && !targetChat.isPinned) {
            val pinned = updatedChats.filter { it.isPinned }
            val unpinned = updatedChats.filter { !it.isPinned && it.id != chatId }
            _chats.value = pinned + listOf(targetChat) + unpinned
        } else {
            _chats.value = updatedChats
        }

        // Handle automated smart replies
        handleAutomatedReply(chatId, text)
    }

    private fun handleAutomatedReply(chatId: String, userText: String) {
        val now = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
            .replace("AM", "ص").replace("PM", "م")

        when (chatId) {
            "ai_bot" -> {
                scope.launch {
                    _typingStatus.value = _typingStatus.value + (chatId to true)
                    delay(1000)
                    _typingStatus.value = _typingStatus.value + (chatId to false)

                    val reply = when {
                        userText.trim().equals("/start", ignoreCase = true) ->
                            "✨ أهلاً بك مجدداً في بوت تلغرام!\nأنا جاهز لمساعدتك في أي سؤال، أو تلخيص نصوص، أو إدارة المهام. ما الذي تحتاجه اليوم؟"
                        userText.trim().equals("/help", ignoreCase = true) ->
                            "📖 قائمة الأوامر المتاحة:\n• /start - إعادة تشغيل البوت\n• /features - استعراض مميزات تلغرام\n• /quote - حكمة اليوم\n• اكتب أي نص للتحدث معي مباشرة!"
                        userText.trim().equals("/features", ignoreCase = true) ->
                            "🚀 مميزات تلغرام الرائعة:\n1. مزامنة سحابية فائقة السرعة\n2. محادثات مشفرة وسرية\n3. مجموعات تتسع لـ 200,000 عضو\n4. قنوات غير محدودة المشتركين\n5. بوتات ذكية لأتمتة كل شيء"
                        userText.trim().equals("/quote", ignoreCase = true) ->
                            "💡 حكمة اليوم:\n'النجاح لا يأتي بالصدفة، بل هو نتاج العمل المستمر والشغف بما تفعل.'"
                        userText.contains("مرحبا", ignoreCase = true) || userText.contains("السلام", ignoreCase = true) ->
                            "أهلاً وسهلاً بك! يسعدني التواصل معك في تلغرام 🌟 كيف يمكنني خدمتك؟"
                        userText.contains("تصميم", ignoreCase = true) || userText.contains("شكل", ignoreCase = true) ->
                            "التصميم الحالي لتطبيق تلجرام يتميز بالألوان الأيقونية، وفقاعات المحادثة الخضراء، وشريط الإجراءات الأزرق، وخلفية Doodle الأصلية!"
                        else ->
                            "شكراً لرسالتك: \"$userText\"\nأنا مساعدك الذكي في تلغرام، تم تسجيل طلبك وتنفيذه بنجاح! ⚡"
                    }

                    val botMsg = Message(
                        id = UUID.randomUUID().toString(),
                        chatId = chatId,
                        senderName = "مساعد تلغرام الذكي",
                        text = reply,
                        timestamp = now,
                        isOutgoing = false
                    )
                    val current = _messages.value[chatId] ?: emptyList()
                    _messages.value = _messages.value + (chatId to (current + botMsg))

                    _chats.value = _chats.value.map {
                        if (it.id == chatId) it.copy(lastMessage = reply, time = now) else it
                    }
                }
            }
            "sarah_ahmed" -> {
                scope.launch {
                    _typingStatus.value = _typingStatus.value + (chatId to true)
                    delay(1800)
                    _typingStatus.value = _typingStatus.value + (chatId to false)

                    val reply = when {
                        userText.contains("كيف", ignoreCase = true) -> "الحمد لله بخير وسعادة! أنت كيف أمورك؟"
                        userText.contains("شكرا", ignoreCase = true) -> "العفو دائماً وأبداً في خدمتك 😊"
                        else -> "رائع جداً! سأقوم بمراجعة التفاصيل ومراسلتك لاحقاً 👍"
                    }

                    val sarahMsg = Message(
                        id = UUID.randomUUID().toString(),
                        chatId = chatId,
                        senderName = "سارة",
                        text = reply,
                        timestamp = now,
                        isOutgoing = false
                    )
                    val current = _messages.value[chatId] ?: emptyList()
                    _messages.value = _messages.value + (chatId to (current + sarahMsg))

                    _chats.value = _chats.value.map {
                        if (it.id == chatId) it.copy(lastMessage = reply, time = now) else it
                    }
                }
            }
        }
    }

    fun filterChats(folder: ChatFolder, query: String = ""): List<Chat> {
        val list = _chats.value
        val folderFiltered = when (folder) {
            ChatFolder.ALL -> list
            ChatFolder.PERSONAL -> list.filter { it.chatType == ChatType.DIRECT || it.chatType == ChatType.SAVED }
            ChatFolder.GROUPS -> list.filter { it.chatType == ChatType.GROUP }
            ChatFolder.CHANNELS -> list.filter { it.chatType == ChatType.CHANNEL }
            ChatFolder.BOTS -> list.filter { it.chatType == ChatType.BOT }
        }

        return if (query.isBlank()) {
            folderFiltered
        } else {
            folderFiltered.filter {
                it.title.contains(query, ignoreCase = true) ||
                it.lastMessage.contains(query, ignoreCase = true) ||
                (it.username?.contains(query, ignoreCase = true) == true)
            }
        }
    }

    fun startChatWithContact(contact: Contact): String {
        val existing = _chats.value.find { it.title == contact.name }
        if (existing != null) {
            return existing.id
        }

        val newId = "contact_${contact.id}"
        val newChat = Chat(
            id = newId,
            title = contact.name,
            lastMessage = "بدأت محادثة جديدة",
            time = "الآن",
            unreadCount = 0,
            isOnline = contact.isOnline,
            chatType = ChatType.DIRECT,
            avatarColorIndex = contact.avatarColorIndex,
            subtitle = contact.status,
            bio = "رقم الهاتف: " + contact.phone
        )
        _chats.value = listOf(newChat) + _chats.value
        _messages.value = _messages.value + (newId to listOf(
            Message(
                id = UUID.randomUUID().toString(),
                chatId = newId,
                text = "مرحباً! لقد بدأت محادثة مع ${contact.name}.",
                timestamp = "الآن",
                isOutgoing = true,
                status = MessageStatus.READ
            )
        ))
        return newId
    }

    fun createNewGroup(name: String, members: List<String>): String {
        val newId = "group_${UUID.randomUUID()}"
        val newChat = Chat(
            id = newId,
            title = name,
            lastMessage = "أنشأت المجموعة مع ${members.size} عضواً",
            time = "الآن",
            unreadCount = 0,
            chatType = ChatType.GROUP,
            avatarColorIndex = 4,
            subtitle = "${members.size + 1} أعضاء",
            bio = "مجموعة جديدة تم إنشاؤها عبر تلغرام"
        )
        _chats.value = listOf(newChat) + _chats.value
        _messages.value = _messages.value + (newId to listOf(
            Message(
                id = UUID.randomUUID().toString(),
                chatId = newId,
                text = "تم إنشاء المجموعة بنجاح. أهلاً بالجميع!",
                timestamp = "الآن",
                isOutgoing = true,
                status = MessageStatus.READ
            )
        ))
        return newId
    }

    fun togglePin(chatId: String) {
        _chats.value = _chats.value.map {
            if (it.id == chatId) it.copy(isPinned = !it.isPinned) else it
        }.sortedByDescending { it.isPinned }
    }

    fun toggleMute(chatId: String) {
        _chats.value = _chats.value.map {
            if (it.id == chatId) it.copy(isMuted = !it.isMuted) else it
        }
    }

    fun clearChatHistory(chatId: String) {
        _messages.value = _messages.value + (chatId to emptyList())
        _chats.value = _chats.value.map {
            if (it.id == chatId) it.copy(lastMessage = "تم مسح سجل المحادثة") else it
        }
    }
}
