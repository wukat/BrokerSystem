package controllers;

import com.lowagie.text.DocumentException;
import org.jose4j.lang.JoseException;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.index;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Application extends Controller {

    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result index() throws JoseException, IOException, DocumentException {

        Document document = XMLResource.load(new ByteArrayInputStream(index.render("a").body().getBytes())).getDocument();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument( document, null );

        renderer.layout();

        String fileNameWithPath = "PDF-XhtmlRendered.pdf";
        FileOutputStream fos = new FileOutputStream(fileNameWithPath);
        renderer.createPDF( fos );
        fos.close();
        System.out.println( "File 1: '" + fileNameWithPath + "' created." );

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

//        Offer a = (Offer) JPA.em().createQuery("FROM Offer c").getResultList().get(0);
//        System.out.println(a.getBookings().size());
//        System.out.println(a.getImages().size());
//        JPA.em().unwrap(Session.class).persist(testUser1);

        //return ok(index.render("Your new application is ready."));


//        Client testUser1 = new Client("publisher@tlen.pl", "password", true);
//        Client testUser2 = new Client("customer@tlen.pl", "password", true);
//
//        ClientData testUserData1 = new ClientData(testUser1, "Anna Kowalska", "666 66 66", "Czarnowiejska 1, Krakow");
//
//        Role testRole1 = new Role(testUser1, "publisher");
//        Role testRole2 = new Role(testUser2, "customer");
//
//        Message testMessage1 = new Message(testUser2, testUser1, new Date(), "pies", "czy dostanie jedzenie");
//
//        Offer testOffer1 = new Offer(testUser1, new Date(), new Date(), 25.0, "calkiem ladnie ale zimno", true, 0);
//
//        Hotel testHotel1 = new Hotel(1,testUser1, "Pod Kasztanem", "Kolobrzegi", "Deszczowo 234", 5);
//
//        Room testRoom1 = new Room(1, testHotel1, 3, true);
//        Room testRoom2 = new Room(2, testHotel1, 2, true);
//        Room testRoom3 = new Room(3, testHotel1, 2, false);
//
//        Image testImage1 = new Image(testRoom1);
//        Image testImage2 = new Image(testRoom1);
//        Image testImage3 = new Image(testRoom2);
//        Image testImage4 = new Image(testRoom3);
//
//        OfferedRoom testOfferedRoom1 = new OfferedRoom(testHotel1, testRoom1, testOffer1);
//        OfferedRoom testOfferedRoom2 = new OfferedRoom(testHotel1, testRoom2, testOffer1);
//        OfferedRoom testOfferedRoom3 = new OfferedRoom(testHotel1, testRoom3, testOffer1);
//
//        Booking testBooking1 = new Booking(testUser2, testOfferedRoom3, new Date(), new Date(), false);
//
//
//
//        JPA.em().persist(testUser1);
//        JPA.em().persist(testUser2);
//
//        JPA.em().persist(testUserData1);
//
//        JPA.em().persist(testRole1);
//        JPA.em().persist(testRole2);
//
//        JPA.em().persist(testMessage1);
//
//        JPA.em().persist(testOffer1);
//
//        JPA.em().persist(testHotel1);
//
//        JPA.em().persist(testRoom1);
//        JPA.em().persist(testRoom2);
//        JPA.em().persist(testRoom3);
//
//        JPA.em().persist(testImage1);
//        JPA.em().persist(testImage2);
//        JPA.em().persist(testImage3);
//        JPA.em().persist(testImage4);
//
//        JPA.em().persist(testOfferedRoom1);
//        JPA.em().persist(testOfferedRoom2);
//        JPA.em().persist(testOfferedRoom3);
//
//        JPA.em().persist(testBooking1);

        return ok(index.render("ok"));
    }

}
