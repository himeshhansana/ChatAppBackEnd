package entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "chat")
public class Chat extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "from_user")
    private User from;

    @Column(name = "message", columnDefinition = "LONGTEXT", nullable = false)
    private String message;

    @ManyToOne
    @JoinColumn(name = "to_user")
    private User to;

    @Column(name = "files", nullable = false, columnDefinition = "LONGTEXT")
    private String files;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 30)
    private Status status = Status.SENT;

    public Chat() {
    }

    public Chat(User from, String message, User to, String files, Status status) {
        this.from = from;
        this.message = message;
        this.to = to;
        this.files = files;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getFiles() {
        return files;
    }

    public void setFiles(String files) {
        this.files = files;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

}
