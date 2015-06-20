package models;

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
    @Column(name = "client_id", unique = true, nullable = false)
    private Integer clientId;

    @Column(name = "active", nullable = false, columnDefinition = "Boolean default false")
    private Boolean active = false;

    @Column(name = "email", unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @OneToOne(mappedBy = "client", cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private ClientData clientData;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientSender", cascade = CascadeType.ALL)
    private List<Message> messagesSent;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientRecipient", cascade = CascadeType.ALL)
    private List<Message> messagesReceived;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientPublisher", cascade = CascadeType.ALL)
    private List<Offer> offersPublished;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client", cascade = CascadeType.ALL)
    private List<Booking> bookings;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "clientPublisher", cascade = CascadeType.ALL)
    private List<Hotel> hotelsPublished;



    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer userNumber) {
        this.clientId = userNumber;
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

    public ClientData getClientData() {
        return clientData;
    }

    public void setClientData(ClientData clientData) {
        this.clientData = clientData;
    }

    public List<Message> getMessagesSent() {
        return messagesSent;
    }

    public void setMessagesSent(List<Message> messagesSent) {
        this.messagesSent = messagesSent;
    }

    public List<Message> getMessagesReceived() {
        return messagesReceived;
    }

    public void setMessagesReceived(List<Message> messagesReceived) {
        this.messagesReceived = messagesReceived;
    }

    public List<Offer> getOffersPublished() {
        return offersPublished;
    }

    public void setOffersPublished(List<Offer> offersPublished) {
        this.offersPublished = offersPublished;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public List<Hotel> getHotelsPublished() {
        return hotelsPublished;
    }

    public void setHotelsPublished(List<Hotel> hotelsPublished) {
        this.hotelsPublished = hotelsPublished;
    }

    public Client() {
    }

    public Client(String email, String password, Boolean active) {
        this.email = email;
        this.password = password;
        this.active = active;
    }

    public Client(String email, String password, String confirmPassword, Boolean active, Role role, ClientData clientData, List<Message> messagesSent, List<Message> messagesReceived, List<Offer> offersPublished, List<Booking> bookings) {
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.active = active;
        this.role = role;
        this.clientData = clientData;
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
                ", userData=" + clientData +
                ", messagesSent=" + messagesSent +
                ", messagesReceived=" + messagesReceived +
                ", offersPublished=" + offersPublished +
                ", bookings=" + bookings +
                '}';
    }

    public static String authenticate(String email, String password) {
        List result = JPA.em().createQuery("SELECT c.password FROM Client c WHERE c.email =:email").setParameter("email", email).getResultList();
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
        try {
            Object result = JPA.em().createQuery("SELECT c.clientId FROM Client c WHERE c.email =:email").setParameter("email", email).getSingleResult();
            if (result != null) {
                return (Integer) result;
            }
            Logger.debug("Client ID is null");
            return null;
        } catch (NoResultException e) {
            Logger.debug("Client ID is null");
            return null;
        }
    }

    public static Role getClientRole(String email) {
        try {
            Object result = JPA.em().createQuery("SELECT c.role FROM Client c WHERE c.email =:email").setParameter("email", email).getSingleResult();
            if (result != null) {
                return (Role) result;
            }
            Logger.debug("Client role is null");
            return null;
        } catch (NoResultException e) {
            Logger.debug("Client role is null");
            return null;
        }
    }

    public static void activateClient(Integer id) {
        JPA.em().createQuery("UPDATE Client c SET active=true where c.clientId=:id").setParameter("id", id).executeUpdate();
    }

    public static boolean checkActive(String email) {
        Object result = JPA.em().createQuery("SELECT c.active FROM Client c WHERE c.email =:email").setParameter("email", email).getSingleResult();
        if (result != null) {
            return (Boolean) result;
        }
        return false;
    }

    public static Client getClientById(Integer id) {
        return JPA.em().find(Client.class, id);
    }

    public static Client getClientByEmail(String email) {
        return (Client) JPA.em().createQuery("SELECT c FROM Client c WHERE c.email =:email").setParameter("email", email).getSingleResult();
    }

    public static boolean isClient(Integer id) {
        return getClientById(id) != null;
    }

    public static boolean isBusinessClient(String email) {
        Role clientRole = Client.getClientRole(email);
        return (clientRole != null && clientRole.getRole().equals("business"));
    }
}
