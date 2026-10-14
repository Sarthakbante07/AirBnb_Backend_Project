package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.BookingDto;
import com.coding.project.airBnbapp.dto.BookingRequest;
import com.coding.project.airBnbapp.entity.*;
import com.coding.project.airBnbapp.entity.enums.BookingStatus;
import com.coding.project.airBnbapp.exception.ResourceNotFoundException;
import com.coding.project.airBnbapp.repository.BookingRepository;
import com.coding.project.airBnbapp.repository.HotelRepository;
import com.coding.project.airBnbapp.repository.InventoryRepository;
import com.coding.project.airBnbapp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService{
    private final ModelMapper modelMapper;

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final BookingRepository bookingRepository;
    private final InventoryRepository inventoryRepository;

    @Override
    @Transactional
    public BookingDto intialiserBooking(BookingRequest bookingRequest) {

        log.info("Initiallizing Booking for Hotel : {}, room: {}, date{} - {}", bookingRequest.getHotelId(),
                bookingRequest.getRoomId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        Hotel hotel = hotelRepository.findById(bookingRequest.getHotelId())
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not Found with id: " + bookingRequest.getHotelId()));

        Room room = roomRepository.findById(bookingRequest.getRoomId())
                .orElseThrow(()-> new ResourceNotFoundException("Room Not Found with id: " + bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.finalAndLockAvailableInventory(room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        long dayCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        if (inventoryList.size() != dayCount) {
            throw new IllegalStateException("Room is not available any more");
        }

        // Reaserve the room/ update the booked count for inventory

        for(Inventory inventory : inventoryList) {
            inventory.setBookedCount(inventory.getBookedCount() + bookingRequest.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);

        // Create the booking
        User user = new User();
        user.setId(1L); //TODO : dummy user

        //TODO : Calculate Dynamic Price

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(user)
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }
}
