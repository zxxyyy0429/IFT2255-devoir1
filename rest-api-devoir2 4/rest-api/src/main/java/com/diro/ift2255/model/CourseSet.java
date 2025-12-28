package com.diro.ift2255.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente un ensemble de cours sélectionnés par un utilisateur pour un
 * trimestre donné. Cet ensemble est utilisé pour :
 *
 * <ul>
 *     <li>générer un horaire combiné pour le trimestre choisi</li>
 *     <li>détecter d'éventuels conflits d'horaire</li>
 *     <li>comparer différents ensembles de cours (fonctionnalité bonus)</li>
 * </ul>
 *
 * L'objet contient uniquement les informations nécessaires à la requête :
 * la liste des identifiants de cours, ainsi que le trimestre cible.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseSet {

    /**
     * Liste des identifiants (sigles) des cours qui composent cet ensemble.
     * Maximum de 6 cours selon les règles fonctionnelles.
     */
    private List<String> courseIds;

    /**
     * Trimestre visé pour l’ensemble, sous forme d’un code (ex. A25, H24, E23).
     */
    private String semester;

    /**
     * Constructeur vide requis pour la désérialisation JSON.
     */
    public CourseSet() {}

    /**
     * Constructeur principal permettant d'initialiser un ensemble de cours.
     *
     * @param courseIds liste des identifiants de cours
     * @param semester  trimestre concerné
     */
    public CourseSet(List<String> courseIds, String semester) {
        this.courseIds = courseIds;
        this.semester = semester;
    }

    /**
     * Retourne la liste des identifiants des cours de l’ensemble.
     *
     * @return liste de sigles de cours
     */
    public List<String> getCourseIds() { return courseIds; }
    /**
     * Définit la liste des identifiants des cours de l’ensemble.
     *
     * @param courseIds liste de sigles de cours
     */
    public void setCourseIds(List<String> courseIds) { this.courseIds = courseIds; }

    /**
     * Retourne le trimestre ciblé par cet ensemble.
     *
     * @return code du trimestre (ex. A25)
     */
    public String getSemester() { return semester; }
    /**
     * Définit le trimestre ciblé par cet ensemble.
     *
     * @param semester code du trimestre
     */
    public void setSemester(String semester) { this.semester = semester; }

    /**
     * Méthode placeholder prévue pour retourner les détails complets
     * des cours contenus dans l’ensemble. Non implémentée dans la version
     * actuelle et volontairement laissée absente pour éviter une mauvaise
     * utilisation.
     *
     * @return rien — toujours exception
     * @throws UnsupportedOperationException car la méthode n'est pas encore implémentée
     */
    public Object getCourseDetails() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

