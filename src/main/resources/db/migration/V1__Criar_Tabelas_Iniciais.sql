CREATE TABLE produtos (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(255) NOT NULL,
    unidade_medida VARCHAR(50) NOT NULL
);

CREATE TABLE historico_compras (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    data_compra DATE NOT NULL,
    quantidade INT NOT NULL,
    preco_unitario DECIMAL(10, 2) NOT NULL,
    fornecedor VARCHAR(255),
    CONSTRAINT fk_produto FOREIGN KEY (produto_id) REFERENCES produtos(id)
);
