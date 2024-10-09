package com.example.dacn.services;

import com.example.dacn.dto.RatingDTO;
import com.example.dacn.dto.ReservationDTO;
import com.example.dacn.dto.request.ReservationRequest;
import com.example.dacn.dto.response.ReservationResponse;


import java.time.LocalDate;
import java.util.List;

public interface ReservationService {
    ReservationResponse findById(Long id) throws Exception;

    List<Long> findReservationBefore(Long roomId, LocalDate startDate, LocalDate endDate);

    List<ReservationResponse> findAllByUsername(String username);

    ReservationResponse cancelReservation(Long id, String username) throws Exception;

    List<ReservationResponse> saveAll(List<ReservationRequest> request);



}
