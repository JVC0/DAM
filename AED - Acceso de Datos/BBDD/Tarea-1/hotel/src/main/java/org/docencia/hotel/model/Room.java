package org.docencia.hotel.model;

import org.springframework.data.annotation.Id;


import jakarta.persistence.*;
import java.util.Objects;
@Entity
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    @Column(name = "number")
    private String numero;
    @Column(name = "type")
    private String tipo;
    @Column(name = "price_per_night")
    private double precio_por_noche;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;



    public Room() {
    }

    public Room(String id, String numero, String tipo, double precio_por_noche, Hotel hotel) {
        this.id = id;
        this.numero = numero;
        this.tipo = tipo;
        this.precio_por_noche = precio_por_noche;
        this.hotel = hotel;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumero() {
        return this.numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getTipo() {
        return this.tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getPrecio_por_noche() {
        return this.precio_por_noche;
    }

    public void setPrecio_por_noche(double precio_por_noche) {
        this.precio_por_noche = precio_por_noche;
    }

    public Hotel getHotel() {
        return this.hotel;
    }

    public void setHotel(Hotel hotel) {
        this.hotel = hotel;
    }
    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Room)) {
            return false;
        }
        Room room = (Room) o;
        return Objects.equals(id, room.id) && Objects.equals(numero, room.numero) && Objects.equals(tipo, room.tipo) && precio_por_noche == room.precio_por_noche && Objects.equals(hotel, room.hotel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, numero, tipo, precio_por_noche, hotel);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", numero='" + getNumero() + "'" +
            ", tipo='" + getTipo() + "'" +
            ", precio_por_noche='" + getPrecio_por_noche() + "'" +
            ", hotel='" + getHotel() + "'" +
            "}";
    }

}
