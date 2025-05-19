# 🗳️ Desafio Votação

## Objetivo

No cooperativismo, cada associado possui um voto e as decisões são tomadas em assembleias, por votação. Imagine que você deve criar uma solução para dispositivos móveis para gerenciar e participar dessas sessões de votação.

Essa solução deve ser executada na nuvem e promover as seguintes funcionalidades através de uma API REST:

- Cadastrar uma nova pauta
- Abrir uma sessão de votação em uma pauta (a sessão de votação deve ficar aberta por um tempo determinado na chamada de abertura ou 1 minuto por default)
- Receber votos dos associados em pautas (os votos são apenas 'Sim'/'Não'. Cada associado é identificado por um id único e pode votar apenas uma vez por pauta)
- Contabilizar os votos e dar o resultado da votação na pauta

> A segurança das interfaces pode ser abstraída. A solução deve ser construída em Java, usando Spring Boot.

**Requisitos Técnicos Importantes:**
- ✅ Tratamento de erros e exceções
- ✅ Uso de testes automatizados
- ✅ Documentação do código e da API
- ✅ Logs da aplicação
- ✅ Mensagens e organização dos commits
- ✅ Explicação breve das escolhas tomadas

---

## 🚀 Funcionalidades

| Módulo                 | Descrição                                                                 |
|------------------------|---------------------------------------------------------------------------|
| **CRUD Associados**    | Cadastro, leitura, atualização e remoção de associados, com paginação     |
| **CRUD Pautas**        | Cadastro, leitura, atualização e remoção de pautas de votação             |
| **CRUD Sessões**       | Cadastro, leitura, atualização e remoção de sessões, com paginação        |
| **Lógica de Sessão**   | Iniciar sessão com PATCH /sessao/iniciar/{id}                             |
| **Lógica de Votação**  | Recebimento de votos com POST /voto                                       |

---

## 🐳 Como Clonar e Rodar o Projeto

```bash
git clone https://github.com/DannyCMMarques/desafio-votacao-spring-java.git
cd demo
docker-compose build
docker-compose up
```

---

## 🛠️ Documentação Swagger

Acesse: `http://localhost:8080/swagger-ui.html`

---

# 🧪 Passo a Passo para Testar a API

## 1. Criar um Associado

**POST /associados**

```json
{
  "nome": "Danielly Marques",
  "cpf": "12345678111"
}
```

---

## 2. Criar uma Pauta

**POST /pauta**

```json
{
  "id": 3,
  "titulo": "Criar um novo artigo",
  "descricao": "Proposta de criação de artigo.",
  "status": "NAO_VOTADA"
}
```

---

## 3. Criar uma Sessão

**POST /sessao**

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

---

## 4. Iniciar a Sessão

**PATCH /sessao/iniciar/3**

---

## 5. Votar

**POST /voto**

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

## 6. Ver Sessão em Andamento

Exemplo de resposta após votos:

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
        "resultado": "EM_ANDAMENTO"
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

---

## 7. Resultado Final Após Encerramento

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

### Sessão:

```json
{
  "id": 3,
  "status": "FINALIZADA",
  "horarioFim": "19/05/2025 18:54:16"
}
```

---

## 8- Como já ficou a sessão após ela ser fechada
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


## ⚠️ Cenários de Erro

### CPF Já Cadastrado

```json
{
  "status": "CONFLICT",
  "message": "Já existe um associado com esse CPF cadastrado."
}
```

### Pauta Inexistente

```json
{
  "status": "NOT_FOUND",
  "message": "Pauta não encontrada com o ID informado."
}
```

### Sessão Não Encontrada

```json
{
  "status": "NOT_FOUND",
  "message": "Sessão não encontrada com ID informado"
}
```

### Associado Já Votou

```json
{
  "status": "CONFLICT",
  "message": "Esse associado já votou nessa pauta."
}
```

### Sessão Já Encerrada

```json
{
  "status": "UNPROCESSABLE_ENTITY",
  "message": "A sessão de votação já foi encerrada."
}
```

### Sessão Não Iniciada

```json
{
  "status": "UNPROCESSABLE_ENTITY",
  "message": "Essa ação não é possível ser realizada, pois, a sessão ainda não foi inicializada"
}
```

---

## 🕒 Criar Sessão com Diferentes Unidades de Tempo

```json
{
  "idPauta": 1,
  "duracao": 30,
  "unidade": "SEG"
}
```

```json
{
  "idPauta": 4,
  "duracao": 5,
  "unidade": "H"
}
```

---

> ✅ Projeto desenvolvido com foco em qualidade, cobertura de testes, tratamento de erros, documentação e logs.
