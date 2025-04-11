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