package controllers;

import models.*;
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
import java.util.Date;
import java.util.List;

import static controllers.WebServiceCaller.*;
import static play.data.Form.form;

/**
 * Created by wukat on 19.06.15.
 */
public class Rooms extends Controller {

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result allInHotel(Integer hotelId) {
        String email = SessionManagement.getEmail(session());
        if (Client.isBusinessClient(email)) {
            Hotel hotel = Hotel.getById(hotelId);
            if (hotel != null && hotel.getClientPublisher().getEmail().equals(email)) {
                return ok(roomsView.render(hotel));
            }
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result newRoomForm(Integer hotelId) {
        String email = SessionManagement.getEmail(session());
        if (Client.isBusinessClient(email)) {
            Hotel hotel = Hotel.getById(hotelId);
            if (hotel != null && hotel.getClientPublisher().getEmail().equals(email)) {
                return ok(createRoom.render(form(Room.class), hotel));
            }
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result newRoom(Integer hotelId) {
        String email = SessionManagement.getEmail(session());
        if (Client.isBusinessClient(email)) {
            Hotel hotel = Hotel.getById(hotelId);
            if (hotel != null && hotel.getClientPublisher().getEmail().equals(email)) {
                Form<Room> roomForm = form(Room.class).bindFromRequest();
                if (roomForm.hasErrors()) {
                    return badRequest(createRoom.render(roomForm, hotel));
                }
                Room room = roomForm.get();
                room.setHotel(hotel);
                room.setHasImages(false);
                room.setBathroom(roomForm.field("bathroom").value() != null);
                JPA.em().persist(room);
                flash("info", "Upload images describing your room!");
                return redirect(routes.Rooms.uploadForm(hotelId, room.getRoomId()));
            }
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional(readOnly = true)
    public static Result uploadForm(Integer hotelId, Integer roomId) {
        Room room = Room.getById(roomId);
        String email = SessionManagement.getEmail(session());
        if (room != null && room.getHotel() != null && room.getHotel().getHotelId().equals(hotelId) && room.getHotel().getClientPublisher().getEmail().equals(email)) {
            return ok(imageUpload.render(hotelId, roomId));
        }
        flash("error", "Access denied.");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result upload(Integer hotelId, Integer roomId) {
        Room room = Room.getById(roomId);
        String email = SessionManagement.getEmail(session());
        if (room != null && room.getHotel() != null && room.getHotel().getHotelId().equals(hotelId) && room.getHotel().getClientPublisher().getEmail().equals(email)) {
            Http.MultipartFormData body = request().body().asMultipartFormData();
            List<Http.MultipartFormData.FilePart> files = body.getFiles();
            room.setHasImages(true);
            JPA.em().merge(room);
            for (Http.MultipartFormData.FilePart p : files) {
                Image img = new Image(room);
                img.setContent("");
                JPA.em().persist(img);
                try {
                    FileUtils.copyFile(p.getFile(), new File("public/images/products", img.getImageId().toString()));
                    img.setContent("/assets/images/products/" + img.getImageId().toString());
                    JPA.em().merge(img);
                } catch (IOException ioe) {
                    Logger.debug("sth wrong with image");
                    JPA.em().remove(img);
                }
            }
            flash("success", "Images uploaded successfully");
            return redirect(routes.Rooms.allInHotel(hotelId));
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result removeImage(Integer hotelId, Integer roomId, Integer imageId) {
        Room room = Room.getById(roomId);
        String email = SessionManagement.getEmail(session());
        if (room != null && room.getHotel() != null && room.getHotel().getHotelId().equals(hotelId) && room.getHotel().getClientPublisher().getEmail().equals(email)) {
            Image image = Image.getById(imageId);
            if (image.getRoom().getRoomId().equals(roomId)) {
                if (room.getImages().size() > 1) {
                    image.getRoom().getImages().remove(image);
                    JPA.em().remove(image);
                    flash("success", "Images removed successfully");
                    return ok();
                } else {
                    flash("error", "Room description have to contain at least one image!");
                    return redirect(routes.Rooms.roomImages(hotelId, roomId));
                }
            }
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

    @Security.Authenticated(Secured.class)
    @Transactional
    public static Result roomImages(Integer hotelId, Integer roomId) {
        Room room = Room.getById(roomId);
        String email = SessionManagement.getEmail(session());
        if (room != null && room.getHotel() != null && room.getHotel().getHotelId().equals(hotelId) && room.getHotel().getClientPublisher().getEmail().equals(email)) {
            return ok(roomImages.render(room.getImages(), room));
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

    @Transactional
    public static Result showOfferedRoom(Integer offerId, Integer hotelId, Integer roomId) {
        OfferedRoom offeredRoom = OfferedRoom.getByAllWithImages(offerId, hotelId, roomId);
        if (offeredRoom != null && !(offeredRoom.getOffer().getPremium() && !SessionManagement.isOk(session()))) {
            return ok(offeredRoomView.render(offeredRoom));
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

    @Transactional(readOnly = true)
    @Security.Authenticated(Secured.class)
    public static Result bookingForm(Integer offerId, Integer hotelId, Integer roomId) {
        List<Date> dates = getBookedDays(roomId, hotelId, offerId);
        if (dates.size() == 0) {
            if (flash("error") != null) {
                return redirect(routes.Application.index());
            }
        }
        return ok(createBooking.render(dates, form(Booking.class), OfferedRoom.getByAllWithImages(offerId, hotelId, roomId)));
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result bookRoom(Integer offerId, Integer hotelId, Integer roomId) {
        Form<Booking> form = form(Booking.class).bindFromRequest();
        Long response = null;
        if (form.hasErrors()) {
            return redirect(routes.Rooms.bookingForm(offerId, hotelId, roomId));
        }
        OfferedRoom offeredRoom = OfferedRoom.getByAllWithImages(offerId, hotelId, roomId);
        if (offeredRoom != null) {
            String endpoint = offeredRoom.getOffer().getClientPublisher().getClientData().getEndpoint();
            if (endpoint != null && offeredRoom.getHotel().getInternalHotelId() != null && offeredRoom.getRoom().getInternalRoomId() != null) {
                response = bookRoomRemote(offeredRoom, form.get());
                if (response == null && flash("error") != null) {
                    return redirect(routes.Application.index());
                }
            }
            Booking booking = form.get();
            if ((response != null && response != -1) && canBeBooked(offeredRoom, booking.getDateFrom(), booking.getDateTo())) {
                booking.setInternalBookingId(response);
                booking.setOfferedRoom(offeredRoom);
                booking.setCancelled(false);
                Client c = Client.getClientByEmail(SessionManagement.getEmail(session()));
                booking.setClient(c);
                JPA.em().persist(booking);
                flash("success", "Room booked");
                return redirect(routes.UserProfile.profile(c.getClientId()));
            } else {
                if (flash("error") == null)
                    flash("error", "Selected dates are already taken! Please try again");
                return redirect(routes.Rooms.bookingForm(offerId, hotelId, roomId));
            }
        } else {
            flash("error", "Access denied");
        }
        return redirect(routes.Application.index());
    }

    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result cancelBooking(Long bookingId) {
        Booking booking = Booking.getById(bookingId);
        if (booking == null) {
            flash("Access denied");
            return redirect(routes.Application.index());
        }
        if (booking.canBeCancelled()) {
            Boolean cancel = cancel(booking);
            if (!cancel) {
                return redirect(routes.UserProfile.profile(Client.getClientByEmail(SessionManagement.getEmail(session())).getClientId()));
            }
            booking.setCancelled(true);
            JPA.em().flush();
            flash("success", "Booking cancelled");
            return redirect(routes.UserProfile.profile(Client.getClientByEmail(SessionManagement.getEmail(session())).getClientId()));
        } else {
            flash("error", "Booking cannot be cancelled");
            return redirect(routes.UserProfile.profile(Client.getClientByEmail(SessionManagement.getEmail(session())).getClientId()));
        }
    }
}
