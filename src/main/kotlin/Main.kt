import java.util.Date

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

    fun addMessege(elem: Message): Message { //добавить сообщение и чат если его еще нет
        var sum = 0
        for (i in chatList.indices) if (chatList[i].id == elem.id) sum++

        when (sum == 0) {
            true -> {
                addChat(elem = Chat(id = elem.id, adminId = elem.fromId, userId = elem.userId))
                messageList.add(elem)
            }

            false -> messageList.add(elem)
        }

        return messageList.last()
    }

    fun deleteChat(chatId: Int): Boolean { //удалить чат и всю переписку
        for (i in messageList.indices) {
            if (messageList[i].id == chatId) {
                messageList.remove(messageList[i])
            }
        }
        for (j in chatList.indices) {
            if (chatList[j].id == chatId) {
                chatList.remove(chatList[j])
                return true
            }
        }
        return false
    }

    fun deleteMessage(messageId: Int): Boolean { //удалить сообщение по Id
        for (i in messageList.indices) {
            if (messageList[i].id == messageId) {
                messageList.remove(messageList[i])
                return true
            }
        }
        return false
    }


    fun getUnreadChatsCount(): Int { //сколько чатов не прочитанно
        var count = 0
        for (i in chatList.indices) {
            var countMes = 0
            for (j in messageList.indices){
                if (!messageList[j].read) {
                    messageList[j] = messageList[j].copy(read = true)
                    countMes++
                }
            }
            if (countMes > 0) count++
        }
        return count
    }

    fun getChats(id: Int): MutableList<Chat> { // чаты одного пользователя с разными людьми
        var getChatListId = mutableListOf<Chat>()
        for (i in chatList.indices) if (chatList[i].adminId == id) getChatListId.add(chatList[i])
        return getChatListId
    }

    fun chatLastMessage(): MutableList<Message> {
        var chatLastMessage = mutableListOf<Message>()
        for (i in chatList.indices) {
            if (chatList[i].id == messageList.last().id)
                chatLastMessage.add(messageList.last())
            else(chatLastMessage.add(element = Message(idMessage = chatList[i].id, date = Date(), fromId = 0, id = 0, userId = 0, text = "Сообщений нет")))
        }
        return chatLastMessage
    }

    fun getListMessage(chatId: Int, idLast: Int, count: Int): MutableList<Message> { //Список сообщений из чата
        chatList.find { chat -> chat.id == chatId } ?: throw Exception("Чат не найден")
        val result = messageList.filter { message ->  message.id == chatId && message.idMessage >= idLast } //Отфильтровать список сообщений, оставив в нём только те, которые принадлежат нужному чату и не превышают заданный id сообщения
            .take(count) //Взять только нужное количество (если count будет больше размера получившегося списка, то take вернёт сколько есть)
        messageList.removeAll(result) //Удалить из списка все найденные элементы
        messageList.addAll(result.map { message -> message.copy(read = true) }) //Вернуть в список все найденные элементы с флагом прочитано
        messageList.sortedBy { message -> message.idMessage } //отсортировать по id
        return result.toMutableList()
    }
}

fun main(args: Array<String>) {
    ChatService.addMessege(elem = Message(1, 1, Date(), 1, 1, text = "1"))
    ChatService.addMessege(elem = Message(1, 2, Date(), 1, 2, text = "2"))
    ChatService.addMessege(elem = Message(1, 3, Date(), 2, 1, text = "3"))
    println(ChatService.getUnreadChatsCount())
    println(ChatService.chatLastMessage())

}
