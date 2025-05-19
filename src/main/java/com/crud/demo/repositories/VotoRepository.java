package com.crud.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.demo.domain.Voto;

@Repository
public interface VotoRepository extends JpaRepository<Voto,Long> {
boolean existsByAssociadoId(Long id);
}
