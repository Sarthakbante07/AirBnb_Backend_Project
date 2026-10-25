package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.RoomDto;
import com.coding.project.airBnbapp.entity.Hotel;
import com.coding.project.airBnbapp.entity.Room;
import com.coding.project.airBnbapp.entity.User;
import com.coding.project.airBnbapp.exception.ResourceNotFoundException;
import com.coding.project.airBnbapp.exception.UnAuthorizeException;
import com.coding.project.airBnbapp.repository.HotelRepository;
import com.coding.project.airBnbapp.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;

    @Override
    public RoomDto createNewRoom(Long HotelId, RoomDto roomDto) {
        log.info("Creating a new room in hotel having id {}", HotelId);

        Hotel hotel = hotelRepository
                .findById(HotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + HotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }

        Room room = modelMapper.map(roomDto, Room.class);
        room.setHotel(hotel);
        room = roomRepository.save(room);

        // TODO : Create inventery as soon as room is created anf if hotel is active.

        if (hotel.getActive()) {
            inventoryService.intializeRoomForAYear(room);
        }

        return modelMapper.map(room, RoomDto.class);
    }

    @Override
    public List<RoomDto> getAllRoomsInHotel(Long hotelId) {
        log.info("getting room with hotel with id {}", hotelId);

        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }

        return hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public RoomDto getRoomById(Long roomId) {
        log.info("getting room with Room with id {}", roomId);

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + roomId));

        return modelMapper.map(room, RoomDto.class);
    }

    @Transactional
    @Override
    public void deleteRoomById(Long roomId) {
        log.info("Delete Room with id {}", roomId);

        Room room = roomRepository
                .findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + roomId));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(room.getHotel().getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }
        inventoryService.deleteAllInventory(room);
        roomRepository.deleteById(roomId);
    }
}
