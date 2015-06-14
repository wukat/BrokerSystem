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
@Table(name="rooms")
public class Room {

    @Id
    @GeneratedValue
    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "internal_room_id")
    private Integer internalRoomId;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "beds_number")
    private Integer bedsNumber;

    @Column(name = "bathroom")
    private Boolean bathroom;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<OfferedRoom> offeredRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Image> images;


    public Room(){}
    public Room(Integer roomId, Hotel hotel, Integer bedsNumber, Boolean bathroom, List<OfferedRoom> offeredRooms) {
        this.roomId = roomId;
        this.hotel = hotel;
        this.bedsNumber = bedsNumber;
        this.bathroom = bathroom;
        this.offeredRooms = offeredRooms;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getInternalRoomId() {
        return internalRoomId;
    }

    public void setInternalRoomId(Integer internalRoomId) {
        this.internalRoomId = internalRoomId;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public Integer getBedsNumber() {
        return bedsNumber;
    }

    public void setBedsNumber(Integer bedsNumber) {
        this.bedsNumber = bedsNumber;
    }

    public Boolean getBathroom() {
        return bathroom;
    }

    public void setBathroom(Boolean bathroom) {
        this.bathroom = bathroom;
    }

    public List<OfferedRoom> getOfferedRooms() {
        return offeredRooms;
    }

    public void setOfferedRooms(List<OfferedRoom> offeredRooms) {
        this.offeredRooms = offeredRooms;
    }
}