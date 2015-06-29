package controllers;

import global.Global;
import logic.ClientLogic;
import logic.SessionManagement;
import org.jose4j.lang.JoseException;
import play.data.Form;
import play.data.validation.Constraints;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import utils.HTTPSmust;
import views.html.login;

import static play.data.Form.form;

/**
 * Created by wukat on 03.06.15.
 */
public class Authentication extends Controller {

    @HTTPSmust
    public static Result login() {
        if(SessionManagement.isOk(session())) {
            flash("info", "You are logged in");
            return redirect(routes.Application.index());
        }
        return ok(
                login.render(form(Login.class))
        );
    }

    @HTTPSmust
    @Transactional(readOnly = true)
    public static Result authenticate() throws JoseException {
        Http.Cookie a = request().cookie("url");
        Form<Login> loginForm = form(Login.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
            String path = (a != null) ? a.value() : "/";
            response().discardCookie("url");
            session().clear();
            Global.jwe.setPayload("ok " + loginForm.get().email);
            session("k", Global.jwe.getCompactSerialization());
            return redirect(
                    path
            );
        }
    }

    public static Result logout() {
        session().clear();
        flash("success", "You've been logged out");
        return redirect(
                routes.Application.index()
        );
    }

    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String email;

        @Constraints.Required
        public String password;

        public String validate() {
            if (ClientLogic.authenticate(email, password) == null) {
                return "Invalid user or password";
            } else if (!ClientLogic.checkActive(email)) {
                return "Your account isn't activated yet! Check your email";
            }
            return null;
        }
    }
}
