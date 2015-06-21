package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.*;

/**
 * Created by magdalena on 01.06.15.
 */
//TODO opis tekstowy
@Entity
@Table(name = "offers")
public class Offer implements Comparable<Offer> {

    @Id
    @GeneratedValue
    @Column(name = "offer_id")
    private Integer keyOfferId;

    @Column(name = "internal_offer_id")
    private Integer offerId;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client clientPublisher;

    @Column(name = "date_from")
    private Date dateFrom;

    @Column(name = "date_to")
    private Date dateTo;

    @Column(name = "price")
    private Double price;

    @Column(name = "description")
    private String description;

    @Column(name = "premium")
    private Boolean premium;

    @Column(name = "visitCount")
    private Integer visitCount;

    @OneToMany(mappedBy = "offer", cascade = CascadeType.ALL)
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

    public Offer() {
    }

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
        for (Object el : JPA.em().createQuery("FROM Offer WHERE dateTo > current_date").getResultList()) {
            result.add((Offer) el);
        }
        return result;
    }

    public static LinkedList<Offer> getNonPremiumActual() {
        LinkedList<Offer> result = new LinkedList<>();
        for (Object el : JPA.em().createQuery("FROM Offer WHERE premium=false AND dateTo > current_date").getResultList()) {
            result.add((Offer) el);
        }
        return result;
    }

    public static Offer getById(Integer offerId) {
        return JPA.em().find(Offer.class, offerId);
    }

    public static Offer getNotExpiredById(Integer offerId) {
        List offers = JPA.em().createNativeQuery("SELECT * FROM offers WHERE date_to > current_date - interval '1 month' AND offer_id =:offerId", Offer.class).setParameter("offerId", offerId).getResultList();
        if (offers.size() != 1) {
            return null;
        }
        return (Offer) offers.get(0);
    }

    public String validate() {
        if (dateTo.before(dateFrom)) {
            return "Date to must be after date from!";
        } else if (dateTo.before(new Date())) {
            return "Date to must be in future";
        } else if (price <= 0) {
            return "Price must be greater than 0";
        }
        return null;
    }

    public Map<Hotel, LinkedList<Room> > mapOfContent() {
        Map<Hotel, LinkedList<Room> > map = new HashMap<>();
        for (OfferedRoom offeredRoom : offeredRooms) {
            if (map.containsKey(offeredRoom.getHotel())) {
                map.get(offeredRoom.getHotel()).add(offeredRoom.getRoom());
            } else {
                LinkedList<Room> rooms = new LinkedList<>();
                rooms.add(offeredRoom.getRoom());
                map.put(offeredRoom.getHotel(), rooms);
            }
        }
        return map;
    }

    @Override
    public int compareTo(Offer offer) {
        if (offer == null) {
            return -1;
        }
        if (offer.getPremium() && !this.getPremium()) {
            return 1;
        } else if (!offer.getPremium() && this.getPremium()) {
            return -1;
        }
        return 0;
    }
}
