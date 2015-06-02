package controllers;

import models.*;

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
        User testUser2 = new User("hanna@tlen.pl", "password");
        Role testRole1 = new Role(testUser1, "admin");
        UserData testUserData1 = new UserData(testUser1, "Anna Kowalska", "666 66 66", "Czarnowiejska 1, Krakow");
        Message testMessage1 = new Message(testUser1, testUser2, new Date(), "pies", "czy dostanie jedzenie");
        Offer testOffer1 = new Offer(testUser2, Boolean.FALSE, 25, new Date(), 79.99, 5, 20, "Sloneczna 22, Shire");
        Image testImage1 = new Image(testOffer1, null);
        Image testImage2 = new Image(testOffer1, null);
        Image testImage3 = new Image(testOffer1, null);

        JPA.em().persist(testUser1);
        JPA.em().persist(testUser2);
        JPA.em().persist(testRole1);
        JPA.em().persist(testUserData1);
        JPA.em().persist(testMessage1);
        JPA.em().persist(testOffer1);
        JPA.em().persist(testImage1);
        JPA.em().persist(testImage2);
        JPA.em().persist(testImage3);

        return ok(index.render("Your new application is ready."));

    }

}
