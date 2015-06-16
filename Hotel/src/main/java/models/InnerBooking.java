package models;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;

/**
 * Created by magdalena on 01.06.15.
 */

@Entity
@Table(name="bookings")
public class InnerBooking {

    @Column(name = "booking_id")
    private Long bookingId;

    @Id
    @GeneratedValue
    @Column(name = "internal_booking_id")
    private Long internalBookingId;

    @Column(name="date_from")
    private Date dateFrom;

    @Column(name="date_to")
    private Date dateTo;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    public InnerBooking(){}

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

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Hotel getHotel() {
        return hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }

    public InnerBooking(Long bookingId, Date dateFrom, Date dateTo, Hotel hotel, Room room) {
        this.bookingId = bookingId;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.hotel = hotel;
        this.room = room;
    }
}