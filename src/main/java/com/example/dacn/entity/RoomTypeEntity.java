package com.example.dacn.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "room_type")
@Getter
@Setter
public class RoomTypeEntity extends BaseEntity {
    String name;
    String code;
    @OneToMany(mappedBy = "roomType")
    private Set<RoomEntity> rooms = new LinkedHashSet<RoomEntity>();
}
