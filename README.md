# ConnectMe

> Pet-проект для демонстрации навыков разработки микросервисных приложений на Spring Boot. API для системы знакомств: управление пользователями, оценки (лайки/дизлайки), взаимные совпадения.

## Description

Микросервисная система с event-driven архитектурой. Основной акцент на применении современных технологий и паттернов: API Gateway, Circuit Breaker, Kafka, OAuth2, агрегированная OpenAPI документация.

## Stack

### Основные технологии

- Spring Boot: `3.3.4`
- Spring Boot Web: `3.3.4`
- Spring Boot Data JPA: `3.3.4`
- Spring Boot Actuator: `3.3.4`
- Spring Boot Security: `3.3.4`
- Spring Cloud Gateway MVC
- Spring Cloud Circuit Breaker Resilience4j: `5.0.0`
- SpringDoc OpenAPI Starter WebMvc (Swagger): `2.6.0`
- Spring Kafka: `4.0.2`

### Базы данных и миграции

- PostgreSQL Driver: `42.7.3`
- Flyway Core: `10.10.0`
- Flyway Database PostgreSQL: `10.10.0`

### Библиотеки

- Lombok: `1.18.34`
- MapStruct: `1.5.5.Final`
- Lombok MapStruct Binding: `0.2.0`

### Инфраструктура

- Apache Kafka: `4.1.1` (3-нодовый KRaft кластер)
- Keycloak: `24.0.1` (OAuth2/OIDC)
- PostgreSQL: `16`
- Docker, Docker Compose

## PreDeployment requirements

- Java: `17` и выше
- Gradle
- Docker
- Docker Compose

## Fast Uses

1. Клонируйте репозиторий:

```shell
git clone <repository-url>
cd ConnectMe
```

2. Скопируйте файл `.env.example` в `.env`:

```shell
cp .env.example .env
```

3. Соберите `.jar` файлы:

```shell
./gradlew clean bootJar
```

4. Запустите стек с автоматической сборкой:

```shell
docker compose up -d
```

5. Swagger UI доступен по адресу:

```
http://localhost:8084/swagger-ui.html
```

## System endpoints

| Сервис | URL |
|--------|-----|
| API Gateway | http://localhost:8084 |
| Swagger UI | http://localhost:8084/swagger-ui.html |
| Keycloak Admin | http://localhost:8181 |
| User Service | http://localhost:8082 |
| Match Service | http://localhost:8081 |
| Notification Service | http://localhost:8083 |

## Development

### Структура проекта

```
connectme/
├── gateway-service/      # API Gateway, маршрутизация, Swagger, Circuit Breaker
├── user-service/         # CRUD пользователей
├── match-service/        # Esteems, matches, Kafka producer
├── notification-service/ # Kafka consumer (демонстрация event-driven)
├── core/                 # Shared event DTOs
└── docker-compose.yaml
```

### Конфигурация

Переменные окружения задаются в `.env`. См. `.env.example` для списка переменных.

## Documentation

- [ARCHITECTURE.md](ARCHITECTURE.md) — архитектура, C4-модель, технологии
