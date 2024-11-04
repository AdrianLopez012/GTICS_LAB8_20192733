package com.example.gticslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gticslab.entity.Reservation;

public interface ReservationRepository extends JpaRepository<Reservation, Integer> {
}
