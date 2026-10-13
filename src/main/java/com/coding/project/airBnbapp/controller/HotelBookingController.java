package com.coding.project.airBnbapp.controller;

import com.coding.project.airBnbapp.dto.BookingDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/booking")
public class HotelBookingController {

    public ResponseEntity<BookingDto> intialBooking();
}
