package com.coding.project.airBnbapp.repository;

import com.coding.project.airBnbapp.entity.Hotel;
import com.coding.project.airBnbapp.entity.Inventory;
import com.coding.project.airBnbapp.entity.Room;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    void deleteByRoom(Room room);

    @Query("""
            SELECT DISTINCT i.hotel
            FROM Inventory i
            WHERE i.city = :city
                        AND i.date BETWEEN :startDate AND :endDate
                        AND i.closed = false
                        AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomCount
            GROUP BY i.hotel, i.room
            HAVING COUNT(i.date) = :dateCount
            """
    )
    Page<Hotel> findHotelWithAvailableInventory(
            @Param("city") String city,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomsCount,
            @Param("dateCount") long dateCount,
            Pageable pageable
    );


    @Query("""
            SELECT i
            FROM Inventory i
            WHERE i.room.id = :roomId
                  AND i.date >= :startDate
                  AND i.date < :endDate
                  AND i.closed = false
                  AND (i.totalCount - i.bookedCount - i.reservedCount) >= :roomCount
            """)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Inventory> finalAndLockAvailableInventory(
            @Param("roomId") Long roomId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            @Param("roomCount") Integer roomsCount
    );

    List<Inventory> findByHotelAndDateBetween(Hotel hotel, LocalDate startDate, LocalDate endDate);

}
