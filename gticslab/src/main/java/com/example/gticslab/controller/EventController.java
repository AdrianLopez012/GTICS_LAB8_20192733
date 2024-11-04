package com.example.gticslab.controller;

import com.example.gticslab.dto.EventDTO;
import com.example.gticslab.entity.Category;
import com.example.gticslab.entity.Event;
import com.example.gticslab.repository.CategoryRepository;
import com.example.gticslab.repository.EventRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping
    public ResponseEntity<?> getEvents(@RequestParam(required = false) String date) {
        List<Event> events;
        if (date != null) {
            try {
                LocalDate filterDate = LocalDate.parse(date);
                events = eventRepository.findByDate(filterDate);
            } catch (Exception e) {
                return new ResponseEntity<>("Formato de fecha inválido. Use YYYY-MM-DD.", HttpStatus.BAD_REQUEST);
            }
        } else {
            events = eventRepository.findAllByOrderByDateAsc();
        }
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable Integer id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (eventOpt.isPresent()) {
            return new ResponseEntity<>(eventOpt.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
        }
    }

    @Transactional
    @PostMapping
    public ResponseEntity<String> createEvent(@RequestBody @Valid EventDTO eventDTO) {
        Optional<Category> categoryOpt = categoryRepository.findById(eventDTO.getCategoryId());
        if (!categoryOpt.isPresent()) {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.BAD_REQUEST);
        }

        Event event = new Event();
        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setCategory(categoryOpt.get());
        event.setMaxCapacity(eventDTO.getMaxCapacity());
        event.setCurrentReservations(0);

        eventRepository.save(event);
        return new ResponseEntity<>("Evento creado con éxito", HttpStatus.CREATED);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<String> updateEvent(@PathVariable Integer id, @RequestBody @Valid EventDTO eventDTO) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (!eventOpt.isPresent()) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
        }

        Event event = eventOpt.get();

        Optional<Category> categoryOpt = categoryRepository.findById(eventDTO.getCategoryId());
        if (!categoryOpt.isPresent()) {
            return new ResponseEntity<>("Categoría no encontrada", HttpStatus.BAD_REQUEST);
        }

        if (eventDTO.getMaxCapacity() < event.getCurrentReservations()) {
            return new ResponseEntity<>("La capacidad máxima no puede ser menor que las reservas actuales", HttpStatus.BAD_REQUEST);
        }

        event.setName(eventDTO.getName());
        event.setDate(eventDTO.getDate());
        event.setCategory(categoryOpt.get());
        event.setMaxCapacity(eventDTO.getMaxCapacity());

        eventRepository.save(event);
        return new ResponseEntity<>("Evento actualizado con éxito", HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Integer id) {
        Optional<Event> eventOpt = eventRepository.findById(id);
        if (!eventOpt.isPresent()) {
            return new ResponseEntity<>("Evento no encontrado", HttpStatus.NOT_FOUND);
        }

        Event event = eventOpt.get();

        if (!event.getReservations().isEmpty()) {
            return new ResponseEntity<>("No se puede eliminar el evento porque tiene reservas asociadas", HttpStatus.BAD_REQUEST);
        }

        eventRepository.deleteById(id);
        return new ResponseEntity<>("Evento eliminado con éxito", HttpStatus.OK);
    }
}
