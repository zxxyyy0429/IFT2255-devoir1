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

Tous les 12 tests sont situés dans le fichier :
CourseControllerTest.java

Ils couvrent :
- recherche par mot-clé
- recherche par sigle
- comparaison de deux cours
- gestion d’erreurs (id manquant ou invalide)
- détection des prérequis communs

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
