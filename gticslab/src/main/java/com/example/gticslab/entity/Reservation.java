package com.example.gticslab.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reservations", schema = "unlp_events")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @NotNull(message = "El evento es obligatorio para una reserva")
    private Event event;

    @NotBlank(message = "El nombre del reservante no puede estar vacío")
    @Size(max = 100, message = "El nombre del reservante no puede exceder 100 caracteres")
    @Column(name = "reserver_name", nullable = false, length = 100)
    private String reserverName;

    @NotBlank(message = "El correo del reservante no puede estar vacío")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Size(max = 100, message = "El correo del reservante no puede exceder 100 caracteres")
    @Column(name = "reserver_email", nullable = false, length = 100)
    private String reserverEmail;

    @NotNull(message = "El número de cupos es obligatorio")
    @Min(value = 1, message = "Debe reservar al menos un cupo")
    @Column(name = "number_of_seats", nullable = false)
    private Integer numberOfSeats;
}
