# 📘 User Service API

## 📌 Описание

Проект представляет собой REST API-сервис для управления пользователями (создание, получение, обновление и удаление пользователей).  
Документация сгенерирована с помощью **Springdoc OpenAPI (Swagger UI)**.

-----------------------------------------------------------------------------------------------------

## 🚀 Запуск проекта

### Требования

- Java 21+
- Maven 3.6+
- PostgreSQL
- Docker
- Kafka

## 🔄 Эндпоинты API
## Метод    |	Эндпоинт    |	                |   Описание
## GET      |	http://localhost:8080/api/v1/user-service/all-users  |	Получить всех пользователей
## GET      |	http://localhost:8080/api/v1/user-service/{id}       |	Получить пользователя по ID
## POST     |	http://localhost:8080/api/v1/user-service/add-user   |	Создать нового пользователя
## PATCH    |	http://localhost:8080/api/v1/user-service/{id}       |	Обновить пользователя по ID
## DELETE   |	http://localhost:8080/api/v1/user-service/id}        |	Удалить пользователя по ID

# Сборка и запуск

# ДЛЯ КОРРЕКТНОЙ РАБОТЫ КАФКИ НЕ ЗАБЫВАЕМ ЗА МИКРОСЕРВИС

```bash
mvn clean package
````
docker-compose build

docker compose up

-------------

### Swagger UI
### http://localhost:8080/swagger-ui/index.html#/user-controller/updateUser

### Spring Eureka
### http://localhost:8761/

### Config-server
### http://localhost:8888/user-service/default