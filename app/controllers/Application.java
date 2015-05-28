package controllers;

import models.TestEntity;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {
    @Transactional
    public static Result index() {
        TestEntity t = new TestEntity();
        JPA.em().persist(t);
        return ok(index.render("Your new application is ready."));
    }

}
