package com.coding.project.airBnbapp.controller;

import com.coding.project.airBnbapp.dto.BookingDto;
import com.coding.project.airBnbapp.dto.BookingRequest;
import com.coding.project.airBnbapp.dto.GuestDto;
import com.coding.project.airBnbapp.service.BookingService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/bookings")
@Slf4j
public class HotelBookingController {

    private final BookingService bookingService;

    @PostMapping("/init")
    public ResponseEntity<BookingDto> intialiserBooking(@RequestBody BookingRequest bookingRequest) {
        log.info("Initializing booking for hotel id {} and room id {}",
                bookingRequest.getHotelId(), bookingRequest.getRoomId());
        return ResponseEntity.ok(bookingService.intialiserBooking(bookingRequest));
    }

    @PostMapping("/{bookingId}/addGuest")
    public ResponseEntity<BookingDto> addGuest(@PathVariable Long bookingId,
                                               @RequestBody List<GuestDto> guestDtolist) {
        log.info("Adding {} guest(s) to booking id {}", guestDtolist.size(), bookingId);
        return ResponseEntity.ok(bookingService.addGuest(bookingId, guestDtolist));
    }
}
