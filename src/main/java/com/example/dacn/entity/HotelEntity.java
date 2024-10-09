package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "hotel")
@Getter
@Setter
public class HotelEntity extends BaseEntity {
    private String name;

    @Column(name = "description", length = 255)
    @Nationalized
    private String description;
    private Double averagePoints;
    private String status;
    private Boolean isFreeCancellation;
    private Boolean isDeals;

    @ManyToOne
    @JoinColumn(name = "username")
    private UserEntity owner;

    @OneToOne
    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToMany(mappedBy = "hotel")
    private Set<RoomEntity> rooms = new HashSet<RoomEntity>();

    @ManyToMany
    @JoinTable(name = "hotel_facility", joinColumns = @JoinColumn(name = "hotel_id"), inverseJoinColumns = @JoinColumn(name = "facility_id"))
    private Set<FacilityEntity> facilities = new LinkedHashSet<FacilityEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<HotelImageEntity> hotelImages = new HashSet<HotelImageEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<ReservationEntity> reservations = new LinkedHashSet<ReservationEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<CartEntity> cartItems = new LinkedHashSet<CartEntity>();

    @OneToMany(mappedBy = "hotel")
    private Set<UserRating> ratings = new LinkedHashSet<UserRating>();

    @OneToMany(mappedBy = "hotel")
    private Set<FavoriteHotelEntity> favoriteHotels = new LinkedHashSet<>();
}
