package controllers;

import models.*;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.conversation;
import views.html.createMessage;
import views.html.received;
import views.html.sent;

import java.util.Date;

import static play.data.Form.form;

/**
 * Created by wukat on 21.06.15.
 */
public class Messages extends Controller {

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result newMessageForm(Integer offerId, Integer hotelId, Integer roomId) {
        if (roomId == null) {
            Offer offer = Offer.getById(offerId);
            Hotel hotel = Hotel.getById(hotelId);
            if (offer != null || hotel != null) {
                return ok(createMessage.render(offer, hotelId, null, form(Message.class)));
            }

        } else {
            OfferedRoom offeredRoom = OfferedRoom.getByAll(offerId, hotelId, roomId);
            if (offeredRoom != null) {
                return ok(createMessage.render(offeredRoom.getOffer(), hotelId, roomId, form(Message.class)));
            }
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result message(Integer messageId) {
        Message message = Message.getById(messageId);
        Client client = Client.getClientByEmail(SessionManagement.getEmail(session()));
        if (message == null || !(message.getClientRecipient().getClientId().equals(client.getClientId()) || message.getClientSender().getClientId().equals(client.getClientId()))) {
            flash("error", "Access denied");
            return redirect(routes.Application.index());
        }
        if (message.getClientRecipient().getClientId().equals(client.getClientId())) {
            if (!message.getRead()) {
                message.setRead(true);
                client.setUnreadMessages(client.getUnreadMessages() - 1);
                JPA.em().flush();
            }
        }
        return ok(conversation.render(message, client, form(Message.class)));
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result newMessage(Integer clientId) {
        Client sender = Client.getClientByEmail(SessionManagement.getEmail(session()));
        Client receiver = Client.getClientById(clientId);

        Form<Message> messageForm = form(Message.class).bindFromRequest();
        Message message = messageForm.get();
        message.setClientRecipient(receiver);
        message.setClientSender(sender);
        message.setDate(new Date());
        message.setRead(false);
        JPA.em().persist(message);
        receiver.setUnreadMessages(receiver.getUnreadMessages() + 1);
        JPA.em().flush();
        flash("success", "Message sent");
        return redirect(routes.Messages.sent());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result received() {
        return ok(received.render(Client.getClientByEmail(SessionManagement.getEmail(session()))));
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result sent() {
        return ok(sent.render(Client.getClientByEmail(SessionManagement.getEmail(session()))));
    }
}
