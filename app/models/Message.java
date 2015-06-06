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
    @Column(name="messageId")
    private Long messageId;

    @ManyToOne
    @JoinColumn(name="sender")
    private Client clientSender;

    @ManyToOne
    @JoinColumn(name="recipient")
    private Client clientRecipient;

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

    public Client getClientSender() {
        return clientSender;
    }

    public void setClientSender(Client clientSender) {
        this.clientSender = clientSender;
    }

    public Client getClientRecipient() {
        return clientRecipient;
    }

    public void setClientRecipient(Client clientRecipient) {
        this.clientRecipient = clientRecipient;
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
    public Message(Client clientSender, Client clientRecipient, Date date, String subject, String content){
        this.clientSender = clientSender;
        this.clientRecipient = clientRecipient;
        this.date = date;
        this.subject = subject;
        this.content = content;
    }

}
