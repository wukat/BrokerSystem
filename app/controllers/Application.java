package controllers;

import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;


public class Application extends Controller {

    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result index() {

//        Client testUser1 = new Client("lolaa@tlen.pl", "password");
//        Client testUser2 = new Client("hanna@tlen.pl", "password");
//        Role testRole1 = new Role(testUser1, "admin");
//        UserData testUserData1 = new UserData(testUser1, "Anna Kowalska", "666 66 66", "Czarnowiejska 1, Krakow");
//        Message testMessage1 = new Message(testUser1, testUser2, new Date(), "pies", "czy dostanie jedzenie");
//        Offer testOffer1 = new Offer(testUser2, Boolean.FALSE, 25, new Date(), 79.99, 5, 20, "Sloneczna 22, Shire");
//        Image testImage1 = new Image(testOffer1, null);
//        Image testImage2 = new Image(testOffer1, null);
//        Image testImage3 = new Image(testOffer1, null);
//        Booking testBooking1 = new Booking(testOffer1, testUser1, new Date(), 5, Boolean.FALSE);

//        JPA.em().persist(testUser1);
//        JPA.em().persist(testUser2);
//        JPA.em().persist(testRole1);
//        JPA.em().persist(testUserData1);
//        JPA.em().persist(testMessage1);
//        JPA.em().persist(testOffer1);
//        JPA.em().persist(testImage1);
//        JPA.em().persist(testImage2);
//        JPA.em().persist(testImage3);
//        JPA.em().persist(testBooking1);

//        System.out.println(JPA.em().unwrap(Session.class).createSQLQuery("SELECT  * FROM clients")
//                .setResultTransformer(Transformers.aliasToBean(Client.class))
//                .list().get(0));
        return ok(index.render("Your new application is ready."));
    }

}
