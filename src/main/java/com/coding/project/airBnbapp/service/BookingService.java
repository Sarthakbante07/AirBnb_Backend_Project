package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.BookingDto;
import com.coding.project.airBnbapp.dto.BookingRequest;
import com.coding.project.airBnbapp.dto.GuestDto;

import java.util.List;

public interface BookingService {

    BookingDto intialiserBooking(BookingRequest bookingRequest);

    BookingDto addGuest(Long bookingId, List<GuestDto> guestDtolist);
}
