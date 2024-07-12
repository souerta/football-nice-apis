
# Football API

## Introduction

Ce projet est une API pour la gestion de l'équipe de football de Nice. Il permet de gérer les équipes et les joueurs en incluant des fonctionnalités pour ajouter, mettre à jour, supprimer et récupérer des informations sur les équipes et les joueurs.

## Prérequis

- Docker
- JDK 17
- Maven

## Installation et Exécution

### 1. Cloner le Répertoire du Projet


>git clone https://github.com/souerta/football-nice-apis.git
>cd football-nice-apis


### 2. Configurer la Base de Données PostgreSQL

Démarrer un conteneur PostgreSQL avec Docker :

> docker-compose up -d


### 3. Construire et Exécuter l'Application


> mvn clean install
> mvn  spring-boot:run


## Utilisation de Swagger pour Visualiser et Tester les API



Naviguez vers l'URL suivante : `http://localhost:8081/swagger-ui.html`.


### Configurer l'Authentification

l'authentification pour accéder aux API se fait avec les informations suivantes :

- **Nom d'utilisateur** : `admin`
- **Mot de passe** : `admin123`



## Docker et Docker Hub

### Créer l'Image Docker Localement


> docker build -t football-nice-apis .


### Utiliser l'Image Docker depuis Docker Hub


> docker pull souerta/football-nice-apis:latest

>
> docker build -t docker.io/souerta/football-nice-apis:latest

### Démarrer les Conteneurs Docker avec une Configuration Réseau pour PostgreSQL et l’Application


> docker-compose up -d


## Contribuer

Les contributions sont les bienvenues! Veuillez soumettre une pull request ou ouvrir une issue pour discuter de ce que vous aimeriez changer.

