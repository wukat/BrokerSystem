package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * Created by magdalena on 01.06.15.
 */
//TODO opis tekstowy
@Entity
@Table(name="rooms")
public class Room {

    @Column(name = "room_id")
    private Integer roomId;

    @Id
    @GeneratedValue
    @Column(name = "internal_room_id")
    private Integer internalRoomId;

    @XmlTransient
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "beds_number")
    private Integer bedsNumber;

    @Column(name = "bathroom")
    private Boolean bathroom;

    @XmlTransient
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<OfferedRoom> offeredRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Image> images;


    public Room(){}

    public Room(Integer roomId, Hotel hotel, Integer bedsNumber, Boolean bathroom, List<OfferedRoom> offeredRooms, List<Image> images) {
        this.roomId = roomId;
        this.hotel = hotel;
        this.bedsNumber = bedsNumber;
        this.bathroom = bathroom;
        this.offeredRooms = offeredRooms;
        this.images = images;
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

    @XmlTransient
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

    @XmlTransient
    public List<OfferedRoom> getOfferedRooms() {
        return offeredRooms;
    }

    public void setOfferedRooms(List<OfferedRoom> offeredRooms) {
        this.offeredRooms = offeredRooms;
    }
}