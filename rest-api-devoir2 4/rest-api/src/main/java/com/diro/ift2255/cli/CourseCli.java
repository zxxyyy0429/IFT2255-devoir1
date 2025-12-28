package com.diro.ift2255.cli;

import java.util.Scanner;

public class CourseCli {
    private Scanner scanner;

    public CourseCli(Scanner scanner) {
        this.scanner = scanner;
    }

    public void rechercheCours() {
        System.out.println("1. Par sigle");
        System.out.println("2. Par sigle partiel");
        System.out.println("3. Par mot-clé");
        System.out.print("Entrez votre choix: ");

        int command = scanner.nextInt();
        scanner.nextLine();

        try {
            switch (command) {
                case 1:
                    rechercheParSigle();
                    break;

                case 2:
                    rechercheParSiglePartiel();
                    break;

                case 3:
                    rechercheParMotCle();
                    break;

                default:
                    System.out.println("Choix invalide");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    private void rechercheParSigle() {
        System.out.print("Sigle (ex: IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/" + ApiClient.encode(sigle)));
    }

    private void rechercheParSiglePartiel() {
        System.out.print("Sigle Partiel (ex: IFT): ");
        String siglePartiel = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/search?sigle=" + ApiClient.encode(siglePartiel)));
    }

    private void rechercheParMotCle() {
        System.out.print("Mot-clé: ");
        String motCle = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/search/description?description=" + ApiClient.encode(motCle)));
    }

    public void voirProgramme(){
        System.out.print("Programme (ex: 117510): ");
        String programme = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/programs/" + ApiClient.encode(programme)+ "/courses"));
    }

    public void voirTrimestreCours(){
        System.out.print("Trimestre (ex: A25): ");
        String trimestre = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/semester?semester=" + ApiClient.encode(trimestre)));
    }

    public void voirSchedule(){
        System.out.print("Sigle (ex: IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.print("Semestre (ex: A25): ");
        String semestre = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/" + ApiClient.encode(sigle)+"/schedule?include_schedule=true&semester=" + ApiClient.encode(semestre)));
    }

    public void verifierEligibilite(){
        System.out.print("Sigle (ex: IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.print("Cycle (ex: bachelor): ");
        String cycle = scanner.nextLine().trim();
        System.out.print("Completed courses (ex: IFT1005,IFT1025): ");
        String completedCourses = scanner.nextLine().trim();

        String[] parts = completedCourses.isBlank() ? new String[0] : completedCourses.split(",");
        StringBuilder json = new StringBuilder();
        json.append("{\"completedCourses\":[");
        for (int i = 0; i < parts.length; i++) {
            String c = parts[i].trim();
            if (c.isEmpty()) {
                continue;
            }
            json.append("\"").append(c).append("\"");
            if (i < parts.length - 1) {
                json.append(",");
            }
        }
        json.append("],\"cycle\":\"").append(cycle).append("\"}");

        String path = "/courses/" + ApiClient.encode(sigle) + "/eligibility";
        System.out.println(ApiClient.post(path, json.toString()));
    }

    public void voirResultatsAcademic(){
        System.out.print("Sigle (ex: IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/courses/" + ApiClient.encode(sigle)+ "/results"));
    }

    public void comparerDeuxCours(){
        System.out.print("Sigle 1 (ex: IFT2255): ");
        String sigle1 = scanner.nextLine().trim();
        System.out.print("Sigle 2 (ex: IFT2255): ");
        String sigle2 = scanner.nextLine().trim();
        System.out.println(ApiClient.get("/compare?course1=" + ApiClient.encode(sigle1)+ "&course2=" + ApiClient.encode(sigle2)));
    }

    public void creerEnsembleCours(){
        System.out.print("Sigle (ex: IFT1025,IFT2255): ");
        String sigle = scanner.nextLine().trim();
        System.out.print("Trimestre (ex: A25): ");
        String trimestre = scanner.nextLine().trim();

        String[] ids = sigle.split(",");
        StringBuilder json = new StringBuilder();
        json.append("{\"courseIds\":[");
        for (int i = 0; i < ids.length; i++) {
            json.append("\"").append(ids[i].trim()).append("\"");
            if (i < ids.length - 1){
                json.append(",");
            }
        }
        json.append("],\"semester\":\"")
                .append(trimestre)
                .append("\"}");

        System.out.println(ApiClient.post("/courseset/schedule", json.toString()));
    }
}


