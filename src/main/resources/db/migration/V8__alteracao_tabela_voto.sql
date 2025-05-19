ALTER TABLE voto
ADD CONSTRAINT fk_voto_sessao
FOREIGN KEY (id_sessao)
REFERENCES sessao(id);