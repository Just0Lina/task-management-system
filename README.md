# Task Management System

## Описание проекта
Простая система управления задачами с использованием Java 17+, Spring Boot, PostgreSQL и Spring Security. Поддерживается аутентификация и авторизация с JWT токеном, ролевая система (ADMIN, USER), управление задачами, фильтрация, пагинация, обработка ошибок и документация API с помощью Swagger.

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
- Java 17+
- Spring Boot
- Spring Security
- PostgreSQL
- Docker, Docker Compose
- JWT (JSON Web Token)
- OpenAPI (Swagger UI)
- JUnit, Mockito, Testcontainers
- Redis (опционально для кэша)

## Тестовые пользователи

- ADMIN - email: **admin@example.com** password: **Admin123!**
- USER - email: **user@example.com** password: **User123!**