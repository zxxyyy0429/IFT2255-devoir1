# Rapport du projet IFT2255

## 1. Cadre du projet

### Description du projet 
L’objectif principal est d’offrir un outil d’aide à la décision pour optimiser le choix de cours des étudiants du Département d’informatique et de recherche opérationnelle (DIRO). Le choix de cours constitue une étape cruciale dans le parcours académique des étudiants de l’Université de Montréal, particulièrement au DIRO. 
La structure des programmes est parfois complexe et les sources d’information disponibles sont fragmentées : les données officielles partielles (Planifium, résultats globaux) et les avis étudiants éparpillés (forums, Discord).
Afin de faciliter la prise de décision et d’offrir une vue plus transparente et centralisée, le projet vise à concevoir une **plateforme web** basée sur une **API REST**, combinant les données officielles et les opinions des étudiants. 

Cette plateforme permettra aux étudiants de :
- rechercher et comparer des cours;
- consulter des résultats académiques agrégés;
- accéder à des avis étudiants représentatifs;
- personnaliser l’affichage selon leur profil et leurs contraintes.

La création de cette plateforme implique plusieurs acteurs dont:
- **les étudiants**, qui souhaitent trouver des cours adaptés à leur cheminement et qui correspond à leurs besoins;
- **les professeurs** définissent le contenu, les objectifs, les horaires du cours, les prérequis des cours et leur disponibilités;
- **les TGDEs** valident les demandes d'inscriptions au cours et respectent les contraintes administratives;
- **les conseillers académiques** accompagnent les étudiants dans leur parcour universitaire et leur fournir des conseils académiques;
- **l’administration et les services informatiques** de l'université de Montréal (UDeM) sont responsables de la mise à jour des données et de la conformité légale.

### Fonctionnement du système
Le système proposé vise à assister les étudiants de l’Université de Montréal dans la **prise de décision pour le choix de cours**.

Son fonctionnement suit un processus interactif et incrémental, conforme au modèle de développement enseigné (modèle en V / itératif-incrémental) :

1. **Authentification et profil utilisateur**
   - Connexion via **SSO UdeM** ; création automatique du profil étudiant.
   - Le profil contient les **préférences** (théorie/pratique, charge de travail souhaitée, plages d’horaires, contraintes personnelles).

2. **Recherche de cours**
   - L’étudiant saisit le mot-clé ou le code du cours.
   - Le système interroge **l’API Planifium** pour obtenir les informations officielles : code, titre, crédits, cycle, horaires, pré-rquis et co-requis.
   - Les résultats sont présentés avec l’indicateur d’**éligibilité** calculé selon le cheminement de l’étudiant.
   - Les professeurs et TGDEs valident la cohérence des informations affichées.

3. **Consultation des résultats académiques**
   - Le système charge les données agrégées (moyenne, nombre d’inscrits, taux d'échecs) depuis les fichiers **CSV** ou fournies par l'administration.
   - L’information est stockée dans une base de données relationnelle et présentée sous forme de tableaux statistiques clairs et à jour.

4. **Lecture des avis étudiants**
   - Un **bot Discord** recueille les évaluations (difficulté, charge de travail, commentaires).
   - Les avis sont agrégés automatiquement et ne sont affichés que si le nombre minimal d'avis **n ≥ 5** est atteint, conformément à la règle métier.
   - Le système calcule des indicateurs synthétiques (moyenne, dispersion, mots-clés dominants).

5. **Comparaison et personnalisation**
   - L’étudiant ajoute plusieurs cours à un **panier de comparaison**.
   - Le moteur de comparaison combine les données officielles (Planifium / CSV) et les avis étudiants.
   - Les résultats sont **filtrés** et **classés** selon les préférences du profil pour offrir une expérience personnalisée.

6. **Mise à jour et sauvegarde**
   - Des **tâches d’import (ETL)** périodiques assurent la synchronisation avec Planifium, la mise à jour des données académiques et la modération des avis Discord.
   - Tous les échanges sont sécurisés via HTTPS ; les données sont anonymisées selon la loi 25 et sont sauvegardées automatiquement.

### Dépendances du système
1. **Dépendances externes**
   - **API Planifium** : source officielle pour les programmes, les horaires et les prérequis ; dépendance critique pour la recherche de cours et la validation des prérequis.
   - **Bot Discord / API Discord** : collecteur des avis étudiants en format JSON ; dépendance asynchrone et soumise à la disponibilité du service Discord.
   - **Fichiers CSV fournis par l’administration** : données de résultats académiques agrégées ; mises à jour à chaque session.
   - **SSO UdeM** : service d’authentification institutionnel obligatoire pour accéder au profil étudiant.

2. **Dépendances internes (techniques)**
   - **Base SQL** : stockage des résultats académiques (moyennes, taux d’échec, nombre d’inscrits).
   - **Base NoSQL (JSON/Document Store)** : stockage des avis étudiants et métadonnées.
   - **Moteur de filtrage et de personnalisation** : applique les préférences utilisateurs aux recherches et comparaisons.
   - **Service API interne (REST)** : gère la communication entre l’interface web, la logique métier et les bases de données.
   - **Module de cache** : optimise les temps de réponse (objectif P95 < 2 s).
   - **Système de journalisation et surveillance** : collecte des métriques (latence, taux d’erreur) et déclenchement d’alertes.


3. **Dépendances organisationnelles et légales**
   - **Loi 25 (Québec)** : encadrement légal du traitement des données personnelles ; anonymisation et consentement obligatoires.
   - **TGDEs et professeurs**: responsables de la validation et de la cohérence entre les données officielles et les informations affichées.
   - **Conseillers académiques**: utilisent la plateforme pour orienter les étudiants lors du choix de cours.
   - **Services informatiques de l’UdeM** : assurent l'hébergement, certificats HTTPS, maintenance du SSO.
   - **Cycle académique** : contraintes temporelles (périodes d’inscription, début de session) influençant la charge du système.
   - **Ressources humaines** : auxiliaires responsables de l’import des fichiers CSV, de la modération des avis et de la vérification de conformité.

### Équipe : 

Ruoxuan Hu (20304027) , username Discord : rx_04

Ziyue Wang (20308297) , username Discord : ZY

Xinyan Zhang (20264873) , username Discord : Xinyan

Yu Tong Zhu (20310738) , username Discord : yutong


### Distribution des tâches : 

#### Première soumission pour le devoir 1 (Septembre 26) :

Ziyue Wang (20308297) et Ruoxuan Hu (20304027): 

- Description du domaine : acteurs
- Description du projet
- Glossaire : liste des termes et expressions utilisés lors de la premier soumission
- Risque : au moins 5 risques
- Besoins de matériel (sans les solutions)

Xinyan Zhang (20264873): 
- Diagrammes des cas d'utilisation

#### Version final pour le devoir 1 (October 10) :

Ziyue Wang (20308297) et Ruoxuan Hu (20304027):  
- Les coordonées des membres
- Hypothèse 
- Solution de stockage et solution d’intégration
- Besoin non fonctionels
- GitHub (rappport)
- Brouillon pour le modèle C4 (niveau 1 et 2)
- Finaliser rapport HTML
- Rapport modèle C4

Xinyan Zhang (20264873): 
- Échéancier
- Diagramme de cas d’utilisation
- Diagramme d’activité
- Brouillon pour le modèle C4 (niveau 1 et 2)
- GitHub (Rapport, README, Release) 
- Finaliser rapport HTML

Yutong Zhu (20310738): 
- Rapport modèle C4
- Modèle C4
- GitHub (Rapport modèle C4, Modèle C4)
- Code bonus

### Échéancier : 
![Échéancier](diagrams/Echéancier.png)



#### Première soumission pour le devoir 1 (Novembre 20) :

Ziyue Wang (20308297) et Ruoxuan Hu (20304027):
- Diagrammes de sequences ( Avis et Recherche du cours)
- Diagramme de classes
- Modele C4 niveau 3

Xinyan Zhang (20264873):
- Mise a jour diagramme d’activite
- Diagramme de sequence ( Comparer cours )
- Modele C4 niveau 3

Yutong Zhu (20310738):
- Mise a jour Glossaire
- Mise a jour description du projet
- Modele C4

#### Version final pour le devoir 2 (Novembre 30) :

Ziyue Wang (20308297) et Ruoxuan Hu (20304027):
- Glossaire mise a jour
- Mise a jour description du projet
- Mise a jour besoin non fonctionels
- Mise a jour cas d’utilisation principeaux dans le rapport
- Mise a jour distribution des taches
- Mise a jour besoins meteriels, solution de stockage et solution d’integreation
- Diagramme de classe
- Diagrammes de sequences (Avis et Recherche du cours)
- Mise a jour risque
- Justification des choix du design pour le diagramme de sequences  (Avis et Recherche du cours) et le diagramme de classe
- Implementation: Recherche du cours
- Test unitaires et oracles du test pour les tests 1-6
- Mise a jour GitHub

Xinyan Zhang (20264873):
- Echeancier
- Mise a jour du diagramme d’activite
- Digramma de sequences ( Comparer cours )
- Diagramme de class
- Implementation recherche du cours
- Justification des choix du design pour le diagramme d’activite et Diagramme de sequence ( Comparer cours )
- Test unitaires et oracle du test pour les tests 9-12
- Mise a jour GitHub
- Release

Yutong Zhu (20310738):
- Modele C4 niveau 3
- La justification du design pour le Modele C4 niveau 1 , 2 et 3
- Test unitaires et oracle du test pour les tests 6-9
- Implementation voir les details d’un cours et comparer des cours
- Mise a jour GitHub

### Échéancier : 

## 2. Analyse des exigences 

### Description du domaine 
**Utilisateurs visés**
   - Nouveaux étudiants (manque de repères académiques)

   - Étudiants en fin de parcours (diplomation, optimisation de la moyenne)

   - Étudiants internationaux (adaptation au système académique québécois)

   - Étudiants travailleurs (charge de travail limitée)

   - Étudiants parents (équilibre vie familiale et études)

   - Étudiants à temps plein / temps partiel


### Facteurs académiques
   - Moyenne de la classe

   - Taux de réussite / échec / abandon 

   - Nombre d’inscription 

   - Préalables et co-requis

   - Nombre de crédits attribués

   - Dates des examens 

   - Description officielle du cours


### Facteurs personnels
   - Charge de travail (devoirs, projets, lectures)

   - Rythme du cours

   - Intérêts académiques et orientation professionnelle

   - Préférence théorie / pratique

### Facteurs logistiques
   - Mode d’enseignement (présentiel, hybride, en ligne)

   - Compatibilité horaire

   - Contraintes de transport


### Facteurs sociaux
   - Avis étudiants (forums, Discord, bouche-à-oreille)

   - Réputation des enseignants

   - Niveau de stress perçu

### Sources de données
   - **Planifium API** : Catalogue officiel (codes, titres, crédits, horaires, préalables).

   - **Résultats académiques agrégés** : CSV (moyenne, inscrits, échecs).

   - **Avis étudiants (Discord)** : JSON via bot (difficulté perçue, charge de travail, commentaires).



## 3. Hypothèses
Ces hypothèses permettent de clarifier le cadre du projet et d’éviter les ambiguïtés lors de la conception.

Dans l’élaboration du projet, certaines hypothèses de travail ont été émises afin de délimiter le périmètre du système :
   - Tous les étudiants disposent d’un accès Internet stable et d’un navigateur moderne compatible (Chrome, Firefox, Edge, Safari).

   - Les données fournies par l’API Planifium sont fiables et mises à jour régulièrement par l’Université.

   - Les étudiants savent utiliser une interface web basique et sont capables de naviguer sur une plateforme en ligne.

   - L’authentification institutionnelle (SSO UdeM) est disponible et fonctionnelle pour identifier les utilisateurs.

   - Les avis étudiants collectés via Discord sont anonymisés et représentatifs d’un nombre suffisant de participants. (les étudiants sont avisés de la collecte des données et sont d’accord).

   - Le volume d’utilisateurs simultanés reste raisonnable (quelques milliers) et ne dépasse pas la capacité initiale prévue.

   - Les fichiers CSV des résultats académiques sont complets et mis à jour à la fin de chaque session.

   - La plateforme est utilisée sur des appareils compatibles et le design responsive fonctionne correctement.

   - Les échanges avec l’API Planifium et les services internes sont sécurisés (HTTPS) et certains, sans pertes de données pendant les transferts.


## 4. Glossaire
   - **Planifium API** : Service fournissant les données officielles des cours/programmes de l’UdeM.
  
   - **Format CSV** : Format de fichier tabulaire utilisé pour stocker les résultats académiques agrégés.
  
   - **Résultats académiques agrégés (CSV)** : Données statistiques globales d’un cours (moyenne, inscrits, échecs).
  
   - **JSON** : Format d’échange de données structurées, utilisé pour stocker et transmettre les avis des étudiants.
  
   - **Avis étudiants**: Retours qualitatifs/quantitatifs collectés via Discord (JSON).

   - **Loi 25** : Loi québécoise sur la protection des renseignements personnels (préserver la confidentialité).

   - **Mot-clé (Recherche)** : Code, titre ou fragment de texte utilisé par l’étudiant pour retrouver un cours.
  
   - **Bloc d’horaire** : Bloc du cours - matin (8:30-13:30), après-midi (13:30-18:30) et soir (18:30-22:30)
  
   - **Type d’utilisateur** : Les étudiants internationaux (12 crédits), les étudiants à temps plein (12-18 crédits), les étudiants à temps partiels (>6 crédits), les étudiants parents et les étudiants qui travaillent (option de bloc d’horaire).
  
   - **Utilisateur** :Les étudiants qui veulent obtenir des informations académiques qui les aident dans leur cheminement académique.
  
   - **Numéro d’utilisateur** : La matricule attribuée à chacun des étudiants de la part de l’institution (UDEM).
  
   - **Service** : Fournir les informations à propos d’un cours et de ses informations complémentaires (ex : notes, avis sur les profs, taux d’échec, moyenne, etc.).
  
   - **Administrateur système** : Maintenance et mise à jour des nouveaux cours.
  
   - **Information personnelle** : Les préférences fournies (ex : jour de la semaine des cours, tri des semestres fourni par l’école, bloc de la journée, etc) par les utilisateurs (les étudiants).

   - **Tableau comparatif** : Vue synthétique qui regroupe les données de plusieurs cours afin de faciliter leur comparaison.

   - **Panier de cours** : Liste temporaire de cours sélectionnés par l’étudiant en vue d’une comparaison.

   - **Préférences (Profil)** : Paramètres définis par l’étudiant (théorie/pratique, contraintes personnelles) afin de personnaliser l’affichage des résultats.

   - **Moteur de filtrage** : Composant logiciel qui applique les préférences de l’étudiant pour trier et adapter la liste de cours affichée.

- **DIRO (Département d’informatique et de recherche opérationnelle)** ： Département de l’Université de Montréal responsable des programmes en informatique, où se situent la majorité des cours analysés dans ce projet.

- **Programme** ：Ensemble structuré de cours menant à un diplôme (BAC, maîtrise). Chaque programme impose des prérequis, cycles et contraintes.

- **Cycle** ：Niveau d’études auquel appartient un cours (1ᵉ cycle = baccalauréat, 2ᵉ cycle = maîtrise, etc.).

- **Conseiller académique** ：Personnel chargé d’aider les étudiants à planifier leur parcours et à faire des choix de cours appropriés.

- **Le choix de cours** ：Processus par lequel un étudiant sélectionne les cours d’une session en fonction de son cheminement, intérêts et contraintes.

- **Nouveaux étudiants**：Étudiants débutant leur programme et ayant peu de repères académiques.

- **Étudiants en fin de parcours**：Étudiants proches de la diplomation, devant prioriser la complétion des exigences du programme.

- **Étudiants internationaux**：Étudiants provenant d’un autre pays et s’adaptant au système universitaire québécois.

- **Étudiants travailleurs**：Étudiants occupant un emploi en parallèle de leurs études et devant limiter leur charge de travail.
  
- **Étudiants parents** ：Étudiants ayant des responsabilités familiales influençant leur disponibilité.

- **Étudiants à temps plein / temps partiel**：Catégories selon le nombre de crédits suivis par session.

- **Profil académique** ：Informations décrivant le parcours et les besoins d’un étudiant : programme, préférences, contraintes et cheminement.

- **Prérequi (ou Prérequis)**：Cours ou compétence obligatoire avant d’accéder à un autre cours.

- **Cheminement (ou Parcours)** ：Progression académique d’un étudiant dans son programme (cours déjà faits, crédits accumulés, niveau atteint).
  
- **Contraintes administratives** ：Règles institutionnelles limitant l’accès à certains cours (cycle, statut, quotas, réservations).

- **Disponibilité** ：Horaire où un cours est offert au moment où un étudiant est libre pour suivre un cours.

- **Conformité légale**：Respect des lois de protection des données personnelles, notamment la Loi 25 au Québec.

- **Évaluation** ：Méthodes utilisées dans un cours pour mesurer la performance des étudiants (examens, travaux, quiz).

- **Taux de réussite / échec / abandon** ：Statistiques indiquant la proportion d’étudiants ayant réussi, échoué ou abandonné un cours durant une session.

- **Intérêts académiques** ：Domaines d’étude ou thématiques qui motivent un étudiant dans le choix de ses cours.

- **Orientation personnelle** ：Objectifs ou préférences individuelles influençant la sélection des cours (ex : carrière, style d’apprentissage).

- **Contrainte de transport** ：Limitations liées au déplacement  vers le campus, influençant l’horaire choisi.

- **Réputation des enseignants**：Perception de la qualité d’un professeur selon les expériences rapportées par les étudiants.

- **Authentification (compte UdeM)**：Processus permettant d’identifier un étudiant avec ses identifiants institutionnels pour personnaliser l’expérience.

- **Préférences (théorie/pratique)** ：Paramètres choisis par l’étudiant indiquant s’il préfère des cours à dominante théorique (concepts, mathématiques, algorithmes) ou pratique (laboratoires, projets, programmation). Ces préférences sont utilisées par l’outil pour personnaliser l’affichage des résultats, recommander certains cours, et filtrer les options lors de la recherche ou de la comparaison.

- **Interface en ligne de commande (CLI)** ：Interface utilisateur basée sur du texte permettant d’interagir avec un système logiciel via l’entrée de commandes. Elle fonctionne sans interface graphique et constitue l’interface minimale exigée dans le cadre du projet.

- **Base de données NoSQL** ：Type de base de données non relationnelle conçue pour stocker des données semi-structurées ou non structurées, comme des documents JSON. Elle est adaptée aux données évolutives, flexibles et hétérogènes, comme les avis étudiants collectés via le bot Discord.

- **Contrôle des accès basé sur les rôles (RBAC – Role-Based Access Control)** ：Mécanisme de sécurité permettant de restreindre l’accès aux ressources du système selon le rôle de l’utilisateur (ex. : étudiant, auxiliaire, administrateur). Il assure la protection des données sensibles et le respect des niveaux d’autorisation.

## 5. Risques du projet
 ### 1. Données incomplètes ou biaisées
- Probabilité : moyenne ; Impact : élevé
- Problème: Les avis des étudiants sont souvent subjectifs (influence des préférences personnelles, style d’enseignement, difficultés perçues) ou insuffisants. De plus, s’il y a trop peu d’avis, les résultats sont faussés et peu représentatifs.
  
- Conséquences : Les étudiants risquent de prendre de mauvaises décisions basées sur un échantillon non fiable.

- Atténuation :

   i. Seuil minimal (≥ 5 avis) avant d’afficher une évaluation globale
  
   ii. Utiliser les méthodes d’agrégation statistique (moyenne, médiane) pour réduire l’effet des avis extrêmes
  
   iii. Ajouter un avertissement visuel et voyant quand les données sont jugées insuffisantes.


 ### 2. Confidentialité et Loi 25

- Probabilité : faible ; Impact : très élevé 
- Problème : Le projet manipule des données sensibles des étudiants (profils, préférences, historiques). Une mauvaise gestion des données peut mener à une violation de la Loi 25.
- Conséquence : Cela peut causer la perte de confiance des utilisateurs, à des sanctions légales et financières.

- Atténuation :

   i. Mise à jour d’une anonymisation stricte des données
  
   ii. Respect des principes de minimisation des données (on collecte seulement ce qui est nécessaire)
  
   iii. Vérification régulière de conformité légale.


### 3. Données obsolètes ou incohérentes

- Probabilité : moyenne ; Impact : moyen à élevé
- Problème : Les informations affichées peuvent différer des données officielles (ex : décalage entre Planifium et résultats réels). Ce décalage entre les données officielles et les données affichées peut induire l'étudiant en erreur. 
  
- Conséquences : L’étudiant peut prendre de mauvaises décisions de choix de cours basées sur des informations inexactes.


- Atténuation :

   i. Synchronisation régulière et automatique des données.
  
   ii. Affichage de la date de la dernière mise à jour pour chaque donnée.


### 4. Accessibilité limitée

- Probabilité : moyenne ; Impact : moyen
- Problème : Difficulté d’accès pour certains profils (connexion lente, appareil mobile, interface non responsive).
- Conséquence : Temps de chargement long, interface illisible ou non responsive


- Atténuation :

   i. Conception mobile-first
  
   ii. Respect des normes WCAG.


### 5. Performance et surcharge technique

- Probabilité : moyenne ; Impact : élevé
- Problème : Trop de requêtes simultanées pendant les périodes de choix de cours peuvent ralentir le plateforme ou causer des interruptions.
- Conséquence : Expérience des utilisateurs dégradée, risque d’abandon de la plateforme.


- Atténuation :

   i. Mise en place d’un système de cache pour éviter les recalculs inutiles.
  
   ii. Architecture scramble pour supporter les pics de charges.
  
   iii. Surveillance en temps réel des performances avec alertes proactives.

   iiii. Un plan de suivi des risques sera mise à jour à chaque itération du projet.

### 6. Accessibilité limitée pour les étudiants handicapé
- Probabilité : faible ; Impact : élevé
- Problème : Difficulté d’accès pour des étudiants en handicap qui sont des personnes aveugles, sourdes ou ayant des troubles cognitifs.
  
- Conséquence : Exclusion d’une partie de la communauté étudiante.


- Atténuation :
 
   i. Intègre les lecteurs d'écran
  
   ii. Option de personnalisation de l’affichage (agrandir la taille du texte, simplifier la mise en page)

## 6. Besoins non fonctionnels
Au-delà des fonctionnalités principales, la plateforme doit répondre aux exigences de qualité suivantes :

   - **Performance** : Chaque requête (recherche, comparaison, consultation) doit être traitée en moins de 2 secondes afin de garantir une navigation fluide. Les données concernées incluent les cours (sigle, horaire, professeurs), les résultats agrégés et les avis étudiants. Les opérations sur la base locale et les appels à l'API Planifium doivent être optimisés avec la mise en cache et indexation.

   - **Sécurité** : Les données personnelles (nom, courriel, numéro d’utilisateur, préférences, résultats académiques) doivent être protégées conformément à la Loi 25. Les échanges entre le client et le serveur sont chiffrées (HTTPS), et les informations sensibles sont anonymisées avant tout stockage. Les bases contenant des données académiques et des avis doivent être séparées des bases d’authentification pour limiter les risques d'accès non autorisé.


   - **Fiabilité** : La plateforme doit assurer une disponibilité de 99 % pendant les périodes critiques (inscriptions, début de session). Les données concernées sont les métadonnées de cours, les avis, les résultats et les profils utilisateurs. Des sauvegardes régulières et un mécanisme de récupération d’urgence doivent garantir l'intégrité en cas de panne.
  
   - **Évolutivité** : L’architecture doit permettre l’intégration future d’autres sources de données (ex. évaluations professorales, API supplémentaires). La conception modulaire du système facilite l’ajout de nouvelles fonctionnalités sans affecter les modules existants.

   - **Utilisabilité** : L’interface doit être simple et accessible, compatible avec les navigateurs récents et adaptée aux différents supports (mobile, tablette, PC). Elle doit respecter les normes d'accessibilité numérique pour garantir l'accès aux utilisateurs en situation de handicap.

   - **Mémoire et stockage** : La plateforme doit minimiser l'utilisation de l’espace disque local. Les caches temporaires peuvent être nettoyées automatiquement, et les données volumineuses (résultats agrégés, historiques d’avis) doivent être stockées côté serveur.
  
   - **Maintenabilité** : Le code et l’architecture doivent être bien documentés pour faciliter les mises à jour, la correction de bugs et l’ajout de nouvelles fonctionnalités. Le système doit pouvoir recevoir des mises à jour logicielles à distance sans interrompre le service.
  
- **Multilinguisme** : La plateforme peut être utilisée en français et en anglais pour soutenir les étudiants internationaux et le personnel anglophone. Les textes et libellées sont stockés dans des fichiers séparées pour faciliter la maintenance.

## 7. Besoins matériels, solution de stockage et solution d’intégration (Conformité et sécurité)
Les besoins matériels définissent les ressources physiques et logicielles nécessaires au bon fonctionnement de la plateforme.

Le système repose sur une architecture client-serveur hébergée sur une infrastructure virtuelle. 

Cette configuration vise à garantir la performance, la sécurité et la capacité d’évolution du système, conformément aux exigences physiques vues en cours.

Elle respecte également les principes étudiés dans le flux d’analyse concernant l’identification des besoins matériels, de performance, de disponibilité et de sécurité.

#### Besoins matériels et solutions proposées
   - **Serveur d’application (Backend)** :
     Hébergé sur un serveur virtuel (cloud UdeM ou infonuagique public) doté de processeurs virtuels (vCPU), suffisamment de mémoire vive et de stockage SSD. 
     Le système d’exploitation recommandé est Linux, configuré pour le déploiement.
     Afin d’assurer la tolérance aux pannes et la continuité du service, le serveur est configuré avec un mécanisme de redondance et de reprise automatique en cas de défaillance.


   - **Serveur de base de données** :
      Hébergé séparément pour assurer la sécurité et l’intégrité des données.
      Une base de données relationnelle est utilisée pour enregistrer les résultats académiques. Elle permet de stocker les moyennes, les taux d’échec et le nombre d’inscriptions pour chaque cours.
      Des sauvegardes automatiques sont faites régulièrement pour éviter toute perte d’information.
     Cette séparation répond aux bonnes pratiques de conception système en matière de sécurité, d’isolation des responsabilités et de protection des données critiques.

   - **Postes clients** :
      Utilisation possible à partir de navigateurs récents tels que Chrome, Firefox, Edge ou Safari.
      Le design est responsive, permettant un accès fluide sur ordinateur, tablette ou mobile, y compris hors campus.
     Le système supporte également une utilisation en ligne de commande (CLI), conformément aux exigences minimales du mandat.

   - **Réseau et bande passante** :
     Une connexion minimale de 10 Mo/s est requise pour interroger l’API Planifium et Discord.
     Un cache local est mis en place afin de réduire les appels redondants et d’optimiser les performances.
     Ce mécanisme de cache permet aussi de limiter la dépendance directe aux API externes et d’augmenter la disponibilité du système.

   - **Accessibilité et hébergement** :
     L’interface est accessible et adaptée à différents types d’appareils et d’utilisateurs.
     L’accessibilité respecte également les principes d’ergonomie et d’inclusivité exigés pour une utilisation par une population étudiante diversifiée.

### Solution de stockage et sécurité
La solution de stockage combine des technologies relationnelles et non relationnelles pour gérer différents types de données et assurer leur intégrité, leur sécurité et leur évolutivité.

   - **Résultats académiques** :
     Les fichiers CSV fournis par l’administration sont importés dans une base de données relationnelle.
     Cette base permet d’effectuer des requêtes structurées, d’assurer la cohérence des statistiques et de gérer les agrégations par session ou trimestre.
     Ce choix est motivé par la nature fortement structurée des résultats académiques et par le besoin d’intégrité référentielle.

   - **Avis étudiants** :
     Collectés via un bot Discord et enregistrés dans une base de données qui peut gérer des avis et des commentaires d’étudiants. Ce modèle permet de traiter des données semi-structurées et évolutives (commentaires, évaluations, métadonnées).
     Une base NoSQL est privilégiée afin de supporter la variabilité et l’évolution future du format des avis, conformément aux besoins de flexibilité et d’évolutivité du système.

   - **Profils étudiants et préférences** :
     Les données des utilisateurs (cheminement, préférences de cours, contraintes personnelles) sont enregistrées dans une section sécurisée, liée à leur identifiant institutionnel via le SSO (authentification unique) UdeM.
     Les données sensibles sont chiffrées au repos (en base) et en transit (HTTPS/TLS) afin d’assurer la confidentialité et la conformité avec la Loi 25.

   - **Fichiers journaux et statistiques** :
     Les journaux d’activité et les mesures de performance sont sauvegardés dans des fichiers JSON structurés pour assurer la traçabilité, la supervision et la maintenance proactive du système.
     Ces fichiers permettent également d’appuyer le diagnostic en cas d’incident et d’alimenter les outils de monitoring.

   - **Sécurité et confidentialité** :
     Toutes les données sensibles sont protégées lorsqu’elles sont enregistrées et lorsqu’elles sont transmises sur le réseau.
     Les accès sont contrôlés par rôles (étudiant, auxiliaire, administrateur).
     Les avis étudiants sont anonymisés avant publication, en conformité avec la Loi 25.
     Des sauvegardes quotidiennes avec rétention de 30 jours sont réalisées, et l’architecture est scalable horizontalement (via CDN et cache Redis) pour absorber la charge.
     Un contrôle des accès basé sur les rôles (RBAC) est mis en place pour limiter l’exposition des données selon le profil d’utilisateur.
     

### Solution d’intégration
La solution d’intégration garantit la cohérence des échanges entre les différents services internes et externes de la plateforme.

   - **API Planifium (Université de Montréal)** :
     L’API REST de Planifium est utilisée pour obtenir les informations officielles sur les programmes, cours, horaires, crédits et prérequis.
     Les données sont synchronisées quotidiennement via un processus automatisé et mises en cache localement pour réduire le délai d’attente.
     Un mécanisme de gestion d’erreurs est prévu afin de gérer les pannes ou indisponibilités temporaires de l’API externe.

   - **Bot Discord (Avis étudiants)** :
     Le bot recueille automatiquement les avis et notes des étudiants.
     Les messages sont filtrés pour que toute information personnelle (PII) soient supprimés avant leur transformation en JSON et leur insertion dans la base NoSQL.
     Un module de validation est ajouté pour détecter les doublons et garantir la règle n ≥ 5 avant l’affichage public des avis.

   - **Authentification SSO UdeM** :
     L’accès au système est protégé par une authentification institutionnelle, garantissant un lien unique entre les préférences et l’identité de l’étudiant.
     L’authentification respecte les standards de sécurité OAuth2 et OpenID Connect.

   - **API interne REST (backend)** :
     Le système expose plusieurs points d’accès (/courses, /results, /compare, /reviews, /profile) permettant à l’interface utilisateur et aux services internes de communiquer en format JSON sur protocole HTTPS.
     Ces endpoints respectent les bonnes pratiques REST (stateless, structuration des ressources, codes HTTP standards).

   - **CI/CD et supervision technique** :
     Le déploiement est automatisé via GitHub Actions.
     La performance et la stabilité sont surveillées en continu, et des alertes sont envoyées en cas d’erreur ou de lenteur du système.
     Un tableau de bord de monitoring (ex. Grafana/Prometheus) permet de suivre l’état du système en temps réel.

### Solutions techniques globales
L’architecture générale de la plateforme suit le modèle itératif et incrémental (I&I) enseigné en cours.

Chaque incrément (ex. recherche, avis, comparaison) est développé, testé et intégré progressivement, ce qui permet des retours fréquents et une amélioration continue.

L’approche repose sur une architecture composée de plusieurs parties qui communiquent entre elles.

Chaque partie du système a un rôle précis (recherche, affichage, gestion des données) et elles échangent des informations de manière organisée et sécurisée.
 Cette solution assure :
 
   - la **modularité** et la **faible dépendance** entre les composants,


   - la **sécurité** et la conformité légale (Loi 25, chiffrement complet),


   - la **scalabilité** et la **résilience** du système,


   - et la **maintenabilité** à long terme grâce à une documentation claire et un déploiement automatisé.
   - 
Elle respecte également l’alignement entre analyse, conception et implémentation tel qu’exigé dans le cadre méthodologique vu en cours.

Dans son ensemble, cette configuration matérielle et logicielle soutient les objectifs pédagogiques du projet : offrir une plateforme performante, transparente et évolutive, conforme aux standards de génie logiciel enseignés à l’Université de Montréal.


## 8. Cas d’utilisation principaux

### CU1 - Créer un compte

- **But**: Permettre à un nouvel utilisateur d’enregistrer son profil pour utiliser la plateforme.
- **Précondition**: Aucun compte n’existe déjà pour le matricule fourni.
- **Acteurs**: Etudiant (principal), Système (secondaire)
- **Déclencheur**: L’utilisateur clique sur “Créer un compte”.

- **Dépendances** : 
1. Dépendances techniques:
   - Service d’inscription, base d’utilisateurs, serveur HTTPS, service d’envoie de courriel de confirmation.
2. Dépendances logiques:
     - include → CU2 (Se connecter)
     - extend → CU3 (Modifier le profil)

- **Scénario principal**:
1. L’utilisateur ouvre la page d'accueil.
   1.1. Cliquez sur “Créer un compte”.
2. Le système affiche le formulaire d’inscription (nom complet, matricule, courriel UdeM, mot de passe).
3. L’utilisateur saisit les données et accepte la politique (Loi 25).
4. Le système valide (format courriel, unicité matricule, règles mot de passe).
5. Le système crée l'entrée dans la base utilisateurs et envoie un courriel de confirmation.
6. Le système affiche un message de succès et propose la connexion.

- **Scénario alternatif**:
  
4a. Courriel/matricule déjà utilisé: message d’erreur “compte existant”.

4b. Mot de passe non conforme: corriger.

(1-6)a. Erreur serveur: message “réessayer plus tard”.

- **Postcondition**: Compte créé et un nouveau profil utilisateur est enregistré dans la base.


### CU2 - Se connecter

- **But**: Permettre à un utilisateur de s’authentifier pour accéder à ses données et préférences.
- **Précondition**: L’utilisateur possède un compte valide dans la base.
- **Acteurs**: Etudiant, TGDE, Professeur (principal), Système (secondaire).
- **Déclencheur**: L’utilisateur saisit son matricule et son mot de passe.

- **Dépendances** : 
1. Dépendances techniques:
- Service d’authentification (vérification mots de passe)
- Gestion de session
- SSO UdeM si nécessaire
2. Dépendances logiques:
- include → CU3 (Modifier le profil)
- include → CU4 (Rechercher un cours) pour un accès complet.

- **Scénario principal**:
1. L’utilisateur ouvre la page de connexion.
2. Saisit courriel/matricule et le mot de passe.
3. Le système vérifie les identifiants dans la base utilisateurs.
4. Le système crée la session et charge le profil (préférences).
5. Redirection vers le tableau de bord personnalisé.

- **Scénario alternatif**:
3a. Identifiants invalides: message d’erreur.

3b. Compte inactif/suspendu, message “contactez un TGDE”

2.a||. L’utilisateur choisit “mot de passe oublié” : déclenche processus de réinitialisation.

- **Postcondition**: L’utilisateur est authentifié et redirige vers la page d'accueil


### CU3 - Modifier le profil

- **But**: Permettre à l'étudiant de modifier ses informations personnelles ou ses préférences.
- **Précondition**: L’utilisateur est connecté.
- **Acteurs**: Etudiant (principal), Système (secondaire)
- **Déclencheur**: L’utilisateur clique sur “Modifier profil”.

- **Dépendances** :
1. **Dépendances techniques**:
- Base utilisateurs
- Validation serveur
- Journalisation/audit (conformité Loi 25)
2. Dépendance logiques:
- include → CU2 (Se connecter)
- extend → CU8 (Personnaliser affichage)

- **Scénario principal**:
1. L'utilisateur ouvre la page de profil.
2. Le système affiche les champs modifiables (nom, courriel, préférences horaires, etc.).
3. L’utilisateur modifie un ou plusieurs champs.
	3.1. Option: changement du mot de passe: validation forte.
4. Le système valide les nouvelles valeurs.
5. Le système met à jour la base utilisateurs.
6. Le système confirme la modification.

- **Scénario alternatif**:
4a. Validation échoue (format): affiche erreur et indique champs erronés.

(1-6)a. Erreur: message “Modification non enregistrée”.

- **Postcondition**: Les nouvelles informations sont enregistrées et appliquées aux futurs affichages.

### CU4 - Rechercher et consulter la fiche d’un cours

- **But**: Permettre à l'étudiant de rechercher un cours spécifique afin d’obtenir ses informations officielles (prérequis, crédits, description, professeurs, etc.) et de consulter sa fiche complète (description, crédits, horaires, professeurs, etc.)

- **Préconditions**: L’étudiant est connecté au système et la base locale est synchronisée avec l’API Planifium ou capable de demander les données à la demande.

- **Acteurs**: Étudiant (principal), Planifium API (secondaire)

- **Déclencheur**: L’étudiant saisit un mot-clé dans la barre de recherche ou clique sur un cours pour en afficher la fiche.

- **Dépendances**:
1. **Dépendances techniques**:
   - API Planifium fournit les données officielles sur les cours (titre, description, prérequis, professeurs, horaires, etc.).
   - Le système a besoin d’une connexion Internet fonctionnelle pour interroger l’API au moment de la recherche ou de l’ouverture d’une page.
   - Le système utilise la barre de recherche pour trier, filtrer et présenter les résultats selon les critères entrés par l’étudiant.
2. Dépendance logiques:
   - include → CU5 (Lire avis)
   - include → CU6 (Consulter résultats)
   - extend → CU7 (Comparer les cours)
   - extend → CU8 (Personnaliser l’affichage)

- **Scénario principal**
1. L’étudiant accède au champ de recherche.
2. Saisit un mot-clé, un sigle ou applique des filtres (session, professeur).
3. Système interroge l’API Planifium pour récupérer la liste des cours correspondant aux critères.
4. Le système affiche les résultats sous forme de liste (sigle, titre, professeurs, session).
5. L’étudiant clique sur un cours.
6. Le système interroge l’API Planifium pour charger la fiche détaillée.
7. Le système affiche la fiche complète du cours: description, crédits, horaire, professeur, prérequis, options “Lire avis”, “Voir résultats”, “Ajouter à la comparaison”.

- **Scénarios alternatifs**
3a. API Planifium est indisponible, le système affiche un message “Veuillez essayer plus tard.”

2a. Recherche vide ou invalide

6.a Le cours n’est pas trouvé, le système affiche “Cours introuvable” et propose une nouvelle recherche.

- **Postcondition**
La page complète d’un cours est affichée et l’étudiant peut consulter les avis, les résultats ou ajouter le cours à sa comparaison.


### CU5 - Lire les avis des étudiants

- **But**: Permettre à l'étudiant de consulter les retours et évaluations de pairs pour un cours.

- **Préconditions**: Des avis existent dans la base de données importée du Bot Discord.

- **Acteurs**: Étudiant (principal), Bot Discord (secondaire)

- **Déclencheur**: L’étudiant clique sur “Avis étudiants” dans la fiche d’un cours.

- **Dépendance**:
1. Dépendances techniques:
   - Synchronisation entre le bot Discord et la base d’avis.
2. Dépendances logiques:
   - include → CU4 (Rechercher et consulter un cours)
   - extend → CU7 (Comparer les cours)

- **Scénario principal**:
1. De la fiche d’un cours, l’utilisateur clique “Avis étudiants”.
2. Le système vérifie le nombre d’avis disponibles pour ce cours.
	2.1. Si n ≥ 5, le système calcule synthèse (moyenne charge, moyenne difficulte, mots-clés).
3. Le système affiche la synthèse et la liste des avis (filtrable: session, année, type d'étudiants).
4. L’utilisateur peut filtrer (avis récents).

- **Scénarios alternatifs**:
2a. Moins de 5 avis disponibles, message “Avis insuffisants”.

2b. Base inaccessible, message d’erreur.

- **Postcondition**: Les avis sont affichés et la consultation est enregistrée dans le profil de l’étudiant.

### CU6 - Mon avis 

- **But**: Permettre à un étudiant de soumettre son propre avis sur un cours afin d’enrichir la base d’évaluations utilisées pour la comparaison des cours et l’aider à la décision. 

- **Préconditions**: 
  - L’étudiant est authentifié dans la plateforme.
  - Le cours existe dans la base de données.
  - L’étudiant n’a pas déjà rédigé un avis pour ce même cours (selon les règles du système)

- **Acteurs**: Étudiant (principal), Bot Discord (secondaire)

- **Déclencheur**: L’étudiant clique sur “Ajouter un avis” dans la fiche d’un cours.

- **Dépendance**:
- **Dépendances techniques**:
   - Synchronisation entre la plateforme et le Bot Discord. 
   - Validation automatique de données (notation, catégories, commentaire)

- **Dépendances logiques**:
  - include → CU4 (Rechercher et consulter un cours)
  - extend → CU5 (Lire les avis des étudiants)

- Scénario principal:
1. De la fiche d’un cours, l’utilisateur clique “Ajouter un avis”.
2. Le système affiche un formulaire d’évaluation comprenant : 
    a. Charge de travail (1-5)
    b. Difficulté (1-5)
    c. Appréciation générale (note charge) (1-5)
    d. Commentaire optionnel
    e. Session suivie
    f. Echelle de disponibilité du professeur
    g. Nom du professeur 
3. L’étudiant remplit le formulaire et soumet son avis.
4. Le système vérifie : 
   4.1. Que tous les champs obligatoires sont valides
5. Le système enregistre l’avis dans la base interne.
6. Le système synchronise l’avis avec le Bot Discord.
7. Un message de confirmation s’affiche : “Votre avis a été enregistré”

- **Scénarios alternatifs**:
2a. Le formulaire contient des champs incomplets ou invalides → message d’erreur “Veuillez corriger les champs indiqués”. 

6a. Échec de synchronisation avec le bot –”l’avis est marqué “en attente de synchronisation” + avertissement 

- **Postcondition**: L’avis est enregistré et sera pris en compte dans les synthèses affichées dans CU5 et dans les comparaisons de cours (CU7)

### CU7 - Consulter les résultats académiques d’un cours

- **But**: Permettre à l'étudiant de visualiser des données statistiques sur un cours (moyenne, taux d'échec, nombre d’inscrits).

- **Précondition**: Le cours est présent dans la base locale et dispose de résultats agrégés valides.

- **Acteur**: Étudiant (principal), Système (secondaire)

- **Déclencheur**: L'étudiant clique sur “Consulter les résultats” depuis la fiche d’un cours.

- **Dépendances**:
1. Dépendances techniques:
   - Accès à la base de données interne contenant les fichiers de résultats.
   - Base SQL (résultats).
2. Dépendances logiques:
   - include → CU4 (Rechercher et consulter un cours)
   - extend → CU7 (Comparer les cours)

- **Scénario principal**:
1. L’utilisateur clique “Voir résultats” sur la page d’un cours (CU4) ou avec le menu “Résultats”.
2. Le système interroge la base de résultats pour la session demandée.
3. Le système calcule la moyenne finale, nombre inscrits, nombre d'échec.
4. Le système affiche les statistiques.
5. Option: export CSV/PDF.

- **Scénarios alternatifs**:
2a. Données manquantes, message “Résultats non disponibles”.

5a. Erreur d’exportation.

- **Postcondition**: Les statistiques sont affichées et enregistrées dans le journal de consultation.

CU8 - Comparer plusieurs cours

But: Permettre à l'étudiant de comparer plusieurs cours selon différents critères (crédits, taux d'échec, avis, horaire).

Précondition: Au moins deux cours sont ajoutés au panier de comparaison.

Acteurs: Étudiant (principal), Système (secondaire)

Déclencheur:
L'étudiant sélectionne plusieurs cours et clique sur “Comparer”.

Dépendance:
Dépendances techniques: 
Module d’agrégation multi-sources.
Gestion état session.
Accès concurrent aux bases (SQL/NoSQL)
Dépendances logiques:
include → CU6 (Consulter résultat)
include → CU5 (Lire les avis des étudiants
) si avis inclus.
extend → CU3 pour filtrer le profil.

Scénario principal:
L’utilisateur ajoute 2+ cours au panier.
Cliquez sur “Comparer”.
Le système récupère pour chaque cours: crédits, horaires, moyenne, taux d’échec.
Le système aligne les données et génère un tableau comparatif.
L’utilisateur peut trier/masquer les colonnes et exporter le tableau.

Scénarios alternatifs:
1a. Moins de deux cours, message d’erreur.
3a. Données manquantes, affichage partiel avec avertissement.
5a. Conflit d’horaire: donne suggestions de remplacements

Postcondition: Un tableau comparatif est enregistré dans le profil utilisateur.

CU9 - Personnaliser l'affichage

But: Permettre à l'étudiant de filtrer et trier les résultats selon ses préférences.

Précondition: L'étudiant dispose d’un profil avec des préférences enregistrées.

Acteurs: Étudiant (principal), Système (secondaire)

Déclencheur: L'étudiant modifie ou active la personnalisation dans son profil.

Dépendance:
Dépendances techniques:
Base utilisateurs (stockage préférences)
Application runtime
Dépendances logiques:
include → CU4 (Rechercher)
include → CU7 (Comparer)
extend → CU5 (Voir la page du cours), CU5 (Lire les avis des étudiants), CU6 (Consulter résultats).

Scenario principal:
L'utilisateur ouvre la section Préférences.
Le système affiche des options.
L’utilisateur configure et enregistre les préférences dans la base utilisateur.
Le système met à jour la base utilisateurs et applique immédiatement les regles (recherche/affichage/comparaison)

Scénarios alternatifs:
2a. Préférences invalides, message d’avertissement et recommandation.
3b. Erreur de sauvegarde: message “préférences non enregistrées”.

Postcondition: Les préférences sont sauvegardées dans la base de données et appliquées automatiquement.

CU10 - Système contrôleur

But: Coordonner l’exécution des cas d’utilisation en orchestrant les interactions entre l’interface utilisateur, la logique métier et les sources de données. Le système contrôleur assure le bon déroulement des opérations et la cohérence des données.

Précondition:
Les modules principaux (interface, services métier, base de données, import Discord) sont fonctionnels.
L’utilisateur interagit avec la plateforme (navigation, actions, requêtes)


Acteurs: Étudiant (principal), Système externes et Contrôleur principal (secondaire)

Déclencheur: Tout action de l’étudiant ou un événement système déclenche une requête à traiter (ex : afficher un cours, consulter un avis, comparer des cours, filtrer, importer des données, etc.)

Dépendance:
Dépendances techniques:
Couche de service logique (gestion des cours, avis, profils)
API vers sources externes (CSV, Discord)
Gestion des erreurs et vérifications 
Dépendances logiques:
include → tous les CU fonctionnels (CU1 à CU9)

Scenario principal:
L’utilisateur ou un système externe déclenche une action (ex : cliquer sur un bouton, lancer une comparaison, filtrer des avis, etc.)
Le contrôleur reçoit la requête et identifie le cas d’utilisation associé
Le contrôleur valide les préconditions (données disponibles, utilisateur authentifié, règles métier).
Le contrôleur appelle les services appropriés : 
Gestion des cours 
Gestion des avis 
Gestion du profil utilisateur 
Synchronisation discord
Les services exécutent la logique métier associée et renvoient les résultats.
Le contrôleur formate la réponse dans un format exploitable par l’interface utilisateur.
L’interface affiche les résultats à l’utilisateur (cours, avis, comparaison, message, erreur)


Scénarios alternatifs:
3a. Les préconditions ne sont pas remplies (ex. utilisateur non connecté) → message d’erreur.
4a. Le service appelé retourne une erreur (ex. manque de données, import discord échoué) → scénario d’exception.
4b. La synchronisation externe échoue → le contrôleur marque l’opération comme “partielle” et affiche un avis à l’utilisateur.
5a. Les données sont incohérentes ou manquantes → message “Données indisponibles pour le moment”.
6a. La réponse est trop lourde (ex. trop de cours, trop d’avis) → pagination ou réduction automatique.

Postcondition: Le contrôleur a traité la requête, appliqué la logique métier, renvoyé une réponse claire à l’utilisateur et consigné les opérations dans l’historique interne du système.




### Diagramme de cas d'utilisation
![Diagramme de cas d’utilisation](diagrams/Diagramme_de_cas_d'utilisation.jpg)
### Justification ( Diagramme de cas d'utilisation ) :

## 9. Diagramme d'activités
![Diagramme d'activités](diagrams/Diagramme_d'activités.jpg)
### Justification ( Diagramme d'activité ) :

## 10. Modèle C4
### Niveau 1 : Diagramme de contexte
Objectif: Décrire les limites externes du système et ses principales interactions avec les utilisateurs et systèmes externes.

Système principal:
   - Plateforme d’aide aux choix de cours
     - Centralise les informations sur les cours et aide les étudiants dans leur processus de sélection.
     
Acteurs principaux:

   - Étudiants [Personne]
     
     - Utilisateur principal de la plateforme
       
     - Consulte les informations sur les cours
       
   - Administrateurs système [Personne]
     
     - Gère la plateforme et la maintenance

Système externes:

   - API Planifium [API]
     
     - Donne les données officielles des cours, programmes et horaires
       
   - Service d’avis Discord [Système]
     
     - Collecte les avis étudiants avec un bot Discord
       
   - Résultats académiques [Base de données CSV]
     
     - Données agrégées des résultats
       
### Modèle C4 niveau 1
![Modèle C4 niveau 1](diagrams/Modèle_C4_niveau_1.jpg)
### Justification ( Modèle C4 niveau 1 ) :


### Niveau 2 : Diagramme des conteneurs
Objectif: Détailler l’architecture interne principale du système et les interactions entre les conteneurs

1. Application Web [Application Client-Side]
   
   - Communication: API REST avec le backend
   - Responsabilités:
     
      - Interface utilisateur responsive
	  
      - Recherche et comparaison de cours
	  
      - Visualisation des données
	  
2. API Backend [Application Server-Side]

   - Responsabilités: 
	
     - Gestions des profils utilisateurs
	  
     - Sécurité et authentification
	  
   - Communication:
	
      - Application Web (REST)
	  
      - Services externes
	  
3. Base de données principale [Base de données]

   - Responsabilités:
	
     - Stockage des profils utilisateurs
	  
4. Service d'agrégations des avis [Software System]

   - Responsabilités:
	
     - Collecte des avis avec Discord
	  
     - Application des seuils statistiques (n>=5)
	  
     - Agrégation et traitement des données
	  
5. Résultats académiques [Software System]

   - Responsabilités: 
   
     - Stocker l’historique des notes et moyennes
	  
     - Assurer que chaque étudiant possède les prérequis nécéssaire

### Modèle C4 niveau 2
![Modèle C4 niveau 2](diagrams/Modèle_C4_niveau_2.jpg)
### Justification ( Modèle C4 niveau 2 ) :

### Niveau 3 : Diagramme des composants (API Backend)
Objectif: Développer le conteneur API Backend pour montrer ses composants internes.

1. Contrôleur d’authentification
   - Gestion des sessions utilisateur
2. Contrôleur de recherche
   - Gestion des paramètres de recherche
3. Service Planifium
   - Client API pour Planifium
   - Gestion des erreurs et retry
4. Moteur de recommandations
   - Algorithmes de suggestion basés sur le profil
   - Analyse des similarités entre cours

### Modèle C4 niveau 3
### Justification ( Modèle C4 niveau 3 ) :

## 11. Diagramme de classe
### Justification ( Diagramme de classe ):
**Abstraction et Flexibilite** : Le design repose sur une abstraction claire des concepts académiques à travers des classes comme Utilisateur, Étudiant, Cours et Profil Académique. Chaque classe représente un concept précis du domaine et possède une responsabilité unique. Cette abstraction améliore la compréhension du système et assure sa flexibilité, en permettant d’ajouter ou de modifier des fonctionnalités sans changer l’architecture globale.
**Cohesion et encapsulation** : Chaque classe présente une forte cohésion en se concentrant sur une seule tâche (ex. RechercheDeCours pour la recherche, Panier pour la sélection). L’encapsulation est respectée par l’usage d’attributs privés et de méthodes contrôlées, protégeant les données internes. Cela améliore la maintenabilité et la robustesse du système.
**Couplage** : Le faible couplage est assuré par le Système Contrôleur, qui sépare l’interface (Menu) de la logique métier. Les classes fonctionnelles restent ainsi indépendantes de la vue, permettant de modifier l’interface ou les fonctionnalités sans impact majeur sur le système.
**Modularite et interoperabilite** ：Les classes sont regroupées en modules fonctionnels (utilisateur, académique, décision, interface), assurant une bonne modularité et facilitant l’évolution du système. Cette organisation permet aussi une bonne interopérabilité avec des systèmes externes comme des API universitaires, en intégrant leurs données sans modifier la structure existante.

## 12. Diagramme de sequences 

## Recherche du cour
### Justification ( Recherche du cour ) :

## Avis
### Justification ( Avis ) :

## Comparer cour
### Justification ( Comparer cour ) :
