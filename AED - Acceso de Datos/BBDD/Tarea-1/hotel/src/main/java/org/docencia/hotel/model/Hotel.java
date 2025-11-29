package org.docencia.hotel.model;

import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "hotels")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "name")
    private String nombre;

    @Column(name = "address")
    private String direccion;
    
    @OneToMany(mappedBy = "hotel")
    private List<Room> rooms;

    public Hotel() {
    }

    public Hotel(String id, String nombre, String direccion, List<Room> rooms) {
        this.id = id;
        this.nombre = nombre;
        this.direccion = direccion;
        this.rooms = rooms;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return this.direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Room> getRooms() {
        return this.rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }


    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof Hotel)) {
            return false;
        }
        Hotel hotel = (Hotel) o;
        return Objects.equals(id, hotel.id) && Objects.equals(nombre, hotel.nombre) && Objects.equals(direccion, hotel.direccion) && Objects.equals(rooms, hotel.rooms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nombre, direccion, rooms);
    }

    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", direccion='" + getDireccion() + "'" +
            ", rooms='" + getRooms() + "'" +
            "}";
    }

}
