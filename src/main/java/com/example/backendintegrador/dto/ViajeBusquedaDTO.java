package com.example.backendintegrador.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ViajeBusquedaDTO {


    private LocalDate fecha;

    @NotNull(message = "La ruta es obligatoria")
    private String nombreRuta;
}