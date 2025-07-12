package com.example.backendintegrador.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusDTO {

    private Integer idCarro;

    @NotBlank(message = "La placa es obligatoria")
    private String placa;

    @NotNull(message = "El conductor es obligatorio")
    private Integer idConductor;

    private ConductorDTO conductorDTO;
}
