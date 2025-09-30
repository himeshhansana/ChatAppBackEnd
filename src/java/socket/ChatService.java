package socket;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Chat;
import entity.FriendList;
import entity.Status;
import entity.User;
import hibernate.HibernateUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss").create();
    private static final String URL = "https://3595b2e5876b.ngrok-free.app"; // ngrok proxy url

    public static void register(int userId, Session session) {
        SESSIONS.put(userId, session);
    }

    public static void unregister(int userId) {
        SESSIONS.remove(userId);
    }

    public static void sendToUser(int userId, Object payload) {
        Session ws = SESSIONS.get(userId);
        if (ws != null && ws.isOpen()) {
            try {
                System.out.println(GSON.toJson(payload));
                ws.getBasicRemote().sendText(GSON.toJson(payload));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<ChatSummary> getFriendChatsForUser(int userId) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Criteria friendListCriteria = session.createCriteria(FriendList.class);
            friendListCriteria.add(Restrictions.eq("userId.id", userId));
            friendListCriteria.add(Restrictions.eq("status", Status.ACTIVE));
            List<FriendList> friendList = friendListCriteria.list();// my friends list

            Map<Integer, ChatSummary> map = new LinkedHashMap<>();
            for (FriendList fl : friendList) {
                User myFriend = fl.getFrirnId();
                Criteria c1 = session.createCriteria(Chat.class);
                Criterion rest1 = Restrictions.and(Restrictions.eq("from.id", userId),
                        Restrictions.eq("to.id", myFriend.getId()));
                Criterion rest2 = Restrictions.and(Restrictions.eq("from.id", myFriend.getId()),
                        Restrictions.eq("to.id", userId));
                Criterion rest3 = Restrictions.or(rest1, rest2);
                c1.add(rest3);
                c1.addOrder(Order.desc("updatedAt"));

                List<Chat> chats = c1.list();

                if (!chats.isEmpty()) {

                    int unread = 0;
                    for (Chat c : chats) {
                        if (!c.getStatus().equals(Status.READ)) {
                            unread += 1;
                        }
                    }

                    if (!map.containsKey(myFriend.getId())) {
                        String profileImage = URL + "/ChatApp/profile-images/" + myFriend.getId() + "/profile1.png";
                        map.put(myFriend.getId(), new ChatSummary(
                                myFriend.getId(),
                                myFriend.getFirstname() + " " + myFriend.getLastname(),
                                chats.get(0).getMessage(),
                                chats.get(0).getUpdatedAt(),
                                unread,
                                profileImage
                        ));
                    }
                }
            }

            return new ArrayList<>(map.values());
        } finally {
            session.close();
        }
    }

    public static void deliverChat(Chat chat) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tr = session.beginTransaction();
        session.persist(chat);
        tr.commit();
        session.close();

        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "chat");
        envelope.put("payload", chat);

        sendToUser(chat.getTo().getId(), envelope);
        sendToUser(chat.getFrom().getId(), envelope);

        sendToUser(chat.getTo().getId(), friendListEnvelope(getFriendChatsForUser(chat.getTo().getId())));
        sendToUser(chat.getFrom().getId(), friendListEnvelope(getFriendChatsForUser(chat.getFrom().getId())));
    }

    public static Map<String, Object> friendListEnvelope(List<ChatSummary> list) {
        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "friend_list");
        envelope.put("payload", list);
        return envelope;
    }

    public static List<Chat> getChatHistory(int userId, int friendId) {
        org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Criteria c = session.createCriteria(Chat.class);
            c.add(Restrictions.or(
                    Restrictions.and(
                            Restrictions.eq("from.id", userId),
                            Restrictions.eq("to.id", friendId)
                    ),
                    Restrictions.and(
                            Restrictions.eq("from.id", friendId),
                            Restrictions.eq("to.id", userId)
                    )
            ));
            c.addOrder(Order.desc("createdAt"));
            List<Chat> list = c.list();

            Transaction tr = session.beginTransaction();

            for (Chat chat : list) {

                if (chat.getFrom().getId() == friendId
                        && chat.getTo().getId() == userId
                        && chat.getStatus().equals(Status.DELIVERD)) {
                    chat.setStatus(Status.READ);
                    session.update(chat);
                }
            }
            tr.commit();
            return list;
        } finally {
            session.close();
        }
    }

    public static Map<String, Object> singleChatEnvelope(List<Chat> chats) {
        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "single_chat");
        envelope.put("payload", chats);
        return envelope;
    }

    public static void saveNewChat(int userId, int friendId, String message) {

        org.hibernate.Session s = HibernateUtil.getSessionFactory().openSession();
        User me = (User) s.get(User.class, userId);
        User friend = (User) s.get(User.class, friendId);
        Chat chat = new Chat();
        chat.setFrom(me);
        chat.setTo(friend);
        chat.setMessage(message);
        chat.setCreatedAt(new Date());
        chat.setUpdatedAt(new Date());
        chat.setFile("FILE:");
        s.save(chat);
        s.beginTransaction().commit();
        s.close();

        Map<String, Object> envelope = new HashMap<>();
        envelope.put("type", "new_message");
        envelope.put("payload", chat);

        // Update both side => SingleChatScreen
        ChatService.sendToUser(chat.getFrom().getId(), envelope);
        ChatService.sendToUser(chat.getTo().getId(), envelope);

        // Update both side => HomeChatList
        List<ChatSummary> fromList = ChatService.getFriendChatsForUser(chat.getFrom().getId()); // from List
        List<ChatSummary> toList = ChatService.getFriendChatsForUser(chat.getTo().getId()); // to list
        Map<String, Object> fromMap = ChatService.friendListEnvelope(fromList); // from
        Map<String, Object> toMap = ChatService.friendListEnvelope(toList); // to

        ChatService.sendToUser(chat.getFrom().getId(), fromMap); // update from home chat
        ChatService.sendToUser(chat.getTo().getId(), toMap);     // update to home chat

    }

}
