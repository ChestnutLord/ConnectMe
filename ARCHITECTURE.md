# ConnectMe - Архитектурное описание системы

## Общее описание системы

**ConnectMe** — это микросервисная система для знакомств, позволяющая пользователям оценивать друг друга и находить взаимные симпатии (matches). Система построена по принципам микросервисной архитектуры и следует паттернам современной разработки enterprise-приложений.

### Демонстрируемые технологии и паттерны (фокус pet-проекта):

Проект ориентирован на демонстрацию владения технологиями. Основной стек:

| Технология | Применение |
|------------|------------|
| **Spring Cloud Gateway MVC** | Единая точка входа, маршрутизация, агрегация Swagger |
| **Resilience4j** | Circuit Breaker, Retry, Time Limiter — отказоустойчивость |
| **Apache Kafka** | Event-driven: producer в match-service, consumer в notification-service |
| **Keycloak + OAuth2 Resource Server** | JWT-аутентификация на Gateway |
| **RestClient** | Синхронная межсервисная коммуникация (match → user) |
| **Flyway** | Версионирование миграций БД |
| **MapStruct** | Маппинг Entity ↔ DTO |
| **SpringDoc OpenAPI** | Документация API, агрегация в Gateway |

### Функциональная оболочка (упрощённая):

1. **User Service** — CRUD пользователей
2. **Match Service** — оценки (esteem), совпадения (match), публикация событий в Kafka
3. **Notification Service** — Kafka consumer, демонстрация event-driven подхода (обработчики событий esteem/match)

### Технологический стек:

- **Backend**: Spring Boot 3.3.4, Java 17
- **БД**: PostgreSQL 16, Flyway 10.10.0
- **Gateway**: Spring Cloud Gateway MVC
- **Resilience**: Resilience4j (Circuit Breaker, Retry, Time Limiter)
- **Messaging**: Apache Kafka 4.1 (3-node KRaft)
- **Security**: Keycloak, OAuth2 Resource Server
- **API Docs**: SpringDoc OpenAPI 3
- **Mapping**: MapStruct 1.5.5
- **Build**: Gradle, Docker, Docker Compose

---

## C4 Model - Описание архитектуры

### Level 1: System Context (Контекст системы)

```
┌─────────────────────────────────────────────────────────────┐
│                        ConnectMe System                     │
│                                                             │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐       │
│  │   Gateway    │  │   User       │  │   Match      │       │
│  │   Service    │  │   Service    │  │   Service    │       │
│  └──────────────┘  └──────────────┘  └──────────────┘       │
│                                                             │
│  ┌──────────────┐                                           │
│  │Notification  │                                           │
│  │  Service     │                                           │
│  └──────────────┘                                           │
└─────────────────────────────────────────────────────────────┘
         │                    │                    │
         │                    │                    │
    ┌────▼────┐          ┌────▼─────┐         ┌────▼─────┐
    │  Users  │          │PostgreSQL│         │PostgreSQL│
    │(Mobile/ │          │(user_db) │         │(match_db)│
    │  Web)   │          └──────────┘         └──────────┘
    └─────────┘
```

**Акторы:**
- **Пользователи** (Users) — взаимодействуют с системой через мобильные приложения или веб-интерфейс

**Система ConnectMe** предоставляет:
- API для управления пользователями
- API для оценки других пользователей (esteems)
- API для просмотра совпадений (matches)
- Единую точку входа через API Gateway

---

### Level 2: Container (Контейнеры/Сервисы)

```
┌─────────────────────────────────────────────────────────────────────┐
│                         ConnectMe System                            │
│                                                                     │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │              Gateway Service (Port 8084)                     │   │
│  │  - Spring Cloud Gateway MVC                                  │   │
│  │  - Routing & Load Balancing                                  │   │ 
│  │  - Circuit Breaker (Resilience4j)                            │   │
│  │  - API Aggregation (Swagger UI)                              │   │
│  └──────────────────────────────────────────────────────────────┘   │
│                              │                                      │
│        ┌─────────────────────┼─────────────────────┐                │
│        │                     │                     │                │
│  ┌─────▼──────┐      ┌───────▼──────┐      ┌───────▼──────┐         │
│  │   User     │      │    Match     │      │ Notification │         │
│  │  Service   │      │   Service    │      │   Service    │         │
│  │ (Port 8082)│      │ (Port 8081)  │      │ (Port 8083)  │         │
│  │            │      │              │      │              │         │
│  │ - REST API │      │ - REST API   │      │ - Kafka      │         │
│  │ - JPA      │      │ - JPA        │      │   Consumer   │         │
│  │ - Flyway   │      │ - Flyway     │      │ - Event      │         │
│  │ - OpenAPI  │      │ - OpenAPI    │      │   Handlers   │         │
│  │            │      │ - Resilience4j│     │              │         │
│  │            │      │ - UserClient │      │              │         │
│  │            │      │ - Kafka      │◄────►│(esteem/match)│         │
│  └─────┬──────┘      └───────┬──────┘      └──────────────┘         │
│        │                     │                                      │
│  ┌─────▼──────┐      ┌───────▼──────┐                               │
│  │ PostgreSQL │      │ PostgreSQL   │                               │
│  │ (user_db)  │      │ (match_db)   │                               │
│  │ Port 5434  │      │ Port 5436    │                               │
│  └────────────┘      └──────────────┘                               │
└─────────────────────────────────────────────────────────────────────┘
```

#### Gateway Service
- **Назначение**: Единая точка входа для всех клиентских запросов
- **Функции**:
  - Маршрутизация запросов к соответствующим микросервисам
  - Агрегация API документации (Swagger UI)
  - Circuit Breaker для отказоустойчивости
  - Retry механизм для повторных попыток
  - Time Limiter для контроля времени выполнения запросов
- **Технологии**: Spring Cloud Gateway MVC, Resilience4j, SpringDoc OpenAPI

#### User Service
- **Назначение**: Управление пользователями системы
- **Функции**:
  - CRUD операции с пользователями
  - Валидация данных пользователей
  - Управление профилями
- **Данные**: Хранит информацию о пользователях (id, name, email, age)
- **Технологии**: Spring Boot, Spring Data JPA, PostgreSQL, Flyway, MapStruct, OpenAPI

#### Match Service
- **Назначение**: Управление оценками (esteems) и совпадениями (matches)
- **Функции**:
  - Создание, обновление, удаление оценок пользователей
  - Автоматическое создание совпадений при взаимной симпатии
  - Удаление совпадений при изменении оценки на отрицательную
  - Валидация существования пользователей через User Service
- **Данные**: 
  - Esteems (оценки): likerId, likedId, esteem (boolean), createdAt
  - Matches (совпадения): user1Id, user2Id, createdAt
- **Технологии**: Spring Boot, Resilience4j, RestClient, Kafka Producer, Spring Data JPA, PostgreSQL, Flyway, MapStruct, OpenAPI

#### Notification Service
- **Назначение**: Демонстрация **Kafka consumer** и event-driven архитектуры
- **Реализация**: Spring Kafka `@KafkaListener` — обработчики событий esteem и match (create/update/delete)
- **Технологии**: Spring Boot, Spring Kafka, shared events из core-модуля
- *Примечание: бизнес-логика минимальна (логирование), фокус на интеграции с Kafka*

---

### Level 3: Component (Компоненты)

#### User Service — Компоненты

**UserController** — REST endpoints:
- `GET /api/v1/users` — список пользователей
- `GET /api/v1/users/{id}` — пользователь по id
- `POST /api/v1/users` — создание
- `PUT /api/v1/users/{id}` — обновление
- `DELETE /api/v1/users/{id}` — удаление

**UserService** — бизнес-логика, валидация.

**UserRepository** — JPA Repository для доступа к БД.

**UserMapper** (MapStruct) — преобразование Entity ↔ DTO.

**User** (Entity) — id, name, email, age.

---

#### Match Service — Компоненты

**EsteemController** — REST endpoints для оценок:
- `GET /api/v1/users/{id}/esteems` — список оценок
- `POST /api/v1/users/{id}/esteems` — создать
- `PUT /api/v1/users/{id}/esteems/{esteemId}` — обновить
- `DELETE /api/v1/users/{id}/esteems/{esteemId}` — удалить

**MatchController** — REST endpoints для совпадений:
- `GET /api/v1/users/{id}/matches` — список совпадений

**EsteemService** — создание/обновление оценок, проверка взаимности, создание/удаление matches, валидация через UserClient.

**MatchService** — управление совпадениями.

**EsteemRepository**, **MatchRepository** — JPA репозитории.

**UserClient** (RestClient) — HTTP-вызовы к User Service, Circuit Breaker.

**Esteem**, **Match** (Entity) — сущности.

---

#### Gateway Service — Компоненты

**Routes** — маршрутизация:
- `/api/v1/users/**` → user-service
- `/api/v1/users/*/matches/**` → match-service
- `/api/v1/users/*/esteems/**` → match-service
- `/aggregate/*/v3/api-docs` → Swagger aggregation

**Circuit Breaker Filter** (Resilience4j) — fallback routes при недоступности сервисов.

**Retry Filter** — max 3 попытки, задержка 2s.

**Time Limiter** — timeout 3s.

---

### Level 4: Code (Код)

#### Основные паттерны и структура кода:

**Структура пакетов (стандартная для всех сервисов):**
```
com.dimidev.{service}
├── controller/
│   ├── api/          # Интерфейсы контроллеров (OpenAPI)
│   └── *Controller.java
├── service/          # Бизнес-логика
├── repository/       # JPA репозитории
├── model/            # JPA сущности
├── dto/              # Data Transfer Objects
├── mapper/           # MapStruct мапперы
├── exception/        # Кастомные исключения
├── config/           # Конфигурации (CORS, OpenAPI, RestClient)
└── *Application.java # Главный класс
```

**Ключевые паттерны:**
- **Repository Pattern** — абстракция доступа к данным
- **Service Layer Pattern** — бизнес-логика отделена от контроллеров
- **DTO Pattern** — разделение внутренних моделей и API контрактов
- **Mapper Pattern** — преобразование между Entity и DTO через MapStruct
- **Circuit Breaker Pattern** — отказоустойчивость при вызовах между сервисами
- **API Gateway Pattern** — единая точка входа

---

## Детальное описание функциональности

### User Service

**Модель данных:**
- `User`: id (Long), name (String), email (String, unique), age (int)

**API Endpoints:**
- `GET /api/v1/users` — получить всех пользователей
- `GET /api/v1/users/{id}` — получить пользователя по ID
- `POST /api/v1/users` — создать нового пользователя
- `PUT /api/v1/users/{id}` — обновить пользователя
- `DELETE /api/v1/users/{id}` — удалить пользователя

### Match Service

**Модели данных:**
- `Esteem`: id, likerId, likedId, esteem (boolean), createdAt
- `Match`: id, user1Id, user2Id, createdAt

**API Endpoints:**

**Esteems:**
- `GET /api/v1/users/{userId}/esteems` — получить все оценки пользователя
- `GET /api/v1/users/{userId}/esteems/{id}` — получить оценку по ID
- `POST /api/v1/users/{userId}/esteems` — создать оценку
- `PUT /api/v1/users/{userId}/esteems/{id}` — обновить оценку
- `DELETE /api/v1/users/{userId}/esteems/{id}` — удалить оценку

**Matches:**
- `GET /api/v1/users/{userId}/matches` — получить все совпадения пользователя
- `GET /api/v1/users/{userId}/matches/{id}` — получить совпадение по ID

**Бизнес-логика EsteemService:**

1. **Создание оценки (create)**:
   - Проверка, что пользователь не оценивает себя
   - Проверка, что оценка между этими пользователями еще не существует
   - Валидация существования обоих пользователей через UserClient
   - Создание оценки
   - Если оценка положительная (esteem = true), проверка взаимности:
     - Если второй пользователь тоже поставил положительную оценку → создается Match

2. **Обновление оценки (update)**:
   - Проверка прав доступа (только создатель оценки может её изменить)
   - Обновление значения оценки
   - Если оценка стала положительной → проверка взаимности и создание Match
   - Если оценка стала отрицательной → проверка существующего Match и его удаление

3. **Удаление оценки (delete)**:
   - Если удаляемая оценка была положительной → проверка и удаление Match

**Особенности Match:**
- Match создается автоматически при взаимной симпатии
- ID пользователей в Match всегда отсортированы (user1Id < user2Id) для консистентности
- Match удаляется автоматически при изменении одной из оценок на отрицательную

---

## Интеграции между сервисами

### Match Service → User Service

**Интеграция через RestClient:**
- Match Service вызывает User Service для валидации существования пользователей
- Используется Circuit Breaker для отказоустойчивости
- Конфигурация в `RestClientConfig` и `UserClient`

**Сценарии вызова:**
- При создании оценки проверяется существование `likerId` и `likedId`
- Circuit Breaker настроен с параметрами:
  - Sliding Window: 5 запросов
  - Failure Rate Threshold: 50%
  - Wait Duration: 5s
  - Retry: 3 попытки с задержкой 5s

---

## Kafka

### Кластер (Docker Compose)

**3-нодовый KRaft-кластер** без ZooKeeper (Apache Kafka 4.1.1):

- **kafka-1** (ports 9090, 9091, 9092) — controller + broker
- **kafka-2** (ports 9090, 9091, 9094) — controller + broker
- **kafka-3** (ports 9090, 9091, 9096) — controller + broker

**Конфигурация:**
- `KAFKA_PROCESS_ROLES=controller,broker` — каждый узел и контроллер, и брокер
- `KAFKA_CONTROLLER_QUORUM_VOTERS` — кворум из трёх узлов
- `PLAINTEXT://:9090` — обмен между брокерами (внутри Docker-сети)
- `EXTERNAL://` — доступ с хоста (9092, 9094, 9096)

Сервисы подключаются по `kafka-1:9090,kafka-2:9090,kafka-3:9090`.

### Producer (match-service)

**KafkaConfig** — несколько типизированных KafkaTemplate:

| Bean | Тип значения | Назначение |
|------|--------------|------------|
| `esteemCreateupdateEventsKafkaTemplate` | EsteemCreateUpdateEvent | esteem create/update |
| `matchCreateupdateEventsKafkaTemplate` | MatchCreateDeleteEvent | match create/delete |
| `kafkaTemplate` | Object | esteem delete (EsteemDeleteEvent) |

**Producer-настройки** (application.yaml):
- `acks: all` — подтверждение от всех in-sync реплик
- `delivery.timeout.ms: 20000` — максимум на повторы
- `request.timeout.ms: 10000` — ожидание ack от брокера
- `linger.ms: 0` — отправка без задержки
- Сериализация: String (ключ), JsonSerializer (значение)

**Топики** (создаются через `NewTopic`): `esteem-created-events-topic`, `esteem-updated-events-topic`, `esteem-deleted-events-topic`, `match-created-events-topic`, `match-deleted-events-topic`. У всех одинаковые параметры: 3 partitions, 3 replicas, min.insync.replicas = 2.

### Consumer (notification-service)

**Конфигурация:**
- `group-id: esteem-match-events`
- `JsonDeserializer` для значений
- `spring.json.trusted.packages: "*"` — для десериализации событий из core

**Обработчики** — `@KafkaListener` в handlers (EsteemCreateUpdateEventHandler, EsteemDeleteEventHandler, MatchCreateEventHandler, MatchDeleteEventHandler).

---

## Отказоустойчивость (Resilience Patterns)

### Circuit Breaker (Resilience4j)

**Gateway Service:**
- Конфигурация для каждого сервиса (user-service, match-service)
- Sliding Window: 10 запросов
- Failure Rate Threshold: 50%
- Minimum Calls: 5
- Wait Duration: 5s
- Fallback: возврат "Service Unavailable"

**Match Service:**
- Circuit Breaker для вызовов User Service
- Аналогичные настройки с меньшим окном (5 запросов)

### Retry

**Gateway Service:**
- Max Attempts: 3
- Wait Duration: 2s

**Match Service:**
- Max Attempts: 3
- Wait Duration: 5s

### Time Limiter

**Gateway Service:**
- Timeout: 3s

**Match Service:**
- Timeout: 3s для User Service вызовов

---

## База данных

### User Service Database (user_db)
- **Таблица**: `users`
- **Колонки**: id (PK), name, email (unique), age
- **Миграции**: Flyway, файл `V2025_09_30_12_00__create_users.sql`

### Match Service Database (match_db)
- **Таблицы**: 
  - `esteems`: id (PK), liker_id, liked_id, esteem (boolean), created_at
  - `matches`: id (PK), user1_id, user2_id, created_at
- **Миграции**: Flyway, файл `V2025_10_02_16_40__create_esteems_and_matches.sql`

---

## Развертывание

### Docker Compose

Система развертывается через Docker Compose с следующими сервисами:

1. **user-service** (Port 8082)
2. **match-service** (Port 8081)
3. **gateway-service** (Port 8084)
4. **notification-service** (Port 8083)
5. **keycloak** (Port 8181)
6. **postgres-user** (Port 5434)
7. **postgres-match** (Port 5436)
8. **kafka-1, kafka-2, kafka-3** (Kafka cluster)

### Health Checks

- PostgreSQL контейнеры имеют health checks
- Сервисы зависят от готовности БД (depends_on с condition: service_healthy)

---

## API Документация

### Swagger UI

Доступна через Gateway Service:
- URL: `http://localhost:8084/swagger-ui.html`
- Агрегированная документация для всех сервисов
- Отдельные endpoints для каждого сервиса:
  - `/aggregate/user-service/v3/api-docs`
  - `/aggregate/match-service/v3/api-docs`

---

## Мониторинг

### Actuator

- Все сервисы используют Spring Boot Actuator
- Health endpoints доступны для мониторинга состояния
- Circuit Breaker health indicators включены

---

## Будущие улучшения

1. **Кэширование** — Redis
2. **Service Discovery** — Eureka/Consul
3. **Конфигурационный сервер** — Spring Cloud Config
4. **Распределенная трассировка** — Zipkin/Micrometer Tracing
5. **Метрики** — Prometheus + Grafana
