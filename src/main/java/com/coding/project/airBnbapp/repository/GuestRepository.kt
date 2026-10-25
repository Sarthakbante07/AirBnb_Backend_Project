package com.coding.project.airBnbapp.repository

import com.coding.project.airBnbapp.entity.Guest
import org.springframework.data.jpa.repository.JpaRepository

interface GuestRepository : JpaRepository<Guest, Long> {
}