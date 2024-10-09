package com.example.dacn.repository;

import com.example.dacn.entity.CartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface CartRepository extends JpaRepository<CartEntity, Long>, JpaSpecificationExecutor<CartEntity> {
    void deleteByIdIn(List<Long> ids);

    CartEntity findFirstBySessionIdAndRoomId(String sessionId, Long roomId);
}
