package com.example.backendintegrador.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViajeDTO {

    private Integer idRutas;

    @NotNull(message = "La hora de salida es obligatoria")
    private LocalTime horaSalida;

    @NotNull(message = "La fecha de salida es obligatoria")
    @FutureOrPresent(message = "La fecha de salida debe ser hoy o en el futuro")
    private LocalDate fechaSalida;

    @Positive(message = "El costo debe ser mayor que cero")
    private Double costo;

    @NotNull(message = "La ruta es obligatoria")
    private Integer idRuta;

    @NotNull(message = "El bus es obligatorio")
    private Integer idCarro;

    private BusDTO busDTO;
    private RutaDTO rutaDTO;
}
