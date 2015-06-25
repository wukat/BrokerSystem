package controllers;

import models.Booking;
import models.Client;
import models.Offer;
import models.OfferedRoom;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import play.Logger;
import play.db.jpa.Transactional;
import play.libs.WS;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wukat on 22.06.15.
 */
public class WebServiceCaller extends Controller {
    private static final Long dayInMiliSecs = 86400000L;

    private static String createXML(String invocation, String ns) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soap:Envelope  xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" " +
                "xmlns:ns=\"" + ns + "\" >" +
                "<soap:Body>" +
                invocation +
                "</soap:Body>" +
                "</soap:Envelope>";
    }

    private static String getXml(String invocation, String ns) {
        String response = null;
        try {
            response = WS.url(ns + "WS").setHeader("content-type", "application/soap+xml").post(createXML(invocation, ns)).get(1000).getBody();
        } catch (Exception a) {
            Logger.error("Connection refused");
        }
        return response;
    }


    @Transactional
    @Security.Authenticated(Secured.class)
    public static Result synchronize() {
        String email = SessionManagement.getEmail(session());
        Client client = Client.getClientByEmail(email);
        if (Client.isBusinessClient(email) && client.getClientData() != null) {
            if (client.getClientData().getEndpoint() != null) {
                String xml = getXml("<ns:getOffers></ns:getOffers>", client.getClientData().getEndpoint());
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

    public static LinkedList<Date> getBookedDays(Integer roomId, Integer hotelId, Integer offerId) {
        LinkedList<Date> dates = new LinkedList<>();
        OfferedRoom offeredRoom = OfferedRoom.getByAllWithImages(offerId, hotelId, roomId);
        if (offeredRoom != null) {
            String endpoint = offeredRoom.getOffer().getClientPublisher().getClientData().getEndpoint();
            if (endpoint != null && offeredRoom.getHotel().getInternalHotelId() != null && offeredRoom.getRoom().getInternalRoomId() != null) {
                String xml = getXml("<ns:getBookedDays><arg0>" + offeredRoom.getRoom().getInternalRoomId() + "</arg0><arg1>" + offeredRoom.getHotel().getInternalHotelId() + "</arg1>" +
                        "</ns:getBookedDays>", endpoint);
                if (xml == null) {
                    flash("error", "Connection refused");
                } else {
                    dates = getBookedDaysList(xml);
                }
            } else {
                dates = getBookedDaysList(offeredRoom);
            }
        } else {
            flash("error", "Access denied");
        }
        return dates;
    }


    private static LinkedList<Date> getBookedDaysList(OfferedRoom offeredRoom) {
        List<Booking> bookings = offeredRoom.getBookings();
        LinkedList<Date> booked = new LinkedList<>();
        Date today = new Date();
        today.setTime(today.getTime() - dayInMiliSecs);
        Date twoMonthsLater = new Date();
        twoMonthsLater.setTime(twoMonthsLater.getTime() + 61 * dayInMiliSecs);
        for (Booking booking : bookings) {
            Date begin = booking.getDateFrom();
            Date end = booking.getDateTo();
            end.setTime(end.getTime() + dayInMiliSecs);
            while (begin.before(booking.getDateTo())) {
                if (begin.after(today) && begin.before(twoMonthsLater)) {
                    Date newDate = new Date();
                    newDate.setTime(begin.getTime());
                    booked.add(newDate);
                }
                begin.setTime(begin.getTime() + dayInMiliSecs);
            }
        }
        return booked;
    }

    private static LinkedList<Date> getBookedDaysList(String xml) {
        LinkedList<Date> booked = new LinkedList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;

        try {
            builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(xml)));
            Node a = document.getDocumentElement().getElementsByTagName("return").item(0);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < a.getParentNode().getChildNodes().getLength(); i++) {
                booked.add(df.parse(a.getParentNode().getChildNodes().item(i).getFirstChild().getNodeValue().split("T")[0]));
            }
        } catch (Exception e) {
            Logger.debug("Empty list? GetBookedDaysList");
            return new LinkedList<>();
        }
        return booked;
    }

    public static Long bookRoomRemote(OfferedRoom offeredRoom, Booking booking) {
        Long resp = null;
        String endpoint = offeredRoom.getOffer().getClientPublisher().getClientData().getEndpoint();
        String xml = getXml("<ns:bookRoom><arg0>" + offeredRoom.getOffer().getOfferId() + "</arg0><arg1>" + offeredRoom.getRoom().getInternalRoomId() + "</arg1><arg2>" + offeredRoom.getHotel().getInternalHotelId() + "</arg2>" +
                "<arg3>" + new SimpleDateFormat("yyyy-MM-dd").format(booking.getDateFrom()) + "</arg3><arg4>" + new SimpleDateFormat("yyyy-MM-dd").format(booking.getDateTo()) + "</arg4></ns:bookRoom>", endpoint);
        if (xml == null) {
            flash("error", "Connection refused");
        } else {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder;

            try {
                builder = factory.newDocumentBuilder();
                Document document = builder.parse(new InputSource(new StringReader(xml)));

                Node a = document.getDocumentElement().getElementsByTagName("return").item(0);
                resp = Long.parseLong(a.getChildNodes().item(0).getNodeValue());
            } catch (Exception e) {
                System.out.println("Sorry, something went wrong");
                e.printStackTrace();
            }
        }
        return resp;
    }

    public static boolean canBeBooked(OfferedRoom offeredRoom, Date from, Date to) {
        LinkedList<Date> booked = getBookedDaysList(offeredRoom);
        for (Date day : booked) {
            if (day.before(to) && day.after(from)) {
                return false;
            }
        }
        return true;
    }

    public static Boolean cancel(Booking booking) {
        Boolean resp = true;
        OfferedRoom offeredRoom = booking.getOfferedRoom();
        String endpoint = offeredRoom.getOffer().getClientPublisher().getClientData().getEndpoint();
        if (endpoint != null && booking.getInternalBookingId() != null) {
            String xml = getXml("<ns:cancelBooking><arg0>" + booking.getInternalBookingId() + "</arg0></ns:cancelBooking>", endpoint);
            if (xml == null) {
                flash("error", "Connection refused");
                resp = false;
            }
        }
        return resp;
    }
}
