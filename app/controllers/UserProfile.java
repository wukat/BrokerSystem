package controllers;

import models.Client;
import models.Role;
import models.UserData;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.createAccount;
import views.html.index;
import views.html.notFound;
import views.html.personalData;

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

    @Transactional(readOnly = true)
    public static Result newUserDataForm(Integer id) {
        if (Client.getClientById(id) != null) {
            return ok(
                    personalData.render(form(UserData.class), id)
            );
        }
        return ok(notFound.render("Wrong client id"));
    }

    @Transactional
    public static Result newUserData(Integer id) {
        Form<UserData> dataForm = form(UserData.class).bindFromRequest();
        if (dataForm.hasErrors()) {
            return badRequest(personalData.render(dataForm, id));
        } else {
            UserData userData = dataForm.get();
            Client client = Client.getClientById(id);
            if (client == null) {
                return ok(notFound.render("Wrong client id"));
            }
            userData.setClient(client);
            JPA.em().persist(userData);
            sendActivationMail(client.getEmail());
            flash("success", "Registration proceeded successfully! Check your email for confirmation mail.");
            return ok();
        }
    }

    @Transactional
    public static Result newUser() {
        Form<Client> userForm = form(Client.class).bindFromRequest();
        if (userForm.hasErrors()) {
            return badRequest(createAccount.render(userForm));
        } else {
            Client newClient = userForm.get();
            boolean businessUser = (userForm.data().get("bu") != null);
            newClient.setPassword(BCrypt.hashpw(newClient.getPassword(), BCrypt.gensalt()));
            Role role = new Role(newClient, (businessUser) ? "business" : "customer");
            Logger.debug(newClient.getEmail());
            JPA.em().persist(newClient);
            JPA.em().persist(role);
            if (businessUser) {
                flash("info", "Fill in your personal data to create business account.");
                return redirect(routes.UserProfile.newUserDataForm(Client.getClientId(newClient.getEmail())));
            } else {
                sendActivationMail(newClient.getEmail());
                flash("success", "Registration proceeded successfully! Check your email for confirmation mail.");
                return ok();
            }
        }
    }

    @Transactional
    public static Result confirm(Integer id) { //TODO do not allow to confirm business account if there is not user data
        Client.activateClient(id);
        flash("success", "Account activated, you can log in now!");
        return ok(index.render("Account activated"));
    }

    private static void sendActivationMail(String to) {
        Integer id = Client.getClientId(to);
        sendMail(to, "Registration confirmation",
                "<html><body><p>Hi,</p><p>Please click the link below to confirm registration in our service <a href=\"http://localhost:9000/users/" + id + "/confirm\">link</a></p><p>Best regards!</p></body></html>");
    }

    private static void sendMail(String to, String subject, String htmlBody) {
        Email email = new Email();
        email.setSubject(subject);
        email.setFrom("wojciech.kasperek.1993@gmail.com");
        email.addTo(to);
        email.setBodyText("");
        email.setBodyHtml(htmlBody);
        MailerPlugin.send(email);
    }
}
