package com.diro.ift2255.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente un programme d'études (ex. Baccalauréat en informatique),
 * tel que retourné par l'API Planifium.
 *
 * <p>Un programme contient :</p>
 * <ul>
 *     <li>un identifiant unique (ex. 117510)</li>
 *     <li>un nom officiel</li>
 *     <li>la liste des cours associés au programme</li>
 * </ul>
 *
 * Cette classe sert principalement à :
 * <ul>
 *     <li>afficher les cours offerts dans un programme donné</li>
 *     <li>filtrer les résultats de recherche selon le programme</li>
 *     <li>alimenter la création d'horaires et de comparaisons</li>
 * </ul>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Program {

    /** Identifiant du programme (ex. 117510). */
    private String id;
    /** Nom complet du programme. */
    private String name;
    /** Liste des cours associés à ce programme. */
    private List<Course> courses;

    /**
     * Constructeur vide requis pour la désérialisation JSON.
     */
    public Program() {}
    /**
     * Constructeur minimal contenant seulement l'identifiant et le nom du programme.
     *
     * @param id   identifiant unique du programme
     * @param name nom complet du programme
     */
    public Program(String id, String name) {
        this.id = id;
        this.name = name;
    }

    /**
     * Retourne l'identifiant du programme.
     *
     * @return identifiant du programme
     */
    public String getId() {
        return id;
    }

    /**
     * Définit l'identifiant du programme.
     *
     * @param id identifiant du programme
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Retourne le nom complet du programme.
     *
     * @return nom du programme
     */
    public String getName() {
        return name;
    }

    /**
     * Définit le nom complet du programme.
     *
     * @param name nom du programme
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Retourne la liste des cours associés à ce programme.
     *
     * @return liste d'objets {@link Course}
     */
    public List<Course> getCourses() {
        return courses;
    }

    /**
     * Définit la liste des cours associés à ce programme.
     *
     * @param courses liste d'objets {@link Course}
     */
    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }
}

