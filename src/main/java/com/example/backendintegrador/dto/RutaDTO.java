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

    private String nombre;

    private Integer idAdministrador;

    private List<Integer> agenciasIds;

    private List<AgenciaDTO> agenciaDTOS;

}
