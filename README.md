# ConnectMe Ч Microservices Project

### Services
- **user-service** Ч управление пользовател€ми
- **match-service** Ч обработка свайпов и совпадений
- **notification-service** Ч уведомлени€ о совпадени€х

### Stack
- Spring Boot 3
- PostgreSQL
- Docker & Docker Compose
- Spring Cloud OpenFeign
- Kafka (планируетс€)

### Run locally
```bash
./gradlew clean build
docker-compose up --build
