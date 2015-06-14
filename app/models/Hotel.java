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
@Table(name="hotels")
public class Hotel {

    @Id
    @GeneratedValue
    @Column(name="hotel_id")
    private Integer keyHotelId;

    @Column(name="internal_hotel_id")
    private Integer hotelId;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client clientPublisher;

    @Column(name="name")
    private String name;

    @Column(name="city")
    private String city;

    @Column(name="address")
    private String address;

    @Column(name="standard")
    private Integer standard;

    @OneToMany(mappedBy="hotel", cascade=CascadeType.ALL)
    private List<Room> rooms;


    public Hotel(){}
    public Hotel(Integer hotelId, Client clientPublisher, String name, String city, String address, Integer standard, List<Room> rooms) {
        this.hotelId = hotelId;
        this.clientPublisher = clientPublisher;
        this.name = name;
        this.city = city;
        this.address = address;
        this.standard = standard;
        this.rooms = rooms;
    }

    public Integer getKeyHotelId() {
        return keyHotelId;
    }

    public void setKeyHotelId(Integer keyHotelId) {
        this.keyHotelId = keyHotelId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Client getClientPublisher() {
        return clientPublisher;
    }

    public void setClientPublisher(Client clientPublisher) {
        this.clientPublisher = clientPublisher;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
