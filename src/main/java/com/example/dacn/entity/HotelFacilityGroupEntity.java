package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "hotel_facility_group")
@Getter
@Setter
public class HotelFacilityGroupEntity extends BaseEntity{

    private String code;
    private String name;

    @OneToMany(mappedBy = "hotelFacilityGroup")
    private Set<FacilityEntity> facilities = new LinkedHashSet<FacilityEntity>();

}
