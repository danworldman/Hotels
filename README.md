# Hotels REST API

RESTful API для работы с информацией об отелях с поддержкой поиска, создания, добавления удобств и получения статистики.

---

## Технологии

- Java 17+
- Spring Boot 3.5.4
- Spring Data JPA
- Liquibase для миграций базы данных
- H2 (in-memory) — база данных по умолчанию
- Swagger UI для документации API
- JUnit 5 + Mockito — для тестирования
- Maven — для сборки и запуска

---

## Архитектура и паттерны проектирования

Приложение построено с использованием современных подходов и паттернов проектирования для обеспечения масштабируемости, удобства поддержки и расширяемости.

### Разделение на слои (слоистая архитектура)

- **Контроллеры (Controllers)**  
  Обработка HTTP-запросов, валидация входных данных, формирование и возврат ответов клиенту.

- **Сервисный слой (Services)**  
  Содержит бизнес-логику приложения, обеспечивает координацию между слоями и управляет транзакциями.

- **Репозитории (Repositories)**  
  Отвечают за взаимодействие с базой данных. Используются интерфейсы и кастомные реализации для поддержки различных СУБД (JPA, MongoDB).

- **Модели и DTO (Models/DTOs)**  
  Представляют модели данных и объекты передачи данных, обеспечивая изоляцию слоя представления от слоя данных.

- **Мапперы (Mappers)**  
  Отвечают за преобразование между сущностями базы данных и DTO, обеспечивая чистоту архитектуры.

### Используемые паттерны проектирования

- **Model-View-Controller (MVC)**  
  Архитектурный паттерн, на котором основан Spring Boot: контроллеры (`@RestController`) принимают HTTP-запросы, делегируют логику сервисам и формируют ответы клиенту.

- **Data Transfer Object (DTO)**  
  Используется для передачи данных между слоями и по сети, поддерживает изоляцию и контроль данных.

- **REST (Representational State Transfer)**  
  Архитектурный стиль для построения распределённых веб-приложений и API, основанный на использовании стандартных HTTP-методов (GET, POST, PUT, DELETE) и ресурсо-ориентированного подхода.

- **Factory Pattern**  
  Применяется при необходимости создания объектов через мапперы.

Данное архитектурное разделение и использование паттернов упрощают смену технологии хранения данных, повышают качество тестирования и поддержки проекта, а также позволяют быстро переключаться между базами данных (H2, MySQL, PostgreSQL, MongoDB и др.) с помощью Spring Profiles.

---

## Запуск проекта

1. Клонируйте или скачайте проект.
2. Убедитесь, что установлен JDK 17+ и Maven.
3. В корне проекта выполните команду:

mvn spring-boot:run

4. Приложение будет доступно по адресу:

http://localhost:8092

---

## REST API эндпоинты

Все маршруты имеют префикс `/property-view`.

| Метод    | URL                                    | Описание                                                  |
|----------|---------------------------------------|-----------------------------------------------------------|
| GET      | `/property-view/hotels`                | Получить список всех отелей с краткой информацией         |
| GET      | `/property-view/hotels/{id}`           | Получить подробную информацию по отелю по ID              |
| GET      | `/property-view/search`                 | Поиск отелей по параметрам (name, brand, city, country, amenities)  |
| POST     | `/property-view/hotels`                 | Создать новый отель                                       |
| POST     | `/property-view/hotels/{id}/amenities` | Добавить список удобств (amenities) к отелю              |
| GET      | `/property-view/histogram/{param}`     | Получить статистику по параметру (brand, city, country, amenities) |

---

## Примеры запросов и ответов

### 1. Получить все отели (GET)

**Запрос:**

curl -X GET "http://localhost:8092/property-view/hotels" -H "accept: application/json"

**Пример ответа (HTTP 200):**

[
{
"id": 1,
"name": "DoubleTree by Hilton Minsk",
"description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
"address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
"phone": "+375 17 309-80-00"
}
]

---

### 2. Получить отель по ID (GET)

**Запрос:**

curl -X GET "http://localhost:8092/property-view/hotels/1" -H "accept: application/json"

**Пример ответа (HTTP 200):**

{
"id": 1,
"name": "DoubleTree by Hilton Minsk",
"brand": "Hilton",
"description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
"address": {
"houseNumber": 9,
"street": "Pobediteley Avenue",
"city": "Minsk",
"country": "Belarus",
"postCode": "220004"
},
"contacts": {
"phone": "+375 17 309-80-00",
"email": "doubletreeminsk.info@hilton.com"
},
"arrivalTime": {
"checkIn": "14:00",
"checkOut": "23:00"
},
"amenities": [
"Business center",
"Concierge",
"Fitness center",
"Free WiFi",
"Free parking",
"Meeting rooms",
"Non-smoking rooms",
"On-site restaurant",
"Pet-friendly rooms",
"Room service"
]
}

---

### 3. Поиск отелей с фильтрами (GET)

Поиск по городу Minsk, бренду Hilton, удобствам Free WiFi и Pool.

**Запрос:**

curl -X GET "http://localhost:8092/property-view/search?city=Minsk&brand=Hilton&amenities=Free%20WiFi,Pool" -H "accept: application/json"

**Пример ответа (HTTP 200):**  
[
{
"id": 1,
"name": "DoubleTree by Hilton Minsk",
"description": "The DoubleTree by Hilton Hotel Minsk offers 193 luxurious rooms in the Belorussian capital and stunning views of Minsk city from the hotel's 20th floor ...",
"address": "9 Pobediteley Avenue, Minsk, 220004, Belarus",
"phone": "+375 17 309-80-00"
}
]
---

### 4. Создать новый отель (POST)

**Запрос:**

curl -X POST "http://localhost:8092/property-view/hotels" \
-H "Content-Type: application/json" \
-d '{
"name": "Sunset Paradise",
"description": "Sunset Paradise offers a serene stay with beautiful sea views and excellent customer service.",
"brand": "Paradise Group",
"address": {
"houseNumber": 77,
"street": "Ocean Drive",
"city": "Sunnyville",
"country": "Sunland",
"postCode": "54321"
},
"contacts": {
"phone": "+123 456 7890",
"email": "contact@sunsetparadise.com"
},
"arrivalTime": {
"checkIn": "15:00",
"checkOut": "11:00"
}
}'

**Пример ответа (HTTP 201):**

{
"id": 4,
"name": "Sunset Paradise",
"description": "Sunset Paradise offers a serene stay with beautiful sea views and excellent customer service.",
"address": "77 Ocean Drive, Sunnyville, 54321, Sunland",
"phone": "+123 456 7890"
}

---

### 5. Добавить удобства к отелю (POST)

**URL:**  
`POST /property-view/hotels/{id}/amenities`  
*(где `{id}` — идентификатор отеля)*

**Запрос:**

curl -X POST "http://localhost:8092/property-view/hotels/2/amenities" \
-H "Content-Type: application/json" \
-d '[
"On-site restaurant",
"Business center",
"Fitness center"
]'

---

### 6. Получить гистограмму по параметру (GET)

Пример запроса для статистики по городам.

**Запрос:**

curl -X GET "http://localhost:8092/property-view/histogram/city" -H "accept: application/json"

**Пример ответа:**

{
"Minsk": 3
}

---

## Swagger UI

Для просмотра документации и удобного тестирования API откройте браузер по адресу:

http://localhost:8092/swagger-ui/index.html

---

## Тестирование

Запуск всех тестов:

mvn test

---

## H2 Console (для разработки)

Для доступа к базе через веб-интерфейс доступна консоль H2:

http://localhost:8092/h2-console

Параметры соединения:

- JDBC URL: `jdbc:h2:mem:hotelsdb`
- User Name: `sa`
- Password: *(оставить пустым)*

---

### Переключение базы данных

Для переключения между базами данных в приложении используется Spring Profiles. Ниже указаны параметры подключения для каждого профиля и пример запуска приложения с нужным профилем.

### Профиль H2 (по умолчанию)

Встроенная in-memory база для разработки и тестирования.

**Параметры соединения:**

- JDBC URL: `jdbc:h2:mem:hotelsdb`
- User Name: `sa`
- Password: *(оставить пустым)*

Запуск приложения (по умолчанию):

mvn spring-boot:run

### Профиль MySQL

**Параметры соединения:**

- JDBC URL: `jdbc:mysql://localhost:3306/hotelsdb?useSSL=false&serverTimezone=UTC`
- User Name: `root`
- Password: `root`

Запуск с профилем:

mvn spring-boot:run "-Dspring-boot.run.profiles=mysql"

### Профиль PostgreSQL


**Параметры соединения:**

- JDBC URL: `jdbc:postgresql://localhost:5432/hotelsdb`
- User Name: `postgres`
- Password: `postgres`

Запуск с профилем:

mvn spring-boot:run "-Dspring-boot.run.profiles=postgresql"

### Профиль MongoDB


Запуск с профилем:

mvn spring-boot:run "-Dspring-boot.run.profiles=mongodb"


---

При использовании любого профиля приложение автоматически подхватывает соответствующие настройки подключения, миграции и конфигурацию.


---

_Проект разработан с использованием Maven, Spring Boot, Java 17+, JPA, H2 и Liquibase._