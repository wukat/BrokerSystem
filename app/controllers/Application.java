package controllers;

import model.Message;
import model.User;
import model.Role;
import model.UserData;

import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

import java.util.Date;


public class Application extends Controller {

    @Transactional
    public static Result index() {

        User testUser1 = new User("lol@tlen.pl", "password");
        Role testRole1 = new Role(testUser1, "admin");
        UserData testUserData1 = new UserData(testUser1, "Anna Kowalska", "666 66 66", "Czarnowiejska 1, Krakow");
        Message testMessage1 = new Message(testUser1, "random@o2.pl", new Date(), "piec", "czy dostanie jedzenie");

        JPA.em().persist(testUser1);
        JPA.em().persist(testRole1);
        JPA.em().persist(testUserData1);
        JPA.em().persist(testMessage1);

        return ok(index.render("Your new application is ready."));

    }

}
