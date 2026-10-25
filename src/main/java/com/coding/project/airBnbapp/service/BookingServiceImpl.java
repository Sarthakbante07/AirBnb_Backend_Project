package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.BookingDto;
import com.coding.project.airBnbapp.dto.BookingRequest;
import com.coding.project.airBnbapp.dto.GuestDto;
import com.coding.project.airBnbapp.entity.*;
import com.coding.project.airBnbapp.entity.enums.BookingStatus;
import com.coding.project.airBnbapp.exception.ResourceNotFoundException;
import com.coding.project.airBnbapp.exception.UnAuthorizeException;
import com.coding.project.airBnbapp.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final GuestRepository guestRepository;
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
                .orElseThrow(() -> new ResourceNotFoundException("Room Not Found with id: " + bookingRequest.getRoomId()));

        List<Inventory> inventoryList = inventoryRepository.finalAndLockAvailableInventory(room.getId(), bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate(), bookingRequest.getRoomsCount());

        long dayCount = ChronoUnit.DAYS.between(bookingRequest.getCheckInDate(), bookingRequest.getCheckOutDate());

        if (inventoryList.size() != dayCount) {
            throw new IllegalStateException("Room is not available any more");
        }

        // Reaserve the room/ update the booked count for inventory

        for (Inventory inventory : inventoryList) {
            inventory.setReservedCount(inventory.getReservedCount() + bookingRequest.getRoomsCount());
        }

        inventoryRepository.saveAll(inventoryList);


        //TODO : Calculate Dynamic Price

        Booking booking = Booking.builder()
                .bookingStatus(BookingStatus.RESERVED)
                .hotel(hotel)
                .room(room)
                .checkInDate(bookingRequest.getCheckInDate())
                .checkOutDate(bookingRequest.getCheckOutDate())
                .user(getCurrentUser())
                .roomsCount(bookingRequest.getRoomsCount())
                .amount(BigDecimal.TEN)
                .build();

        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    @Override
    @Transactional
    public BookingDto addGuest(Long bookingId, List<GuestDto> guestDtolist) {
        log.info("Adding Guest for Booking with id : {}", bookingId);

        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not Found with id: " + bookingId));

        User user = getCurrentUser();

        if(user.equals(booking.getUser())) {
            throw new UnAuthorizeException("Booking does not belong to the current user, You can not add guest");
        }

        if (hasBookingExpired(booking)) {
            throw new IllegalStateException("Booking has expired");
        }

        if (booking.getBookingStatus() != BookingStatus.RESERVED) {
            throw new IllegalStateException("Booking status is not RESERVED State , You can not add guest");
        }

        for (GuestDto guestDto : guestDtolist) {
            Guest guest = modelMapper.map(guestDto, Guest.class);
            guest.setUser(user);
            guest = guestRepository.save(guest);
            booking.getGuests().add(guest);
        }
        booking.setBookingStatus(BookingStatus.GUESTS_ADDED);
        booking = bookingRepository.save(booking);
        return modelMapper.map(booking, BookingDto.class);
    }

    public boolean hasBookingExpired(Booking booking) {
        return booking.getCreated_at().plusMinutes(10).plusMinutes(10).isBefore(LocalDateTime.now());

    }

    public User getCurrentUser() {
       return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
