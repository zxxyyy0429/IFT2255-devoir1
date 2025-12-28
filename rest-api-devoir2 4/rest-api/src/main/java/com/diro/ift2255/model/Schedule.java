package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Représente une entrée d'horaire simplifiée pour un cours,
 * généralement utilisée lors de l'affichage compact ou dans la génération
 * d'un horaire combiné.
 *
 * <p>Un objet {@code Schedule} contient :</p>
 * <ul>
 *     <li>la section (ex. 01, 02)</li>
 *     <li>le type d’activité (théorie, laboratoire, TP, etc.)</li>
 *     <li>le jour où l’activité a lieu</li>
 *     <li>l’heure de début</li>
 *     <li>l’heure de fin</li>
 *     <li>le trimestre (ex. A25, H24)</li>
 * </ul>
 *
 * <p>
 * Cette classe est distincte de {@link CourseDetails.Schedule} et sert
 * principalement pour des représentations simplifiées dans l’application.
 * </p>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Schedule {

    /** Numéro de section de l'activité (ex. "01"). */
    private String section;
    /** Type de l'activité (ex. "Lecture", "Lab", "TP"). */
    private String activity_type;
    /** Jour de la semaine où se déroule l'activité (ex. "Monday"). */
    private String day;
    /** Heure de début (format HH:MM). */
    private String start;
    /** Heure de fin (format HH:MM). */
    private String end;
    /** Trimestre concerné (ex. A25, H24). */
    private String semester;

    /**
     * Retourne le numéro de section de cette activité.
     *
     * @return section du cours
     */
    public String getSection() { return section; }
    /**
     * Définit le numéro de section de cette activité.
     *
     * @param section numéro de section
     */
    public void setSection(String section) { this.section = section; }

    /**
     * Retourne le type d'activité.
     *
     * @return type d’activité (Lecture, Lab, etc.)
     */
    public String getActivity_type() { return activity_type; }
    /**
     * Définit le type d'activité.
     *
     * @param activity_type type d’activité
     */
    public void setActivity_type(String activity_type) { this.activity_type = activity_type; }

    /**
     * Retourne le jour où l’activité a lieu.
     *
     * @return jour de la semaine
     */
    public String getDay() { return day; }
    /**
     * Définit le jour où l’activité a lieu.
     *
     * @param day jour de la semaine
     */
    public void setDay(String day) { this.day = day; }

    /**
     * Retourne l'heure de début de l'activité.
     *
     * @return heure de début (HH:MM)
     */
    public String getStart() { return start; }
    /**
     * Définit l'heure de début de l'activité.
     *
     * @param start heure de début (HH:MM)
     */
    public void setStart(String start) { this.start = start; }

    /**
     * Retourne l'heure de fin de l'activité.
     *
     * @return heure de fin (HH:MM)
     */
    public String getEnd() { return end; }
    /**
     * Définit l'heure de fin de l'activité.
     *
     * @param end heure de fin (HH:MM)
     */
    public void setEnd(String end) { this.end = end; }


    /**
     * Retourne le trimestre associé à cette activité.
     *
     * @return code du trimestre (ex. A25)
     */
    public String getSemester() { return semester; }

    /**
     * Définit le trimestre associé à cette activité.
     *
     * @param semester code du trimestre
     */
    public void setSemester(String semester) { this.semester = semester; }

    

}
