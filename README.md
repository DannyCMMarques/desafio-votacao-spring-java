# 🗳️ Desafio Técnico - API de Votação para Cooperativas

## 📋 Descrição do Desafio

No cooperativismo, cada associado possui direito a um voto, e as decisões são tomadas por meio de assembleias. Este desafio consiste no desenvolvimento de uma **API REST** em Java com Spring Boot para gerenciar sessões de votação via dispositivos móveis. A solução deve ser hospedada na nuvem e permitir:

- ✅ Cadastro de novas pautas
- ✅ Abertura de sessões de votação com tempo determinado (default de 1 minuto)
- ✅ Registro de votos únicos por associado em cada pauta (`Sim` ou `Não`)
- ✅ Contabilização dos votos e retorno do resultado da votação
- ✅ Persistência dos dados mesmo após reinício da aplicação

---

## 🚀 Tecnologias Utilizadas

- Java 17
- Spring Boot
- Spring Data JPA
- Spring AOP
- Spring Scheduling (`@Scheduled`) 
- Banco de dados H2 / PostgreSQL
- Swagger/OpenAPI
- JUnit e Mockito
- Flyway 
- Docker 
- Lombok
- MapStruct 

## 🏗️ Arquitetura

A aplicação segue uma arquitetura baseada em:

- **Camada Controller:** Responsável por receber e responder às requisições da API.
- **Camada Service:** Contém a lógica de negócio.
- **Camada Repository:** Abstrai a comunicação com o banco de dados.
- **Camada DTOs e Mappers:** Facilita a comunicação entre camadas e separa entidades do modelo de domínio.
- **Camada de Exceptions:** Trata erros específicos e retorna mensagens amigáveis para o cliente.

---

## 🐳 Como Clonar e Rodar o Projeto

```bash
git clone https://github.com/DannyCMMarques/desafio-votacao-spring-java.git
cd demo
docker-compose build
docker-compose up
```

---


## 📚 Documentação da API (Swagger)


A documentação da API está disponível via Swagger UI, permitindo explorar e testar os endpoints diretamente pelo navegador.

🔗 Acesse: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)

> Certifique-se de que a aplicação está rodando localmente antes de acessar o link.

---
## 🧪 Testes Automatizados

Para garantir a confiabilidade da aplicação e a robustez das regras de negócio, foram implementados testes automatizados com foco em:

- **Testes de Integração nos Controllers:** para validar o comportamento completo da aplicação em cenários reais de requisição e resposta HTTP.
- **Testes Unitários nas Camadas de Serviço:** utilizando `Mockito` para isolar dependências e garantir que cada unidade da lógica funcione corretamente.

### 📦 Tecnologias utilizadas nos testes

- [✅] JUnit 5
- [✅] Mockito
- [✅] Spring Boot Test
- [✅] MockMvc (para testes com endpoints REST)


### ▶️ Executar os testes


Para executar todos os testes automatizados da aplicação, use o comando abaixo:

```bash
mvn test
````
---

## 📌 Funcionalidades da API

| Módulo                 | Descrição                                                                                  |
|------------------------|---------------------------------------------------------------------------------------------|
| **CRUD Associados**    | Cadastro, leitura, atualização, remoção e listagem com paginação e ordenação               |
| **CRUD Pautas**        | Cadastro, leitura, atualização, remoção e listagem com paginação e ordenação               |
| **CRUD Sessões**       | Cadastro, leitura, atualização, remoção e listagem com paginação e ordenação               |
| **Iniciar Sessão**     | Iniciar uma sessão de votação com `PATCH /sessoes/{id}/start`                              |
| **Lógica de Votação**  | Realizar votação com `POST /sessoes/{id}/votar`                                            |

---

##  🔍  Entendendo o Fluxo Principal

Esta seção apresenta um exemplo real do fluxo da aplicação, com destaque para a estrutura dos DTOs, o comportamento da sessão e os principais cenários de erro tratados.

---

### 1. Criação de um associado 
**POST /associados**

```json
{
  "nome": "Danielly Marques",
  "cpf": "12345678111"
}

```

**Resposta:**

```json
{
  "id": 3,
  "nome": "Danielly Marques"
}
```
**GET /associados?page=1&size=10**
```json
{
  "content": [
    {
      "id": 2,
      "nome": "Aline"
    },
    {
      "id": 1,
      "nome": "João da Silva"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "empty": false,
      "sorted": true,
      "unsorted": false
    }
{...}
  }
}
```
- > Ao visualizar os dados dos associados não será permitido a visualização do CPF desses, além disso foi implementado a páginação com ordenação.
  > Não é possível excluir um associado que já tenha votado.
  
---

### 2. Criação de uma Pauta a Ser Votada

**POST /pauta**

**Resposta:**
```json
{
  "id": 3,
  "titulo": "Criar um novo artigo",
  "descricao": "Proposta de criação de artigo.",
  "status": "NAO_VOTADA"
}
```
- >A pauta possui três **status principais**, que são gerenciados automaticamente pela aplicação: `NAO_VOTADA`: Pauta criada e disponível para ser votada em uma sessão, `EM_VOTACAO`: Sessão de votação iniciada e em andamento. Uma vez em votação, ela não pode ser escolhida novamente para uma nova sessão e `VOTADA`: Votação encerrada, com resultado consolidado.
  >Pautas só podem ser excluídas/atualizadas se estiverem com status NAO_VOTAD


### 3. Criar uma Sessão

**POST /sessao**

**Resposta:**

```json
{
  "id": 3,
  "pauta": {
    "id": 3,
    "titulo": "Criar um novo artigo",
    "descricao": "Proposta de criação de artigo.",
    "status": "NAO_VOTADA"
  },
  "duracao": 5,
  "status": "NAO_INICIADA"
}
```
##### Ao criar uma sessão, é informada a **duração** desejada para essa pauta, bem como a **unidade de tempo**, que pode ser:

##### - SEG` (segundos)
##### - MIN` (minutos)
##### - H` (horas)

- > Essa abordagem oferece **flexibilidade na requisição**, enquanto internamente o valor é salvo **padronizado em minutos no banco de dados**, garantindo consistência.


#####  Exemplos de Entrada
##### - Exemplo 1: Entrada em segundos
  
  ##### **Requisição:**
```json
{
  "idPauta": 1,
  "duracao": 30,
  "unidade": "SEG"
}
```
##### **Resposta:**

```json
{
  "id": 2,
  "pauta": { ...},
  "duracao": 0.5,
  "status": "NAO_INICIADA"
}
````
##### - Exemplo 2: Entrada em horas
  
#####  **Requisição:**

```json
{
  "idPauta": 4,
  "duracao": 5,
  "unidade": "H"
}
````
##### **Resposta:**

```json
{
  "id": 4,
  "pauta": {  ...
  },
  "duracao": 300,
  "status": "NAO_INICIADA"
}
```

---

### 4. Inicializando a sessão 

**PATCH /sessao/{id}/start**

```json
{
  "id": 3,
  "pauta": {
    "id": 3,
    "titulo": "Criar um novo artigo",
    "descricao": "Proposta de criação de artigo.",
    "status": "EM_VOTACAO",
    "votosContra": 0,
    "votosFavor": 0,
    "votosTotais": 0,
    "resultado": "EM_VOTACAO"
  },
  "duracao": 5,
  "status": "EM_ANDAMENTO",
  "horarioInicio": "19/05/2025 18:49:12",
  "horarioFim": null,
  "votos": []
}
```
- >Ao inicializar uma sessão por meio de uma requisição `PATCH`, os seguintes comportamentos são acionados: A sessão recebe o **horário atual como `horarioInicio`**, O status da sessão é alterado de `NAO_INICIADA` para `EM_ANDAMENTO`,É retornado o DTO atualizado com os novos campos: `horarioInicio`, `horarioFim` (inicialmente `null`) e a lista de `votos`.
  > Não é possível excluir ou editar uma sessão que já esteja em andamento 
 
---

### 5. Votar

**POST sessao/{idSessao}/votar**

**Request:**

```json
{
  "voto": true,
  "idAssociado":1
  }
```
**Resposta:**

```json

{
  "id": 1,
  "voto": "SIM",
  "associado": {
    "id": 1,
    "nome": "João Atualizado"
  }
}
```
---

### 6. Ver Sessão em Andamento
**GET sessao/{id}**

**Exemplo de resposta após votos:**

```json
 {
      "id": 3,
      "pauta": {
        "id": 3,
        "titulo": "Criar um novo artigo",
        "descricao": "Proposta de criação de artigo.",
        "status": "EM_VOTACAO",
        "votosContra": 0,
        "votosFavor": 2,
        "votosTotais": 2,
        "resultado": "EM_VOTACAO"
      },
      "duracao": 5,
      "status": "EM_ANDAMENTO",
      "horarioInicio": "19/05/2025 18:49:12",
      "horarioFim": null,
      "votos": [
        {
          "id": 2,
          "voto": "SIM",
          "associado": {
            "id": 1,
            "nome": "João Atualizado"
          }
        },
        {
          "id": 3,
          "voto": "SIM",
          "associado": {
            "id": 3,
            "nome": "Danielly Marques"
          }
        }
        }
      ]
    }
```
- > Atualização Dinâmica do Resultado: Durante a sessão a cada voto recebido, a lista `votos` da sessão é atualizada. O DTO `PautaResultadoDTO` entra em ação para refletir em tempo real:`votosFavor`, `votosContra`, `votosTotais`

### 7. Com o encerramento da sessão é possível visualizar como ficou a pauta 

** GET pauta/{id} ** 
### Pauta:

```json
{
  "id": 3,
  "status": "VOTADA",
  "votosContra": 1,
  "votosFavor": 4,
  "votosTotais": 5,
  "resultado": "APROVADO"
}
```
- > **Nota:** O campo `resultado` (ex: `APROVADO`, `REPROVADO`, `INDECISIVO`) só é efetivamente preenchido após o encerramento automático da sessão via `@Scheduled`, garantindo que os votos considerados sejam apenas os registrados dentro do tempo limite.
---
Essa abordagem torna o fluxo de votação **totalmente autônomo e confiável**, sem necessidade de intervenção externa para encerrar sessões ou calcular resultados.

### 8- Como já ficou a sessão após ela ser fechada

** GET sessao/{id} **

```json

{
      "id": 3,
      "pauta": {
        "id": 3,
        "titulo": "Criar um novo artigo",
        "descricao": "Proposta de criação de artigo.",
        "status": "VOTADA",
        "votosContra": 1,
        "votosFavor": 4,
        "votosTotais": 5,
        "resultado": "APROVADO"
      },
      "duracao": 5,
      "status": "FINALIZADA",
      "horarioInicio": "19/05/2025 18:49:12",
      "horarioFim": "19/05/2025 18:54:16",
      "votos": [
        {
          "id": 2,
          "voto": "SIM",
          "associado": {
            "id": 1,
            "nome": "João Atualizado"
          }
        },
        {
          "id": 3,
          "voto": "SIM",
          "associado": {
            "id": 3,
            "nome": "Danielly Marques"
          }
        },
        {
          "id": 4,
          "voto": "SIM",
          "associado": {
            "id": 4,
            "nome": "Jhonathan"
          }
        },
        {
          "id": 5,
          "voto": "SIM",
          "associado": {
            "id": 5,
            "nome": "Gisele"
          }
        },
        {
          "id": 6,
          "voto": "NAO",
          "associado": {
            "id": 6,
            "nome": "Maria"
          }
        },
        {
          "id": 7,
          "voto": "NAO",
          "associado": {
            "id": 7,
            "nome": "Luisa"
          }
        }
      ]
    }
  ],
```
- > Quando uma sessão expira, ela é encerrada automaticamente, e os seguintes efeitos ocorrem:O campo `horarioFim` é preenchido, O status da sessão muda para `FINALIZADA`e a pauta associada é marcada como `VOTADA`.

---
### ⚠️ Cenários de Erro (Tratamento de erros e exceções)
A aplicação trata adequadamente os erros mais comuns durante o processo de votação, retornando respostas com status HTTP apropriados e mensagens claras para o cliente. Isso garante robustez, previsibilidade e boa experiência de uso.

#### Ao cadastrar um CPF já cadastrado

```json
{
  "status": "CONFLICT",
  "message": "Já existe um associado com esse CPF cadastrado."
}
```

#### Evitar que um mesmo associado vote mais de uma vez na mesma pauta 

```json
{
  "status": "CONFLICT",
  "message": "Esse associado já votou nessa pauta."
}
```

#### Evitar o registro de votos após a sessão ser encerrada

```json
{
  "status": "UNPROCESSABLE_ENTITY",
  "message": "A sessão de votação já foi encerrada."
}
```

#### Evitar o registro de votos antes da sessão ser inicializada

```json
{
  "status": "UNPROCESSABLE_ENTITY",
  "message": "Essa ação não é possível ser realizada, pois, a sessão ainda não foi inicializada"
}
```
---

