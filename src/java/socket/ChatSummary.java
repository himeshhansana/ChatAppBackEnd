package socket;

import java.util.Date;

public class ChatSummary {

    private int friendId;
    private String friendName;
    private String lastMessage;
    private Date lastTimeStamp;
    private int unreadCount;
    private String profileImage;

    public ChatSummary() {
    }

    public ChatSummary(int friendId, String friendName, String lastMessage, Date lastTimeStamp, int unreadCount, String profileImage) {
        this.friendId = friendId;
        this.friendName = friendName;
        this.lastMessage = lastMessage;
        this.lastTimeStamp = lastTimeStamp;
        this.unreadCount = unreadCount;
        this.profileImage = profileImage;
    }

    public int getFriendId() {
        return friendId;
    }

    public void setFriendId(int friendId) {
        this.friendId = friendId;
    }

    public String getFriendName() {
        return friendName;
    }

    public void setFriendName(String friendName) {
        this.friendName = friendName;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Date getLastTimeStamp() {
        return lastTimeStamp;
    }

    public void setLastTimeStamp(Date lastTimeStamp) {
        this.lastTimeStamp = lastTimeStamp;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

}
