package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.HotelDto;
import com.coding.project.airBnbapp.dto.HotelSearchRequest;
import com.coding.project.airBnbapp.entity.Room;
import org.springframework.data.domain.Page;

public interface InventoryService {

    void intializeRoomForAYear(Room room);

    void deleteAllInventory(Room room);

    Page<HotelDto> searchHotels(HotelSearchRequest hotelSearchRequest);
}
