package controllers;

import play.mvc.*;
import play.mvc.Http.*;

public class Secured extends Security.Authenticator {

    @Override
    public String getUsername(Context ctx) {
        return SessionManagement.getEmail(ctx.session());
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        ctx.flash().put("info", "Please log in first!");
        ctx.flash().put("url", "GET".equals(ctx.request().method()) ? ctx.request().uri() : "/");
        return redirect(routes.Authentication.login());
    }
}