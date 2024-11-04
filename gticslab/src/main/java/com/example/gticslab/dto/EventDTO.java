package com.example.gticslab.dto;

import com.example.gticslab.entity.Category;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class EventDTO {

    @NotBlank(message = "El nombre del evento no puede estar vacío")
    @Size(max = 100, message = "El nombre del evento no puede exceder 100 caracteres")
    private String name;

    @NotNull(message = "La fecha del evento es obligatoria")
    @Future(message = "La fecha del evento debe ser en el futuro")
    private LocalDate date;

    @NotNull(message = "La categoría del evento es obligatoria")
    private Integer categoryId;

    @NotNull(message = "La capacidad máxima del evento es obligatoria")
    @Min(value = 1, message = "La capacidad máxima debe ser al menos 1")
    private Integer maxCapacity;
}
