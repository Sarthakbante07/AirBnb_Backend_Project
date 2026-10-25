package com.coding.project.airBnbapp.service;

import com.coding.project.airBnbapp.dto.HotelDto;
import com.coding.project.airBnbapp.dto.HotelInfoDto;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;
    private final ModelMapper modelMapper;
    private final InventoryService inventoryService;
    private final RoomRepository roomRepository;


    @Override
    public HotelDto createNewHotel(HotelDto hotelDto) {
        log.info("Hotel Created with name : {} ", hotelDto.getName());

        Hotel hotel = modelMapper.map(hotelDto, Hotel.class);
        hotel.setActive(false);

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        hotel.setOwner(user);

        hotel = hotelRepository.save(hotel);
        log.info("Hotel created with id {}", hotel.getId());
        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    public HotelDto getHotelById(Long id) {
        log.info("Hotel Found with ID : {} ", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }
        return modelMapper.map(hotel, HotelDto.class);

    }

    @Override
    public HotelDto updateHotelById(Long id, HotelDto hotelDto) {
        log.info("Hotel Found with ID : {} ", id);

        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }
        modelMapper.map(hotelDto, hotel);
        hotel.setId(id);
        hotel = hotelRepository.save(hotel);

        return modelMapper.map(hotel, HotelDto.class);
    }

    @Override
    @Transactional
    public void deleteHotelById(Long id) {
        log.info("Deleting hotel id {}", id);
        Hotel hotel = hotelRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + id));
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }

        for (Room room : hotel.getRooms()) {
            inventoryService.deleteAllInventory(room);
            roomRepository.deleteById(room.getId());
        }
        hotelRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void activateHotel(Long hotelId) {
        log.info("Activate Hotel with ID : {} ", hotelId);

        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + hotelId));

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!user.equals(hotel.getOwner())) {
            throw new UnAuthorizeException("Hotel does not belong to the current user, You can not view it");
        }

        hotel.setActive(true);
        //      TODO : Create inventory for all the rooms for this hotel
        // assuming only do it once
        for (Room room : hotel.getRooms()) {
            inventoryService.intializeRoomForAYear(room);
        }
    }

    //public
    @Override
    public HotelInfoDto getHotelInfoById(Long hotelId) {
        log.info("Fetching hotel information for hotel id {}", hotelId);
        Hotel hotel = hotelRepository
                .findById(hotelId)
                .orElseThrow(() -> new ResourceNotFoundException("Hotel Not found With ID: " + hotelId));

        List<RoomDto> rooms = hotel.getRooms()
                .stream()
                .map((element) -> modelMapper.map(element, RoomDto.class))
                .toList();

        return new HotelInfoDto(modelMapper.map(hotel, HotelDto.class), rooms);
    }


}
