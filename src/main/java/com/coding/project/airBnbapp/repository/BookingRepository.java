package com.coding.project.airBnbapp.repository;

import com.coding.project.airBnbapp.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<Booking, Long> {

}
