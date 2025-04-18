# Atelier Coordination Front-Back

Dans le cadre du cours de coordination front-end & back-end, nous avons réalisé une application faisant appel à une IA afin de corriger des textes de français.

# Membres

Félix CHABELLARD
Flavio DE FREITAS

# Guide

Une fois le repo clôné, il faut se rendre dans le dossier du projet avec un terminal et exécuter la commande suivante :

  docker compose up

Puis ouvrir le projet "back-spring" à l'aide d'un IDE (ex: IntelliJ) et exécuter l'application.
Enfin, se rendre dans le dossier "front-vue" et exécuter les commandes suivantes :

  npm install
  
  npm run serve

Et enfin se rendre à l'adresse fournie (http://localhost:8082) afin d'accéder au site.

Voilà ! Vous pouvez désormais faire appel à notre correcteur de texte (en français !).

# Stack Technique

Front : VueJS
Back : Spring Boot
+ MySQL (docker)

# Correcteur Orthographique IA - Guide de Démarrage

Ce projet est une application de correction orthographique et grammaticale en français utilisant l'intelligence artificielle. Il se compose d'un backend Spring Boot qui intègre l'API OpenAI et d'un frontend Vue.js pour l'interface utilisateur.

## Prérequis

- Java 17 ou supérieur
- Maven 3.6 ou supérieur
- Node.js 16 ou supérieur
- npm 8 ou supérieur
- Docker et Docker Compose (pour l'environnement de développement)
- Une clé API OpenAI valide

## Structure du Projet

```
correcteur-orthographique/
├── backend/                 # Application Spring Boot
│   ├── src/
│   ├── Dockerfile
│   └── pom.xml
├── frontend/                # Application Vue.js
│   ├── src/
│   ├── public/
│   └── package.json
├── docker-compose.yml       # Configuration Docker pour le développement
└── README.md                # Ce fichier
```

## Configuration

### Backend (Spring Boot)

1. **Configuration de l'API OpenAI**

   Créez un fichier `application.properties` dans `backend/src/main/resources/` avec le contenu suivant :

   ```properties
   # Configuration de base
   spring.application.name=correcteur-orthographique
   server.port=8080

   # Configuration de la base de données
   spring.datasource.url=jdbc:mysql://localhost:3306/mysql
   spring.datasource.username=user
   spring.datasource.password=password
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

   # Configuration OpenAI
   openai.api-key=votre-clé-api-openai
   ```

   Remplacez `votre-clé-api-openai` par votre clé API OpenAI.
   Remplacez `user` et `password` par le user et password utiliser pour la base de donnée.

3. **Fichiers de prompt**

   Créez un fichier `promptCorrecteur.txt` dans `backend/src/main/resources/prompts/` avec le contenu suivant :

   ```
   Tu es un correcteur orthographique et grammatical expert en français. 
   Analyse le texte fourni et corrige-le en respectant ces règles:

   1. Corrige toutes les fautes d'orthographe
   2. Corrige les erreurs grammaticales
   3. Améliore la syntaxe si nécessaire
   4. Respecte le style et le ton du texte original

   Format de réponse:
   - Fournis d'abord le texte entièrement corrigé
   - Ensuite, liste les corrections effectuées avec une brève explication pour chacune au format:
     - "mot original" → "mot corrigé": explication

   N'ajoute pas d'introduction ni de conclusion à ta réponse.
   ```

### Frontend (Vue.js)

Aucune configuration spécifique n'est nécessaire pour le frontend. Les variables d'environnement sont définies dans le fichier `.env` à la racine du projet frontend.

## Démarrage Rapide avec Docker

Le moyen le plus simple de démarrer l'application est d'utiliser Docker Compose :

1. **Définir la clé API OpenAI**

   ```bash
   export OPENAI_API_KEY=votre-clé-api-openai
   ```

2. **Lancer l'application**

   ```bash
   docker-compose up -d
   ```

   Cela va démarrer :
   - Une base de données MySQL
   - Le backend Spring Boot
   - Le frontend Vue.js

3. **Accéder à l'application**

   Ouvrez votre navigateur et accédez à :
   ```
   http://localhost:8080
   ```

## Démarrage Manuel (Développement)

### Base de données

1. **Lancer MySQL avec Docker**

   ```bash
   docker-compose up -d mysql
   ```

### Backend (Spring Boot)

1. **Compiler le projet**

   ```bash
   cd backend
   mvn clean install
   ```

2. **Lancer l'application**

   ```bash
   mvn spring-boot:run
   ```

   Le backend sera accessible à l'adresse `http://localhost:8080`.

### Frontend (Vue.js)

1. **Installer les dépendances**

   ```bash
   cd frontend
   npm install
   ```

2. **Lancer le serveur de développement**

   ```bash
   npm run serve
   ```

   Le frontend sera accessible à l'adresse `http://localhost:8081`.

## Utilisation

1. Accédez à l'application via votre navigateur
2. Entrez le texte à corriger dans la zone de texte
3. Cliquez sur "Corriger" ou attendez 2 secondes après avoir arrêté de taper pour une correction automatique
4. Consultez le texte corrigé et les explications des corrections
5. Consultez l'historique des corrections précédentes en bas de la page

## API REST

Le backend expose les endpoints REST suivants :

- `POST /api/corrections` : Soumet un texte pour correction
- `GET /api/corrections` : Récupère l'historique des corrections
- `GET /api/corrections/{id}` : Récupère une correction spécifique
- `DELETE /api/corrections/{id}` : Supprime une correction
- `GET /api/corrections/stats` : Récupère des statistiques sur les corrections

## Dépannage

### Problèmes de connexion à la base de données

Si vous rencontrez des problèmes de connexion à la base de données, vérifiez que :
- Le conteneur MySQL est en cours d'exécution : `docker ps`
- Les informations de connexion dans `application.properties` sont correctes

### Problèmes avec l'API OpenAI

Si l'API OpenAI ne répond pas correctement :
- Vérifiez que votre clé API est valide et correctement configurée
- Vérifiez les logs du backend pour plus de détails sur l'erreur

### Problèmes de CORS

Si vous rencontrez des erreurs CORS lors de l'appel du backend depuis le frontend :
- Vérifiez que l'annotation `@CrossOrigin` est présente sur les contrôleurs REST
- Assurez-vous que l'URL du backend est correctement configurée dans le frontend

## Contribution

Les contributions sont les bienvenues ! N'hésitez pas à ouvrir une issue ou une pull request.

## Licence

Ce projet est sous licence MIT. Voir le fichier LICENSE pour plus de détails.

---

Pour toute question ou assistance, veuillez contacter l'équipe de développement.
