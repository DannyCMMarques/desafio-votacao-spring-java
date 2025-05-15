package com.crud.demo.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.crud.demo.domain.Sessao;
@Repository
public interface SessaoRepository extends JpaRepository<Sessao,Long>{

}
