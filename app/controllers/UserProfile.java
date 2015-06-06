package controllers;

import models.Client;
import org.mindrot.jbcrypt.BCrypt;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.createAccount;
import views.html.index;

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

    @Transactional
    public static Result newUser() {
        Form<Client> loginForm = form(Client.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(createAccount.render(loginForm));
        } else {
            Client newClient = loginForm.get();
            newClient.setPassword(BCrypt.hashpw(newClient.getPassword(), BCrypt.gensalt()));
            JPA.em().persist(newClient);
            sendActivationMail(newClient.getEmail());
            return ok();
        }
    }

    @Transactional
    public static Result confirm(Long id) {
        Client.activateClient(id);
        flash("success", "Account activated, you can log in now!");
        return ok(index.render("Account activated"));
    }

    private static void sendActivationMail(String to) {
        Integer id = Client.getClientId(to);
        sendMail(to, "Registration confirmation",
                "<html><body><p>Hi,</p><p>Please click the link below to confirm registration in our service <a href=\"http://localhost:9000/users/" + id + "/confirm\">link</a></p><p>Best regards!</p></body></html>");
    }

    private  static void sendMail(String to, String subject, String htmlBody) {
        Email email = new Email();
        email.setSubject(subject);
        email.setFrom("wojciech.kasperek.1993@gmail.com");
        email.addTo(to);
        email.setBodyText("");
        email.setBodyHtml(htmlBody);
        MailerPlugin.send(email);
    }
}
