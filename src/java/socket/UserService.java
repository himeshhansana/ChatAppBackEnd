package socket;

import entity.Chat;
import entity.FriendList;
import entity.Status;
import entity.User;
import hibernate.HibernateUtil;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

public class UserService {

    public static void updateLogInStatus(int userId) {
        updateStatus(userId, Status.ONLINE);
    }

    public static void updateLogOutStatus(int userId) {
        updateStatus(userId, Status.OFFLINE);
    }

    private static void updateStatus(int userId, Status status) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        User fromUser = (User) s.get(User.class, userId);
        fromUser.setStatus(status);
        fromUser.setUpdatedAt(new Date());
        s.update((fromUser));
        s.beginTransaction().commit();
    }

    public static void updateFriendChatStatus(int userId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Criteria c1 = s.createCriteria(FriendList.class);
        c1.add(Restrictions.eq("userId.id", userId));
        c1.add(Restrictions.eq("status", Status.ACTIVE));

        //get active friend list
        List<FriendList> myFriends = c1.list();

        Transaction tr = s.beginTransaction();

        for (FriendList myFriend : myFriends) {
            User me = myFriend.getUserId();
            User friend = myFriend.getFrirnId();

            if (me.getStatus().equals(Status.ONLINE)) {
                Criteria c2 = s.createCriteria(Chat.class);
                Criterion rest1 = Restrictions.and(Restrictions.eq("from", friend),
                        Restrictions.eq("to", me), Restrictions.eq("status", Status.SENT));
                c2.add(rest1);

                List<Chat> chats = c2.list();
                for (Chat chat : chats) {
                    chat.setStatus(Status.DELIVERD);
                    chat.setUpdatedAt(new Date());
                    s.update(chat);
                }
            }
        }
        tr.commit();
    }
}
