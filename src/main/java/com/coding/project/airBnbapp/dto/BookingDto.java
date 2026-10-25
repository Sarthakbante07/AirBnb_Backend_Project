package com.coding.project.airBnbapp.dto;

import com.coding.project.airBnbapp.entity.Hotel;
import com.coding.project.airBnbapp.entity.Room;
import com.coding.project.airBnbapp.entity.User;
import com.coding.project.airBnbapp.entity.enums.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class BookingDto {

    private Long id;
    private Integer roomsCount;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
    private BookingStatus bookingStatus;
    private Set<GuestDto> guests;

}
