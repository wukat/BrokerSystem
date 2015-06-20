package models;

import play.db.jpa.JPA;

import javax.persistence.*;
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

    @Column(name = "room_number")
    private Integer roomNumber;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "beds_number")
    private Integer bedsNumber;

    @Column(name = "bathroom")
    private Boolean bathroom;

    @Column(name = "hasImages")
    private Boolean hasImages;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<OfferedRoom> offeredRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Image> images;


    public Room(){}
    public Room(Integer internalRoomId, Hotel hotel, Integer bedsNumber, Boolean bathroom) {
        this.internalRoomId = internalRoomId;
        this.hotel = hotel;
        this.bedsNumber = bedsNumber;
        this.bathroom = bathroom;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Boolean getHasImages() {
        return hasImages;
    }

    public void setHasImages(Boolean hasImages) {
        this.hasImages = hasImages;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
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

    public static Room getById(Integer roomId) {
        return JPA.em().find(Room.class, roomId);
    }
}