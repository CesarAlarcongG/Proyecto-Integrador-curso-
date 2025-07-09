package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Ruta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    @Query("SELECT DISTINCT r FROM Ruta r " +
            "LEFT JOIN FETCH r.rutaAgencias ra " +
            "LEFT JOIN FETCH ra.agencia " +
            "LEFT JOIN FETCH r.actividad a " +
            "LEFT JOIN FETCH a.administrador " +
            "LEFT JOIN FETCH r.viajes v " +
            "LEFT JOIN FETCH v.bus " +
            "LEFT JOIN FETCH r.pasajes p " +
            "LEFT JOIN FETCH p.usuario")
    List<Ruta> findAllWithRelations();
}