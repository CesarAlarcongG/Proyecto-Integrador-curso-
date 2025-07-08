package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Asiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AsientoRepository extends JpaRepository<Asiento, Integer> {
    @Query("SELECT a FROM Asiento a WHERE a.bus.idCarro = :idCarro")
    List<Asiento> findByBusId(@Param("idCarro") Integer idCarro);
    List<Asiento> findByEstado(String estado);
    @Query("SELECT a FROM Asiento a JOIN a.usuarioAsientos ua WHERE ua.usuario.idUsuario = :usuarioId")
    List<Asiento> findByUsuarioId(@Param("usuarioId") Integer usuarioId);
}
