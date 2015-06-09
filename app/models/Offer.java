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
@Table(name="offers")
public class Offer {

    @Id
    @GeneratedValue
    @Column(name="offerId")
    private Integer offerId;

    @ManyToOne
    @JoinColumn(name="clientPublisher_userNumber")
    private Client clientPublisher;

    @Column(name="description")
    private String description;

    @Column(name="premium")
    private Boolean premium;

    @Column(name="visitCount")
    private Integer visitCount;

    @Column(name="expiryDate")
    private Date expiryDate;

    @Column(name="price")
    private Double price;

    @Column(name="standard")
    private Integer standard;

    @Column(name="placesNumber")
    private Integer placesNumber;

    @Column(name="address")
    private String address;

    @Column(name="hasImages")
    private boolean hasImages;

    @OneToMany(mappedBy="offer", cascade=CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy="offer", cascade=CascadeType.ALL)
    private List<Booking> bookings;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHasImages() {
        return hasImages;
    }

    public void setHasImages(boolean hasImages) {
        this.hasImages = hasImages;
    }

    public Integer getOfferId() {
        return offerId;
    }

    public void setOfferId(Integer offerId) {
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

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
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

    public static LinkedList<Offer> getAllActual() {
        LinkedList<Offer> result = new LinkedList<>();
        for (Object el : JPA.em().createQuery("FROM Offer WHERE expiryDate > current_date AND hasImages=true").getResultList()) {
            result.add((Offer) el);
        }
        return result;
    }

    public static LinkedList<Offer> getNonPremiumActual() {
        LinkedList<Offer> result = new LinkedList<>();
        for (Object el : JPA.em().createQuery("FROM Offer WHERE premium=false AND expiryDate > current_date AND hasImages=true").getResultList()) {
            result.add((Offer) el);
        }
        return result;
    }

    public static Offer getOfferById(Integer offerId) {
        return JPA.em().find(Offer.class, offerId);
    }

    public static void activateOffer(Integer id) {
        JPA.em().createQuery("UPDATE Offer o SET hasImages=true WHERE offerId=:id").setParameter("id", id).executeUpdate();
    }

    public String validate() {
        if (expiryDate.before(new Date())) {
            return "Expiry date must be in future";
        } else if (placesNumber <= 0) {
            return "Number of places must be greater than 0";
        } else if (price <= 0) {
            return "Price must be greater than 0";
        }
        return null;
    }
}
