package logic;

import models.Client;
import models.ClientData;
import models.Message;
import models.Role;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.db.jpa.JPA;

import java.util.*;

import static logic.MailUtils.sendActivationMail;
import static models.Client.getClientById;

/**
 * Created by wukat on 23.06.15.
 */
public class ClientLogic {

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

    public static boolean isClient(Integer id) {
        return getClientById(id) != null;
    }

    public static boolean isBusinessClient(String email) {
        Role clientRole = Client.getClientRole(email);
        return (clientRole != null && clientRole.getRole().equals("business"));
    }

    public static LinkedList<Message> getConversation(Integer clientId, Client client) {
        Set<Message> conversationSet = new HashSet<>();
        for (Message m : client.getMessagesReceived()) {
            if (m.getClientSender().getClientId().equals(clientId)) {
                conversationSet.add(m);
            }
        }
        for (Message m : client.getMessagesSent()) {
            if (m.getClientRecipient().getClientId().equals(clientId)) {
                conversationSet.add(m);
            }
        }
        LinkedList<Message> conversation = new LinkedList<>(conversationSet);
        Collections.sort(conversation);
        Collections.reverse(conversation);
        return conversation;
    }

    public static void newClientData(Client client, ClientData clientData) {
        clientData.setClient(client);
        JPA.em().persist(clientData);
        sendActivationMail(client.getEmail());
    }

    public static void newUser(Client client, boolean business) {
        client.setUnreadMessages(0);
        client.setPassword(BCrypt.hashpw(client.getPassword(), BCrypt.gensalt()));
        Role role = new Role(client, (business) ? "business" : "customer");
        JPA.em().persist(client);
        JPA.em().persist(role);
        if (business) {
            sendActivationMail(client.getEmail());
        }
    }
}
