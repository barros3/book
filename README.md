
# 📚 Book Management System

Sistema completo para gerenciamento de **Autores, Assuntos e Livros**, desenvolvido com foco em **boas práticas de engenharia de software**, **arquitetura limpa** e **uso avançado de recursos do PostgreSQL**.

Este projeto foi pensado para avaliação técnica, demonstrando não apenas CRUDs básicos, mas também decisões arquiteturais, organização de código e integração entre backend, frontend e banco de dados.

---

## 🎯 Visão Geral

O sistema permite:

- Gerenciar autores, assuntos e livros
- Executar regras de negócio diretamente no banco de dados
- Gerar relatórios consolidados
- Disponibilizar uma API REST documentada
- Consumir a API através de uma SPA em Angular
- A paginação funciona apenas no listar do Swagger.
- O arquivo dados-mockados-biblioteca.sql é opcional 
  caso deseje inserir dados na base.
- Ao realizar exclusão de alguma entidade que já possui 
  um relacionamento, o campo ativo torna-se falso.
  Essa implementação até o momento só é visível
  no banco de dados.

---

## ⚙️ Funcionalidades

### 📌 CRUD
- Autores
- Assuntos
- Livros

Cada recurso possui operações completas de **criação, edição, listagem e remoção**, seguindo o padrão REST.

---

### 🗄️ Processamento no Banco de Dados
- Ao cadastrar um **Livro**, uma **trigger no PostgreSQL** é executada
- Essa trigger atualiza uma **view materialized**
- A maior parte das operações complexas é realizada através de **functions no PostgreSQL**
- Essa abordagem reduz lógica na aplicação e melhora a performance

---

### 📊 Relatórios
- Relatório de autores com:
  - Somatório do valor total dos livros vinculados
- Implementação utilizando **iReport**
- Consumo via backend

---

### 🔄 Mapeamento de Objetos
- Utilização de **Struct Mapper**
- Conversão clara entre:
  - Entidades
  - DTOs
- Código mais limpo e desacoplado

---

## 🧱 Arquitetura e Padrões

- Domain Driven Design (DDD)
- Clean Code
- Padrão RESTful
- Separação clara de camadas
- Testes unitários com foco em comportamento

---

## 🛠️ Tecnologias Utilizadas

### Backend
- Java 17
- Spring Boot
- Spring Data JPA
- Hibernate
- Maven
- Docker
- Mockito
- TDD
- iReport
- Swagger (OpenAPI)

### Frontend
- Angular 17
- NPM
- SPA (Single Page Application)
- Docker

### Banco de Dados
- PostgreSQL
- Functions
- Triggers
- Views Materialized

---

## 🚀 Como Executar o Projeto

### Pré-requisitos

- Docker instalado
- Java SDK 17
- Node.js com NPM **10.5.2**
- Angular CLI 17
- Portas disponíveis:
  - **4200** (Frontend)
  - **8080** (Backend)
  - **5432** (PostgreSQL)

---

### 🐳 Subindo o Ambiente

1. Acesse a pasta raiz do projeto:

```bash
cd book-root
```

2. Suba a infraestrutura e dependências:

```bash
docker-compose up -d --build
```

3. Com a base preparada, execute novamente para subir frontend e backend:

```bash
docker-compose up -d --build
```

---

## 🌐 Acessos

- **Swagger / OpenAPI**
  - http://localhost:8080/swagger

- **Aplicação Web**
  - http://localhost:4200

---

## 🧪 Testes

- Testes unitários implementados com **Mockito**
- Abordagem orientada a **TDD**
- Cobertura focada em regras de negócio

---

## 📌 Considerações Técnicas

- O uso de lógica no banco faz parte da proposta arquitetural
- O projeto prioriza clareza, separação de responsabilidades e manutenibilidade
- O Docker garante ambiente consistente para execução e avaliação

---

## 👨‍💻 Autor

Projeto desenvolvido para fins de estudo e avaliação técnica.

---
