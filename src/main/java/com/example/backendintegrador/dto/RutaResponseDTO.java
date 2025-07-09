package com.example.backendintegrador.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class RutaResponseDTO {
    private Integer idRuta;
    private String nombre;
    private ActividadSimpleDTO actividad;
    private List<RutaAgenciaDTO> agencias;

    @Data
    public static class ActividadSimpleDTO {
        private Integer idActividad;
        private LocalDate fecha;
        private LocalTime hora;
        private String descripcion;
    }

    @Data
    public static class RutaAgenciaDTO {
        private Integer idAgencia;
        private String nombreAgencia;
        private Integer orden;
    }
}
