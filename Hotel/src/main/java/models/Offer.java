package models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by magdalena on 01.06.15.
 */
//TODO opis tekstowy
@Entity
@Table(name="offers")
public class Offer {

    @Column(name="offer_id")
    private Integer keyOfferId;

    @Id
    @GeneratedValue
    @Column(name="internal_offer_id")
    private Integer offerId;

    @Transient
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

    @OneToMany(mappedBy="offer", cascade= CascadeType.ALL)
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

    public Offer(Integer keyOfferId, Client clientPublisher, Date dateFrom, Date dateTo, Double price, String description, Boolean premium, Integer visitCount, List<OfferedRoom> offeredRooms) {
        this.keyOfferId = keyOfferId;
        this.clientPublisher = clientPublisher;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.price = price;
        this.description = description;
        this.premium = premium;
        this.visitCount = visitCount;
        this.offeredRooms = offeredRooms;
    }
}
