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
import java.util.List;

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
            return redirect(routes.Rooms.newRoomForm(hotelId));
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
        OfferedRoom offeredRoom = OfferedRoom.getByAll(offerId, hotelId, roomId);
        if (offeredRoom != null && !(offeredRoom.getOffer().getPremium() && !SessionManagement.isOk(session()))) {
            return ok(offeredRoomView.render(offeredRoom));
        }
        flash("error", "Access denied");
        return redirect(routes.Application.index());
    }

}
