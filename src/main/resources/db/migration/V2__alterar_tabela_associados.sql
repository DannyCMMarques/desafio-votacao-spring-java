ALTER TABLE associados
ADD CONSTRAINT uq_associado_cpf UNIQUE (cpf);