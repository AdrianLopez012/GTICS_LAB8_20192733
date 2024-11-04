package com.example.gticslab.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDTO {

    @NotNull(message = "El ID del evento es obligatorio")
    private Integer eventId;

    @NotBlank(message = "El nombre del reservante no puede estar vacío")
    @Size(max = 100, message = "El nombre del reservante no puede exceder 100 caracteres")
    private String reserverName;

    @NotBlank(message = "El correo del reservante no puede estar vacío")
    @Email(message = "Debe proporcionar un correo electrónico válido")
    @Size(max = 100, message = "El correo del reservante no puede exceder 100 caracteres")
    private String reserverEmail;

    @NotNull(message = "El número de cupos es obligatorio")
    @Min(value = 1, message = "Debe reservar al menos un cupo")
    private Integer numberOfSeats;
}
