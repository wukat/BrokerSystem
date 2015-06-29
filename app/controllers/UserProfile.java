package controllers;

import logic.ClientLogic;
import logic.SessionManagement;
import models.Client;
import models.Role;
import models.ClientData;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.mailer.Email;
import play.libs.mailer.MailerPlugin;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import utils.HTTPSmust;
import views.html.*;

import static logic.MailUtils.sendActivationMail;
import static play.data.Form.form;

/**
 * Created by wukat on 04.06.15.
 */
public class UserProfile extends Controller {

    @HTTPSmust
    public static Result newUserForm() {
        if (SessionManagement.isOk(session())) {
            flash("error", "You already have an account");
            return redirect(routes.Application.index());
        }
        return ok(
                createAccount.render(form(Client.class))
        );
    }

    @Transactional(readOnly = true)
    public static Result newUserDataForm(Integer id) {
        if (ClientLogic.isClient(id)) {
            Client c = Client.getClientById(id);
            if (c.getRole().getRole().equals("business") && c.getClientData() == null) {
                return ok(
                        personalData.render(form(ClientData.class), id)
                );
            }
            flash("error", "Access denied!");
            return redirect(routes.Application.index());
        }
        return ok(notFound.render("Wrong client id"));
    }

    @Transactional
    public static Result newUserData(Integer id) {
        Form<ClientData> dataForm = form(ClientData.class).bindFromRequest();
        if (dataForm.hasErrors()) {
            return badRequest(personalData.render(dataForm, id));
        } else {
            Client client = Client.getClientById(id);
            if (client == null) {
                return ok(notFound.render("Wrong client id"));
            }
            ClientLogic.newClientData(client, dataForm.get());
            flash("success", "Registration proceeded successfully! Check your email for confirmation mail.");
            return redirect(routes.Application.index());
        }
    }

    @HTTPSmust
    @Transactional
    public static Result newUser() {
        Form<Client> userForm = form(Client.class).bindFromRequest();
        if (userForm.hasErrors()) {
            return badRequest(createAccount.render(userForm));
        } else {
            Client newClient = userForm.get();
            boolean businessUser = (userForm.data().get("bu") != null);
            ClientLogic.newUser(newClient, businessUser);
            if (businessUser) {
                flash("info", "Fill in your personal data to create business account.");
                return redirect(routes.UserProfile.newUserDataForm(newClient.getClientId()));
            } else {
                flash("success", "Registration proceeded successfully! Check your email for confirmation mail.");
                return redirect(routes.Application.index());
            }
        }
    }

    @Transactional
    public static Result confirm(Integer id) {
        Client client = Client.getClientById(id);
        if (client == null) {
            flash("error", "Access denied!");
        } else {
            ClientLogic.activateClient(id);
            flash("success", "Account activated, you can log in now!");
        }
        return ok(index.render(""));
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result profile(Integer id) {
        if (ClientLogic.isClient(id)) {
            Client client = Client.getClientById(id);
            if (client.getEmail().equals(SessionManagement.getEmail(session()))) {
                return ok(profileView.render(client));
            }
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }
}
