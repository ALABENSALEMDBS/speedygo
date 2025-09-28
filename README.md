# 🚚 SpeedyGo – Plateforme de Livraison et Covoiturage

**SpeedyGo** est une application moderne de **livraison et covoiturage** conçue avec une **architecture microservices**.  
Elle propose une gestion complète des utilisateurs, commandes, paiements, véhicules, produits, livraisons, réclamations et covoiturage, avec un **frontend Angular** et un système d’authentification basé sur **Keycloak**.  

---

## 🏗️ Architecture Globale

### 🖥️ Frontend
- **Angular** : interface utilisateur responsive (clients, livreurs, administrateurs).  

### ⚙️ Backend – Microservices
- **Spring Boot (Java)**  
  - `user-service` → gestion des utilisateurs (**H2**)  
  - `livraison-service` → gestion des livraisons (**H2**)  
  - `covoiturage-service` → gestion du covoiturage (**H2**)  
  - `vehicule-service` → gestion des véhicules (**MySQL**)  
  - `paiement-service` → gestion des paiements (**MySQL**)  
  - `commande-service` → gestion des commandes (**MySQL**)  
  - `reclamation-service` → gestion des réclamations (**MySQL**)  

- **.NET Core (C#)**  
  - `produit-service` → gestion des produits (**MongoDB**)  

### 🔑 Sécurité
- **Keycloak**  
  - Gestion des utilisateurs, rôles et permissions.  
  - Authentification basée sur **JWT tokens**.  

### 🗄️ Bases de données
- **MySQL** → véhicules, paiements, commandes, réclamations  
- **MongoDB** → produits  
- **H2 (in-memory)** → utilisateurs, livraisons, covoiturage  
- **PostgreSQL (par défaut)** → utilisé par Keycloak  

---

## 📦 Liste des Services

| Service               | Technologie   | Base de données |
|------------------------|--------------|----------------|
| Gestion Utilisateurs   | Spring Boot  | H2             |
| Gestion Livraisons     | Spring Boot  | H2             |
| Gestion Covoiturage    | Spring Boot  | H2             |
| Gestion Véhicules      | Spring Boot  | MySQL          |
| Gestion Paiements      | Spring Boot  | MySQL          |
| Gestion Commandes      | Spring Boot  | MySQL          |
| Gestion Réclamations   | Spring Boot  | MySQL          |
| Gestion Produits       | .NET Core    | MongoDB        |
| Authentification       | Keycloak     | PostgreSQL     |

---

## 🗂️ Structure du Projet

<img width="500" height="403" alt="image" src="https://github.com/user-attachments/assets/7c1a11d7-34c9-45c4-b030-087ac8b4a7ca" />

