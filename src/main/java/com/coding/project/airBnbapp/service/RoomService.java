package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.RoomDto;

import java.util.List;

public interface RoomService {

 RoomDto createNewRoom(Long hotelId, RoomDto roomDto);

 List<RoomDto> getAllRoomsInHotel(Long hotelId);

 RoomDto getRoomById(Long id);

 void deleteRoomById(Long id);
}
