# CSV Profiler avec Deequ - Guide d'installation et d'utilisation

## 📋 Description

Cette application web permet de réaliser le profilage de fichiers CSV en utilisant la bibliothèque **Deequ** d'Amazon Web Services. L'application est composée de :

- **Backend** : Spring Boot avec intégration Spark et Deequ
- **Frontend** : Angular avec Angular Material pour l'interface utilisateur
- **Profilage** : Analyse de la qualité des données avec Deequ

## 🏗️ Architecture

```
┌─────────────────┐    HTTP/REST    ┌─────────────────┐
│                 │   ←----------→   │                 │
│   Frontend      │                 │   Backend       │
│   Angular       │                 │   Spring Boot   │
│   Port: 4200    │                 │   Port: 8080    │
│                 │                 │                 │
└─────────────────┘                 └─────────────────┘
                                             │
                                             ▼
                                    ┌─────────────────┐
                                    │   Apache Spark  │
                                    │   + Deequ       │
                                    │   Profilage CSV │
                                    └─────────────────┘
```

## 🚀 Installation rapide avec Docker

### Prérequis
- Docker et Docker Compose installés
- Au moins 4GB de RAM disponible

### Démarrage rapide
```bash
# Cloner le repository
git clone <repository-url>
cd csv-profiler

# Construire et démarrer l'application
make build
make run

# Vérifier que tout fonctionne
make health
```

L'application sera accessible à :
- **Frontend** : http://localhost
- **Backend API** : http://localhost:8080/api

## 🛠️ Installation en mode développement

### Backend (Spring Boot)

#### Prérequis
- Java 11 ou supérieur
- Maven 3.6+
- Au moins 2GB de RAM

#### Installation
```bash
cd backend

# Installer les dépendances
./mvnw dependency:resolve

# Lancer les tests
./mvnw test

# Démarrer l'application
./mvnw spring-boot:run
```

Le backend sera accessible sur http://localhost:8080

#### Configuration
Modifier `src/main/resources/application.properties` :
```properties
# Taille maximale des fichiers
spring.servlet.multipart.max-file-size=100MB
spring.servlet.multipart.max-request-size=100MB

# Configuration Spark (optionnel)
spark.sql.adaptive.enabled=true
```

### Frontend (Angular)

#### Prérequis
- Node.js 18+ et npm
- Angular CLI

#### Installation
```bash
cd frontend

# Installer Angular CLI globalement
npm install -g @angular/cli

# Installer les dépendances
npm install

# Démarrer le serveur de développement
ng serve
```

Le frontend sera accessible sur http://localhost:4200

## 📊 Utilisation de l'application

### 1. Téléchargement d'un fichier CSV
- Cliquer sur "Sélectionner un fichier CSV"
- Choisir un fichier CSV (format .csv uniquement)
- Le nom et la taille du fichier s'affichent

### 2. Analyse du fichier
- Cliquer sur "Analyser le fichier"
- L'analyse commence (peut prendre quelques secondes selon la taille)
- Une barre de progression indique que l'analyse est en cours

### 3. Résultats du profilage
Les résultats s'affichent dans un tableau avec les colonnes suivantes :

| Colonne | Description |
|---------|-------------|
| **Nom de la colonne** | Nom de la colonne dans le CSV |
| **Type** | Type de données détecté (String, Integer, Double, etc.) |
| **Complétude** | Pourcentage de valeurs non nulles |
| **Valeurs distinctes** | Nombre de valeurs uniques |
| **Moyenne** | Moyenne (pour les colonnes numériques) |
| **Écart-type** | Écart-type (pour les colonnes numériques) |
| **Min** | Valeur minimale |
| **Max** | Valeur maximale |
| **Valeurs nulles** | Nombre de valeurs nulles |
| **Total** | Nombre total de lignes |

## 🔧 Commandes disponibles

### Avec Make
```bash
make help          # Afficher l'aide
make build         # Construire l'application
make run           # Démarrer l'application
make stop          # Arrêter l'application
make clean         # Nettoyer les conteneurs
make logs          # Afficher les logs
make health        # Vérifier la santé des services
```

### Développement
```bash
make dev-backend   # Démarrer le backend en mode dev
make dev-frontend  # Démarrer le frontend en mode dev
make test-backend  # Tester le backend
make test-frontend # Tester le frontend
```

## 📁 Structure du projet

```
csv-profiler/
├── backend/                    # Application Spring Boot
│   ├── src/main/java/
│   │   └── com/example/csvprofiler/
│   │       ├── controller/     # Contrôleurs REST
│   │       ├── service/        # Services métier
│   │       ├── model/          # Modèles de données
│   │       └── config/         # Configuration
│   ├── pom.xml                # Dépendances Maven
│   └── Dockerfile.backend     # Image Docker backend
├── frontend/                   # Application Angular
│   ├── src/app/
│   │   ├── components/        # Composants Angular
│   │   ├── services/          # Services Angular
│   │   └── models/            # Modèles TypeScript
│   ├── package.json           # Dépendances npm
│   └── Dockerfile.frontend    # Image Docker frontend
├── docker-compose.yml         # Configuration Docker Compose
├── Makefile                   # Commandes simplifiées
└── README.md                  # Ce fichier
```

## 🔍 Fonctionnalités de Deequ utilisées

### Profilage automatique
- **Complétude** : Détection des valeurs manquantes
- **Cardinalité** : Comptage des valeurs distinctes
- **Types de données** : Inférence automatique des types
- **Statistiques** : Calculs statistiques de base

### Métriques disponibles
- Complétude par colonne
- Distribution des types de données
- Statistiques descriptives (min, max, moyenne, écart-type)
- Détection d'anomalies potentielles

## 🚨 Résolution des problèmes

### Problèmes courants

#### Backend ne démarre pas
```bash
# Vérifier les logs
make logs

# Vérifier la mémoire disponible
free -h

# Redémarrer les services
make stop && make run
```

#### Erreur "Out of Memory"
```bash
# Augmenter la mémoire allouée à Java
export JAVA_OPTS="-Xmx4g -Xms2g"
```

#### Frontend ne peut pas se connecter au backend
- Vérifier que le backend est démarré sur le port 8080
- Vérifier la configuration CORS dans `WebConfig.java`
- Vérifier l'URL de l'API dans `csv-profiler.service.ts`

#### Fichier CSV trop volumineux
- Modifier `spring.servlet.multipart.max-file-size` dans `application.properties`
- Augmenter la mémoire allouée à Spark

### Logs et débogage

```bash
# Logs de l'application
docker-compose logs -f

# Logs du backend uniquement
docker-compose logs -f backend

# Logs du frontend uniquement
docker-compose logs -f frontend
```

## 🔒 Sécurité

### Recommandations de production
- Limiter la taille des fichiers uploadés
- Implémenter une authentification
- Utiliser HTTPS
- Valider les formats de fichiers côté serveur
- Nettoyer les fichiers temporaires

### Configuration sécurisée
```properties
# Limiter la taille des uploads
spring.servlet.multipart.max-file-size=50MB
spring.servlet.multipart.max-request-size=50MB

# Sécuriser les uploads
spring.servlet.multipart.location=/tmp/secure-uploads
```

## 📈 Performance

### Optimisations Spark
```properties
# Configuration Spark pour de gros fichiers
spark.sql.adaptive.enabled=true
spark.sql.adaptive.coalescePartitions.enabled=true
spark.serializer=org.apache.spark.serializer.KryoSerializer
```

### Monitoring
- Utiliser les endpoints Spring Boot Actuator
- Surveiller l'utilisation mémoire
- Monitorer les temps de réponse

## 🤝 Contribution

1. Fork le projet
2. Créer une branche feature (`git checkout -b feature/AmazingFeature`)
3. Commit les changements (`git commit -m 'Add some AmazingFeature'`)
4. Push vers la branche (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📝 Licence

Ce projet est sous licence MIT. Voir le fichier `LICENSE` pour plus de détails.

## 📞 Support

Pour toute question ou problème :
- Ouvrir une issue sur GitHub
- Consulter la documentation de [Deequ](https://github.com/awslabs/deequ)
- Vérifier les logs de l'application
