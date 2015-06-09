package controllers;

import models.Client;
import models.Image;
import models.Offer;
import models.Role;
import org.apache.commons.io.FileUtils;
import play.Logger;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import views.html.*;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static play.data.Form.form;

/**
 * Created by wukat on 07.06.15.
 */
public class Offers extends Controller {

    @Transactional(readOnly = true)
    public static Result all() {
        LinkedList<Offer> offers;
        if (SessionManagement.isOk(session())) {
            offers = Offer.getAllActual();
        } else {
            offers = Offer.getNonPremiumActual();
        }
        return ok(offersView.render(offers));
    }

    @Transactional
    public static Result seeOffer(Integer offerId) { // TODO show offer even if it's expired
        Offer offer = Offer.getOfferById(offerId);
        if (offer == null) {
            return ok(notFound.render("Wrong offer id"));
        }
        if (offer.getPremium() && !SessionManagement.isOk(session())) {
            flash("info", "Log in to access premium offers.");
            return redirect(routes.Authentication.login());
        }
        offer.setVisitCount(offer.getVisitCount() + 1);
        JPA.em().flush();
        return ok(offerView.render(offer));
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result newOfferForm() {
        Role clientRole = Client.getClientRole(SessionManagement.getEmail(session()));
        if (clientRole != null && clientRole.getRole().equals("business")) {
            return ok(createOffer.render(form(Offer.class)));
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result newOffer() {
        Form<Offer> offerForm = form(Offer.class).bindFromRequest();
        if (offerForm.hasErrors()) {
            return badRequest(createOffer.render(offerForm));
        }
        String email = SessionManagement.getEmail(session());
        Role clientRole = Client.getClientRole(email);
        if (clientRole != null && clientRole.getRole().equals("business")) {
            Offer offer = offerForm.get();
            offer.setPremium(offerForm.data().get("premium") != null);
            offer.setVisitCount(0);
            offer.setHasImages(false);
            offer.setClientPublisher(Client.getClientByEmail(email));
            JPA.em().persist(offer);
            flash("info", "Upload photos to save offer");
            return redirect(routes.Offers.upload(offer.getOfferId()));
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result uploadForm(Integer offerId) {
        Offer offer = Offer.getOfferById(offerId);
        String email = SessionManagement.getEmail(session());
        if (offer != null && offer.getClientPublisher().getEmail().equals(email)) {
            return ok(imageUpload.render(offerId));
        }
        flash("error", "You are not allowed to upload images for this offer");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result upload(Integer offerId) {
        Offer offer = Offer.getOfferById(offerId);
        String email = SessionManagement.getEmail(session());
        if (offer != null && offer.getClientPublisher().getEmail().equals(email)) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            List<Http.MultipartFormData.FilePart> file = body.getFiles();
            Offer.activateOffer(offerId);
            for (Http.MultipartFormData.FilePart p : file) {
                Image img = new Image();
                img.setOffer(offer);
                img.setName("");
                JPA.em().persist(img);
                try {
                    FileUtils.copyFile(p.getFile(), new File("public/images/products", img.getImageId().toString()));
                } catch (IOException ioe) {
                    Logger.debug("sth wrong with image");
                }
            }
            return ok();
        }
        flash("error", "You are not allowed to upload images for this offer");
        return redirect(routes.Application.index());
    }
}
