

TP Architecture logicielle - Microservice

### **Architecture des microservices**

En vous basant sur les éléments vus pendant les cours, vous allez mettre en place une application avec une architecture microservice, qui aura comme objectif de gérer des emrpunts de livre.

Voici sa structuration : 

1. **API Gateway** : Pour centraliser les requêtes et gérer le routage vers les services respectifs.
2. **Service Discovery** : Eureka pour enregistrer et découvrir les services.
3. **Config Server** : Gestion centralisée des configurations.
4. **Services** :
   - **Book Service** : Gestion des livres (ajout, suppression, mise à jour, recherche).
   - **User Service** : Gestion des utilisateurs (inscription, mise à jour, suppression).
   - **Borrowing Service** : Gestion des emprunts (prêter, retourner, vérifier disponibilité, suppression).
  
---

### **Étapes du TP**

#### **1. Créer les projets Spring Boot**
Utilisez Spring Initializr pour générer les projets suivants :
- **config-server**
- **discovery-server**
- **api-gateway**
- **book-service**
- **user-service**
- **borrowing-service**

Ajoutez les dépendances nécessaires :
- Spring Web
- Spring Data JPA
- Spring Boot Actuator
- Spring Cloud Config
- Eureka Server/Client
- Spring for Apache Kafka
- MySQL Driver (ou autre base de données) ou H2.

---

#### **5. Implémenter les microservices**

##### **a) Book Service**
- Entité `Book` :
  ```java
  @Entity
  public class Book {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String title;
      private String author;
      private String category;
      private boolean isAvailable;
  }
  ```
- Crud complet sur le livre.
- Un livre (1 seul exemplaire) ne peut être emprunté que par une personne à la fois.
- Quand un livre est emprunté, il n'est plus disponible.

##### **b) User Service**
- Entité `User` :
  ```java
  @Entity
  public class User {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private String name;
      private String email;
      private Enum membershipType; // Regular, Premium
      private boolean isLocked
      private Integer nombreMaxEmprunt
  }
  ```
- Crud complet sur USER.
- Un emprunteur ne peut emprunter que 5 livres maximums quand il est Regular et 7 quand il est Premium.
- Quand il atteint le nombre maximum de livre, isLocked est à true. Il ne peut plus emprunter.

##### **c) Borrowing Service**
- Entité `Borrowing` :
  ```java
  @Entity
  public class Borrowing {
      @Id
      @GeneratedValue(strategy = GenerationType.IDENTITY)
      private Long id;
      private Long bookId;
      private Long userId;
      private LocalDate borrowDate;
      private LocalDate returnDate;
  }
  ```
- Implémentez une logique pour :
  - Vérifier la disponibilité des livres.
  - Enregistrer un emprunt. 

#### **6. Intégrer Kafka**

Kafka doit nous permettre d'intégrer les mécanimes suivants : 

- Si je supprime un livre, l'emprunt qui est associé à ce livre doit aussi être supprimé.
- Si je supprime un emprunteur, les emprunts associés à cet emprunteur doivent aussi disparaitre.
- Quand je crée un emprunt, si l'emprunteur emprunte son dernier livre possible, je mets à jour l'emprunteur pour qu'il soit bloqué pour le prochain emprunt.
  
---

#### **7. Design Pattern**

- Mettez un en place un design Pattern le plus adapté pour le microservice User.
- Vous êtes libre du type de Design Pattern : creationnal, behavior ou structural.
- Justifiez votre choix.

---

#### **8. Tester l’ensemble**

- Testez les interactions entre les services avec postman :
  - Ajout d’un livre.
  - Inscription d’un utilisateur.
  - Emprunt d’un livre et mise à jour du statut.
  - Effectur des suppressions de livre, user et emprunt pour mesurer le fonctionnement de Kafka.
  - Etc.

---

#### **8. Documentation**
- Utilisez Swagger pour documenter chaque service.

#### **9. Resultat**
- Envoyez votre dépôt git par MP sur Teams à Mohamed Aijjou.
- Dans le dépôt git, il faudrait tout le code source nécessaire pour tester le fonctionnement de l'application.