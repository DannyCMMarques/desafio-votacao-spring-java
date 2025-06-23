package com.crud.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.crud.demo.domain.Pauta;
import com.crud.demo.domain.enums.StatusPautaEnum;

@Repository
public interface PautaRepository extends JpaRepository<Pauta, Long>, JpaSpecificationExecutor<Pauta> {
List<Pauta> findAllByStatus(StatusPautaEnum statusEnum);
    
}
