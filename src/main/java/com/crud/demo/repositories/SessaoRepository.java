package com.crud.demo.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crud.demo.domain.Sessao;

@Repository
public interface SessaoRepository extends JpaRepository<Sessao, Long> {

    @Query(value = """
            SELECT s.id
            FROM sessao s
            WHERE s.status = 'EM_ANDAMENTO'
              AND s.horario_inicio
                  + (s.duracao * INTERVAL '1 minute')
                <= :agora
            """, nativeQuery = true)
    List<Long> findIdsVencidas(@Param("agora") LocalDateTime agora);
}
