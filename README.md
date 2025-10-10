# IFT2255-devoir1

# Système: Plateforme d'aide au choix de cours

## Description du projet
Ce système, Plateforme d'aide au choix de cours, est une plateforme web basée sur une API REST, combinant les données officielles et les opinions des étudiants. Cette plateforme permettra aux étudiants de rechercher et comparer des cours, de consulter des résultats académiques agrégés, d'accéder à des avis étudiants représentatifs et de personnaliser l’affichage selon leur profil et leurs contraintes. Ce sysystème facilite le choix de cours et la prise de décision des étudiants, et offre une vue plus transparente et centralisée des informations disponibles. 

## version 2
Le choix de cours constitue une étape cruciale dans le parcours académique des étudiants de l’Université de Montréal, particulièrement au Département d’informatique et de recherche opérationnelle (DIRO). La structure des programmes est parfois complexe et les sources d’information disponibles sont fragmentées : les données officielles partielles (Planifium, résultats globaux) et les avis étudiants éparpillés (forums, Discord).
Afin de faciliter la prise de décision et d’offrir une vue plus transparente et centralisée, le projet vise à concevoir une plateforme web basée sur une API REST, combinant les données officielles et les opinions des étudiants. Cette plateforme permettra aux étudiants de :
rechercher et comparer des cours,
consulter des résultats académiques agrégés,
accéder à des avis étudiants représentatifs,
personnaliser l’affichage selon leur profil et leurs contraintes.

Ce document présente une première analyse des besoins, les cas d’utilisation principaux ainsi qu’une identification des risques, afin de préparer les prochaines étapes de conception et d’implémentation.

### Fonctionnement du système
Le système proposé vise à assister les étudiants de l’Université de Montréal dans la prise de décision pour le choix de cours.
Son fonctionnement suit un processus interactif et incrémental, conforme au modèle de développement enseigné (modèle en V / itératif-incrémental) :
Authentification et profil utilisateur
Connexion via SSO UdeM ; création automatique du profil étudiant.
Le profil contient les préférences (théorie/pratique, charge de travail souhaitée, plages d’horaires, contraintes personnelles)
Recherche de cours
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

## Organisation du répertoire
```
IFT2255-devoir1/
  ├── docs/
  │  └── rapport.md
  ├── diagrams/
  │  ├── Diagramme de cas d'utilisation
  │  ├── Diagramme d'activités
  │  └── Modèle C4
  └── README.md
