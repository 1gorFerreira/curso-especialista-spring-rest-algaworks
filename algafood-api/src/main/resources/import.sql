insert into tb_cozinha (id, nome) values (1, 'Tailandesa');
insert into tb_cozinha (id, nome) values (2, 'Indiana');

insert into tb_restaurante (id, nome, taxa_frete, cozinha_id) values (1, 'Thai Gourmet', 10, 1);
insert into tb_restaurante (id, nome, taxa_frete, cozinha_id) values (2, 'Thai Delivery', 9.50, 1);
insert into tb_restaurante (id, nome, taxa_frete, cozinha_id) values (3, 'Tuk Tuk Comida Indiana', 15, 2);

insert into tb_estado (id, nome) values (1, 'Minas Gerais');
insert into tb_estado (id, nome) values (2, 'São Paulo');
insert into tb_estado (id, nome) values (3, 'Ceará');

insert into tb_cidade (id, nome, estado_id) values (1, 'Uberlândia', 1);
insert into tb_cidade (id, nome, estado_id) values (2, 'Belo Horizonte', 1);
insert into tb_cidade (id, nome, estado_id) values (3, 'São Paulo', 2);
insert into tb_cidade (id, nome, estado_id) values (4, 'Campinas', 2);
insert into tb_cidade (id, nome, estado_id) values (5, 'Fortaleza', 3);

insert into tb_forma_pagamento (id, descricao) values (1, 'Cartão de crédito');
insert into tb_forma_pagamento (id, descricao) values (2, 'Cartão de débito');
insert into tb_forma_pagamento (id, descricao) values (3, 'Dinheiro');

insert into tb_permissao (id, nome, descricao) values (1, 'CONSULTAR_COZINHAS', 'Permite consultar cozinhas');
insert into tb_permissao (id, nome, descricao) values (2, 'EDITAR_COZINHAS', 'Permite editar cozinhas');