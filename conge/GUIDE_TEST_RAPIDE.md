# 🧪 Guide de Test Rapide - Microservice Conge

## 🎯 Objectif

Tester la communication OpenFeign entre **Conge** et **User** microservices.

---

## 📋 Prérequis

- ✅ Keycloak démarré (port 8060)
- ✅ Eureka Server démarré (port 8761)
- ✅ User microservice démarré (port 8081)
- ✅ Conge microservice démarré (port 8085)

---

## 🚀 Étape 1 : Démarrer les Services

### Terminal 1 - Eureka Server
```bash
cd Eureka
mvn spring-boot:run
```

**Vérifier :** `http://localhost:8761`

---

### Terminal 2 - User Microservice
```bash
cd user
mvn spring-boot:run
```

**Vérifier :** `http://localhost:8081/api/users` (avec JWT)

---

### Terminal 3 - Conge Microservice
```bash
cd conge
mvn spring-boot:run
```

**Vérifier :** `http://localhost:8085/conge/leaves` (avec JWT)

---

## 🔐 Étape 2 : Obtenir un Token JWT

### Dans Postman

**Méthode :** `POST`  
**URL :** `http://localhost:8060/realms/SpeedyGo5se4/protocol/openid-connect/token`

**Headers :**
```
Content-Type: application/x-www-form-urlencoded
```

**Body (x-www-form-urlencoded) :**

| Key | Value |
|-----|-------|
| `grant_type` | `password` |
| `client_id` | `gatwey` |
| `client_secret` | `ssPPSYZZNYVFFkxrV0UJdxzpHHIoSiUB` |
| `username` | `testing@example.com` |
| `password` | `votre-mot-de-passe` |

**Réponse attendue :**
```json
{
  "access_token": "eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldU...",
  "expires_in": 300,
  "token_type": "Bearer"
}
```

**Copier le `access_token` !**

---

## ✅ Étape 3 : Tester les Endpoints Conge

### Test 1 : Créer un Congé

**Méthode :** `POST`  
**URL :** `http://localhost:8085/conge/leaves/create`

**Headers :**
```
Authorization: Bearer VOTRE_ACCESS_TOKEN
Content-Type: application/json
```

**Body (JSON) :**
```json
{
  "startDate": "2025-11-15",
  "endDate": "2025-11-20",
  "reason": "Vacances d'été",
  "driverId": "1"
}
```

**Réponse attendue (201 Created) :**
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

---

### Test 2 : Récupérer tous les Congés

**Méthode :** `GET`  
**URL :** `http://localhost:8085/conge/leaves`

**Headers :**
```
Authorization: Bearer VOTRE_ACCESS_TOKEN
```

**Réponse attendue (200 OK) :**
```json
[
  {
    "id": "uuid-1",
    "startDate": "2025-11-15",
    "endDate": "2025-11-20",
    "reason": "Vacances d'été",
    "status": "PENDING",
    "driverId": "1"
  }
]
```

---

### Test 3 : Récupérer les Congés avec Noms des Drivers (Feign)

**Méthode :** `GET`  
**URL :** `http://localhost:8085/conge/leaves/with-driver-names`

**Headers :**
```
Authorization: Bearer VOTRE_ACCESS_TOKEN
```

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

### Test 4 : Résumé Détaillé par Driver (Feign)

**Méthode :** `GET`  
**URL :** `http://localhost:8085/conge/leaves/summary`

**Headers :**
```
Authorization: Bearer VOTRE_ACCESS_TOKEN
```

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

## 🔍 Vérifier les Logs

### Dans les logs du microservice Conge

Vous devriez voir :

```
✅ Feign: Added Authorization header to request
```

Cela confirme que le JWT est bien propagé automatiquement.

---

## 🐛 Dépannage

### Erreur 401 Unauthorized

**Cause :** Token JWT manquant ou expiré

**Solution :**
1. Vérifier que le header `Authorization: Bearer TOKEN` est présent
2. Réobtenir un nouveau token (expire après 5 minutes)

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

## ✅ Checklist de Vérification

- [ ] ✅ Keycloak démarré (http://localhost:8060)
- [ ] ✅ Eureka démarré (http://localhost:8761)
- [ ] ✅ User microservice enregistré dans Eureka
- [ ] ✅ Conge microservice enregistré dans Eureka
- [ ] ✅ Token JWT obtenu depuis Keycloak
- [ ] ✅ Endpoint `/leaves/create` fonctionne
- [ ] ✅ Endpoint `/leaves` fonctionne
- [ ] ✅ Endpoint `/leaves/with-driver-names` fonctionne (Feign)
- [ ] ✅ Endpoint `/leaves/summary` fonctionne (Feign)
- [ ] ✅ Logs montrent "✅ Feign: Added Authorization header"

---

## 🎉 Résultat Attendu

Si tous les tests passent :

✅ **Communication Feign fonctionnelle**  
✅ **Propagation JWT automatique**  
✅ **Récupération des infos drivers depuis User**  
✅ **Architecture identique à fastpost-delivery**

**Votre intégration OpenFeign est 100% opérationnelle !** 🚀

---

## 📚 Documentation Complète

Pour plus de détails, consultez :
- `COMMUNICATION_FEIGN_IMPLEMENTATION.md` - Documentation technique complète
- `SpeedyGo_Conge_Postman_Collection.json` - Collection Postman prête à importer

---

## 🆘 Besoin d'Aide ?

Si vous rencontrez des problèmes :

1. Vérifier les logs des microservices
2. Vérifier que tous les services sont démarrés
3. Vérifier que le token JWT est valide
4. Vérifier la configuration dans `application.properties`

**Bonne chance avec vos tests !** 🚀

