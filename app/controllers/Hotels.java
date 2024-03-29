package controllers;

import logic.ClientLogic;
import logic.HotelsLogic;
import logic.SessionManagement;
import models.Client;
import models.Hotel;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import views.html.createHotel;
import views.html.hotelsView;

import static play.data.Form.form;

/**
 * Created by wukat on 19.06.15.
 */
public class Hotels extends Controller {

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result newHotelForm() {
        if (ClientLogic.isBusinessClient(SessionManagement.getEmail(session()))) {
            return ok(createHotel.render(form(Hotel.class)));
        }
        flash("error", "You are not allowed to add hotels.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result newHotel() {
        Form<Hotel> hotelForm = form(Hotel.class).bindFromRequest();
        if (hotelForm.hasErrors()) {
            return badRequest(createHotel.render(hotelForm));
        }
        if (HotelsLogic.newHotel(hotelForm.get(), SessionManagement.getEmail(session()))) {
            flash("info", "New hotel added successfully");
            return redirect(routes.Hotels.all());
        }
        flash("error", "You are not allowed to create new offers.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result editHotelForm(Integer hotelId) {
        String email = SessionManagement.getEmail(session());
        if (ClientLogic.isBusinessClient(email)) {
            Hotel hotel = Hotel.getById(hotelId);
            if (hotel != null && hotel.getClientPublisher().getEmail().equals(email)) {
                Form<Hotel> hotelForm = form(Hotel.class).fill(hotel);
                return ok(createHotel.render(hotelForm));
            }
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result editHotel(Integer hotelId) {
        Form<Hotel> hotelForm = form(Hotel.class).bindFromRequest();
        if (hotelForm.hasErrors()) {
            return badRequest(createHotel.render(hotelForm));
        }
        if (HotelsLogic.editHotel(hotelId, hotelForm.get(), SessionManagement.getEmail(session()))) {
            flash("info", "Hotel saved successfully");
            return redirect(routes.Hotels.all());
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result all() {
        String email = SessionManagement.getEmail(session());
        if (ClientLogic.isBusinessClient(email)) {
            Client client = Client.getClientByEmail(email);
            return ok(hotelsView.render(client.getHotelsPublished()));
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }
}
