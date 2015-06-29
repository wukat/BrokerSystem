package logic;

import com.lowagie.text.DocumentException;
import models.*;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.resource.XMLResource;
import play.db.jpa.JPA;
import views.html.offerToPdf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by wukat on 23.06.15.
 */
public class OffersLogic {
    public static void addViewed(Offer offer) {
        offer.setVisitCount(offer.getVisitCount() + 1);
        JPA.em().flush();
    }

    public static File toPdf(List<OfferedRoom> offeredRoomList, Offer offer, Hotel hotel) {
        String fileNameWithPath = "Offer-o-" + offer.getKeyOfferId() + "-h-" + hotel.getHotelId() + ".pdf";
        File f = new File(fileNameWithPath);
        try {
            Document document = XMLResource.load(new ByteArrayInputStream(offerToPdf.render(offeredRoomList, offer, hotel).body().getBytes())).getDocument();
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocument(document, null);
            renderer.layout();
            FileOutputStream fos = new FileOutputStream(fileNameWithPath);
            renderer.createPDF(fos);
            fos.close();
            return f;
        } catch (DocumentException | IOException e) {
            System.out.println(e);
            return null;
        }
    }

    public static boolean isClientsOffer(Offer offer, String email) {
        return offer.getClientPublisher().getEmail().equals(email);
    }

    private static boolean insertOfferedRooms(List<OfferedRoom> offeredRoomList, String[] values, Offer offer) {
        for (String a : values) {
            if (a.matches("[0-9]+ [0-9]+")) {
                Hotel hotel = Hotel.getById(Integer.parseInt(a.split(" ")[0]));
                Room room = Room.getById(Integer.parseInt(a.split(" ")[1]));
                if (hotel != null && room != null) {
                    offeredRoomList.add(new OfferedRoom(hotel, room, offer));
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }
        return true;
    }

    public static boolean newOffer(Offer offer, Map<String, String[]> values, Client client) {
        String[] checkedVal = values.get("rooms");
        LinkedList<OfferedRoom> offeredRooms = new LinkedList<>();
        offer.setVisitCount(0);
        offer.setClientPublisher(client);
        if (insertOfferedRooms(offeredRooms, checkedVal, offer)) {
            offer.setOfferedRooms(offeredRooms);
            JPA.em().persist(offer);
            return true;
        }
        return false;
    }

    public static boolean editOffer(Offer offer, Offer offerOld, Map<String, String[]> values, Client client) {
        String[] checkedVal = values.get("rooms");
        LinkedList<OfferedRoom> offeredRooms = new LinkedList<>();
        offer.setVisitCount(0);
        offer.setClientPublisher(client);
        if (insertOfferedRooms(offeredRooms, checkedVal, offer)) {
            offer.setOfferedRooms(offeredRooms);
            Date today = new Date();
            offerOld.setDateTo(today);
            if (offerOld.getDateFrom().after(today))
                offerOld.setDateFrom(today);
            JPA.em().merge(offerOld);
            JPA.em().persist(offer);
            return true;
        }
        return false;
    }

    public static boolean deactivate(Offer offer) {
        Date today = new Date();
        if (offer.getDateTo().before(today)) {
            return false;
        } else {
            offer.setDateTo(today);
            if (offer.getDateFrom().after(today))
                offer.setDateFrom(today);
            JPA.em().merge(offer);
            return true;
        }
    }
}
