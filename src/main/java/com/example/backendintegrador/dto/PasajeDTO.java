package com.example.backendintegrador.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasajeDTO {

    private Integer idPasaje;

    @Positive(message = "El total a pagar debe ser mayor que cero")
    private Double totalPagar;

    @NotNull(message = "El usuario es obligatorio")
    private Integer idUsuario;

    @NotNull(message = "La ruta es obligatoria")
    private Integer idRuta;

    @NotNull(message = "El viaje es obligatorio")
    private Integer idViaje;

    private List<Integer> asientosIds;
}
