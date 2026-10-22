# Stock Order Manager 📦

Sistema Backend desenvolvido com Spring Boot, Java 21, Spring Data JPA e Bean Validation para gerenciamento de estoque, catálogo de produtos e processamento de pedidos.

---

## 🛠️ Tecnologias e Conceitos Aplicados
- **Java Moderno:** Records para DTOs, Streams API, `BigDecimal` para precisão financeira.
- **Spring Boot:** Spring Web, Spring Data JPA, Bean Validation (`@Valid`, `@NotNull`, `@NotBlank`, `@Positive`).
- **Arquitetura:** Camadas bem definidas (Controller, Service, Repository, DTO, Entity).
- **Integridade de Dados:** Controle transacional de atomicidade (`@Transactional`), histórico de preço unitário e sincronização automática de estoque na criação e cancelamento de pedidos.

---

## 🎯 Funcionalidades e Regras de Negócio

### 🛍️ Módulo de Produtos
- [x] **Listagem de Produtos:** Retorna todo o catálogo cadastrado.
- [x] **Busca por ID:** Retorna dados detalhados do produto ou `404 NOT_FOUND`.
- [x] **Cadastro de Produto:** Valida campos obrigatórios e impede nomes duplicados (`400 BAD_REQUEST`).
- [x] **Edição de Produto:** Atualiza dados garantindo que o novo nome não pertença a outro produto existente.
- [x] **Remoção de Produto:** Exclui o produto do banco.
- [x] **Baixa Manual de Estoque:** Subtrai quantidade do saldo disponível com validação de limite.
- [x] **Reposição de Estoque:** *(Pendente de implementação)*

### 🛒 Módulo de Pedidos
- [x] **Criação de Pedido:** Valida saldo em estoque de cada item, deduz do estoque, grava o preço unitário do momento e calcula o valor total acumulado.
- [x] **Listagem e Consulta por ID:** Retorna o histórico de pedidos e seus itens associados.
- [x] **Cancelamento de Pedido:** Atualiza o status para `CANCELADO`, devolve os itens ao estoque e impede cancelamentos duplicados.

---

## 🗺️ Mapeamento de Endpoints REST

### Products (`/produtos`)
| Verbo | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/produtos` | Listar todos os produtos |
| `GET` | `/produtos/{id}` | Buscar produto por ID |
| `POST` | `/produtos` | Cadastrar novo produto |
| `PUT` | `/produtos/{id}` | Atualizar produto existente |
| `DELETE` | `/produtos/{id}` | Deletar produto |
| `PUT` | `/produtos/{id}/baixa-estoque?quantidade=X` | Dar baixa manual no estoque |
| `PUT` | `/produtos/{id}/repor-estoque?quantidade=X` | Repor estoque de um produto |
### Orders (`/pedidos`)
| Verbo | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/pedidos` | Criar novo pedido (com abate automático de estoque) |
| `GET` | `/pedidos` | Listar todos os pedidos |
| `GET` | `/pedidos/{id}` | Buscar pedido por ID |
| `PUT` | `/pedidos/{id}/cancelar` | Cancelar pedido e estornar itens ao estoque |