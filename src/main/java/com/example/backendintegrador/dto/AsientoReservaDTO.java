package com.example.backendintegrador.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AsientoReservaDTO {

    @NotNull(message = "El asiento es obligatorio")
    private Integer idAsiento;

    @NotNull(message = "El usuario es obligatorio")
    private Integer idUsuario;
}