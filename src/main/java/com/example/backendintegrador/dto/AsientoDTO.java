package com.example.backendintegrador.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsientoDTO {

    private Integer idAsiento;

    @NotNull(message = "El piso es obligatorio")
    @Min(value = 1, message = "El piso mínimo es 1")
    @Max(value = 2, message = "El piso máximo es 2")
    private Integer piso;

    @NotBlank(message = "El posición de la columna es obligatoria")
    private String columna;

    @NotBlank(message = "El posición de la fila es obligatoria")
    private String fila;

    @NotBlank(message = "L descripción es obligatoria")
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    @NotNull(message = "El id bus es obligatorio")
    private Integer idBus;
}