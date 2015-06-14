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
    @Column(name="offer_id")
    private Integer keyOfferId;

    @Column(name="internal_offer_id")
    private Integer offerId;

    @ManyToOne
    @JoinColumn(name="client_id")
    private Client clientPublisher;

    @Column(name="date_from")
    private Date dateFrom;

    @Column(name="date_to")
    private Date dateTo;

    @Column(name="price")
    private Double price;

    @Column(name="description")
    private String description;

    @Column(name="premium")
    private Boolean premium;

    @Column(name="visitCount")
    private Integer visitCount;

    @OneToMany(mappedBy="offer", cascade=CascadeType.ALL)
    private List<OfferedRoom> offeredRooms;

    public Integer getKeyOfferId() {
        return keyOfferId;
    }

    public void setKeyOfferId(Integer keyOfferId) {
        this.keyOfferId = keyOfferId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Date getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(Date dateFrom) {
        this.dateFrom = dateFrom;
    }

    public Date getDateTo() {
        return dateTo;
    }

    public void setDateTo(Date dateTo) {
        this.dateTo = dateTo;
    }

    public List<OfferedRoom> getOfferedRooms() {
        return offeredRooms;
    }

    public void setOfferedRooms(List<OfferedRoom> offeredRooms) {
        this.offeredRooms = offeredRooms;
    }

    public Offer(){}
    public Offer(Client clientPublisher, Date dateFrom, Date dateTo, Double price, String description, Boolean premium, Integer visitCount) {
        this.clientPublisher = clientPublisher;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.description = description;
        this.premium = premium;
        this.visitCount = visitCount;
    }

    public static LinkedList<Offer> getAllActual() {
        LinkedList<Offer> result = new LinkedList<>();
        for (Object el : JPA.em().createQuery("FROM Offer WHERE dateTo > current_date AND hasImages=true").getResultList()) {
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
        //TODO repair this method :(
//        if (expiryDate.before(new Date())) {
//            return "Expiry date must be in future";
//        } else if (placesNumber <= 0) {
//            return "Number of places must be greater than 0";
//        } else if (price <= 0) {
//            return "Price must be greater than 0";
//        }
        return null;
    }

}
