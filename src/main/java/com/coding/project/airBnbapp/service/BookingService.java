package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.BookingDto;
import com.coding.project.airBnbapp.dto.BookingRequest;

public interface BookingService {

    BookingDto intialiserBooking(BookingRequest bookingRequest);
}
