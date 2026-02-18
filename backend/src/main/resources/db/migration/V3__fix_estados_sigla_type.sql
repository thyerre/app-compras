-- Fix: change estados.sigla from CHAR(2) to VARCHAR(2) to match JPA entity mapping
ALTER TABLE estados ALTER COLUMN sigla TYPE VARCHAR(2);
