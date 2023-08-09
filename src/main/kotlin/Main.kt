import java.util.*

interface Discussion {
    val id: Int
}

data class Chat(
    override val id: Int, //Идентификатор беседы
    val adminId: Int, //Идентификатор пользователя, который является создателем беседы
    val userId: Int, //Идентификатор участника беседы
) : Discussion

data class Message(
    override val id: Int, //Идентификатор беседы
    val idMessage: Int, //Идентификатор сообщения
    val date: Date, //Время отправки
    val fromId: Int, //Идентификатор отправителя
    val userId: Int, //Идентификатор участника беседы
    val read: Boolean = false,
    val text: String,
) : Discussion


object ChatService {
    private var messageList = mutableListOf<Message>()
    private var chatList = mutableListOf<Chat>()

    fun clear() {
        messageList = mutableListOf<Message>()
        chatList = mutableListOf<Chat>()
    }

    private fun addChat(elem: Chat): Chat {
        chatList.add(elem)
        return chatList.last()
    }

    fun addMessage(elem: Message): Message { //добавить сообщение и чат если его еще нет
        chatList.find { it.id == elem.id } ?: addChat(elem = Chat(id = elem.id, adminId = elem.fromId, userId = elem.userId))
        messageList.add(elem)
        return messageList.last()

    }

    fun deleteChat(chatId: Int): Boolean { //удалить чат и всю переписку
        val messageFilter = messageList.filter { it.id == chatId }
        messageList.removeAll(messageFilter)
        val result = chatList.find { it.id == chatId } ?: throw Exception("Чат не найден")
        return chatList.remove(result)
    }

    fun deleteMessage(messageId: Int): Boolean { //удалить сообщение по Id
        val messageFilter = messageList.filter { it.idMessage == messageId }
        return messageList.removeAll(messageFilter)
    }

    fun getUnreadChatsCount(): Int { //сколько чатов не прочитанно
        var count = 0
        val messageFilter = messageList.filter { !it.read }
        messageList.removeAll(messageFilter)
        messageList.addAll(messageFilter.map { message -> message.copy(read = true) })
        if (messageFilter.isNotEmpty()) count++
        return count
    }

    fun getChats(id: Int): MutableList<Chat> { // чаты одного пользователя с разными людьми
        var getChatListId = mutableListOf<Chat>()
        val result = chatList.filter { it.adminId == id }
        getChatListId.addAll(result)
        return getChatListId

    }

    fun chatLastMessage(): MutableList<Message> { //Получить список последних сообщений из чатов
        var chatLastMessage = mutableListOf<Message>()
        chatList.forEach { chat ->
            chatLastMessage.add(
                messageList.filter { it.id == chat.id }.maxByOrNull { it.idMessage } ?: Message(
                    id = chat.id,
                    idMessage = -1,
                    date = Date(),
                    fromId = -1,
                    userId = -1,
                    read = true,
                    text = "Сообщений нет"
                )
            )
        }
        return chatLastMessage
    }

    fun getListMessage(chatId: Int, idLast: Int, count: Int): MutableList<Message> { //Список сообщений из чата
        chatList.find { chat -> chat.id == chatId } ?: throw Exception("Чат не найден") // нашли чат с нужным id
        val result =
            messageList.asSequence().filter { it.id == chatId }
                .take(count).toList()
        messageList.removeAll(result) // удалем из  messageList все элементы из messageSequence
        messageList.addAll(result.map { it.copy(read = true)})
        messageList = messageList.sortedBy { it.idMessage }.toMutableList()
        messageList.sortedBy { message -> message.idMessage }
        return result.toMutableList()
    }
}

fun main(args: Array<String>) {
    ChatService.addMessage(elem = Message(1, 1, Date(), 1, 1, text = "1"))
    ChatService.addMessage(elem = Message(1, 2, Date(), 1, 2, text = "2"))
    ChatService.addMessage(elem = Message(1, 3, Date(), 2, 1, text = "3"))
    println(ChatService.getUnreadChatsCount())
    println(ChatService.chatLastMessage())
    println("Get list Message ${ChatService.getListMessage(1, 3, 1)}")

}
