package logic;

import models.Client;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;

/**
 * Created by wukat on 23.06.15.
 */
public class MailUtils {
    public static void sendActivationMail(String to) {
        Integer id = Client.getClientId(to);
        sendMail(to, "Registration confirmation",
                "<html><body><p>Hi,</p><p>Please click the link below to confirm registration in our service <a href=\"http://localhost:9000/users/" + id + "/confirm\">link</a></p><p>Best regards!</p></body></html>");
    }

    public static void sendMail(String to, String subject, String htmlBody) {
        Email email = new Email();
        email.setSubject(subject);
        email.addTo(to);
        email.setBodyText("");
        email.setBodyHtml(htmlBody);
        MailerPlugin.send(email);
    }

}
