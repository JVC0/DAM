package org.docencia.hotel.model;

import jakarta.persistence.*;
import java.util.Objects;


@Entity
@Table(name = "bookings")
public class Booking {
    @Id
    private String id;
    @Column(name="check_in")
    private String checkIn;
    @Column(name="check_out")
    private String checkOut;
    @OneToOne
    @JoinColumn(name = "room_id",nullable = false)
    private Room room ;

    @OneToOne
    @JoinColumn(name = "guest_id",nullable = false)
    private Guest guest ;

    public Booking() {
    }

    public Booking(String id, String checkIn, String checkOut, Room room, Guest guest) {
        this.id = id;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.room = room;
        this.guest = guest;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCheckIn() {
        return this.checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return this.checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public Room getRoom() {
        return this.room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Guest getGuest() {
        return this.guest;
    }

    public void setGuest(Guest guest) {
        this.guest = guest;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Booking)) {
            return false;
        }
        Booking booking = (Booking) o;
        return Objects.equals(id, booking.id) && Objects.equals(checkIn, booking.checkIn) && Objects.equals(checkOut, booking.checkOut) && Objects.equals(room, booking.room) && Objects.equals(guest, booking.guest);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, checkIn, checkOut, room, guest);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", checkIn='" + getCheckIn() + "'" +
            ", checkOut='" + getCheckOut() + "'" +
            ", room='" + getRoom() + "'" +
            ", guest='" + getGuest() + "'" +
            "}";
    }

}
