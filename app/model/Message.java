package model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="messages")
public class Message {

    @Id
    @GeneratedValue
    @Column(name="message_id")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name="email")
    private User user;

    @Column(name="sender")
    private String sender;

    @Column(name="date")
    private Date date;

    @Column(name="subject")
    private String subject;

    @Column(name="content")
    private String content;

    public Long getMessageId() {
        return messageId;
    }

    public void setMessageId(Long messageId) {
        this.messageId = messageId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User userFrom) {
        this.user = userFrom;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Message(){}
    public Message(User user, String sender, Date date, String subject, String content){
        this.user = user;
        this.sender = sender;
        this.date = date;
        this.subject = subject;
        this.content = content;
    }

}
