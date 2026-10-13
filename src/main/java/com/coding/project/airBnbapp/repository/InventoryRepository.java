package com.coding.project.airBnbapp.repository;

import com.coding.project.airBnbapp.entity.Hotel;
import com.coding.project.airBnbapp.entity.Inventory;
import com.coding.project.airBnbapp.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

     void deleteByRoom(Room room);

     @Query("""
            SELECT DISTINCT i.hotel
            FROM Inventory i
            WHERE i.city = :city
                        AND i.date BETWEEN :startDate AND :endDate
                        AND i.closed = false
                        AND (i.totalCount - i.bookedCount) >= :roomCount
            GROUP BY i.hotel, i.room
            HAVING COUNT(i.date) = :dateCount
            """
            )
     Page<Hotel> findHotelWithAvailableInventory(
             @Param("city") String city,
             @Param("startDate")LocalDate startDate,
             @Param("endDate") LocalDate endDate,
             @Param("roomCount") Integer roomsCount,
             @Param("dateCount") long dateCount,
             Pageable pageable
             );
}
