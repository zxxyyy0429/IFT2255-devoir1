package com.diro.ift2255.cli;

import java.util.Scanner;

public class CliApp {
    private static Scanner scanner = new Scanner(System.in);
    private static CourseCli courseCli = new CourseCli(scanner);
    private static AvisCli avisCli = new AvisCli(scanner);

    public static void main(String[] args) {
        while (true) {
            System.out.println("==========");
            System.out.println("Bienvenue au REST API client!");
            System.out.println("1. Rechercher un cours");
            System.out.println("2. Voir les cours d'un programme");
            System.out.println("3. Voir les cours offerts d'un trimestre");
            System.out.println("4. Voir l’horaire d’un cours");
            System.out.println("5. Vérifier l'éligibilité à un cours");
            System.out.println("6. Voir les résultats académiques d'un cours");
            System.out.println("7. Voir et soumettre d'un avis");
            System.out.println("8. Comparer deux cours");
            System.out.println("9. Créer un ensemble des cours");
            System.out.println("0. Quitter");
            System.out.print("Entrez votre choix: ");

            int command = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (command) {
                    case 1:
                        courseCli.rechercheCours();
                        break;

                    case 2:
                        courseCli.voirProgramme();
                        break;

                    case 3:
                        courseCli.voirTrimestreCours();
                        break;

                    case 4:
                        courseCli.voirSchedule();
                        break;

                    case 5:
                        courseCli.verifierEligibilite();
                        break;

                    case 6:
                        courseCli.voirResultatsAcademic();
                        break;

                    case 7:
                        avisCli.voirSoumettreAvis();
                        break;

                    case 8:
                        courseCli.comparerDeuxCours();
                        break;

                    case 9:
                        courseCli.creerEnsembleCours();
                        break;

                    case 0:
                        scanner.close();
                        System.out.println("Fin!");
                        return;

                    default:
                        System.out.println("Choix invalide");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
}

