# Task Management System

## Описание проекта

**Task Management System** — это RESTful API для управления задачами, реализованное с использованием Java, Spring Boot, Spring Security, Spring Data и PostgreSQL. Система поддерживает создание, редактирование, удаление и просмотр задач, управление пользователями с ролями (администратор и пользователь), аутентификацию с использованием JWT, а также хранение комментариев к задачам.

---

## Основные функции

- **Управление задачами**: создание, редактирование, удаление, назначение приоритета и статуса.
- **Управление комментариями**: добавление комментариев к задачам.
- **Управление пользователями**: регистрация пользователей, аутентификация через JWT, разграничение доступа на основе ролей (администратор или пользователь).
- **Пагинация и фильтрация задач**.
- **Документация API**: Swagger UI.

---

## Запуск:
1. Клонируйте репозиторий

```shell
git clone https://github.com/levchig737/taskManagementSystem.git && cd taskManagementSystem
```

2. Создание контейнера PostgresSql

```shell
docker-compose up -d
```

3. Скомпилируйте проект с помощью Maven

```shell
mvn clean compile
```

4. Запустите приложение

```shell
mvn spring-boot:run
```

## Запросы
Доступ по ссылке: http://localhost:8080/

---

## Архитектура базы данных

База данных построена на основе PostgreSQL. В проекте используются три основные таблицы:

1. **users** (Пользователи)
2. **tasks** (Задачи)
3. **comments** (Комментарии)

### Отношения между таблицами:
- **User ↔ Task**:
    - Один пользователь может быть автором или исполнителем множества задач.
    - Задача всегда связана с автором (`author_id`), исполнитель (`assignee_id`) может быть NULL.
- **Task ↔ Comment**:
    - Одна задача может иметь множество комментариев.
    - Комментарий всегда связан с задачей и пользователем (автором комментария).

---

### Реляционная модель базы данных
<img src="images/Реляционная_Модель_taskManagmentSystem.drawio.png" width="621" height="461" alt="Реляционная модель"/>

### Доступные пользователи (Таблица users):
| id | email              | password | name       | role  |
|----|--------------------|----------|------------|-------|
| 1  | admin@example.com  | admin    | Admin User | ADMIN |
| 2  | user1@example.com  | user1    | User One   | USER  |
| 3  | user2@example.com  | user2    | User Two   | USER  |

---

### Доступные задачи (Таблица tasks):
| id | title    | description           | status       | priority | author_id | assignee_id |
|----|----------|-----------------------|--------------|----------|-----------|-------------|
| 1  | Task 1   | Description for Task 1| PENDING      | HIGH     | 1         | 2           |
| 2  | Task 2   | Description for Task 2| IN_PROGRESS  | MEDIUM   | 1         | 3           |
| 3  | Task 3   | Description for Task 3| COMPLETED    | LOW      | 2         | NULL        |

---

### Доступные пользователи (Таблица comments):
| id | task_id | user_id | text                             |
|----|---------|---------|----------------------------------|
| 1  | 1       | 2       | Comment for Task 1 by User 2    |
| 2  | 2       | 3       | Comment for Task 2 by User 3    |
| 3  | 3       | 2       | Comment for Task 3 by User 2    |
