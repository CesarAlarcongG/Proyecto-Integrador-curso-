package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Viaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje, Integer> {

    @Query("SELECT v FROM Viaje v WHERE v.fechaSalida = :fecha AND v.ruta.idRuta = :rutaId")
    List<Viaje> findByFechaAndRuta(@Param("fecha") LocalDate fecha, @Param("rutaId") Integer rutaId);

    List<Viaje> findByFechaSalidaAndRutaId(LocalDate fechaSalida, Integer rutaId);
}
