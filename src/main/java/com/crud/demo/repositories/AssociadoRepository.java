package com.crud.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.demo.domain.Associado;

import org.springframework.stereotype.Repository;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {


    public Optional<Associado> findByCpf(String cpf);
}
