package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.List;

/**
 * Created by magdalena on 01.06.15.
 */
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

    @Column(name = "hasImages")
    private Boolean hasImages;

    @Column(name = "room_number")
    private Integer roomNumber;

    @XmlTransient
    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<OfferedRoom> offeredRooms;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private List<Image> images;

    @XmlTransient
    @OneToMany(mappedBy="room", cascade= CascadeType.ALL)
    private List<InnerBooking> innerBooking;

    public Room(){}

    public Room(Integer roomId, Hotel hotel, Integer bedsNumber, Boolean bathroom, List<OfferedRoom> offeredRooms, List<Image> images) {
        this.roomId = roomId;
        this.hotel = hotel;
        this.bedsNumber = bedsNumber;
        this.bathroom = bathroom;
        this.offeredRooms = offeredRooms;
        this.images = images;
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

    @XmlTransient
    public List<InnerBooking> getInnerBooking() {
        return innerBooking;
    }

    public void setInnerBooking(List<InnerBooking> innerBooking) {
        this.innerBooking = innerBooking;
    }
}