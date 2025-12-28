package com.diro.ift2255.model;

/**
 * Représente les résultats académiques agrégés pour un cours,
 * tels que fournis par le fichier CSV des statistiques du baccalauréat.
 *
 * <p>Chaque instance contient :
 * <ul>
 *     <li>le sigle du cours (ex. IFT2255)</li>
 *     <li>le nom complet du cours</li>
 *     <li>la moyenne obtenue (A+, A, A-, ...)</li>
 *     <li>un score numérique représentant la réussite globale (1 à 5)</li>
 *     <li>le nombre total de participants</li>
 *     <li>le nombre de trimestres où le cours a été offert</li>
 * </ul>
 *
 * Cette classe est utilisée pour :
 * <ul>
 *     <li>afficher les statistiques dans le tableau de bord</li>
 *     <li>comparer deux cours (difficulté, réussite)</li>
 *     <li>alimenter les fonctionnalités de recherche et de filtrage</li>
 * </ul>
 */
public class CourseResult {

    /** Sigle du cours (ex. IFT2255). */
    private String sigle;
    /** Nom du cours. */
    private String nom;
    /**
     * Moyenne littérale obtenue (A+, A, A-, B+, ...).
     * Représente la performance globale des étudiants dans ce cours.
     */
    private String moyenne;
    /**
     * Score numérique (1 à 5) indiquant le niveau de réussite :
     * <ul>
     *   <li>1 = faible réussite</li>
     *   <li>5 = très forte réussite</li>
     * </ul>
     */
    private double score;
    /** Nombre total d'étudiants ayant suivi ce cours. */
    private int participants;
    /** Nombre de trimestres pendant lesquels le cours a été offert. */
    private int trimestres;

    /**
     * Retourne le sigle du cours.
     *
     * @return sigle du cours
     */
    public String getSigle() {
        return sigle;
    }
    /**
     * Définit le sigle du cours.
     *
     * @param sigle sigle du cours (ex. IFT2255)
     */
    public void setSigle(String sigle) {
        this.sigle = sigle;
    }

    /**
     * Retourne le nom du cours.
     *
     * @return nom du cours
     */
    public String getNom() {
        return nom;
    }
    /**
     * Définit le nom du cours.
     *
     * @param nom nom du cours
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Retourne la moyenne littérale du cours.
     *
     * @return moyenne (A+, A, A-, etc.)
     */
    public String getMoyenne() {
        return moyenne;
    }
    /**
     * Définit la moyenne littérale du cours.
     *
     * @param moyenne moyenne obtenue
     */
    public void setMoyenne(String moyenne) {
        this.moyenne = moyenne;
    }

    /**
     * Retourne le score numérique associé à la réussite du cours.
     *
     * @return score entre 1 et 5
     */
    public double getScore() {
        return score;
    }
    /**
     * Définit le score numérique associé à la réussite du cours.
     *
     * @param score valeur entre 1 et 5
     */
    public void setScore(double score) {
        this.score = score;
    }

    /**
     * Retourne le nombre total de participants au cours.
     *
     * @return total des participants
     */
    public int getParticipants() {
        return participants;
    }
    /**
     * Définit le nombre total de participants au cours.
     *
     * @param participants total des participants
     */
    public void setParticipants(int participants) {
        this.participants = participants;
    }

    /**
     * Retourne le nombre de trimestres durant lesquels le cours a été offert.
     *
     * @return nombre de trimestres
     */
    public int getTrimestres() {
        return trimestres;
    }
    /**
     * Définit le nombre de trimestres durant lesquels le cours a été offert.
     *
     * @param trimestres nombre de trimestres
     */
    public void setTrimestres(int trimestres) {
        this.trimestres = trimestres;
    }
}