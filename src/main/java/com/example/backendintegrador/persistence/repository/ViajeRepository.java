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

    List<Viaje> findByFechaSalidaAndIdRutas(LocalDate fechaSalida, Integer rutaId);
}
