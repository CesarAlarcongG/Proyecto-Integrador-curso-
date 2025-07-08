package com.example.backendintegrador.persistence.repository;

import com.example.backendintegrador.persistence.entity.Agencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgenciaRepository extends JpaRepository<Agencia, Integer> {
}