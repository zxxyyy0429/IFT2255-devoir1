# IFT2255-devoir1

## Système: Plateforme d'aide au choix de cours

### Description du projet
Ce système, Plateforme d'aide au choix de cours, est une plateforme web basée sur une API REST, combinant les données officielles et les opinions des étudiants. Cette plateforme permettra aux étudiants de rechercher et comparer des cours, de consulter des résultats académiques agrégés, d'accéder à des avis étudiants représentatifs et de personnaliser l’affichage selon leur profil et leurs contraintes. Ce sysystème facilite le choix de cours et la prise de décision des étudiants, et offre une vue plus transparente et centralisée des informations disponibles. 

### Le guide pour evaluer notre code
#### Recherche de cours 

-	par un sigle particulier : http://localhost:7070/courses/IFT1005
-	par des mot-clés : lister tous les cours du département informatique : http://localhost:7070/courses/search?sigle=IFT
- lister tous les cours où il y a le mot «programmation» dans la description : http://localhost:7070/courses/search?description=programmation 

#### Voir les détails d’un cours :

- afficher les détails d’un cours avec son id(montrer plus des informations, inclut le sigle, le nom, la description, le crédit, le trimestre disponible et le prérequis du cours) : http://localhost:7070/courses/IFT2255/details

#### Comparer des cours :

- Comparer deux cours par id (lister deux cours avec quelques simples informations et montrer le prérequis commun) : http://localhost:7070/courses/compare?id1=IFT2015&id2=IFT2255

### Test unitaire

Tous les 30 tests sont situés dans le fichier :
CourseControllerTest.java

Ils couvrent :


1. Lister les cours (GET /courses)
- Retour de la liste complète lorsque aucun paramètre n’est fourni
- Transmission correcte des query parameters au service
- Gestion d’un résultat vide (liste vide)
- Tolérance aux paramètres inconnus (ignorés)
  

2. Recherche / Consultation d’un cours par sigle (GET /courses/:id)
- Retour d’un cours lorsque l’identifiant existe
- Erreur 404 lorsque le cours est introuvable
- Erreur 400 lorsque l’ID est nul ou vide
- Cas d’ID avec espaces (validation/erreur)


3. Détails d’un cours (GET /courses/:id/details)
- Retour d’un JSON formaté (ex. crédits, prérequis, trimestres disponibles)
- Validation d’un ID invalide (ex. longueur trop courte) → 400
- Cas particulier : aucun prérequis → affichage "Aucun"
- Erreur 404 si le cours n’existe pas
- Cas où aucun trimestre n’est disponible (termes tous à false)


4. Recherche par mot-clé (GET /courses/search …)
- Recherche valide (retour d’une liste filtrée)
- Absence de mot-clé → 400
- Aucun résultat → retour liste vide (sans erreur)


5. Comparer deux cours (GET /courses/compare?id1=…&id2=…)
- Comparaison de deux cours valides (retour JSON comparatif)
- Détection des prérequis communs
- Gestion d’erreurs : cours introuvable → 404
- Gestion d’erreurs : id1 manquant / id2 manquant → 400
- Gestion d’erreurs : ids identiques → erreur (statut d’erreur)


6. Service utilisateur (UserService)
- Récupération de la liste des utilisateurs (non nulle, taille attendue)
- Recherche par ID existant / inexistant
- Cas limites : ID négatif et ID = 0 → retour vide

Pour exécuter les tests, utiliser la commande Maven :
mvn test

### Installation et exécution
#### Prérequis :
Java 17
Maven 3.8 ou plus

#### Installation du projet :
1. Télécharger ou cloner le dépôt
2. Installer les dépendances et compiler : mvn clean install

#### Exécuter l'application REST
Après compilation, exécuter : java -jar target/rest-api-1.0-SNAPSHOT.jar

Le serveur démarre sur :
http://localhost:7070

### Organisation des fichiers
Aperçu simplifié de la structure du projet :

pom.xml – configuration Maven
src/main/java – code source de l’API REST
src/main/resources – fichiers CSV et configuration
src/test/java – tests unitaires
docs/diagrams – diagrammes UML utilisés dans le rapport
README.md – manuel d’utilisation

### Utilisation de la CLI

Le projet inclut une interface en ligne de commande (CLI) qui permet d’appeler l’API REST sans utiliser directement les URLs dans le navigateur.

#### Lancer le serveur REST

Avant d’utiliser la CLI, il faut d’abord démarrer le serveur sur `http://localhost:7070` :

1. Compiler le projet  
   `mvn clean package`
2. Lancer le serveur REST (par exemple via IntelliJ avec la classe `Main`, ou avec le JAR généré) :  
   `java -jar target/rest-api-1.0-SNAPSHOT.jar`

#### Lancer la CLI

Une fois le serveur démarré, la CLI peut être lancée en ligne de commande :

`java -cp target/rest-api-1.0-SNAPSHOT.jar com.diro.ift2255.cli.CliApp`

Un menu texte s’affiche :

1. **Rechercher un cours**  
   - Par sigle (ex : `IFT2255`)  
   - Par sigle partiel (ex : `IFT`)  
   - Par mot-clé dans la description  

2. **Voir les cours d'un programme**  
   - Saisir un code de programme (ex : `117510`)  
   - La CLI appelle `/programs/{programId}/courses`

3. **Voir les cours offerts d'un trimestre**  
   - Saisir un trimestre (ex : `A25`)  
   - La CLI appelle `/courses/semester?semester=A25`

4. **Voir l’horaire d’un cours**  
   - Saisir un sigle (ex : `IFT2255`)  
   - Saisir un trimestre (ex : `A25`)  
   - La CLI appelle `/courses/{courseId}/schedule?include_schedule=true&semester=A25`

5. **Vérifier l'éligibilité à un cours**  
   - Saisir un sigle (ex : `IFT2255`)  
   - Saisir un cycle (ex : `bachelor`)  
   - Entrer la liste de cours complétés (ex : `IFT1005,IFT1025`)  
   - La CLI envoie un JSON à `/courses/{courseId}/eligibility`

6. **Voir les résultats académiques d'un cours**  
   - Saisir un sigle (ex : `IFT2255`)  
   - La CLI appelle `/courses/{courseId}/results`

7. **Voir et soumettre un avis**  
   - **Voir les avis** : saisie du sigle, appel à `/courses/{courseId}/reviews`  
   - **Soumettre un avis** : saisie du sigle, nom d’étudiant, charge de travail (1–5) et commentaire,  
     envoi d’un JSON à `/courses/{courseId}/review`

8. **Comparer deux cours**  
   - Saisir deux sigles (ex : `IFT2015` et `IFT2255`)  
   - La CLI appelle `/compare?course1=...&course2=...`

9. **Créer un ensemble de cours**  
   - Saisir une liste de sigles séparés par des virgules (ex : `IFT1025,IFT2255`)  
   - Saisir un trimestre (ex : `A25`)  
   - La CLI envoie un JSON à `/courseset/schedule` pour obtenir l’horaire de l’ensemble

10. **Quitter**  
   - Ferme proprement la CLI.

### Conclusion / Remarques

Ce dépôt contient l’API REST, la CLI, les tests unitaires et la documentation du projet.

Pour toute question d’exécution (port, dépendances, etc.), consulter les sections Installation et Exécution ci-dessus.

Documentation JavaDoc : target/site/apidocs/index.html (après génération).
