# Task Management System

## Описание проекта

Простая система управления задачами с использованием Java 17+, Spring Boot, PostgreSQL и Spring Security. Поддерживается
аутентификация и авторизация с JWT токеном, ролевая система (ADMIN, USER), управление задачами, фильтрация, пагинация,
обработка ошибок и документация API с помощью Swagger.

---

## 📋 Функционал

- Аутентификация и авторизация через JWT
- Роли: Администратор и Пользователь
- CRUD-операции с задачами
- Управление статусами и приоритетами
- Назначение исполнителя задачи
- Оставление комментариев
- Фильтрация и пагинация задач
- Обработка ошибок и валидация входных данных
- Документация API с помощью Swagger UI
- Docker Compose для запуска в дев-среде
- Базовые unit-тесты

---

## 🛠️ Стек технологий

- Java 21
- Spring Boot
- Spring Security
- PostgreSQL
- Docker, Docker Compose
- JWT (JSON Web Token)
- OpenAPI (Swagger UI)
- JUnit

## Тестовые пользователи

- ADMIN - email: **admin@example.com** password: **Admin123!**
- USER - email: **user@example.com** password: **User123!**

# Настройка локального запуска

Для локального запуска требуется указать секреты в переменных окружения при запуске в IDE:

```plaintext
SPRING_PROFILES_ACTIVE=
DATASOURCE_URL=
DATASOURCE_USERNAME=
DATASOURCE_PASSWORD=
JWT_ACCESS_SECRET=
JWT_REFRESH_SECRET=
```

# Тестовые значения

Для тестирования можно использовать доступные ключи:

```plaintext
JWT_ACCESS_SECRET=qBTmv4oXFFR2GwjexDJ4t6fsIUIUhhXqlktXjXdkcyygs8nPVEwMfo29VDRRepYDVV5IkIxBMzr7OEHXEHd37w==
JWT_REFRESH_SECRET=zL1HB3Pch05Avfynovxrf/kpF9O2m4NCWKJUjEp27s9J2jEG3ifiKCGylaZ8fDeoONSTJP/wAzKawB8F9rOMNg==
```

# Запуск через Docker compose

Для запуска через Docker необходимо создать файлы с переменными окружения.

1. Файл app.env для приложения:
   ```plaintext
   SPRING_PROFILES_ACTIVE=
   DATASOURCE_URL=
   DATASOURCE_USERNAME=
   DATASOURCE_PASSWORD=
   JWT_ACCESS_SECRET=
   JWT_REFRESH_SECRET=
   ```
2. Файл db.env для базы данных:
   ```plaintext
   POSTGRES_DB=
   POSTGRES_USER=
   POSTGRES_PASSWORD=
   ```

Для запуска приложения с помощью Docker Compose, выполните следующую команду в корневой директории проекта:

``` bash
docker compose up
```

## Swagger документация

Swagger документация доступна по адресу:  
[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

## Тестирование

Реализованы тесты для сервисов, обеспечивающие проверку их корректной работы.