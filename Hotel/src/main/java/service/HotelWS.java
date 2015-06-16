package service;

import models.*;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by wukat on 15.06.15.
 */
@WebService(name = "Hotel", targetNamespace = "http://localhost:8080/Hotel_war_exploded/Hotel")
public class HotelWS {

    private static final Long dayInMiliSecs = 86400000L;
    private static SessionFactory ourSessionFactory;

    public static Session getSession() throws HibernateException {
        return ourSessionFactory.openSession();
    }

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            ourSessionFactory = configuration.buildSessionFactory(serviceRegistry);
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static final Session session = getSession();

    @WebMethod
    public String daysToHolidays(String a) {
        return a + "leń";
    }

    @WebMethod
    public LinkedList<Offer> getOffers() {
        Session s = session;
        LinkedList<Offer> o = new LinkedList<>();
        for (Object e : s.createQuery("FROM Offer").list()) {
            o.add((Offer) e);
        }
        return o;
    }

    private LinkedList<Date> getBookedDaysList(Integer roomId, Integer hotelId) {
        Session s = getSession();
        List bookings = s.createQuery("FROM InnerBooking WHERE hotel.internalHotelId = :hotelId AND room.internalRoomId = :roomId").setInteger("roomId", roomId).setInteger("hotelId", hotelId).list();
        LinkedList<Date> booked = new LinkedList<>();
        Date today = new Date();
        today.setTime(today.getTime() - dayInMiliSecs);
        Date twoMonthsLater = new Date();
        twoMonthsLater.setTime(twoMonthsLater.getTime() + 61 * dayInMiliSecs);
        for (Object innerBookingObj : bookings) {
            InnerBooking innerBooking = (InnerBooking) innerBookingObj;
            Date begin = innerBooking.getDateFrom();
            Date end = innerBooking.getDateTo();
            end.setTime(end.getTime() + dayInMiliSecs);
            while (begin.before(innerBooking.getDateTo())) {
                if (begin.after(today) && begin.before(twoMonthsLater)) {
                    Date newDate = new Date();
                    newDate.setTime(begin.getTime());
                    booked.add(newDate);
                }
                begin.setTime(begin.getTime() + dayInMiliSecs);
            }
        }
        s.close();
        return booked;
    }

    @WebMethod
    public LinkedList<Date> getBookedDays(Integer roomId, Integer hotelId) {
        return getBookedDaysList(roomId, hotelId);
    }

    @WebMethod
    public Long bookRoom(Integer offerId, Integer roomId, Integer hotelId, Date from, Date to) {
        synchronized (HotelWS.class) {
            LinkedList<Date> booked = getBookedDaysList(roomId, hotelId);
            for (Date day : booked) {
                if (day.before(to) && day.after(from)) {
                    return -1L;
                }
            }
            Session s = getSession();
            List rooms = s.createQuery("FROM OfferedRoom WHERE hotel.internalHotelId = :hotelId AND room.internalRoomId = :roomId AND offer.offerId = :offerId").setInteger("offerId", offerId).setInteger("roomId", roomId).setInteger("hotelId", hotelId).list();
            if (rooms.size() != 1) {
                s.close();
                return -1L;
            } else {
                InnerBooking innerBooking = new InnerBooking(null, from, to, ((OfferedRoom) rooms.get(0)).getHotel(), ((OfferedRoom) rooms.get(0)).getRoom());
                Transaction t = s.beginTransaction();
                s.persist(innerBooking);
                t.commit();
                s.close();
                return innerBooking.getInternalBookingId();
            }
        }
    }

    @WebMethod
    public void cancelBooking(Long internalBookingId) {
        Session s = getSession();
        List bookings = s.createQuery("FROM InnerBooking WHERE internalBookingId = :bookingId").setLong("bookingId", internalBookingId).list();
        if (bookings.size() == 1) {
            Transaction t = s.beginTransaction();
            s.delete(bookings.get(0));
            t.commit();
        }
        s.close();
    }

    // faktura

    public static void main(String[] args) {
        Offer testOffer1 = new Offer(null, null, new Date(), new Date(), 25.0, "calkiem ladnie ale zimno", true, 0, null);

        Hotel testHotel1 = new Hotel(1, null, "Pod Kasztanem", "Kolobrzegi", "Deszczowo 234", 5);

        Room testRoom1 = new Room(1, testHotel1, 3, true, null, null);
        Room testRoom2 = new Room(2, testHotel1, 2, true, null, null);
        Room testRoom3 = new Room(3, testHotel1, 2, false, null, null);

        Image testImage1 = new Image(testRoom1);
        Image testImage2 = new Image(testRoom1);
        Image testImage3 = new Image(testRoom2);
        Image testImage4 = new Image(testRoom3);

        OfferedRoom testOfferedRoom1 = new OfferedRoom(testHotel1, testRoom1, testOffer1);
        OfferedRoom testOfferedRoom2 = new OfferedRoom(testHotel1, testRoom2, testOffer1);
        OfferedRoom testOfferedRoom3 = new OfferedRoom(testHotel1, testRoom3, testOffer1);

        InnerBooking testInnerBooking1 = new InnerBooking(null, new Date(), new Date(), testOfferedRoom3.getHotel(), testOfferedRoom3.getRoom());

        Session s = getSession();
        Transaction t = s.beginTransaction();
        s.persist(testOffer1);
        s.persist(testRoom1);
        s.persist(testRoom2);
        s.persist(testRoom3);
        s.persist(testImage1);
        s.persist(testImage2);
        s.persist(testImage3);
        s.persist(testImage4);
        s.persist(testOfferedRoom1);
        s.persist(testOfferedRoom2);
        s.persist(testOfferedRoom3);
        s.persist(testInnerBooking1);
        s.persist(testHotel1);
        t.commit();
        s.close();
//        LinkedList<Offer> o = new LinkedList<>();
//        Session s = getSession();
//        Transaction t = s.beginTransaction();
//        for (Object e : s.createQuery("FROM Offer").list()) {
//            o.add((Offer) e);
//        }
//        t.commit();
//        System.out.println(o.size());
//        for (Offer e : o) {
//            System.out.println(e.getOfferedRooms().get(0));
//        }
    }
}
