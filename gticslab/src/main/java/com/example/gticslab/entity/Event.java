package com.example.gticslab.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "events", schema = "unlp_events")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "reservations"})
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre del evento no puede estar vacío")
    @Size(max = 100, message = "El nombre del evento no puede exceder 100 caracteres")
    @Column(nullable = false, length = 100)
    private String name;

    @NotNull(message = "La fecha del evento es obligatoria")
    @Future(message = "La fecha del evento debe ser en el futuro")
    @Column(nullable = false)
    private LocalDate date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @NotNull(message = "La categoría del evento es obligatoria")
    private Category category;

    @NotNull(message = "La capacidad máxima del evento es obligatoria")
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
    @Column(name = "max_capacity", nullable = false)
    private Integer maxCapacity;

    @NotNull
    @Min(value = 0, message = "El número de reservas actuales no puede ser negativo")
    @Column(name = "current_reservations", nullable = false)
    private Integer currentReservations = 0;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations;
}
