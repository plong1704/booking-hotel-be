package com.example.dacn.repository;

import com.example.dacn.entity.UserRating;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRatingRepository extends JpaRepository<UserRating, Long> {
}
