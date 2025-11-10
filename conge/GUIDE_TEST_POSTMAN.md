# 🧪 Guide de Test Postman - Microservice Conge

## 📋 Vue d'ensemble

Ce guide vous explique comment tester **tous les endpoints** du microservice Conge avec Postman, y compris les endpoints qui utilisent **OpenFeign** pour communiquer avec le microservice User.

---

## 🚀 ÉTAPE 1 : Démarrer les Services

### 1.1 Démarrer Keycloak (si pas déjà démarré)
```bash
# Vérifier si Keycloak est accessible
curl http://localhost:8060
```

### 1.2 Démarrer Eureka Server
```bash
cd Eureka
mvn spring-boot:run
```
**Vérifier :** `http://localhost:8761`

### 1.3 Démarrer User Microservice
```bash
cd user
mvn spring-boot:run
```
**Vérifier :** Doit apparaître dans Eureka comme **USER**

### 1.4 Démarrer Conge Microservice
```bash
cd conge
mvn spring-boot:run
```
**Vérifier :** Doit apparaître dans Eureka comme **CONGE**

---

## 📦 ÉTAPE 2 : Importer la Collection Postman

### 2.1 Ouvrir Postman

### 2.2 Importer la Collection
1. Cliquer sur **"Import"** (en haut à gauche)
2. Sélectionner **"File"**
3. Choisir le fichier : `conge/SpeedyGo_Conge_Complete_Tests.postman_collection.json`
4. Cliquer sur **"Import"**

### 2.3 Vérifier les Variables
Cliquer sur la collection → **Variables** :
- `jwt_token` : (vide au début, sera rempli automatiquement)
- `base_url` : `http://localhost:8085/conge`
- `keycloak_url` : `http://localhost:8060/realms/SpeedyGo5se4/protocol/openid-connect/token`
- `driver_id` : `1` (ID du driver de test)
- `leave_id` : (vide au début, sera rempli automatiquement)

---

## 🔐 ÉTAPE 3 : Obtenir un Token JWT

### 3.1 Exécuter la Requête "Get JWT Token from Keycloak"

**Dossier :** `0. Authentication`  
**Requête :** `Get JWT Token from Keycloak`

**Avant d'exécuter :**
1. Ouvrir la requête
2. Aller dans **Body** → **x-www-form-urlencoded**
3. **Modifier le mot de passe** dans le champ `password` (actuellement `password123`)
4. Cliquer sur **Send**

**Réponse attendue (200 OK) :**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldU...",
  "expires_in": 300,
  "token_type": "Bearer"
}
```

**✅ Le token est automatiquement sauvegardé dans la variable `jwt_token` !**

---

## ✅ ÉTAPE 4 : Tester les Endpoints

### 4.1 Créer un Congé

**Dossier :** `1. Create Leave`  
**Requête :** `Create Leave`

**Body :**
```json
{
  "startDate": "2025-11-15",
  "endDate": "2025-11-20",
  "reason": "Vacances d'été",
  "driverId": "1"
}
```

**Réponse attendue (200 OK) :**
```json
{
  "id": "generated-uuid",
  "startDate": "2025-11-15",
  "endDate": "2025-11-20",
  "reason": "Vacances d'été",
  "status": "PENDING",
  "driverId": "1",
  "exceededDays": 0,
  "exceededSalaryCut": 0.0
}
```

**✅ L'ID du congé est automatiquement sauvegardé dans la variable `leave_id` !**

---

### 4.2 Vérifier le Dépassement

**Dossier :** `1. Create Leave`  
**Requête :** `Check Leave Exceeding`

**Body :**
```json
{
  "startDate": "2025-12-01",
  "endDate": "2025-12-10",
  "reason": "Test dépassement",
  "driverId": "1"
}
```

**Réponse attendue (200 OK) :**
```json
{
  "exceedsLimit": false,
  "totalDays": 16,
  "maxAllowedDays": 30,
  "exceededDays": 0,
  "message": "Le congé ne dépasse pas la limite autorisée."
}
```

---

### 4.3 Récupérer Tous les Congés

**Dossier :** `2. Get Leaves`  
**Requête :** `Get All Leaves`

**Réponse attendue (200 OK) :**
```json
[
  {
    "id": "uuid-1",
    "startDate": "2025-11-15",
    "endDate": "2025-11-20",
    "reason": "Vacances d'été",
    "status": "PENDING",
    "driverId": "1",
    "exceededDays": 0,
    "exceededSalaryCut": 0.0
  }
]
```

---

### 4.4 🔥 TEST FEIGN : Récupérer les Congés avec Noms des Drivers

**Dossier :** `3. Get Leaves with Driver Names (Feign)`  
**Requête :** `Get Detailed Leaves (with Driver Names)`

**URL :** `GET http://localhost:8085/conge/leaves/detailed`

**Réponse attendue (200 OK) :**
```json
[
  {
    "id": "uuid-1",
    "startDate": "2025-11-15",
    "endDate": "2025-11-20",
    "reason": "Vacances d'été",
    "status": "PENDING",
    "driverId": "1",
    "driverFirstName": "ranya",
    "driverLastName": "ben",
    "driverFullName": "ranya ben"
  }
]
```

**✅ Si vous voyez les noms des drivers, la communication Feign fonctionne !**

---

### 4.5 🔥 TEST FEIGN : Résumé par Driver

**Dossier :** `4. Summary and Statistics (Feign)`  
**Requête :** `Get Summary by Driver`

**URL :** `GET http://localhost:8085/conge/leaves/summary-by-driver`

**Réponse attendue (200 OK) :**
```json
[
  {
    "driverId": 1,
    "firstName": "ranya",
    "lastName": "ben",
    "totalDaysTaken": 6,
    "limit": 30
  }
]
```

**✅ Si vous voyez les informations des drivers, la communication Feign fonctionne !**

---

### 4.6 🔥 TEST FEIGN : Total de Jours par Driver

**Dossier :** `4. Summary and Statistics (Feign)`  
**Requête :** `Get Total Days by Driver`

**URL :** `GET http://localhost:8085/conge/leaves/total-days-by-driver`

**Réponse attendue (200 OK) :**
```json
[
  {
    "firstName": "ranya",
    "lastName": "ben",
    "totalDaysTaken": 6
  }
]
```

**✅ Si vous voyez les noms des drivers, la communication Feign fonctionne !**

---

### 4.7 Approuver un Congé

**Dossier :** `5. Update Leave`  
**Requête :** `Approve Leave`

**URL :** `PUT http://localhost:8085/conge/leaves/approve/{{leave_id}}`

**Réponse attendue (200 OK) :** Pas de body (void)

**Vérifier :** Récupérer le congé avec `Get Leave by ID` → le statut doit être `APPROVED`

---

### 4.8 Rejeter un Congé

**Dossier :** `5. Update Leave`  
**Requête :** `Reject Leave`

**URL :** `PUT http://localhost:8085/conge/leaves/reject/{{leave_id}}`

**Réponse attendue (200 OK) :** Pas de body (void)

**Vérifier :** Récupérer le congé avec `Get Leave by ID` → le statut doit être `REJECTED`

---

### 4.9 Récupérer les Congés par Statut

**Dossier :** `2. Get Leaves`  
**Requête :** `Get Leaves by Status`

**URL :** `GET http://localhost:8085/conge/leaves/status?status=PENDING`

**Paramètres :**
- `status` : `PENDING`, `APPROVED`, ou `REJECTED`

**Réponse attendue (200 OK) :**
```json
[
  {
    "id": "uuid-1",
    "status": "PENDING",
    ...
  }
]
```

---

### 4.10 Supprimer un Congé

**Dossier :** `6. Delete Leave`  
**Requête :** `Delete Leave`

**URL :** `DELETE http://localhost:8085/conge/leaves/{{leave_id}}`

**Réponse attendue (204 No Content) :** Pas de body

**Vérifier :** Récupérer tous les congés → le congé supprimé ne doit plus apparaître

---

## 📊 RÉSUMÉ DES ENDPOINTS

### Endpoints Standard (sans Feign)
| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/leaves/create` | Créer un congé |
| POST | `/leaves/check` | Vérifier le dépassement |
| GET | `/leaves` | Récupérer tous les congés |
| GET | `/leaves/{id}` | Récupérer un congé par ID |
| GET | `/leaves/me?driverId=1` | Récupérer mes congés |
| GET | `/leaves/by-driver/{driverId}` | Récupérer congés par driver |
| GET | `/leaves/status?status=PENDING` | Récupérer congés par statut |
| PUT | `/leaves/{id}/status?status=APPROVED` | Mettre à jour le statut |
| PUT | `/leaves/approve/{id}` | Approuver un congé |
| PUT | `/leaves/reject/{id}` | Rejeter un congé |
| PUT | `/leaves/confirm-update/{id}` | Mettre à jour un congé |
| DELETE | `/leaves/{id}` | Supprimer un congé |

### Endpoints avec Feign (Communication avec User)
| Méthode | Endpoint | Description | Test Feign |
|---------|----------|-------------|------------|
| GET | `/leaves/detailed` | Congés avec noms des drivers | ✅ |
| GET | `/leaves/dto/{id}` | Congé avec nom du driver | ✅ |
| GET | `/leaves/summary-by-driver` | Résumé par driver | ✅ |
| GET | `/leaves/total-days-by-driver` | Total jours par driver | ✅ |
| GET | `/leaves/total-days?driverId=1` | Total jours pour un driver | ❌ |
| GET | `/leaves/salary-cuts/{driverId}` | Réductions de salaire | ❌ |

---

## 🔍 Vérifier les Logs

### Dans les logs du microservice Conge

Vous devriez voir :

```
✅ Feign: Added Authorization header to request
```

Cela confirme que le JWT est bien propagé automatiquement vers le microservice User.

---

## 🐛 Dépannage

### Erreur 401 Unauthorized

**Cause :** Token JWT manquant ou expiré

**Solution :**
1. Réexécuter la requête `Get JWT Token from Keycloak`
2. Vérifier que le token est bien sauvegardé dans la variable `jwt_token`
3. Le token expire après 5 minutes (300 secondes)

---

### Erreur 500 Internal Server Error

**Cause :** Le microservice User n'est pas accessible

**Solution :**
1. Vérifier que User est démarré : `http://localhost:8081/api/users`
2. Vérifier les logs du microservice Conge
3. Vérifier que Eureka voit les deux services : `http://localhost:8761`

---

### Erreur "No JWT token found in SecurityContext"

**Cause :** Le token JWT n'est pas dans le SecurityContext

**Solution :**
1. Vérifier que le token est valide
2. Vérifier la configuration OAuth2 dans `application.properties`
3. Redémarrer le microservice Conge

---

## ✅ Checklist de Test

- [ ] ✅ Token JWT obtenu
- [ ] ✅ Créer un congé
- [ ] ✅ Récupérer tous les congés
- [ ] ✅ Récupérer un congé par ID
- [ ] ✅ **TEST FEIGN:** Récupérer congés avec noms des drivers
- [ ] ✅ **TEST FEIGN:** Résumé par driver
- [ ] ✅ **TEST FEIGN:** Total jours par driver
- [ ] ✅ Approuver un congé
- [ ] ✅ Rejeter un congé
- [ ] ✅ Récupérer congés par statut
- [ ] ✅ Supprimer un congé
- [ ] ✅ Logs montrent "✅ Feign: Added Authorization header"

---

## 🎉 Conclusion

Si tous les tests passent, votre microservice Conge est **100% fonctionnel** avec la communication OpenFeign vers User !

**Bonne chance avec vos tests !** 🚀

