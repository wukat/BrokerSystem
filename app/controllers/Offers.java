package controllers;

import models.*;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static play.data.Form.form;

/**
 * Created by wukat on 07.06.15.
 */
public class Offers extends Controller {

    @Transactional(readOnly = true)
    public static Result all() {
        LinkedList<OfferedRoom> offers;
        if (SessionManagement.isOk(session())) {
            offers = OfferedRoom.getAllActual();
        } else {
            offers = OfferedRoom.getNonPremiumActual();
        }
        return ok(offersView.render(offers));
    }

    @Transactional
    public static Result seeOffer(Integer offerId, Integer hotelId) {
        Offer offer = Offer.getNotExpiredById(offerId);
        Hotel hotel = Hotel.getById(hotelId);
        if (offer == null || hotel == null) {
            return ok(notFound.render("Wrong offer id"));
        }
        if (offer.getPremium() && !SessionManagement.isOk(session())) {
            flash("url", "GET".equals(request().method()) ? request().uri() : "/");
            flash("info", "Log in to access premium offers.");
            return redirect(routes.Authentication.login());
        }
        List<OfferedRoom> offeredRoomList = OfferedRoom.getByHotelAndOffer(offerId, hotelId);
        offer.setVisitCount(offer.getVisitCount() + 1);
        JPA.em().flush();
        if (offer.getDateTo().before(new Date())) {
            flash("info", "Offer has expired.");
        }
        return ok(offerView.render(offeredRoomList, offer, hotel));
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result seeOfferDetails(Integer offerId) {
        Offer offer = Offer.getNotExpiredById(offerId);
        if (offer == null) {
            return ok(notFound.render("Wrong offer id"));
        }
        if (offer.getClientPublisher().getEmail().equals(SessionManagement.getEmail(session()))) {
            return ok(offerDetailsView.render(offer));
        }
        flash("info", "Access denied");
        return redirect(routes.Authentication.login());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result newOfferForm() {
        String email = SessionManagement.getEmail(session());
        if (Client.isBusinessClient(email)) {
            return ok(createOffer.render(form(Offer.class), Client.getClientByEmail(email)));
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result editOfferForm(Integer offerId) {
        String email = SessionManagement.getEmail(session());
        Offer offer = Offer.getNotExpiredById(offerId);
        if (Client.isBusinessClient(email) && offer != null && offer.getClientPublisher().getEmail().equals(email)) {
            Form<Offer> offerForm = form(Offer.class).fill(offer);
            return ok(createOffer.render(offerForm, Client.getClientByEmail(email)));
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result newOffer() {
        String email = SessionManagement.getEmail(session());
        if (Client.isBusinessClient(email)) {
            Form<Offer> offerForm = form(Offer.class).bindFromRequest();
            if (offerForm.hasErrors()) {
                return badRequest(createOffer.render(offerForm, Client.getClientByEmail(email)));
            }
            Map<String, String[]> map = request().body().asFormUrlEncoded();
            String[] checkedVal = map.get("rooms");
            LinkedList<OfferedRoom> offeredRooms = new LinkedList<>();
            Offer offer = offerForm.get();
            offer.setPremium(offerForm.data().get("premium") != null);
            offer.setVisitCount(0);
            offer.setClientPublisher(Client.getClientByEmail(email));
            boolean flag = true;
            for (String a : checkedVal) {
                if (a.matches("[0-9]+ [0-9]+")) {
                    Hotel hotel = Hotel.getById(Integer.parseInt(a.split(" ")[0]));
                    Room room = Room.getById(Integer.parseInt(a.split(" ")[1]));
                    if (hotel != null && room != null) {
                        offeredRooms.add(new OfferedRoom(hotel, room, offer));
                    } else {
                        flag = false;
                    }
                } else {
                    flag = false;

                }
            }
            if (flag) {
                offer.setOfferedRooms(offeredRooms);
                JPA.em().persist(offer);
                flash("success", "Offer added successfully");
                return redirect(routes.UserProfile.profile(Client.getClientId(email)));
            } else {
                flash("error", "Error");
                return redirect(routes.Application.index());
            }
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result editOffer(Integer offerId) {
        String email = SessionManagement.getEmail(session());
        Offer offer = Offer.getNotExpiredById(offerId);
        if (Client.isBusinessClient(email) && offer != null && offer.getClientPublisher().getEmail().equals(email)) {
            Form<Offer> offerForm = form(Offer.class).bindFromRequest();
            if (offerForm.hasErrors()) {
                return badRequest(createOffer.render(offerForm, Client.getClientByEmail(email)));
            }
            Map<String, String[]> map = request().body().asFormUrlEncoded();
            String[] checkedVal = map.get("rooms");
            LinkedList<OfferedRoom> offeredRooms = new LinkedList<>();
            Offer offerNew = offerForm.get();
            offerNew.setPremium(offerForm.data().get("premium") != null);
            offerNew.setVisitCount(0);
            offerNew.setClientPublisher(Client.getClientByEmail(email));
            boolean flag = true;
            for (String a : checkedVal) {
                if (a.matches("[0-9]+ [0-9]+")) {
                    Hotel hotel = Hotel.getById(Integer.parseInt(a.split(" ")[0]));
                    Room room = Room.getById(Integer.parseInt(a.split(" ")[1]));
                    if (hotel != null && room != null) {
                        offeredRooms.add(new OfferedRoom(hotel, room, offerNew));
                    } else {
                        flag = false;
                    }
                } else {
                    flag = false;

                }
            }
            if (flag) {
                offerNew.setOfferedRooms(offeredRooms);
                Date today = new Date();
                offer.setDateTo(today);
                if (offer.getDateFrom().after(today))
                    offer.setDateFrom(today);
                JPA.em().merge(offer);
                JPA.em().persist(offerNew);
                flash("success", "Offer updated successfully");
                return redirect(routes.UserProfile.profile(Client.getClientId(email)));
            } else {
                flash("error", "Error");
                return redirect(routes.Application.index());
            }
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result removeOffer(Integer offerId) {
        String email = SessionManagement.getEmail(session());
        Offer offer = Offer.getNotExpiredById(offerId);
        if (Client.isBusinessClient(email) && offer != null && offer.getClientPublisher().getEmail().equals(email)) {
            Date today = new Date();
            if (offer.getDateTo().before(today)) {
                flash("info", "Offer was inactive before!");
            } else {
                offer.setDateTo(today);
                if (offer.getDateFrom().after(today))
                    offer.setDateFrom(today);
                JPA.em().merge(offer);
                flash("success", "Offer deactivated successfully");
            }
            return redirect(routes.UserProfile.profile(Client.getClientId(email)));
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }
}
