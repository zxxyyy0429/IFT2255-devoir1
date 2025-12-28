package com.diro.ift2255.cli;

import java.util.Scanner;

public class AvisCli {
    private Scanner scanner;

    public AvisCli(Scanner scanner) {
        this.scanner = scanner;
    }

    public void voirSoumettreAvis(){
        System.out.println("1. Voir les avis");
        System.out.println("2. Soumettre un avis");
        System.out.print("Entrez votre choix: ");

        int command = scanner.nextInt();
        scanner.nextLine();

        try{
            switch (command) {
                case 1:
                    voirAvis();
                    break;

                case 2:
                    soumettreAvis();
                    System.out.println("Soumettre avec succès!");
                    break;

                default:
                    System.out.println("Choix invalide");
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void voirAvis(){
        System.out.print("Sigle (ex: IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/" + ApiClient.encode(sigle)+ "/reviews"));
    }

    private void soumettreAvis(){
        System.out.print("Sigle (ex: IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.print("Nom d'étudiant: ");
        String nom = scanner.nextLine().trim();
        System.out.print("Charge du travail (1-5): ");
        String chargeTravail = scanner.nextLine().trim();
        System.out.print("Commentaire: ");
        String commentaire = scanner.nextLine().trim();

        String jsonBody = "{"
                + "\"courseId\":\"" + escapeJson(sigle) + "\","
                + "\"studentName\":\"" + escapeJson(nom) + "\","
                + "\"workload\":" + escapeJson(chargeTravail) + ","
                + "\"comment\":\"" + escapeJson(commentaire) + "\""
                + "}";

        String path = "/courses/" + ApiClient.encode(sigle) + "/review";
        System.out.println(ApiClient.post(path, jsonBody));

    }

    private String escapeJson(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}
