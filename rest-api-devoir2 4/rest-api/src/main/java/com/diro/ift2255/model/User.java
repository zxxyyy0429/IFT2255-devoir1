package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente un utilisateur de l'application.
 * <p>
 * Cette classe est optionnelle dans le cadre du projet, car l'exigence
 * fonctionnelle ne nécessite pas la gestion de comptes utilisateurs.
 * Toutefois, elle peut être utilisée pour :
 * <ul>
 *     <li>personnaliser l'expérience (préférences, filtres, etc.)</li>
 *     <li>identifier un étudiant déposant un avis</li>
 *     <li>étendre l'application vers une fonctionnalité authentifiée</li>
 * </ul>
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    /** Identifiant unique de l'utilisateur. */
    private int id;
    /** Nom complet de l'utilisateur. */
    private String name;
    /** Adresse courriel de l'utilisateur. */
    private String email;

    /**
     * Constructeur vide requis pour la désérialisation JSON.
     */
    public User() {}

    /**
     * Constructeur principal permettant d'initialiser un utilisateur.
     *
     * @param id    identifiant unique
     * @param name  nom de l'utilisateur
     * @param email adresse courriel
     */
    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /**
     * Retourne l'identifiant unique de l'utilisateur.
     *
     * @return identifiant numérique
     */
    public int getId() { return id; }
    /**
     * Définit l'identifiant unique de l'utilisateur.
     *
     * @param id identifiant numérique
     */
    public void setId(int id) { this.id = id; }

    /**
     * Retourne le nom complet de l'utilisateur.
     *
     * @return nom de l'utilisateur
     */
    public String getName() { return name; }
    /**
     * Définit le nom complet de l'utilisateur.
     *
     * @param name nom de l'utilisateur
     */
    public void setName(String name) { this.name = name; }

    /**
     * Retourne l'adresse courriel de l'utilisateur.
     *
     * @return courriel
     */
    public String getEmail() { return email; }
    /**
     * Définit l'adresse courriel de l'utilisateur.
     *
     * @param email courriel
     */
    public void setEmail(String email) { this.email = email; }
}
