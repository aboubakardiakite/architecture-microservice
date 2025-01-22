# Microservices Library Management System

## Description
Système de gestion de bibliothèque basé sur une architecture microservices permettant la gestion des livres, utilisateurs et emprunts.

## Architecture
- **Config Server** (port: 8880) : Configuration centralisée
- **Discovery Server** (port: 8761) : Service discovery avec Eureka
- **Book Service** (port: 8082) : Gestion des livres
- **User Service** (port: 9092) : Gestion des utilisateurs
- **Borrowing Service** (port: 8083) : Gestion des emprunts

## Technologies
- Java 17
- Spring Boot 3.x
- Spring Cloud
- Apache Kafka
- H2 Database
- Maven
- Docker
- Swagger/OpenAPI

## API Documentation
Swagger UI disponible pour chaque service :
- Book Service : http://localhost:8082/swagger-ui.html
- User Service : http://localhost:9092/swagger-ui.html
- Borrowing Service : http://localhost:8083/swagger-ui.html

## Endpoints Principaux

### Book Service
```
GET    /api/v1/book          - Liste tous les livres
GET    /api/v1/book/{id}     - Détails d'un livre
POST   /api/v1/book          - Crée un livre
PUT    /api/v1/book/{id}     - Met à jour un livre
DELETE /api/v1/book/{id}     - Supprime un livre
GET    /api/v1/book/{id}/available - Vérifie la disponibilité
```

### User Service
```
GET    /api/v1/user          - Liste tous les utilisateurs
GET    /api/v1/user/{id}     - Détails d'un utilisateur
POST   /api/v1/user          - Crée un utilisateur
PUT    /api/v1/user/{id}     - Met à jour un utilisateur
DELETE /api/v1/user/{id}     - Supprime un utilisateur
GET    /api/v1/user/{id}/can-borrow - Vérifie l'éligibilité
```

### Borrowing Service
```
GET    /api/v1/borrowing          - Liste tous les emprunts
GET    /api/v1/borrowing/{id}     - Détails d'un emprunt
POST   /api/v1/borrowing          - Crée un emprunt
PUT    /api/v1/borrowing/{id}/return - Retourne un livre
```

## Installation

1. Cloner le projet :
```bash
git clone [url-du-repo]
```

2. Démarrer les services dans l'ordre :
```bash
# 1. Config Server
cd config-server && mvn spring-boot:run

# 2. Discovery Server
cd discovery-server && mvn spring-boot:run

# 3. Services métier
cd book-service && mvn spring-boot:run
cd user-service && mvn spring-boot:run
cd borrowing-service && mvn spring-boot:run
```

## Kafka Topics
- borrowing-events : Événements d'emprunt
- user-events : Événements utilisateur
- book-events : Événements livre

## Monitoring
- Eureka : http://localhost:8761
- H2 Console :
  - Book Service : http://localhost:8082/h2-console
  - User Service : http://localhost:9092/h2-console
  - Borrowing Service : http://localhost:8083/h2-console

## Design Patterns
- Builder (User, Book, Borrowing)
- Singleton (Services)
- Observer (Kafka Events)

## Auteur
[aboubakar DIAKITE] 