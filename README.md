# 🔍 Radar de Licitações — Backend

API REST para **auditoria preventiva de compras públicas**, desenvolvida para a Prefeitura de Irecê. O sistema utiliza modelagem estatística (Distribuição Normal e Escore-Z) para detectar automaticamente riscos de superfaturamento em licitações.

---

## 📋 Sumário

- [Como Funciona](#-como-funciona)
- [Arquitetura e Stack](#%EF%B8%8F-arquitetura-e-stack)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Fluxo da Informação (DDD)](#-fluxo-da-informação-ddd)
- [A Estatística Aplicada](#-a-estatística-aplicada)
- [Banco de Dados](#-banco-de-dados)
- [Endpoints da API](#-endpoints-da-api)
- [Como Executar](#-como-executar)
- [Padrões de Código](#-padrões-de-código)

---

## 🧠 Como Funciona

1. **A Base de Dados (O Passado):** O sistema possui um histórico de compras da prefeitura. Por exemplo, sabe-se por quanto a prefeitura comprou "Amoxicilina 500mg" nos últimos 2 anos.
2. **A Entrada (O Presente):** Um pregoeiro vai lançar um novo edital ou analisar uma proposta vencedora e insere o preço proposto no sistema (ex: R$ 0,85 por comprimido).
3. **O Cérebro (A Estatística):** O sistema calcula a **Média (μ)** e o **Desvio Padrão (σ)** do histórico da Amoxicilina. Em seguida, calcula o **Escore-Z** do novo preço.
4. **A Saída (O Alerta):** Se o Escore-Z for maior que 2 (ou seja, o preço está a mais de 2 desvios padrão acima da média histórica), a API retorna um alerta de **Risco de Superfaturamento**.

---

## ⚙️ Arquitetura e Stack

| Tecnologia | Versão | Finalidade |
|---|---|---|
| **Java** | 21 (LTS) | Linguagem principal |
| **Spring Boot** | 4.1.0 | Framework da API REST |
| **PostgreSQL** | 15 | Banco de dados relacional |
| **Redis** | 7 | Cache de aplicação |
| **Flyway** | - | Versionamento de migrações SQL |
| **Apache Commons Math3** | 3.6.1 | Cálculos estatísticos (Distribuição Normal) |
| **Springdoc OpenAPI** | 2.8.5 | Documentação Swagger UI interativa |
| **ModelMapper** | 3.2.1 | Conversão de entidades para DTOs |
| **Lombok** | - | Redução de boilerplate |
| **Docker Compose** | 3.8 | Orquestração de containers |

---

## 📁 Estrutura do Projeto

A arquitetura segue o padrão **Domain-Driven Design (DDD)** com isolamento de responsabilidades:

```
src/main/java/br/com/jorgeargolo/radar_licitacoes_backend/
├── config/
│   ├── cache/
│   │   ├── RedisCacheConfig.java            # Configuração do Redis com serialização JSON
│   │   └── CustomCacheErrorHandler.java     # Tolerância a falhas quando o Redis está offline
│   └── swagger/
│       └── SwaggerConfig.java               # Configuração do OpenAPI/Swagger com JWT
├── infraestructure/
│   ├── model/
│   │   ├── PersistenceEntity.java           # Classe base com ID do tipo UUID
│   │   └── SimplePersistenceEntity.java     # Classe base com ID do tipo Long
│   └── util/
│       └── ResultError.java                 # Utilitário para erros de validação (BindingResult)
└── modules/
    └── radarlicitacao/
        ├── produto/                         # Domínio: Catálogo de Produtos
        │   ├── controller/
        │   ├── dto/request/ & dto/response/
        │   ├── model/
        │   ├── repository/
        │   └── service/
        ├── historicocompra/                 # Domínio: Histórico de Compras
        │   ├── controller/
        │   ├── dto/request/ & dto/response/
        │   ├── model/
        │   ├── repository/
        │   │   └── projection/              # Projection para AVG/STDDEV via SQL nativo
        │   └── service/
        └── analise/                         # Domínio: Análise Estatística (sem @Entity)
            ├── controller/
            ├── dto/request/ & dto/response/
            └── service/
```

---

## 🔄 Fluxo da Informação (DDD)

A arquitetura segue três domínios com responsabilidades bem definidas. O domínio de **Análise** age como orquestrador:

```
┌─────────────┐      ┌─────────────────────┐      ┌────────────────────┐
│   Produto   │      │  Histórico Compra   │      │      Análise       │
│ (O Catálogo)│      │    (O Passado)      │      │  (A Inteligência)  │
├─────────────┤      ├─────────────────────┤      ├────────────────────┤
│ O QUE está  │      │ POR QUANTO e QUANDO │◄─────│ Consulta média e   │
│ sendo       │◄─────│ foi comprado        │      │ desvio padrão      │
│ comprado    │      │                     │      │                    │
│             │      │ AVG + STDDEV via    │      │ Calcula Escore-Z   │
│             │      │ query nativa SQL    │      │ Gera alerta        │
└─────────────┘      └─────────────────────┘      └────────────────────┘
```

### Domínio: Produto
Gerencia o catálogo de itens da prefeitura.
- `salvarProduto` · `listarProdutos` · `buscarPorId`

### Domínio: Histórico de Compra
Gerencia o banco de dados de compras anteriores e processa agregações estatísticas direto no PostgreSQL.
- `salvarHistorico` · `listarHistoricoPorProduto`
- **Query nativa:** `AVG(preco_unitario)`, `STDDEV(preco_unitario)`, `COUNT(*)`

### Domínio: Análise (sem tabela no banco)
Puro processamento de dados. Recebe um preço proposto, consulta o Histórico, calcula o Escore-Z e retorna o veredito.
- `analisarProposta(produtoId, precoProposto)`

> **Vantagem:** Se no futuro a fórmula estatística for substituída por um modelo de Machine Learning, apenas o domínio **Análise** sofrerá alterações.

---

## 📊 A Estatística Aplicada

O sistema utiliza a **Distribuição Normal** e o **Escore-Z** para identificar anomalias:

$$Z = \frac{x - \mu}{\sigma}$$

| Variável | Descrição |
|---|---|
| **x** | Preço da licitação atual (proposto) |
| **μ** | Média dos preços históricos do produto |
| **σ** | Desvio Padrão dos preços históricos |

**Regra de negócio:** ~95% dos preços justos caem entre -2σ e +2σ. Se **Z > 2**, é uma anomalia estatística e suspeita de sobrepreço.

### Casos Extremos (Edge Cases)

| Cenário | Proteção |
|---|---|
| **Desvio Padrão = 0** (todos os preços iguais) | Se preço ≤ média → aprovado. Se preço > média × 1.05 → alerta (margem de 5%) |
| **Menos de 3 amostras** | Retorna "Amostragem Insuficiente" e sugere análise manual pelo pregoeiro |
| **Economia de escala** | A tabela armazena `quantidade` para filtros futuros por volume similar |

---

## 🗄️ Banco de Dados

### Modelo Relacional

```sql
produtos                          historico_compras
┌──────────────────┐              ┌─────────────────────────┐
│ id (BIGSERIAL)   │──────────┐   │ id (BIGSERIAL)          │
│ nome (VARCHAR)   │          └──►│ produto_id (FK)         │
│ unidade_medida   │              │ data_compra (DATE)      │
└──────────────────┘              │ quantidade (INT)        │
                                  │ preco_unitario (DEC)    │
                                  │ fornecedor (VARCHAR)    │
                                  └─────────────────────────┘
```

### Migrações Flyway

| Arquivo | Descrição |
|---|---|
| `V1__Criar_Tabelas_Iniciais.sql` | Cria as tabelas `produtos` e `historico_compras` |
| `V2__Inserir_Dados_Teste.sql` | Insere dados de teste (4 produtos, ~30 registros de compras) |

### Dados de Teste Pré-carregados

| Produto | Cenário de Teste | Preço p/ Teste |
|---|---|---|
| Amoxicilina 500mg | Variação normal (média ~R$ 0,50) | Propor R$ 0,85 → Z > 2 (alerta) |
| Paracetamol 750mg | Desvio Padrão = 0 (preço fixo R$ 0,20) | Testar divisão por zero |
| Asfalto CBUQ | Valores altos (média ~R$ 496/ton) | Propor R$ 600 |
| Tomógrafo X-Ray 3000 | Apenas 1 registro | Testar amostragem insuficiente |

---

## 🌐 Endpoints da API

### Produto — `/api/v1/radar-licitacao/produtos`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/` | Cadastra um novo produto |
| `GET` | `/` | Lista produtos (paginado) |
| `GET` | `/{id}` | Busca produto por ID (com cache Redis) |

### Histórico de Compra — `/api/v1/radar-licitacao/historico-compras`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/` | Registra uma nova compra no histórico |
| `GET` | `/produto/{produtoId}` | Lista compras de um produto (paginado) |

### Análise — `/api/v1/radar-licitacao/analise-licitacoes`
| Método | Rota | Descrição |
|---|---|---|
| `POST` | `/{id}/analisar` | Analisa risco de superfaturamento |

**Exemplo de request para análise:**
```json
POST /api/v1/radar-licitacao/analise-licitacoes/1/analisar

{
  "precoProposto": 0.85
}
```

**Exemplo de response (alerta de risco):**
```json
{
  "riscoSuperfaturamento": true,
  "mensagem": "Risco de Superfaturamento! Preço 99.38% mais caro que os dados da amostra histórica normal.",
  "mediaHistorica": 0.4968,
  "desvioPadrao": 0.0283,
  "escoreZ": 12.47,
  "probabilidade": 0.9938,
  "quantidadeAmostras": 19
}
```

> 📖 Documentação interativa disponível em: `http://localhost:8081/swagger-ui/index.html`

---

## 🚀 Como Executar

### Pré-requisitos
- **JDK 21** (configurável via `update-alternatives --config java`)
- **Docker** e **Docker Compose**
- **Maven** (ou use o wrapper `./mvnw`)

### 1. Subir os containers (PostgreSQL + Redis)
```bash
docker compose up -d
```

### 2. Rodar a aplicação
```bash
./mvnw spring-boot:run
```

### 3. Acessar a documentação
```
http://localhost:8081/swagger-ui/index.html
```

### Portas utilizadas
| Serviço | Porta Host | Porta Container |
|---|---|---|
| API Spring Boot | 8081 | 8081 |
| PostgreSQL | 5433 | 5432 |
| Redis | 6380 | 6379 |

> As portas no host são diferentes das padrão para evitar conflitos com outros projetos.

### Executar via Docker (produção)
```bash
docker compose up --build
```

---

## 📐 Padrões de Código

### Estrutura de Pastas (por domínio)
```
/modules
  /modulo
    /dominio
      /controller    (quando necessário)
      /dto           (quando necessário)
      /model         (obrigatório)
      /repository    (obrigatório)
      /service       (quando necessário)
      /enums         (quando necessário)
```

### Model (Entidade JPA)
- Herda de `SimplePersistenceEntity` (ID Long) ou `PersistenceEntity` (ID UUID)
- Anotações obrigatórias: `@Entity`, `@Table`, `@Getter`, `@Setter`, `@NoArgsConstructor`, `@AllArgsConstructor`, `@EqualsAndHashCode(callSuper = true)`
- Cada atributo usa `@Column(name = "snake_case")`
- **Não usar** `@Data`

### Repository (Interface)
- Prefixo **I** no nome: `IProdutoRepository`
- Estende `JpaRepository<Entidade, TipoId>`

### Service
- **Interface** com prefixo **I** e Javadoc completo (`@param`, `@return`, `@throws`)
- **Implementação** com `@Slf4j`, `@RequiredArgsConstructor`, `@Service`
- Métodos públicos: `@Transactional` (ou `@Transactional(readOnly = true)` para consultas)
- Conversão DTO → Entidade: `mapearProdutoRequestDTOParaProduto()`
- Conversão Entidade → DTO: `mapearParaProdutoResponseDTO()`

### Controller
- Javadoc + anotações Swagger (`@Operation`, `@Tag`)
- Retorno com **wildcard**: `ResponseEntity<?>`, `ResponseEntity<Page<?>>`
- Endpoints `POST`/`PUT`/`PATCH`: parâmetro `BindingResult` com validação de erros
- Endpoints `GET` estáticos: header `Cache-Control: max-age=60`

### DTOs (Records Java)
- `@Schema` com `description` e `example` em cada campo
- `@JsonProperty` em todos os atributos
- **Request:** validações com mensagem (`@NotNull(message = "...")`)
- **Response:** `@JsonInclude(JsonInclude.Include.NON_NULL)`

### Cache (Redis)
- **Leituras:** `@Cacheable(value = "regiao", key = "#id")`
- **Mutações:** `@CacheEvict(value = "regiao", allEntries = true)` — nunca `@CachePut`
- Tolerância a falhas configurada via `CustomCacheErrorHandler`

### Segurança
- RBAC via `@PreAuthorize("hasAuthority('PERMISSAO')")`

---

## 📝 Licença

Projeto acadêmico desenvolvido para auditoria preventiva de compras públicas de Prefeituras.
