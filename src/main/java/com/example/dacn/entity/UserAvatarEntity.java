package com.example.dacn.entity;

import com.example.dacn.enums.ImageStatus;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_avatar")
@Getter
@Setter
public class UserAvatarEntity extends BaseEntity {
    private String imageUrl;
    private ImageStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public UserAvatarEntity() {
    }

    public UserAvatarEntity(String imageUrl, ImageStatus status, UserEntity user) {
        this.imageUrl = imageUrl;
        this.status = status;
        this.user = user;
    }

    @Override
    public String toString() {
        return "UserAvatarEntity{" +
                "imageUrl='" + imageUrl + '\'' +
                ", status=" + status +
                ", user=" + user +
                '}';
    }
}

