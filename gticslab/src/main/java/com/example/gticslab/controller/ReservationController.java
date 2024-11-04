package com.example.gticslab.controller;

import com.example.gticslab.dto.ReservationDTO;
import com.example.gticslab.entity.Event;
import com.example.gticslab.entity.Reservation;
import com.example.gticslab.repository.EventRepository;
import com.example.gticslab.repository.ReservationRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationRepository.findAll();
        return new ResponseEntity<>(reservations, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable Integer id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (reservationOpt.isPresent()) {
            return new ResponseEntity<>(reservationOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> createReservation(@RequestBody @Valid ReservationDTO reservationDTO) {
        Optional<Event> eventOpt = eventRepository.findById(reservationDTO.getEventId());
        if (!eventOpt.isPresent()) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
        }

        Event event = eventOpt.get();

        if (event.getCurrentReservations() + reservationDTO.getNumberOfSeats() > event.getMaxCapacity()) {
            return new ResponseEntity<>("No hay cupos disponibles para esta reserva", HttpStatus.BAD_REQUEST);
        }

        Reservation reservation = new Reservation();
        reservation.setEvent(event);
        reservation.setReserverName(reservationDTO.getReserverName());
        reservation.setReserverEmail(reservationDTO.getReserverEmail());
        reservation.setNumberOfSeats(reservationDTO.getNumberOfSeats());
        event.setCurrentReservations(event.getCurrentReservations() + reservationDTO.getNumberOfSeats());

        reservationRepository.save(reservation);
        eventRepository.save(event);

        return new ResponseEntity<>("Reserva creada con éxito", HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<String> updateReservation(@PathVariable Integer id, @RequestBody @Valid ReservationDTO reservationDTO) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (!reservationOpt.isPresent()) {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }

        Reservation reservation = reservationOpt.get();
        Event event = reservation.getEvent();

        if (!event.getId().equals(reservationDTO.getEventId())) {
            Optional<Event> newEventOpt = eventRepository.findById(reservationDTO.getEventId());
            if (!newEventOpt.isPresent()) {
                return new ResponseEntity<>("Evento no encontrado", HttpStatus.BAD_REQUEST);
            }
            event = newEventOpt.get();
            reservation.setEvent(event);
        }

        int difference = reservationDTO.getNumberOfSeats() - reservation.getNumberOfSeats();

        if (difference > 0 && (event.getCurrentReservations() + difference) > event.getMaxCapacity()) {
            return new ResponseEntity<>("No hay suficientes cupos disponibles para esta actualización", HttpStatus.BAD_REQUEST);
        }

        reservation.setReserverName(reservationDTO.getReserverName());
        reservation.setReserverEmail(reservationDTO.getReserverEmail());
        reservation.setNumberOfSeats(reservationDTO.getNumberOfSeats());

        event.setCurrentReservations(event.getCurrentReservations() + difference);

        reservationRepository.save(reservation);
        eventRepository.save(event);

        return new ResponseEntity<>("Reserva actualizada con éxito", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteReservation(@PathVariable Integer id) {
        Optional<Reservation> reservationOpt = reservationRepository.findById(id);
        if (!reservationOpt.isPresent()) {
            return new ResponseEntity<>("Reserva no encontrada", HttpStatus.NOT_FOUND);
        }

        Reservation reservation = reservationOpt.get();
        Event event = reservation.getEvent();
        event.setCurrentReservations(event.getCurrentReservations() - reservation.getNumberOfSeats());

        reservationRepository.deleteById(id);
        eventRepository.save(event);

        return new ResponseEntity<>("Reserva eliminada con éxito", HttpStatus.OK);
    }
}
