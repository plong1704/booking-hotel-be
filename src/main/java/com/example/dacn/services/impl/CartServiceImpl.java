package com.example.dacn.services.impl;

import com.example.dacn.dto.request.CartRequest;
import com.example.dacn.dto.response.BenefitResponse;
import com.example.dacn.dto.response.CartResponse;
import com.example.dacn.dto.response.HotelResponse;
import com.example.dacn.dto.response.RoomResponse;
import com.example.dacn.entity.CartEntity;
import com.example.dacn.entity.DiscountEntity;
import com.example.dacn.entity.HotelEntity;
import com.example.dacn.entity.RoomEntity;
import com.example.dacn.enums.RoomStatus;
import com.example.dacn.repository.CartRepository;
import com.example.dacn.services.*;
import com.example.dacn.specification.CartSpecification;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private CartRepository repository;
    @Autowired
    private HotelService hotelService;
    @Autowired
    private RoomService roomService;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private IHotelImageService imageService;
    @Autowired
    private ModelMapper mapper;

    @Override
    public List<CartResponse> findBySessionId(String sessionId) {
        Specification<CartEntity> spec = Specification.where(CartSpecification.hasSessionId(sessionId));
        return repository.findAll(spec).stream().map(i ->
                CartResponse.builder()
                        .id(i.getId())
                        .adult(i.getAdult())
                        .child(i.getChild())
                        .fromDate(i.getFromDate())
                        .toDate(i.getToDate())
                        .hotel(mapper.map(i.getHotel(), HotelResponse.class))
                        .room(mapper.map(i.getRoom(), RoomResponse.class))
                        .sessionId(i.getSessionId())
                        .address(i.getHotel().getAddress().getProvince().get_code())
                        .bannerImage(imageService.findFirstBannerImage(i.getHotel().getId()))
                        .totalReviews(i.getHotel().getRatings().size())
                        .roomType(i.getRoom().getRoomType().getName())
                        .benefits(i.getRoom().getBenefits().stream().map(item -> mapper.map(item, BenefitResponse.class)).collect(Collectors.toSet()))
                        .status(isReservedBefore(i.getHotel(), i.getRoom(), i.getFromDate(), i.getToDate()))
                        .discountPercent(getDiscountPercent(i.getRoom().getDiscount()))
                        .build()
        ).collect(Collectors.toList());
    }

    private Double getDiscountPercent(DiscountEntity discount) {
        if (ObjectUtils.isEmpty(discount)) return 0.0;
        return discount.getDiscountPercent();
    }

    @Override
    public CartResponse addToCart(CartRequest cart) throws Exception {
        String sessionId = cart.getSessionId();
        if (ObjectUtils.isEmpty(sessionId)) {
            sessionId = String.valueOf(UUID.randomUUID());
        } else {
            CartEntity existedItem = repository.findFirstBySessionIdAndRoomId(cart.getSessionId(), cart.getRoomId());
            if (!ObjectUtils.isEmpty(existedItem)) throw new Exception("Phòng này đã tồn tại trong giỏ hàng !");
        }
        Optional<RoomEntity> foundRoom = roomService.findByRoomId(cart.getRoomId());
        if (!foundRoom.isPresent()) {
            throw new Exception("Không tìm thấy phòng hợp lệ !");
        }
        RoomEntity room = foundRoom.get();
        if (cart.getAdult() > room.getMaxAdults() || cart.getChild() > room.getMaxChildren())
            throw new Exception("Số lượng người vượt quá ngưỡng cho phép");

        //find reserved list before by hotel id ,room id, from-to date
        List<Long> reservedList = reservationService.findReservationBefore(cart.getRoomId(), cart.getFromDate(), cart.getToDate());
        if (reservedList.size() > 0) throw new Exception("Phòng đã có khách hàng đặt vui lòng chọn phòng khác !");


        CartEntity cartEntity = CartEntity.builder()
                .sessionId(sessionId)
                .adult(cart.getAdult())
                .child(cart.getChild())
                .fromDate(cart.getFromDate())
                .toDate(cart.getToDate())
                .hotel(room.getHotel())
                .room(room)
                .sessionId(sessionId)
                .build();
        CartEntity addedCartItem = repository.save(cartEntity);
        return CartResponse.builder()
                .id(addedCartItem.getId())
                .adult(addedCartItem.getAdult())
                .child(addedCartItem.getChild())
                .fromDate(addedCartItem.getFromDate())
                .toDate(addedCartItem.getToDate())
                .hotel(mapper.map(addedCartItem.getHotel(), HotelResponse.class))
                .room(mapper.map(addedCartItem.getRoom(), RoomResponse.class))
                .sessionId(addedCartItem.getSessionId())
                .address(addedCartItem.getHotel().getAddress().getWard().get_name() + " ," + addedCartItem.getHotel().getAddress().getProvince().get_code())
                .bannerImage(imageService.findFirstBannerImage(addedCartItem.getHotel().getId()))
                .totalReviews(addedCartItem.getHotel().getRatings().size())
                .roomType(addedCartItem.getRoom().getRoomType().getName())
                .benefits(addedCartItem.getRoom().getBenefits().stream().map(i -> mapper.map(i, BenefitResponse.class)).collect(Collectors.toSet()))
                .status(isReservedBefore(addedCartItem.getHotel(), addedCartItem.getRoom(), addedCartItem.getFromDate(), addedCartItem.getToDate()))
                .discountPercent(getDiscountPercent(addedCartItem.getRoom().getDiscount()))
                .build();
    }

    @Override
    public CartResponse findBySessionIdAndRoomId(String sessionId, Long roomId) throws Exception {
        CartEntity existedItem = repository.findFirstBySessionIdAndRoomId(sessionId, roomId);
        if (ObjectUtils.isEmpty(existedItem)) throw new Exception("Phòng không tồn tại !");
        return CartResponse.builder()
                .id(existedItem.getId())
                .adult(existedItem.getAdult())
                .child(existedItem.getChild())
                .fromDate(existedItem.getFromDate())
                .toDate(existedItem.getToDate())
                .hotel(mapper.map(existedItem.getHotel(), HotelResponse.class))
                .room(mapper.map(existedItem.getRoom(), RoomResponse.class))
                .sessionId(existedItem.getSessionId())
                .address(existedItem.getHotel().getAddress().getProvince().get_code())
                .bannerImage(imageService.findFirstBannerImage(existedItem.getHotel().getId()))
                .totalReviews(existedItem.getHotel().getRatings().size())
                .roomType(existedItem.getRoom().getRoomType().getName())
                .benefits(existedItem.getRoom().getBenefits().stream().map(item -> mapper.map(item, BenefitResponse.class)).collect(Collectors.toSet()))
                .status(isReservedBefore(existedItem.getHotel(), existedItem.getRoom(), existedItem.getFromDate(), existedItem.getToDate()))
                .discountPercent(getDiscountPercent(existedItem.getRoom().getDiscount()))
                .build();
    }

    @Override
    public void deleteCartItemById(Long id) throws Exception {
        repository.deleteById(id);
    }

    @Override
    @Transactional // delete by each id in list
    public void deleteByIds(List<Long> ids) throws Exception {
        repository.deleteByIdIn(ids);
    }

    private RoomStatus isReservedBefore(HotelEntity hotel, RoomEntity room, LocalDate startDate, LocalDate endDate) {
        try {
            if (null == hotel || null == room) return RoomStatus.UNDEFINED;
            List<Long> reservedList = reservationService.findReservationBefore(room.getId(), startDate, endDate);
            if (reservedList.size() > 0)
                return RoomStatus.RESERVED;
            else
                return RoomStatus.AVAILABLE;
        } catch (Exception e) {
            return RoomStatus.UNDEFINED;
        }
    }
}
