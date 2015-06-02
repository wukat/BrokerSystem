package models;

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
    @JoinColumn(name="sender")
    private User userSender;

    @ManyToOne
    @JoinColumn(name="recipient")
    private User userRecipient;

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

    public User getUserSender() {
        return userSender;
    }

    public void setUserSender(User userSender) {
        this.userSender = userSender;
    }

    public User getUserRecipient() {
        return userRecipient;
    }

    public void setUserRecipient(User userRecipient) {
        this.userRecipient = userRecipient;
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
    public Message(User userSender, User userRecipient, Date date, String subject, String content){
        this.userSender = userSender;
        this.userRecipient = userRecipient;
        this.date = date;
        this.subject = subject;
        this.content = content;
    }

}
