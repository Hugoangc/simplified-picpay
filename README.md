# Desafio Back-end PicPay - PicPay Simplificado 

![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Apache Kafka](https://img.shields.io/badge/Apache%20Kafka-000?style=for-the-badge&logo=apachekafka)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

Este repositório contém a  solução para o  [Desafio Back-end do PicPay](https://github.com/PicPay/picpay-desafio-backend). O objetivo é criar uma API RESTful de pagamentos simplificada, permitindo transferências de dinheiro entre usuários comuns e lojistas.

##  Funcionalidades

- **Cadastro de Usuários:** Criação de carteiras para Usuários Comuns e Lojistas.
- **Transferências Financeiras:**
  - Validação de saldo.
  - Verificação de tipo de usuário (Lojistas apenas recebem).
  - Garantia de atomicidade (Transações de banco de dados).
- **Autorização Externa:** Consulta a um serviço mock externo para autorizar a transação.
- **Notificações Assíncronas:** Envio de notificações de recebimento de pagamento utilizando **Kafka** para garantir alta performance e desacoplamento.

##  Tecnologias Utilizadas

- **Java 21** & **Spring Boot 3**
- **Spring Data JDBC**: Para persistência simples e eficiente.
- **Spring Kafka**: Para mensageria assíncrona.
- **H2 Database**: Banco de dados em memória (facilidade de execução).
- **Docker & Docker Compose**: Orquestração dos containers (Zookeeper e Kafka).
- **RestClient**: Para consumo de APIs externas.

##  Arquitetura e Design

O projeto segue princípios de **Clean Code** e **SOLID**, com foco em desacoplamento:

1.  **API Layer (Controller):** Recebe as requisições HTTP.
2.  **Service Layer:** Contém as regras de negócio (validações, débitos/créditos).
3.  **Domain Layer:** Records imutáveis (`Transaction`, `Wallet`) representando o núcleo do negócio.
4.  **Async Layer:** Produtores e Consumidores Kafka para lidar com notificações sem bloquear o fluxo principal.

##  Como rodar o projeto

### Pré-requisitos
- Java 21+ instalado.
- Docker e Docker Compose instalados.
- Maven.

### Passo a passo

Suba a infraestrutura (Kafka & Zookeeper):

```Bash
docker-compose up -d
```
Aguarde alguns instantes até o Kafka iniciar completamente.

Execute a aplicação:
```Bash
mvn spring-boot:run
```
A API estará disponível em http://localhost:8080.

📡 Endpoints Principais
Realizar Transação
POST /transaction

Payload:
```JSON
{
  "value": 100.00,
  "payer": 1,
  "payee": 2
}
```
Resposta (Sucesso - 200 OK):
```JSON
{
  "id": 1,
  "payer": 1,
  "payee": 2,
  "value": 100.00,
  "createdAt": "2025-11-22T10:00:00"
}
```
H2 Console (banco de dados em memória):
- URL:``` http://localhost:8080/h2-console```
- JDBC URL: ```jdbc:h2:file:./data/picpay```
- User: ```sa```
- Password: (vazio)

Melhorias:
[x] Uso de Records do Java para imutabilidade e redução de boilerplate.
[x] Implementação de Kafka para lidar com notificações instáveis.
[x] Tratamento global de exceções.
[x] Configuração via Docker Compose.
