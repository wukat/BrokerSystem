package controllers;

import models.Client;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.createAccount;

import static play.data.Form.form;

/**
 * Created by wukat on 04.06.15.
 */
public class UserProfile extends Controller {


    public static Result newUserForm() {
        return ok(
                createAccount.render(form(Client.class))
        );
    }

    public static Result newUser() {
        Form<Client> loginForm = form(Client.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(createAccount.render(loginForm));
        } else {
            Client newClient = loginForm.get();
            newClient.setPassword(BCrypt.hashpw(newClient.getPassword(), BCrypt.gensalt()));
            sendMail(newClient.getEmail());
//            JPA.em().persist(newClient);
            return ok();
        }
    }

    public static void sendMail(String to) {
        Email email = new Email();
        email.setSubject("Registration confirmation");
        email.setFrom("wojciech.kasperek.1993@gmail.com");
        email.addTo(to);
        email.setBodyText("");
        email.setBodyHtml("<html><body> \"<p>Hi,</p><p>Please click the link below to confirm registration in our service <a href=\\\"localhost:9000/confirm/\\\">link</a></p><p>Best regards!</p>\"</body></html>");
        MailerPlugin.send(email);
    }
}
