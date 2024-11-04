package com.example.gticslab.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.gticslab.entity.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findByDate(LocalDate date);
    List<Event> findAllByOrderByDateAsc();
}
