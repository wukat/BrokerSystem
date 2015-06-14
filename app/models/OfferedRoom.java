package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by magdalena on 01.06.15.
 */
//TODO opis tekstowy
@Entity
@Table(name="offered_rooms")
public class OfferedRoom {

    @Id
    @GeneratedValue
    @Column(name = "offered_room_id")
    private Integer keyRoomId;

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

    public Integer getKeyRoomId() {
        return keyRoomId;
    }

    public void setKeyRoomId(Integer keyRoomId) {
        this.keyRoomId = keyRoomId;
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
}

