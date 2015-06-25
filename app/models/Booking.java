package models;

import play.db.jpa.JPA;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

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

    @Column(name = "internal_booking_id")
    private Long internalBookingId;

    @ManyToOne
    @JoinColumn(name = "client")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "offered_room_id")
    private OfferedRoom offeredRoom;

    @Column(name="date_from")
    private Date dateFrom;

    @Column(name="date_to")
    private Date dateTo;

    @Column(name = "cancelled")
    private Boolean cancelled;


    public Booking(){}
    public Booking(Client client, OfferedRoom offeredRoom, Date dateFrom, Date dateTo, Boolean cancelled) {
        this.client = client;
        this.offeredRoom = offeredRoom;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.cancelled = cancelled;
    }

    public Long getInternalBookingId() {
        return internalBookingId;
    }

    public void setInternalBookingId(Long internalBookingId) {
        this.internalBookingId = internalBookingId;
    }

    public Long getBookingId() {
        return bookingId;
    }

    public void setBookingId(Long bookingId) {
        this.bookingId = bookingId;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public OfferedRoom getOfferedRoom() {
        return offeredRoom;
    }

    public void setOfferedRoom(OfferedRoom offeredRoom) {
        this.offeredRoom = offeredRoom;
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

    public Boolean getCancelled() {
        return cancelled;
    }

    public void setCancelled(Boolean cancelled) {
        this.cancelled = cancelled;
    }

    public static Booking getById(Long id) {
        return JPA.em().find(Booking.class, id);
    }

    public boolean canBeCancelled() {
        GregorianCalendar gc = new GregorianCalendar();
        gc.add(Calendar.WEEK_OF_YEAR, 2);
        return dateFrom.after(gc.getTime());
    }
}