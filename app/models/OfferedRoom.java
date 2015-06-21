package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by magdalena on 01.06.15.
 */
//TODO opis tekstowy
@Entity
@Table(name="offered_rooms")
public class OfferedRoom implements Comparable<OfferedRoom> {

    @Id
    @GeneratedValue
    @Column(name = "offered_room_id")
    private Integer offeredRoomId;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

    @OneToMany(mappedBy = "offeredRoom", cascade = CascadeType.ALL)
    private List<Booking> bookings;


    public OfferedRoom(){}
    public OfferedRoom(Hotel hotel, Room room, Offer offer) {
        this.hotel = hotel;
        this.room = room;
        this.offer = offer;
    }

    public Integer getOfferedRoomId() {
        return offeredRoomId;
    }

    public void setOfferedRoomId(Integer offeredRoomId) {
        this.offeredRoomId = offeredRoomId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public static LinkedList<OfferedRoom> getAllActualDistinct() {
        LinkedList<OfferedRoom> result = new LinkedList<>();
        for (Object el : JPA.em().createNativeQuery("SELECT DISTINCT ON (hotel_id, offer_id) * FROM offered_rooms NATURAL JOIN offers WHERE date_to > CURRENT_DATE", OfferedRoom.class).getResultList()) {
        //createQuery("SELECT distinct o.offer, o.hotel, o FROM OfferedRoom o WHERE o.offer.dateTo > current_date").getResultList()) {
            result.add((OfferedRoom) el);
        }
        return result;
    }

    public static LinkedList<OfferedRoom> getNonPremiumActualDistinct() {
        LinkedList<OfferedRoom> result = new LinkedList<>();
        for (Object el : JPA.em().createNativeQuery("SELECT DISTINCT ON (hotel_id, offer_id) * FROM offered_rooms NATURAL JOIN offers WHERE premium=false AND date_to > CURRENT_DATE", OfferedRoom.class).getResultList()) {
        // createQuery("SELECT distinct o.offer, o.hotel, o FROM OfferedRoom o WHERE o.offer.premium=false AND o.offer.dateTo > current_date").getResultList()) {
            result.add((OfferedRoom) el);
        }
        return result;
    }

    public static OfferedRoom getById(Integer offerId) {
        return JPA.em().find(OfferedRoom.class, offerId);
    }

    public static List<OfferedRoom> getByHotelAndOffer(Integer offerId, Integer hotelId) {
        return JPA.em().createQuery("FROM OfferedRoom WHERE offer.keyOfferId =:offerId AND hotel.hotelId =:hotelId AND room.hasImages = true", OfferedRoom.class).setParameter("offerId", offerId).setParameter("hotelId", hotelId).getResultList();
    }

    public static OfferedRoom getByAll(Integer offerId, Integer hotelId, Integer roomId) {
        List<OfferedRoom> offeredRoomList = JPA.em().createQuery("FROM OfferedRoom WHERE offer.keyOfferId =:offerId AND hotel.hotelId =:hotelId AND room.roomId =:roomId AND room.hasImages = true", OfferedRoom.class).setParameter("offerId", offerId).setParameter("hotelId", hotelId).setParameter("roomId", roomId).getResultList();
        if (offeredRoomList.size() == 1) {
            return offeredRoomList.get(0);
        }
        return null;
    }

    @Override
    public int compareTo(OfferedRoom offeredRoom) {
        if (offeredRoom == null) {
            return -1;
        }
        return this.getOffer().compareTo(offeredRoom.getOffer());
    }
}

