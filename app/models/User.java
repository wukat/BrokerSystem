package models;

import java.util.Set;

import javax.persistence.*;


@Entity
@Table(name="users")
public class User {

    @Id
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL)
//    @JoinColumn(name = "email")
    private Role role;

    @OneToOne(mappedBy="user", cascade=CascadeType.ALL)
//    @JoinColumn(name = "email")
    private UserData userData;

    @OneToMany(mappedBy="userSender", cascade=CascadeType.ALL)
    private Set<Message> messagesSent;

    @OneToMany(mappedBy="userRecipient", cascade=CascadeType.ALL)
    private Set<Message> messagesReceived;

    @OneToMany(mappedBy="userPublisher", cascade=CascadeType.ALL)
    private Set<Offer> offersPublished;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public Set<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(Set<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public Set<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(Set<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public User() {}
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

}
