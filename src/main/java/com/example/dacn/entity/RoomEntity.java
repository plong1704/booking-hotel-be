package com.example.dacn.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "room")
@Getter
@Setter
public class RoomEntity extends BaseEntity {
    private String name;
    private Integer maxAdults;
    private Integer maxChildren;
    private String status;
    private Double originPrice;
    private Double rentalPrice;
    private Double finalPrice;

    @ManyToOne
    @JoinColumn(name = "room_type_id")
    private RoomTypeEntity roomType;

    @ManyToOne

    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    @ManyToMany
    @JoinTable(name = "room_facility", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "facility_id"))
    private Set<FacilityEntity> facilities = new LinkedHashSet<FacilityEntity>();

    @ManyToMany
    @JoinTable(name = "room_benefit", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "benefit_id"))
    private Set<BenefitEntity> benefits = new LinkedHashSet<BenefitEntity>();

    @OneToMany(mappedBy = "room")
    private Set<RoomImageEntity> roomImages = new LinkedHashSet<RoomImageEntity>();

    @ManyToMany
    @JoinTable(name = "room_payment_method", joinColumns = @JoinColumn(name = "room_id"), inverseJoinColumns = @JoinColumn(name = "payment_method"))
    private Set<PaymentMethodEntity> paymentMethods = new LinkedHashSet<PaymentMethodEntity>();

    @ManyToOne()
    @JoinColumn(name = "discount_id")
    private DiscountEntity discount;

    @OneToMany(mappedBy = "room")
    private Set<ReservationEntity> reservations = new LinkedHashSet<ReservationEntity>();

    @OneToMany(mappedBy = "room")
    private Set<CartEntity> cartItems = new LinkedHashSet<CartEntity>();

    public RoomEntity(){
        System.out.println("final Price"  + this.finalPrice);
    }

    @PostLoad
    public void updateSellingPrice() {
        DiscountEntity roomDiscount = this.discount;
        if(roomDiscount != null){
        Double truthPercent = 100 - roomDiscount.getDiscountPercent();
        this.finalPrice = (this.getRentalPrice() * (truthPercent / 100));
        }else{
            this.finalPrice = 0.0;
        }
    }

}
