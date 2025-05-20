CREATE TABLE sessao(
    id SERIAL PRIMARY KEY, 
    id_pauta INTEGER NOT NULL, 
    duracao INTEGER DEFAULT 1, 
    horario_inicio TIMESTAMP, 
    horario_fim TIMESTAMP, 
    status CHARACTER(20) DEFAULT 'NAO_INICIADA'
);


ALTER TABLE sessao
ADD CONSTRAINT fk_sessao_pauta
FOREIGN KEY (id_pauta)
REFERENCES pauta(id);

