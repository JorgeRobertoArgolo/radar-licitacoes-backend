-- Inserindo Produtos Base
INSERT INTO produtos (nome, unidade_medida) VALUES
('Amoxicilina 500mg', 'Comprimido'),
('Paracetamol 750mg', 'Comprimido'),
('Asfalto CBUQ', 'Tonelada'),
('Tomógrafo X-Ray 3000', 'Unidade');

-- Amoxicilina 500mg (Produto ID 1) 
-- Variação normal entre 0.45 e 0.55 (Média ~0.50). 
-- Ideal para testar Escore-Z alto, ex: propor 0.85
INSERT INTO historico_compras (produto_id, data_compra, quantidade, preco_unitario, fornecedor) VALUES
(1, '2023-01-10', 10000, 0.45, 'Farma Bem LTDA'),
(1, '2023-02-15', 5000, 0.47, 'Medicamentos Brasil'),
(1, '2023-03-20', 15000, 0.49, 'Farma Bem LTDA'),
(1, '2023-04-12', 12000, 0.51, 'Saúde & Cia'),
(1, '2023-05-18', 8000, 0.53, 'Medicamentos Brasil'),
(1, '2023-06-25', 20000, 0.46, 'Farma Bem LTDA'),
(1, '2023-07-30', 10000, 0.48, 'Saúde & Cia'),
(1, '2023-08-14', 9000, 0.50, 'Medicamentos Brasil'),
(1, '2023-09-05', 11000, 0.52, 'Farma Bem LTDA'),
(1, '2023-10-19', 14000, 0.54, 'Saúde & Cia'),
(1, '2023-11-22', 13000, 0.45, 'Medicamentos Brasil'),
(1, '2023-12-10', 10000, 0.49, 'Farma Bem LTDA'),
(1, '2024-01-08', 25000, 0.51, 'Saúde & Cia'),
(1, '2024-02-16', 7000, 0.53, 'Medicamentos Brasil'),
(1, '2024-03-12', 18000, 0.47, 'Farma Bem LTDA'),
(1, '2024-04-20', 10000, 0.48, 'Saúde & Cia'),
(1, '2024-05-15', 6000, 0.50, 'Medicamentos Brasil'),
(1, '2024-06-25', 12000, 0.52, 'Farma Bem LTDA'),
(1, '2024-07-10', 8000, 0.49, 'Saúde & Cia'),
(1, '2024-08-05', 15000, 0.55, 'Medicamentos Brasil');

-- Paracetamol 750mg (Produto ID 2)
-- Desvio Padrão = 0, preço fixo de 0.20 em todo o histórico.
-- Ideal para testar a regra de proteção contra Divisão por Zero.
INSERT INTO historico_compras (produto_id, data_compra, quantidade, preco_unitario, fornecedor) VALUES
(2, '2023-01-15', 5000, 0.20, 'Fornecedor Fixo A'),
(2, '2023-04-20', 8000, 0.20, 'Fornecedor Fixo A'),
(2, '2023-07-10', 10000, 0.20, 'Fornecedor Fixo A'),
(2, '2023-10-05', 12000, 0.20, 'Fornecedor Fixo B'),
(2, '2024-02-25', 15000, 0.20, 'Fornecedor Fixo A');

-- Asfalto CBUQ (Produto ID 3)
-- Média mais alta, entre 470 e 520
INSERT INTO historico_compras (produto_id, data_compra, quantidade, preco_unitario, fornecedor) VALUES
(3, '2023-02-10', 500, 480.00, 'Construtora Pavimentos'),
(3, '2023-05-20', 1000, 490.50, 'Asfalto Forte'),
(3, '2023-08-15', 800, 510.00, 'Construtora Pavimentos'),
(3, '2023-11-30', 1200, 505.20, 'Vias Seguras LTDA'),
(3, '2024-01-10', 600, 475.00, 'Asfalto Forte'),
(3, '2024-04-05', 900, 520.00, 'Vias Seguras LTDA'),
(3, '2024-07-22', 1500, 495.80, 'Construtora Pavimentos');

-- Tomógrafo X-Ray 3000 (Produto ID 4)
-- Apenas 1 registro (faltam amostras)
-- Ideal para testar a regra de Amostragem Insuficiente (< 3)
INSERT INTO historico_compras (produto_id, data_compra, quantidade, preco_unitario, fornecedor) VALUES
(4, '2023-12-01', 1, 1500000.00, 'Siemens Health');
