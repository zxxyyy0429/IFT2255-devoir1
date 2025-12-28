package com.diro.ift2255.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Représente un cours de l'Université, tel que récupéré depuis l'API Planifium
 * ou enrichi par l'application interne.
 *
 * <p>Cette classe contient les informations de base d'un cours :
 * <ul>
 *     <li>le sigle ou identifiant unique du cours (ex: IFT2255)</li>
 *     <li>le nom complet du cours</li>
 *     <li>la description officielle</li>
 *     <li>la liste des horaires offerts pour différents trimestres</li>
 * </ul>
 *
 * La classe est compatible avec la désérialisation JSON provenant de plusieurs
 * formats possibles, grâce à {@link JsonAlias}. Les champs inconnus sont ignorés
 * afin d'assurer la robustesse lors de changements dans l'API externe.
 */

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Course {
    /**
     * Identifiant unique du cours (sigle), tel que "IFT2255".
     * Peut aussi être nommé "id" ou "_id" dans certaines sources JSON.
     */
    @JsonAlias({ "id", "_id" })
    private String id;
    /** Nom complet du cours (ex: "Génie logiciel"). */
    private String name;
    /** Description officielle du cours. */
    private String description;

    /**
     * Constructeur vide requis pour la désérialisation JSON.
     */
    public Course() {}

    /**
     * Constructeur minimal contenant seulement l'identifiant et le nom du cours.
     */
    public Course(String id, String name) {
        this.id = id;
        this.name = name;
    }


    /**
     * Constructeur incluant l'identifiant, le nom et la description.
     */
    public Course(String id, String name, String desc) {
        this(id, name);
        this.description = desc;
    }
    /**
     * Retourne l'identifiant unique du cours.
     *
     * @return sigle du cours (ex: IFT2255)
     */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Retourne le nom complet du cours.
     *
     * @return nom du cours
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    /**
     * Retourne la description officielle du cours.
     *
     * @return description du cours
     */
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description;}

    /**
     * Retourne la liste des horaires associés au cours.
     *
     * @return liste d'objets {@link Schedule}
     */
    private List<Schedule> schedule;

    /**
     * Assigne les horaires disponibles pour ce cours.
     *
     * @param schedule liste des horaires offerts
     */
    public List<Schedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<Schedule> schedule) {
        this.schedule = schedule;
    }

}
