package logic;

import models.*;
import org.apache.commons.io.FileUtils;
import play.Logger;
import play.db.jpa.JPA;
import play.mvc.Http;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static logic.WebServiceCaller.canBeBookedRemote;

/**
 * Created by wukat on 23.06.15.
 */
public class RoomsLogic {

    public static void newRoom(Room room, Hotel hotel, boolean bathroom) {
        room.setHotel(hotel);
        room.setHasImages(false);
        room.setBathroom(bathroom);
        JPA.em().persist(room);
    }

    public static void uploadImages(Room room, List<Http.MultipartFormData.FilePart> files) {
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
    }

    public static boolean removeImage(Image image, Room room) {
        if (room.getImages().size() > 1) {
            image.getRoom().getImages().remove(image);
            JPA.em().remove(image);
            return true;
        }
        return false;
    }

    public static boolean bookRoom(OfferedRoom offeredRoom, Booking booking, Client client, Long response) {
        if ((response != null && response != -1) && canBeBookedRemote(offeredRoom, booking.getDateFrom(), booking.getDateTo())) {
            booking.setInternalBookingId(response);
            booking.setOfferedRoom(offeredRoom);
            booking.setCancelled(false);
            booking.setClient(client);
            JPA.em().persist(booking);
            return  true;
        }
        return false;
    }
}
