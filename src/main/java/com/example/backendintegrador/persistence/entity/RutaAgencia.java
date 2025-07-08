package com.example.backendintegrador.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Entity
@Table(name = "ruta_agencia")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RutaAgencia {

    @EmbeddedId
    private RutaAgenciaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idRuta")
    @JoinColumn(name = "id_ruta")
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAgencia")
    @JoinColumn(name = "id_agencia")
    private Agencia agencia;

    @Column(name = "orden")
    private Integer orden;

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RutaAgenciaId implements Serializable {
        private Integer idRuta;
        private Integer idAgencia;
    }
}
