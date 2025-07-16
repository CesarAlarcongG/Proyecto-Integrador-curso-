package com.example.backendintegrador.persistence.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class RutaAgencia implements Comparable<RutaAgencia> {
    @EmbeddedId
    private RutaAgenciaId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idRuta")
    @JoinColumn(name = "id_ruta")
    @JsonBackReference
    private Ruta ruta;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idAgencia")
    @JoinColumn(name = "id_agencia")
    private Agencia agencia;

    @Column(name = "orden")
    private Integer orden;

    @Override
    public int compareTo(RutaAgencia otra) {
        return this.orden.compareTo(otra.orden);
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RutaAgenciaId implements Serializable {
        private Integer idRuta;
        private Integer idAgencia;
    }
}

