package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente le résultat d'une comparaison entre deux cours.
 * <p>
 * Cette classe encapsule les métriques utilisées lors de la comparaison :
 * <ul>
 *     <li>La charge de travail moyenne estimée pour chaque cours</li>
 *     <li>La difficulté moyenne estimée pour chaque cours</li>
 * </ul>
 * Elle est utilisée par le service de comparaison pour retourner un objet
 * structuré au client (REST ou CLI).
 * </p>
 *
 * Les champs ignorent les propriétés inconnues lors du mapping JSON, ce qui
 * permet de préserver la robustesse lors de l'interaction avec différentes
 * sources de données ou versions de modèles.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class ComparisonResult {
    /** Premier cours comparé. */
    private Course course1;

    /** Deuxième cours comparé. */
    private Course course2;

    /** Charge de travail estimée du premier cours. */
    private double workload1;

    /** Charge de travail estimée du deuxième cours. */
    private double workload2;

    /** Difficulté estimée du premier cours. */
    private double difficulty1;

    /** Difficulté estimée du deuxième cours. */
    private double difficulty2;

    /**
     * Constructeur par défaut requis pour la désérialisation JSON.
     */
    public ComparisonResult() {}

// --------------------
// Getters et Setters
// --------------------

    /** Retourne le premier cours comparé. */
    public Course getCourse1() { return course1; }
    public void setCourse1(Course course1) { this.course1 = course1; }

    /** Retourne le deuxième cours comparé. */
    public Course getCourse2() { return course2; }
    public void setCourse2(Course course2) { this.course2 = course2; }

    /** Retourne la charge de travail estimée du premier cours. */
    public double getWorkload1() { return workload1; }
    public void setWorkload1(double workload1) { this.workload1 = workload1; }

    /** Retourne la charge de travail estimée du deuxième cours. */
    public double getWorkload2() { return workload2; }
    public void setWorkload2(double workload2) { this.workload2 = workload2; }

    /** Retourne la difficulté estimée du premier cours. */
    public double getDifficulty1() { return difficulty1; }
    public void setDifficulty1(double difficulty1) { this.difficulty1 = difficulty1; }

    /** Retourne la difficulté estimée du deuxième cours. */
    public double getDifficulty2() { return difficulty2; }
    public void setDifficulty2(double difficulty2) { this.difficulty2 = difficulty2; }
}
