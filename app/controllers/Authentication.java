package controllers;

import models.Client;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.login;

import static play.data.Form.form;

/**
 * Created by wukat on 03.06.15.
 */
public class Authentication extends Controller {
    public static Result login() {
        return ok(
                login.render(form(Login.class))
        );
    }

    @Transactional
    public static Result authenticate() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            session().clear();
            session("email", loginForm.get().email);
            return redirect(
                    routes.Application.index()
            );
        }
    }


    public static class Login {

        public String email;
        public String password;

        public String validate() {
            if (Client.authenticate(email, password) == null) {
                return "Invalid user or password";
            }
            return null;
        }
    }
}
