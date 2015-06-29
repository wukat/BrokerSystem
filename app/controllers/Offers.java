package controllers;

import logic.ClientLogic;
import logic.OffersLogic;
import logic.SessionManagement;
import logic.WebServiceCaller;
import models.*;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.io.File;
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
            offers = OfferedRoom.getAllActualDistinct();
        } else {
            offers = OfferedRoom.getNonPremiumActualDistinct();
        }
        return ok(offersView.render(offers));
    }

    @Transactional
    public static Result seeOffer(Integer offerId, Integer hotelId) {
        Offer offer = Offer.getNotExpiredById(offerId);
        final Hotel hotel = Hotel.getById(hotelId);
        if (offer == null || hotel == null) {
            return ok(notFound.render("Wrong offer id"));
        }
        if (offer.getPremium() && !SessionManagement.isOk(session())) {
            flash("url", "GET".equals(request().method()) ? request().uri() : "/");
            flash("info", "Log in to access premium offers.");
            return redirect(routes.Authentication.login());
        }
        OffersLogic.addViewed(offer);
        if (offer.getDateTo().before(new Date())) {
            flash("info", "Offer has expired.");
        }
        return ok(offerView.render(OfferedRoom.getByHotelAndOfferWithImages(offerId, hotelId), offer, hotel));
    }

    @Transactional(readOnly = true)
    public static Result offerToPdf(Integer offerId, Integer hotelId) {
        final Offer offer = Offer.getNotExpiredById(offerId);
        final Hotel hotel = Hotel.getById(hotelId);
        if (offer == null || hotel == null) {
            flash("error", "Access denied");
            return redirect(routes.Application.index());
        }
        if (offer.getPremium() && !SessionManagement.isOk(session())) {
            flash("url", "GET".equals(request().method()) ? request().uri() : "/");
            flash("info", "Log in to access premium offers.");
            return redirect(routes.Authentication.login());
        }
        final List<OfferedRoom> offeredRoomList = OfferedRoom.getByHotelAndOfferWithImages(offerId, hotelId);
        File f = OffersLogic.toPdf(offeredRoomList, offer, hotel);
        if (f == null) {
            flash("error", "Sorry, an error occurred. Please, try again!");
            return ok(offerView.render(offeredRoomList, offer, hotel));
        }
        return ok(f);
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result seeOfferDetails(Integer offerId) {
        Offer offer = Offer.getNotExpiredById(offerId);
        if (offer == null) {
            return ok(notFound.render("Wrong offer id"));
        }
        if (OffersLogic.isClientsOffer(offer, SessionManagement.getEmail(session()))) {
            return ok(offerDetailsView.render(offer));
        }
        flash("info", "Access denied");
        return redirect(routes.Authentication.login());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result newOfferForm() {
        String email = SessionManagement.getEmail(session());
        if (ClientLogic.isBusinessClient(email)) {
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
        if (ClientLogic.isBusinessClient(email) && offer != null && OffersLogic.isClientsOffer(offer, email)) {
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
        if (ClientLogic.isBusinessClient(email)) {
            Form<Offer> offerForm = form(Offer.class).bindFromRequest();
            if (offerForm.hasErrors()) {
                return badRequest(createOffer.render(offerForm, Client.getClientByEmail(email)));
            }
            Map<String, String[]> map = request().body().asFormUrlEncoded();
            Offer offer = offerForm.get();
            offer.setPremium(offerForm.data().get("premium") != null);
            if (OffersLogic.newOffer(offer, map, Client.getClientByEmail(SessionManagement.getEmail(session())))) {
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
        if (ClientLogic.isBusinessClient(email) && offer != null && OffersLogic.isClientsOffer(offer, email)) {
            Form<Offer> offerForm = form(Offer.class).bindFromRequest();
            if (offerForm.hasErrors()) {
                return badRequest(createOffer.render(offerForm, Client.getClientByEmail(email)));
            }
            Map<String, String[]> map = request().body().asFormUrlEncoded();
            Offer offerNew = offerForm.get();
            offerNew.setPremium(offerForm.data().get("premium") != null);
            if (OffersLogic.editOffer(offerNew, offer, map, Client.getClientByEmail(SessionManagement.getEmail(session())))) {
                flash("success", "Offer updated successfully");
                return redirect(routes.UserProfile.profile(Client.getClientId(email)));
            }
            flash("error", "Error");
            return redirect(routes.Application.index());
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result removeOffer(Integer offerId) {
        String email = SessionManagement.getEmail(session());
        Offer offer = Offer.getNotExpiredById(offerId);
        if (ClientLogic.isBusinessClient(email) && offer != null && OffersLogic.isClientsOffer(offer, email)) {
            if (OffersLogic.deactivate(offer)) {
                flash("info", "Offer was inactive before!");
            } else {
                flash("success", "Offer deactivated successfully");
            }
            return redirect(routes.UserProfile.profile(Client.getClientId(email)));
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result synchronize() {
        String email = SessionManagement.getEmail(session());
        Client client = Client.getClientByEmail(email);
        if (ClientLogic.isBusinessClient(email) && client.getClientData() != null) {
            if (client.getClientData().getEndpoint() != null) {
                String xml = WebServiceCaller.getXml("<ns:getOffers></ns:getOffers>", client.getClientData().getEndpoint());
                if (xml == null) {
                    flash("error", "Connection refused");
                }
                LinkedList<Offer> offers = Offer.getOffersFromXml(xml);
                if (offers.size() == 0) {
                    flash("info", "Nothing to update");
                } else {
                    if (Offer.updateOrSave(offers, client)) {
                        flash("success", "Offers updated");
                    } else {
                        flash("info", "No changes");
                    }
                }
                return redirect(routes.UserProfile.profile(client.getClientId()));
            } else {
                flash("error", "Endpoint not defined");
            }
        } else {
            flash("error", "Access denied");
        }
        return redirect(routes.Application.index());
    }
}
