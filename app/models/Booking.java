package models;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="bookings")
public class Booking {

    @Id
    @GeneratedValue
    @Column(name = "booking_id")
    private Long bookingId;

    @ManyToOne
    @JoinColumn(name = "offer")
    private Offer offer;

    @ManyToOne
    @JoinColumn(name = "client")
    private Client client;

    @Column(name = "date")
    private Date date;

    @Column(name = "days_number")
    private Integer daysNumber;

    @Column(name = "cancelled")
    private Boolean cancelled;


    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Integer getDaysNumber() {
        return daysNumber;
    }

    public void setDaysNumber(Integer daysNumber) {
        this.daysNumber = daysNumber;
    }

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Booking(){}
    public Booking(Offer offer, Client client, Date date, Integer daysNumber, Boolean cancelled) {
        this.offer = offer;
        this.client = client;
        this.date = date;
        this.daysNumber = daysNumber;
        this.cancelled = cancelled;
    }
}