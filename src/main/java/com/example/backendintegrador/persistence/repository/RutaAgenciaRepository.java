package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.RutaAgencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RutaAgenciaRepository extends JpaRepository<RutaAgencia, RutaAgencia.RutaAgenciaId> {
    void deleteByRutaId(Integer idRuta);
}