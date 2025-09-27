package socket;

import com.google.gson.Gson;
import entity.Chat;
import entity.Status;
import entity.User;
import hibernate.HibernateUtil;
import java.util.Date;
import java.util.Map;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author hansa
 */
@ServerEndpoint(value = "/chat")
public class ChatEndPoint {
    
    private static final Gson GSON = new Gson();
    private int userId;
    
    @OnOpen
    public void onOpen(Session session) {
        String query = session.getQueryString();
        
        if (query != null && query.startsWith("userId=")) {
            userId = Integer.parseInt(query.substring("userId=".length()));
            ChatService.register(userId, session);
            ChatService.sendToUser(userId,
                    ChatService.friendListEnvelope(ChatService.getFriendChatsFouUser(userId)));
        }
    }
    
    @OnClose
    public void onClose(Session session) {
        
        if (userId >= 0) { //userid != null
            ChatService.unregister(userId);
        }
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
        throwable.printStackTrace();
    }
    
    @OnMessage
    public void onMessage(String message, Session session) {
        try {
            Map<String, Object> map = ChatEndPoint.GSON.fromJson(message, Map.class);
            String type = (String) map.get("type");
            switch (type) {
                case "send_chat":
                    int fromId = (int) map.get("fromId");
                    int toId = (int) map.get("toId");
                    String chatText = (String) map.get("message");
                    org.hibernate.Session s = HibernateUtil.getSessionFactory().openSession();
                    User fromUser = (User) s.get(User.class, fromId);
                    User toUser = (User) s.get(User.class, toId);
                    
                    if (fromUser != null && toUser != null) {
                        Chat chat = new Chat(fromUser, chatText, toUser, "", Status.SENT);
                        chat.setCreatedAt(new Date());
                        chat.setUpdatedAt(new Date());
                        ChatService.deliverChat(chat);
                    }
                    
                    break;
                default:
                    throw new AssertionError();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
