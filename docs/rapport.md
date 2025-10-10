#Rapport du projet IFT2255
1. Cadre du projet

Description du projet 
Le choix de cours constitue une étape cruciale dans le parcours académique des étudiants de l’Université de Montréal, particulièrement au Département d’informatique et de recherche opérationnelle (DIRO). La structure des programmes est parfois complexe et les sources d’information disponibles sont fragmentées : les données officielles partielles (Planifium, résultats globaux) et les avis étudiants éparpillés (forums, Discord).
Afin de faciliter la prise de décision et d’offrir une vue plus transparente et centralisée, le projet vise à concevoir une plateforme web basée sur une API REST, combinant les données officielles et les opinions des étudiants. Cette plateforme permettra aux étudiants de :
rechercher et comparer des cours,
- consulter des résultats académiques agrégés,
- accéder à des avis étudiants représentatifs,
- personnaliser l’affichage selon leur profil et leurs contraintes.
- Ce document présente une première analyse des besoins, les cas d’utilisation principaux ainsi qu’une identification des risques, afin de préparer les prochaines étapes de conception et d’implémentation.

Fonctionnement du système
Le système proposé vise à assister les étudiants de l’Université de Montréal dans la prise de décision pour le choix de cours.
Son fonctionnement suit un processus interactif et incrémental, conforme au modèle de développement enseigné (modèle en V / itératif-incrémental) :
Authentification et profil utilisateur
- Connexion via SSO UdeM ; création automatique du profil étudiant.
Le profil contient les préférences (théorie/pratique, charge de travail souhaitée, plages d’horaires, contraintes personnelles).

- Recherche de cours:
L’étudiant saisit le mot-clé ou le code du cours.
Le système interroge l’API Planifium pour obtenir les informations officielles : code, titre, crédits, cycle, horaires, pré- et co-requis.
Les résultats sont présentés avec l’indicateur d’éligibilité calculé selon le cheminement de l’étudiant.
Consultation des résultats académiques
Le système charge les données agrégées depuis les fichiers CSV (moyenne, nombre d’inscrits, échecs).
L’information est stockée dans une base de données relationnelle et présentée sous forme de tableaux statistiques clairs.
Lecture des avis étudiants
Un bot Discord recueille les évaluations (difficulté, charge, commentaire).
Les avis sont agrégés automatiquement et ne sont affichés que lorsque n ≥ 5, conformément à la règle métier.
Le système calcule des indicateurs synthétiques (moyenne, dispersion, mots-clés dominants).
Comparaison et personnalisation
L’étudiant ajoute plusieurs cours à un panier de comparaison.
Le moteur de comparaison combine les données officielles (Planifium / CSV) et les avis étudiants.
Les résultats sont filtrés et classés selon les préférences du profil pour offrir une expérience personnalisée.
Mise à jour et sauvegarde
Des tâches d’import (ETL) périodiques assurent la synchronisation avec Planifium, l’actualisation des CSV et la modération des avis Discord.
Tous les échanges sont sécurisés via HTTPS ; les données sont sauvegardées automatiquement .


Dépendances du système
1. Dépendances externes
API Planifium : source officielle pour les programmes et horaires ; dépendance critique pour la recherche et la validation des prérequis.
Bot Discord / API Discord : collecteur des avis étudiants en format JSON ; dépendance asynchrone et soumise à la disponibilité du service Discord.
Fichiers CSV fournis par l’administration : données de résultats agrégées mises à jour à chaque session.
SSO UdeM : service d’authentification institutionnel obligatoire pour accéder au profil.

2. Dépendances internes (techniques)
Base SQL : stockage des résultats académiques (moyennes, taux d’échec, nombre d’inscrits).
Base NoSQL (JSON/Document Store) : stockage des avis étudiants et métadonnées.
Moteur de filtrage et de personnalisation : applique les préférences utilisateurs aux recherches et comparaisons.
Service API interne (REST) : gère la communication entre l’interface web, la logique métier et les bases de données.
Module de cache : optimise les temps de réponse (objectif P95 < 2 s).
Système de journalisation et surveillance : collecte des métriques (latence, taux d’erreur) et déclenchement d’alertes.


3. Dépendances organisationnelles et légales
Loi 25 (Québec) : encadrement légal du traitement des données personnelles ; anonymisation et consentement obligatoires.
Services informatiques de l’UdeM : hébergement, certificats HTTPS, maintenance du SSO.
Cycle académique : contraintes temporelles (périodes d’inscription, début de session) influençant la charge du système.
Ressources humaines : auxiliaires responsables de l’import des fichiers CSV, de la modération des avis et de la vérification de conformité.

Équipe : 


Ruoxuan Hu (20304027) , username Discord : rx_04


Ziyue Wang (20308297) , username Discord : ZY


Xinyan Zhang (20264873) , username Discord : Xinyan


Yutong Zhu (20310738) , username Discord : yutong

Distribution de tache : 


Premier submission ( Septembre 26):

Ziyue et Ruoxuan : 
Description du domaine : acteurs
description du projet
Glossaire : liste des termes et expressions utilises lors de la premier submission
Risque : au moins 5 risque
Besoins de materiel ( sans le solution )

Xinyan : 
Diagram de cas utilisation

- Version final ( October 10 )
Ziyue et Ruoxuan : 
Les coordonees des membres
Hypothese 
Solution de stockage et solution d’integration
Besoin non fonctionels
Git hub
Brouillon pour le modele C4 ( niveau 1 et 2 )
Finaliser rapport HTML

Xinyan : 
Echeancier
Diagramme de cas d’utilisation
Diagramme d’activite
Brouillon pour le modele C4 ( niveau 1 et 2 )
Git hub
Finaliser rapport HTML

Yutong : 
Modele C4 

