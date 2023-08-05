import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.*

class ChatServiceTest {
    @Before
    fun clearBeforeTest () {
        ChatService.clear()
    }

    @Test
    fun addMessege() {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),1, 2, text = "text"))
        val result = message.id

        assertEquals(result, 1)
    }

    @Test
    fun deleteChatTrue () {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),1, 2, text = "text"))
        val delete = ChatService.deleteChat(1)

        assertTrue(delete)
    }

    @Test
    fun deleteChatFalse () {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),1, 2, text = "text"))
        val delete = ChatService.deleteChat(2)

        assertFalse(delete)
    }

    @Test
    fun deleteMessageTrue () {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),1, 2, text = "text"))
        val delete = ChatService.deleteMessage(1)

        assertTrue(delete)
    }

    @Test
    fun deleteMessageFalse () {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),1, 2, text = "text"))
        val delete = ChatService.deleteMessage(2)

        assertFalse(delete)
    }

    @Test
    fun getUnreadChatsCount() {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),1, 2, text = "text"))
        val message1 = ChatService.addMessege(elem = Message(1,2, Date(),2, 1, text = "text"))
        val message2 = ChatService.addMessege(elem = Message(3,14, Date(),5, 6, read = true, text = "text"))
        val result = ChatService.getUnreadChatsCount()
        assertEquals(result, 1)

    }

    @Test
    fun getChats() {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),2, 1, text = "text"))
        val message2 = ChatService.addMessege(elem = Message(2,2, Date(),1, 2, text = "text"))
        val result = ChatService.getChats(1)
        assertEquals(result.size, 1)
    }

    @Test
    fun chatLastMessage(){
        val message = ChatService.addMessege(elem = Message(1,1, Date(),2, 1, text = "text"))
        val result = ChatService.chatLastMessage()
        assertEquals(result.size, 1)
    }

    @Test
    fun getListMessage() {
        val message = ChatService.addMessege(elem = Message(1,1, Date(),2, 1, text = "text"))
        val result = ChatService.getListMessage(1,1,1)
        assertEquals(result.size, 1)
    }
}
