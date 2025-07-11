# Attus – Plataforma de Processos

[![CI Build](https://github.com/joaovneres/attus-backend/actions/workflows/ci.yml/badge.svg)](https://github.com/joaovneres/attus-backend/actions/workflows/ci.yml)
[![Coverage](https://img.shields.io/badge/coverage-76%25-yellow)]()
[![Sonarcloud](https://sonarcloud.io/api/project_badges/measure?project=joaovneres_attus-backend\&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=joaovneres_attus-backend)
[![Image Size](https://img.shields.io/docker/image-size/ghcr.io/joaovneres/attus-backend/latest-api)](https://github.com/joaovneres/attus-backend/pkgs/container/attus-backend)

## 📖 Visão Geral

Repositório para a avaliação de backend — implementa uma API REST para gerenciamento de processos jurídicos, com modelos hexagonais, CQRS assíncrono via Kafka e deploy em Kubernetes/Helm.

## 🎯 Público-Alvo

* Avaliadores técnicos
* Desenvolvedores interessados na arquitetura

## 🚀 Tecnologias Principais

* **Java 17**, Spring Boot 3.3
* **PostgreSQL 16**, Flyway (migrations em `processos-nucleo/src/main/resources/db/migration`)
* **Kafka (Bitnami)** 3.x
* **Docker**, **Docker Compose**, **Helm**, **Oracle OKE**
* **MapStruct**, **Spring Data JPA**, **SpringDoc OpenAPI**

## 🏗 Arquitetura

Adotamos **Clean/Hexagonal** com cinco módulos Maven:

```
├─ processos-nucleo          # Domínio puro, entidades, eventos e portas
├─ processos-api             # API REST + adapters in/out na camada web
├─ processos-evento-kafka    # Publisher KafkaTemplate para commands
├─ processos-consumidor      # Consumer Kafka + persistência JPA
└─ processos-infra           # Infra (docker, compose, helm, env-sample)
```

### Módulos

* **processos-nucleo:** entidades `Processo`, `Parte`, `Acao`; VOs; enums; migrations Flyway; ports e events.
* **processos-api:** controllers, DTOs, MapStruct, services (commands assíncronos + queries síncronas), config OpenAPI.
* **processos-evento-kafka:** publisher de commands nos tópicos `processo.commands`, `parte.commands`, `acao.commands`.
* **processos-consumidor:** listeners Kafka, adaptações JPA, persiste entidades em banco.
* **processos-infra:** Dockerfiles, `docker-compose.yml`, `env-sample`, Helm chart (`attus-processo-msa`).

## 🔌 Endpoints REST

### Processos

* `POST /processos`            — cria processo (publica command)
* `GET /processos/{id}`        — busca por ID (consulta direta)
* `GET /processos`             — pesquisa paginada (filtros: status, datas, cpfCnpj)
* `PUT /processos/{id}`        — atualiza descrição (publica command)
* `PATCH /processos/{id}/status` — muda status (publica command)

### Partes

* `POST /processos/{id}/partes`       — cria parte (publica command)
* `PATCH /partes/{id}/contato`        — atualiza contato (publica command)
* `GET /partes/{id}`                  — busca parte
* `GET /processos/{id}/partes`        — pesquisa paginada (filtros: tipo, cpfCnpj, nome)

### Ações

* `POST /processos/{id}/acoes`        — registra ação (publica command)
* `GET /processos/{id}/acoes`         — pesquisa ações (tipo, período)

## 💻 Configuração Local

### Pré-requisitos

* Java 17, Maven 3.8+
* Docker & Docker Compose
* Helm 3.13+
* OCI CLI (para gerar kubeconfig OKE)

### Variáveis de ambiente (`processos-infra/.env-sample`)

```dotenv
# Banco de dados
POSTGRES_USER=process
POSTGRES_PASSWORD=secret
POSTGRES_DB=processdb

# Kafka (Bitnami)
KAFKA_BROKER_ID=1
KAFKA_CFG_ZOOKEEPER_CONNECT=zookeeper:2181
ALLOW_PLAINTEXT_LISTENER=yes
KAFKA_CFG_ADVERTISED_LISTENERS=PLAINTEXT://kafka:9092

# GitHub Actions
GITHUB_REPOSITORY=joaovneres/attus-backend
```

### Rodando com Docker Compose

```bash
cd processos-infra
cp .env-sample .env  # preencha secrets se desejar
docker-compose up -d
# API em http://localhost:8080/swagger-ui.html
```

### Rodando com Helm no OKE

```bash
# Gere kubeconfig: oci ce cluster create-kubeconfig --cluster-id <OCID> --region sa-saopaulo-1
kubectl create namespace process
kubectl -n process create secret generic attus-processo-db --from-literal=db-password=secret

cd processos-infra/helm/attus-processo-msa
helm dependency update
helm upgrade --install attus-processo . -n process \
  --set image.tag=$(git rev-parse --short HEAD)
kubectl -n process port-forward svc/attus-processo-msa-api 8080:80
# Swagger UI em http://localhost:8080/swagger-ui.html
```

## 🗄 Banco de Dados & Migrations

* Usamos **PostgreSQL 16** local ou em cluster.
* Migrations Flyway versionadas em `processos-nucleo/src/main/resources/db/migration`.

## 📡 Mensageria Kafka

| Tópico              | Descrição                       |
| ------------------- | ------------------------------- |
| `processo.commands` | Commands de criação/atualização |
| `parte.commands`    | Commands de Parte               |
| `acao.commands`     | Commands de Ação                |

Os payloads seguem os records em `dominio.event.*Command`.

## ✅ Testes & Cobertura

* Executar:

  ```bash
  ./mvnw -B verify   # testes + JaCoCo (>80%) + Sonar
  ```
* Relatório local em `target/site/jacoco/index.html`
* Análise no SonarCloud: cobertura mínimo de 80%.

## 🔄 CI/CD (GitHub Actions)

Workflow `ci.yml` com três jobs:

1. **build-test-sonar**: build Maven, testes, JaCoCo, SonarCloud.
2. **docker-image**: build e push de 3 módulos no GHCR (`latest-<module>` + `<sha>-<module>`).
3. **helm-package**: empacota Helm chart e, em trigger manual (`workflow_dispatch`), faz `helm upgrade` no cluster.

## ☸ Deploy Produção

* Cluster: **Oracle OKE** (sa-saopaulo-1).
* Release Helm: `attus-processo-msa` no namespace `process`.

## 📄 Documentação & Swagger

* **Swagger UI** disponível em `/swagger-ui.html` (SpringDoc OpenAPI).
* Configurada em `OpenApiConfig.java` com metadados e agrupamento.

## 🔮 Futuras Implementações

* Aumentar cobertura de testes do módulo consumidor Kafka.
* Autenticação JWT + roles (Spring Security) para endpoints.
* Testes de integração end-to-end (Postman/Newman).
* Monitoramento e métricas (Prometheus + Grafana).
* Suporte a Oracle em produção (configurable via `application-prod.yml`).
* Deploy automatizado no OKE via GitOps (Argo CD).

## 🤝 Contato
João V. Neres | [LinkedIn](https://www.linkedin.com/in/joaovneres) | [Email](mailto:'victorsousa247@gmail.com'
[GitHub](https://github.com/joaovneres/attus-backend) | [SonarCloud](https://sonarcloud.io/summary/new_code?id=joaovneres_attus-backend)
