select * from fornecedores;

INSERT INTO fornecedores (
    razao_social, nome_fantasia, cnpj_cpf, inscricao_estadual,
    tipo_fornecedor_id, classificacao_id,
    cep, logradouro, numero, complemento, bairro,
    municipio_id, estado_id,
    telefone, celular, email,
    responsavel_nome, responsavel_cpf, responsavel_cargo,
    banco_nome, banco_agencia, banco_conta,
    ativo
) VALUES (
    'Tech Solutions Ltda', 'TechSol', '12.345.678/0001-90', '123456789',
    1, 1,
    '01310-100', 'Av. Paulista', '1000', 'Sala 501', 'Bela Vista',
    (SELECT id FROM municipios WHERE nome = 'São Paulo'),
    (SELECT id FROM estados WHERE sigla = 'SP'),
    '(11) 3456-7890', '(11) 98765-4321', 'contato@techsol.com.br',
    'João da Silva', '123.456.789-00', 'Diretor',
    'Banco do Brasil', '1234-5', '56789-0',
    true
);