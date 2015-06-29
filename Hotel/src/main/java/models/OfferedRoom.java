package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;

/**
 * Created by magdalena on 01.06.15.
 */
@Entity
@Table(name="offered_rooms")
public class OfferedRoom {

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

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private Offer offer;

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

    @XmlTransient
    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}

