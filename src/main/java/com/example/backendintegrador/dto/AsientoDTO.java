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

    @NotBlank(message = "El número de asiento es obligatorio")
    private String asiento;

    @PositiveOrZero(message = "El precio no puede ser negativo")
    private Double precio;

    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    private String estado;

    private Integer idBus;
}
