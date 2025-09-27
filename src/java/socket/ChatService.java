package socket;

import com.google.gson.Gson;
import entity.Chat;
import entity.User;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.Session;
import org.hibernate.Criteria;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ChatService {

    private static final ConcurrentHashMap<Integer, Session> SESSIONS = new ConcurrentHashMap<>();
    private static final Gson GSON = new Gson();
    private static final String URL = ""; //ngrok proxy url

    public static void register(int userId, Session session) { // websocket.Session
        ChatService.SESSIONS.put(userId, session);
    }

    public static void unregister(int userId) {
        ChatService.SESSIONS.remove(userId);
    }

    public static void sendToUser(int userId, Object paylout) {
        Session ws = ChatService.SESSIONS.get(userId);
        if (ws != null && ws.isOpen()) {
            try {
                ws.getBasicRemote().sendText(ChatService.GSON.toJson(paylout));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ChatSummary> getFriendChatsFouUser(int userId) {
        try {
            org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria c1 = session.createCriteria(Chat.class);
            Criterion rest1 = Restrictions.or(Restrictions.eq("from.id", userId),
                    Restrictions.eq("to.id", userId));
            c1.add(rest1);
            c1.addOrder(Order.desc("updateAt"));

            List<Chat> chats = c1.list();
            Map<Integer, ChatSummary> map = new LinkedHashMap<>();

            for (Chat chat : chats) {
                User friend = chat.getFrom().getId() == userId ? chat.getTo() : chat.getFrom();
                if (!map.containsKey(friend.getId())) {
                    String profileImage = ChatService.URL + "/chatApp/profile-images/" + friend.getId() + "/profile1.png";
                    int unread = 2;
                    map.put(friend.getId(), new ChatSummary(
                            friend.getId(),
                            friend.getFirstname() + "" + friend.getLastname(),
                            chat.getMessage(),
                            chat.getUpdatedAt(),
                            unread,
                            profileImage));
                }
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Data fetch failed!");
        }
    }

    public static void deliverChat(Chat chat) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.persist(chat);
        tr.commit();

        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "chat");
        envelope.put("payload", chat);

        ChatService.sendToUser(chat.getTo().getId(), envelope);
        ChatService.sendToUser(chat.getFrom().getId(), envelope);

        ChatService.sendToUser(chat.getTo().getId(),
                friendListEnvelope(getFriendChatsFouUser(chat.getTo().getId())));
        ChatService.sendToUser(chat.getFrom().getId(),
                friendListEnvelope(getFriendChatsFouUser(chat.getFrom().getId())));
    }

    public static Map<String, Object> friendListEnvelope(List<ChatSummary> list) {
        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "friend_list");
        envelope.put("payload", list);
        return envelope;
    }
}
