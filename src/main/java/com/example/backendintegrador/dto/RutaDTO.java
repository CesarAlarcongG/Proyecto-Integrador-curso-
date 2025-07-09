package com.example.backendintegrador.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaDTO {

    private Integer idRuta;

    @NotBlank(message = "El nombre de la ruta es obligatorio")
    private String nombre;

    private Integer idAdministrador;

    private List<Integer> agenciasIds;
    private List<Integer> ordenAgencias;

    private List<AgenciaDTO> agenciaDTOS;

}
