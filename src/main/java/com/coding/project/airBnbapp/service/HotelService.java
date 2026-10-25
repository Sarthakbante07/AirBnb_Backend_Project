package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.HotelDto;
import com.coding.project.airBnbapp.dto.HotelInfoDto;
import com.coding.project.airBnbapp.entity.Hotel;

public interface HotelService {
    HotelDto createNewHotel(HotelDto hotelDto);

    HotelDto getHotelById(Long id);

    HotelDto updateHotelById(Long id, HotelDto hotelDto);

    void deleteHotelById(Long id);

    void activateHotel(Long id);

    HotelInfoDto getHotelInfoById(Long hotelId);
}
