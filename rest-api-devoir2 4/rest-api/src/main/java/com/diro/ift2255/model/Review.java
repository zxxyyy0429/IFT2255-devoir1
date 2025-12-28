package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente un avis étudiant associé à un cours.
 * Cet avis est soumis via l'application ou via le bot Discord.
 *
 * <p>Un avis contient :</p>
 * <ul>
 *     <li>l'identifiant du cours concerné</li>
 *     <li>le nom (ou pseudonyme) de l'étudiant</li>
 *     <li>une estimation de la charge de travail (workload)</li>
 *     <li>un commentaire optionnel</li>
 * </ul>
 *
 * Les avis sont utilisés pour :
 * <ul>
 *     <li>afficher une synthèse de l'expérience étudiante pour chaque cours</li>
 *     <li>alimenter la comparaison entre deux cours</li>
 *     <li>générer des tableaux de bord personnalisés</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Review {
    /** Identifiant du cours auquel cet avis est associé. */
    private String courseId;
    /** Nom ou pseudonyme de l'étudiant ayant laissé l'avis. */
    private String studentName;
    /**
     * Estimation de la charge de travail (en général sur une échelle de 1 à 5).
     */
    private int workload;
    /** Commentaire libre permettant à l'étudiant d'expliquer son expérience. */
    private String comment;

    /**
     * Constructeur vide requis pour la désérialisation JSON.
     */
    public Review() {}

    /**
     * Constructeur principal permettant de créer un avis complet.
     *
     * @param courseId    identifiant du cours
     * @param studentName nom de l'étudiant
     * @param workload    charge de travail estimée (1 à 5)
     * @param comment     commentaire descriptif (optionnel)
     */
    public Review(String courseId, String studentName, int workload, String comment) {
        this.courseId = courseId;
        this.studentName = studentName;
        this.workload = workload;
        this.comment = comment;
    }

    /**
     * Retourne l'identifiant du cours concerné par cet avis.
     *
     * @return identifiant du cours
     */
    public String getCourseId() { return courseId; }
    /**
     * Définit l'identifiant du cours concerné par cet avis.
     *
     * @param courseId identifiant du cours
     */
    public void setCourseId(String courseId) { this.courseId = courseId; }

    /**
     * Retourne le nom de l'étudiant ayant laissé l'avis.
     *
     * @return nom ou pseudonyme de l'étudiant
     */
    public String getStudentName() { return studentName; }
    /**
     * Définit le nom de l'étudiant ayant laissé l'avis.
     *
     * @param studentName nom ou pseudonyme
     */
    public void setStudentName(String studentName) { this.studentName = studentName; }

    /**
     * Retourne la charge de travail estimée pour ce cours.
     *
     * @return charge de travail (généralement 1–5)
     */
    public int getWorkload() { return workload; }
    /**
     * Définit la charge de travail estimée.
     *
     * @param workload valeur numérique (souvent entre 1 et 5)
     */
    public void setWorkload(int workload) { this.workload = workload; }

    /**
     * Retourne le commentaire fourni par l'étudiant.
     *
     * @return commentaire textuel (peut être null)
     */
    public String getComment() { return comment; }

    /**
     * Définit le commentaire associé à l'avis.
     *
     * @param comment texte libre
     */
    public void setComment(String comment) { this.comment = comment; }
}

