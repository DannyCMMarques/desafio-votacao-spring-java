CREATE TABLE voto(
    id SERIAL PRIMARY KEY, 
    id_sessao INTEGER NOT NULL, 
    id_associado INTEGER NOT NULL,
    voto BOOLEAN
);
ALTER TABLE voto
ADD CONSTRAINT fk_voto_associado
FOREIGN KEY (id_associado)
REFERENCES associados(id);