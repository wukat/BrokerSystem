package models;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="offers")
public class Offer {

    @Id
    @GeneratedValue
    @Column(name="offer_id")
    private Long offerId;

    @ManyToOne
    @JoinColumn(name="client_publisher")
    private Client clientPublisher;

    @Column(name="premium")
    private Boolean premium;

    @Column(name="visit_count")
    private Integer visitCount;

    @Column(name="expiry_date")
    private Date expiryDate;

    @Column(name="price")
    private Double price;

    @Column(name="standard")
    private Integer standard;

    @Column(name="places_number")
    private Integer placesNumber;

    @Column(name="address")
    private String address;

    @OneToMany(mappedBy="offer", cascade=CascadeType.ALL)
    private Set<Image> images;

    @OneToMany(mappedBy="offer", cascade=CascadeType.ALL)
    private Set<Booking> bookings;


    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

    public Client getClientPublisher() {
        return clientPublisher;
    }

    public void setClientPublisher(Client clientPublisher) {
        this.clientPublisher = clientPublisher;
    }

    public Boolean getPremium() {
        return premium;
    }

    public void setPremium(Boolean premium) {
        this.premium = premium;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getStandard() {
        return standard;
    }

    public void setStandard(Integer standard) {
        this.standard = standard;
    }

    public Integer getPlacesNumber() {
        return placesNumber;
    }

    public void setPlacesNumber(Integer placesNumber) {
        this.placesNumber = placesNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Image> getImages() {
        return images;
    }

    public void setImages(Set<Image> images) {
        this.images = images;
    }

    public Set<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(Set<Booking> bookings) {
        this.bookings = bookings;
    }

    public Offer(){}
    public Offer(Client clientPublisher, Boolean premium, Integer visitCount, Date expiryDate, Double price, Integer standard, Integer placesNumber, String address){
        this.clientPublisher = clientPublisher;
        this.premium = premium;
        this.visitCount = visitCount;
        this.expiryDate = expiryDate;
        this.price = price;
        this.standard = standard;
        this.placesNumber = placesNumber;
        this.address = address;
    }

}
