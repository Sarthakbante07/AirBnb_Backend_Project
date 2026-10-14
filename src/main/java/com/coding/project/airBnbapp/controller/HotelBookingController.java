package com.coding.project.airBnbapp.controller;

import com.coding.project.airBnbapp.dto.BookingDto;
import com.coding.project.airBnbapp.dto.BookingRequest;
import com.coding.project.airBnbapp.service.BookingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class HotelBookingController {

    private final BookingService bookingService;

    public ResponseEntity<BookingDto> intialiserBooking(@RequestBody BookingRequest bookingRequest){
        return ResponseEntity.ok(bookingService.intialiserBooking(bookingRequest));
    }
}
