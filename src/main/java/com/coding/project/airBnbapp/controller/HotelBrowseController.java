package com.coding.project.airBnbapp.controller;

import com.coding.project.airBnbapp.dto.HotelDto;
import com.coding.project.airBnbapp.dto.HotelInfoDto;
import com.coding.project.airBnbapp.dto.HotelPriceDto;
import com.coding.project.airBnbapp.dto.HotelSearchRequest;
import com.coding.project.airBnbapp.service.HotelService;
import com.coding.project.airBnbapp.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
@Slf4j
public class HotelBrowseController {

    private final InventoryService inventoryService;
    private final HotelService hotelService;

    @GetMapping("/search")
    public ResponseEntity<Page<HotelPriceDto>> searchHotels(@RequestBody HotelSearchRequest hotelSearchRequest) {
        log.info("Searching hotels in city {} for dates {} to {}",
                hotelSearchRequest.getCity(), hotelSearchRequest.getStartDate(), hotelSearchRequest.getEndDate());
        var page = inventoryService.searchHotels(hotelSearchRequest);
        return ResponseEntity.ok(page);
    }

    @GetMapping("/{hotelId}/info")
    public ResponseEntity<HotelInfoDto> getHotelInfo(@PathVariable Long hotelId) {
        log.info("Fetching hotel information for hotel id {}", hotelId);
        return ResponseEntity.ok(hotelService.getHotelInfoById(hotelId));
    }

}
