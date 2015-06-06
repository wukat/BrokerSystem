package models;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.jpa.JPA;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "clients")
public class Client implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "userNumber", unique = true, nullable = false)
    private Integer userNumber;

    @Column(name = "active", nullable = false, columnDefinition = "Boolean default false")
    private Boolean active = false;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @JoinColumn(name = "client_userNumber")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    @JoinColumn(name = "client_userNumber")
    private UserData userData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientSender", cascade = CascadeType.ALL)
    private Set<Message> messagesSent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientRecipient", cascade = CascadeType.ALL)
    private Set<Message> messagesReceived;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientPublisher", cascade = CascadeType.ALL)
    private Set<Offer> offersPublished;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private Set<Booking> bookings;

    public Integer getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(Integer userNumber) {
        this.userNumber = userNumber;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

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

    public Client() {
    }

    public Client(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Client(Integer userNumber,  Boolean active, String email, String password) {
        this.email = email;
        this.password = password;
        this.active = active;
        this.userNumber = userNumber;
    }

    public Client(String email, String password, String confirmPassword, Boolean active, Role role, UserData userData, Set<Message> messagesSent, Set<Message> messagesReceived, Set<Offer> offersPublished, Set<Booking> bookings) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.active = active;
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
        if (result.size() == 1) {
            try {
                if (BCrypt.checkpw(password, (String) result.get(0)))
                    return "";
            } catch (Exception e) {
                Logger.debug("Password in database not encrypted, authenticate method");
            }
        }
        return null;
    }

    public String validate() {
        if (password.length() < 6) {
            return "Password to short, insert at least 6 signs";
        } else if (!confirmPassword.equals(password)) {
            return "Password mismatch";
        } else if (getClientId(email) != null) {
            return "You already have an account!";
        }
        return null;
    }

    public static Integer getClientId(String email) {
        List result = JPA.em().unwrap(Session.class).createQuery("SELECT c.userNumber FROM Client c WHERE c.email =:email").setString("email", email).list();
        if (result.size() == 1) {
            return (Integer) result.get(0);
        }
        return null;
    }

    public static void activateClient(Integer id) {
        JPA.em().unwrap(Session.class).createQuery("UPDATE Client c SET active=true").executeUpdate();
    }

    public static boolean checkActive(String email) {
        List result = JPA.em().unwrap(Session.class).createQuery("SELECT c.active FROM Client c WHERE c.email =:email").setString("email", email).list();
        if (result.size() == 1) {
            return (Boolean) result.get(0);
        }
        return false;
    }

    public static Client getClientById(Integer id) {
//        final Session session = JPA.em().unwrap(Session.class);
//        Transaction t = session.beginTransaction();
//       session.createQuery("SELECT c FROM Client c WHERE c.userNumber=:id").setInteger("id", id).list();
//            t.commit();
//        System.out.println(result.get(0));
//        if (result.size() == 1) {
//            return (Client) result.get(0);
//        }
        System.out.println(JPA.em().find(Client.class, id));
        return null;
    }
}
