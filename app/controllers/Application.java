package controllers;

import models.User;
import models.Role;
import models.UserData;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;



public class Application extends Controller {

    @Transactional
    public static Result index() {

        User testUser1 = new User("lol@tlen.pl", "password");
        Role testRole1 = new Role(testUser1, "admin");
        UserData testUserData1 = new UserData(testUser1, "Anna Kowalska", "666 66 66", "Czarnowiejska 1, Krakow");

        JPA.em().persist(testUser1);
        JPA.em().persist(testRole1);
        JPA.em().persist(testUserData1);

        return ok(index.render("Your new application is ready."));

    }

}
