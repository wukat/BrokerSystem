package controllers;

import models.Offer;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.notFound;
import views.html.offerView;
import views.html.offersView;

import java.util.LinkedList;

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

    @Transactional(readOnly = true)
    public static Result seeOffer(Integer offerId) { // TODO show offer even if it's expired
        Offer offer = Offer.getOfferById(offerId);
        if (offer == null) {
            return ok(notFound.render("Wrong offer id"));
        }
        if (offer.getPremium() && !session().containsKey("email")) {
            flash("info", "Log in to access premium offers.");
            return redirect(routes.Authentication.login());
        }
        return ok(offerView.render(offer));
    }
}
