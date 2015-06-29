package logic;

import models.Client;
import models.Message;
import play.db.jpa.JPA;

import java.util.Date;

/**
 * Created by wukat on 23.06.15.
 */
public class MessagesLogic {
    public static void setRead(Message message, Client client) {
        if (message.getClientRecipient().getClientId().equals(client.getClientId())) {
            if (!message.getRead()) {
                message.setRead(true);
                client.setUnreadMessages(client.getUnreadMessages() - 1);
                JPA.em().flush();
            }
        }
    }

    public static boolean canBeRead(Message message, Client client) {
        return !(message == null || !(message.getClientRecipient().getClientId().equals(client.getClientId()) || message.getClientSender().getClientId().equals(client.getClientId())));
    }

    public static void newMessage(Message message, Client sender, Client receiver) {
        message.setClientRecipient(receiver);
        message.setClientSender(sender);
        message.setDate(new Date());
        message.setRead(false);
        JPA.em().persist(message);
        receiver.setUnreadMessages(receiver.getUnreadMessages() + 1);
        JPA.em().flush();
    }
}
