package models;

import org.hibernate.Session;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="clients")
public class Client implements Serializable {

    @Id
    @Column(name="email")
    private String email;

    @Column(name="password")
    private String password;

    @Column(name="active", nullable = false, columnDefinition = "Boolean default false")
    private Boolean active = false;

    @OneToOne(mappedBy="client", cascade=CascadeType.ALL)
    @JoinColumn(name = "client_email")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, mappedBy="client", cascade=CascadeType.ALL)
    @JoinColumn(name = "client_email")
    private UserData userData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="clientSender", cascade=CascadeType.ALL)
    private Set<Message> messagesSent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="clientRecipient", cascade=CascadeType.ALL)
    private Set<Message> messagesReceived;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="clientPublisher", cascade=CascadeType.ALL)
    private Set<Offer> offersPublished;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="client", cascade=CascadeType.ALL)
    private Set<Booking> bookings;

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

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

    public Set<Offer> getOffersPublished() {
        return offersPublished;
    }

    public void setOffersPublished(Set<Offer> offersPublished) {
        this.offersPublished = offersPublished;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Client() {}
    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Client(String email, String password, Role role, UserData userData, Set<Message> messagesSent, Set<Message> messagesReceived, Set<Offer> offersPublished, Set<Booking> bookings) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.userData = userData;
        this.messagesSent = messagesSent;
        this.messagesReceived = messagesReceived;
        this.offersPublished = offersPublished;
        this.bookings = bookings;
    }

    @Override
    public String toString() {
        return "Client{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", active=" + active +
                ", role=" + role +
                ", userData=" + userData +
                ", messagesSent=" + messagesSent +
                ", messagesReceived=" + messagesReceived +
                ", offersPublished=" + offersPublished +
                ", bookings=" + bookings +
                '}';
    }

    public static String authenticate(String email, String password) {
        List result = JPA.em().unwrap(Session.class).createQuery("SELECT c.password FROM Client c WHERE c.email =:email").setString("email", email).list();
        if (result.size() == 1 && password.equals(result.get(0))) {
            return "";
        }
        return null;
    }
}
