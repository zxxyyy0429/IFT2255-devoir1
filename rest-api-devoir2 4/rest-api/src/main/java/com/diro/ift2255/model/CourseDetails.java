package com.diro.ift2255.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Détaille un cours avec ses métadonnées complètes telles que retournées
 * par l'API Planifium et enrichies par l'application.
 *
 * <p>Un {@code CourseDetails} contient notamment :
 * <ul>
 *     <li>l'identifiant du cours et son nom</li>
 *     <li>la description et le nombre de crédits</li>
 *     <li>les trimestres où le cours est offert</li>
 *     <li>la liste des prérequis</li>
 *     <li>les horaires détaillés (sections, volets, activités)</li>
 *     <li>le cycle (baccalauréat, maîtrise, etc.)</li>
 * </ul>
 * </p>
 */

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CourseDetails {

    /** Identifiant du cours (champ JSON « _id »). */
    @JsonProperty("_id")
    private String id;

    /** Nom complet du cours. */
    private String name;

    /** Description officielle du cours. */
    private String description;

    /** Nombre de crédits associés au cours. */
    private int credits;

    /**
     * Trimestres durant lesquels le cours est offert (automne, hiver, été).
     */
    @JsonProperty("available_terms")
    private AvailableTerms availableTerms;
    //private String horaire;
    //private String professeur= null;

    /**
     * Liste des codes de cours qui sont prérequis pour celui-ci.
     */
    @JsonProperty("prerequisite_courses")
    private List<String> prerequisiteCourses;

    /**
     * Liste des horaires détaillés liés à ce cours.
     */
    @JsonProperty("schedule")
    private List<Schedule> schedule;

    /**
     * Constructeur vide requis pour la désérialisation JSON.
     */
    public CourseDetails() {}

    /**
     * Constructeur complet pour initialiser la majorité des champs principaux.
     *
     * @param id              identifiant du cours
     * @param name            nom du cours
     * @param description     description du cours
     * @param credits         nombre de crédits
     * @param availableTerms  trimestres où le cours est offert
     * @param prerequis       liste des codes de cours prérequis
     */
    public CourseDetails(String id, String name, String description, int credits, AvailableTerms availableTerms, List<String> prerequis) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.availableTerms = availableTerms;
        this.prerequisiteCourses = prerequis; }

    /**
     * Retourne l'identifiant du cours.
     *
     * @return identifiant du cours
     */
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    /**
     * Retourne le nom du cours.
     *
     * @return nom du cours
     */
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    /**
     * Retourne la description du cours.
     *
     * @return description du cours
     */
    public String getDescription() { return description; }
    public void setDescription(String email) { this.description = email; }

    /**
     * Retourne le nombre de crédits du cours.
     *
     * @return nombre de crédits
     */
    public int getCredits() {return credits;}
    public void setCredits(int credits) {this.credits = credits;}

    /**
     * Retourne les trimestres durant lesquels le cours est offert.
     *
     * @return trimestres disponibles
     */
    public AvailableTerms getAvailableTerms() { return availableTerms; }
    public void setAvailableTerms(AvailableTerms availableTerms) { this.availableTerms = availableTerms; }

    //public String getHoraire() {return horaire;}
    //public void setHoraire(String horaire) { this.horaire = horaire; }

    //public String getProfesseur() {return professeur;}
    //public void setProfesseur(String professeur) {this.professeur = professeur;}
    /**
     * Retourne la liste des prérequis (codes de cours).
     *
     * @return liste de sigles de cours prérequis
     */
    public List<String> getPrerequisiteCourses() {return prerequisiteCourses;}
    public void setPrerequisiteCourses(List<String> prerequis) {this.prerequisiteCourses = prerequis;}

    /**
     * Retourne la liste des horaires associés à ce cours.
     *
     * @return liste d'objets {@link Schedule}
     */
    public List<Schedule> getSchedules() { return schedule; }
    public void setSchedules(List<Schedule> schedule) { this.schedule = schedule; }


    /**
     * Retourne une représentation textuelle des prérequis, destinée à l'affichage.
     *
     * @return chaîne contenant les prérequis séparés par une virgule,
     *         ou "Aucun" s'il n'y en a pas
     */
    @JsonIgnore
    public String getPrerequis(){
        if (prerequisiteCourses != null && !prerequisiteCourses.isEmpty()) {
            return String.join(", ", prerequisiteCourses);
        }
        return "Aucun";
    }

    /**
     * Retourne une description textuelle des trimestres où le cours est offert.
     *
     * @return liste des trimestres (Automne, Été, Hiver) séparés par une virgule,
     *         ou un message indiquant qu'aucun trimestre n'est disponible
     */
    @JsonIgnore
    public String getAvailableTermsString() {
        if (availableTerms == null) {
            return "Aucun Trimestre offert ce cours.";
        }

        List<String> terms = new ArrayList<>();
        if (availableTerms.isAutumn()){
            terms.add("Automne");
        }
        if (availableTerms.isSummer()){
            terms.add("Ete");
        }
        if (availableTerms.isWinter()){
            terms.add("Hiver");
        }

        return String.join(", ", terms);
    }

    /**
     * Représente les trimestres durant lesquels un cours est offert
     * (automne, hiver, été).
     */
    public static class AvailableTerms {
        /** Indique si le cours est offert à l'automne. */
        private boolean autumn;
        /** Indique si le cours est offert en hiver. */
        private boolean winter;
        /** Indique si le cours est offert en été. */
        private boolean summer;

        /**
         * Indique si le cours est offert à l'automne.
         *
         * @return {@code true} si offert à l'automne
         */
        public boolean isAutumn() { return autumn; }
        public void setAutumn(boolean autumn) { this.autumn = autumn; }

        /**
         * Indique si le cours est offert en hiver.
         *
         * @return {@code true} si offert en hiver
         */
        public boolean isWinter() { return winter; }
        public void setWinter(boolean winter) { this.winter = winter; }

        /**
         * Indique si le cours est offert en été.
         *
         * @return {@code true} si offert en été
         */
        public boolean isSummer() { return summer; }
        public void setSummer(boolean summer) { this.summer = summer; }

    }

    /**
     * Représente un horaire de cours pour un trimestre donné, incluant ses sections.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Schedule {
        @JsonProperty("_id")
        /** Identifiant technique de l'horaire .*/
        private String id;
        /** Sigle du cours (ex. IFT2255). */
        private String sigle;
        /** Nom du cours associé à cet horaire. */
        private String name;
        /** Trimestre sous forme textuelle (ex. A25, H25). */
        private String semester;
        /** Liste des sections offertes pour cet horaire. */
        private List<Section> sections;

        /** Date de récupération des données (pour la mise à jour). */
        @JsonProperty("fetch_date")
        private String fetchDate;

        /** Représentation numérique du trimestre (tri, comparaison, etc.). */
        @JsonProperty("semester_int")
        private int semesterInt;

        /**
         * Retourne l'identifiant de l'horaire.
         *
         * @return identifiant de l'horaire
         */
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }

        /**
         * Retourne le sigle du cours.
         *
         * @return sigle du cours
         */
        public String getSigle() { return sigle; }
        public void setSigle(String sigle) { this.sigle = sigle; }

        /**
         * Retourne le nom du cours associé.
         *
         * @return nom du cours
         */
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        /**
         * Retourne le code du trimestre (ex. A25, H25).
         *
         * @return code du trimestre
         */
        public String getSemester() { return semester; }
        public void setSemester(String semester) { this.semester = semester; }

        /**
         * Retourne la liste des sections offertes pour cet horaire.
         *
         * @return liste de sections
         */
        public List<Section> getSections() { return sections; }
        public void setSections(List<Section> sections) { this.sections = sections; }

        /**
         * Retourne la date de récupération des données.
         *
         * @return date de récupération des données
         */
        public String getFetchDate() { return fetchDate; }
        public void setFetchDate(String fetchDate) { this.fetchDate = fetchDate; }

        /**
         * Retourne la représentation numérique du trimestre.
         *
         * @return trimestre sous forme entière
         */
        public int getSemesterInt() { return semesterInt; }
        public void setSemesterInt(int semesterInt) { this.semesterInt = semesterInt; }
    }

    /**
     * Représente une section d'un cours pour un trimestre donné, incluant les volets
     * (théorie, labo, TP, etc.).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Section {
        @JsonProperty("number_inscription")
        /** Numéro d'inscription de la section. */
        private String numberInscription;
        /** Nom ou type de la section (ex. Groupe 10). */
        private String name;
        /** Liste des enseignants associés à cette section. */
        private List<String> teachers;
        /** Capacité d'inscription de la section. */
        private String capacity;

        /** Liste des volets (théorie, laboratoire, etc.). */
        @JsonProperty("volets")
        private List<Volet> volets;

        /**
         * Retourne le numéro d'inscription de la section.
         *
         * @return numéro d'inscription
         */
        public String getNumberInscription() { return numberInscription; }
        public void setNumberInscription(String numberInscription) { this.numberInscription = numberInscription; }

        /**
         * Retourne le nom de la section.
         *
         * @return nom de la section
         */
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        /**
         * Retourne la liste des enseignants de cette section.
         *
         * @return liste des enseignants
         */
        public List<String> getTeachers() { return teachers; }
        public void setTeachers(List<String> teachers) { this.teachers = teachers; }

        /**
         * Retourne la capacité d'inscription de la section.
         *
         * @return capacité de la section
         */
        public String getCapacity() { return capacity; }
        public void setCapacity(String capacity) { this.capacity = capacity; }

        /**
         * Définit la liste des volets de cette section.
         *
         * @param volets liste des volets
         */
        public List<Volet> getVolets() { return volets; }
        public void setVolets(List<Volet> volets) { this.volets = volets; }
    }

    /**
     * Représente un volet d'une section (par exemple Théorie, Laboratoire, TP),
     * contenant lui-même une liste d'activités horaires.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Volet {
        /** Nom du volet (ex. "Théorie"). */
        private String name;
        /** Liste des activités associées à ce volet. */
        private List<Activity> activities;

        /**
         * Retourne le nom du volet.
         *
         * @return nom du volet
         */
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        /**
         * Retourne la liste des activités de ce volet.
         *
         * @return liste des activités
         */
        public List<Activity> getActivities() { return activities; }
        public void setActivities(List<Activity> activities) { this.activities = activities; }
    }

    /**
     * Représente une activité précise dans un horaire (ex. un bloc de cours
     * un certain jour à une certaine heure, dans un pavillon et une salle).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Activity {
        /** Jours de la semaine où l'activité a lieu. */
        private List<String> days;

        /** Heure de début de l'activité. */
        @JsonProperty("start_time")
        private String startTime;

        /** Heure de fin de l'activité. */
        @JsonProperty("end_time")
        private String endTime;

        /** Campus où l'activité est donnée. */
        private String campus;
        private String place;

        /** Nom du pavillon où a lieu l'activité. */
        @JsonProperty("pavillon_name")
        private String pavillonName;
        /** Numéro de salle. */
        private String room;
        /** Mode d'enseignement (présentiel, hybride, en ligne, etc.). */
        private String mode;

        /**
         * Retourne les jours de la semaine où l'activité a lieu.
         *
         * @return liste des jours
         */
        public List<String> getDays() { return days; }
        public void setDays(List<String> days) { this.days = days; }

        /**
         * Retourne l'heure de début de l'activité.
         *
         * @return heure de début
         */
        public String getStartTime() { return startTime; }
        public void setStartTime(String startTime) { this.startTime = startTime; }

        /**
         * Retourne l'heure de fin de l'activité.
         *
         * @return heure de fin
         */
        public String getEndTime() { return endTime; }
        public void setEndTime(String endTime) { this.endTime = endTime; }

        /**
         * Retourne le campus où a lieu l'activité.
         *
         * @return nom du campus
         */
        public String getCampus() { return campus; }
        public void setCampus(String campus) { this.campus = campus; }

        /**
         * Retourne le lieu de l'activité.
         *
         * @return lieu de l'activité
         */
        public String getPlace() { return place; }
        public void setPlace(String place) { this.place = place; }

        /**
         * Retourne le nom du pavillon.
         *
         * @return nom du pavillon
         */
        public String getPavillonName() { return pavillonName; }
        public void setPavillonName(String pavillonName) { this.pavillonName = pavillonName; }

        /**
         * Retourne le numéro de la salle.
         *
         * @return numéro de salle
         */
        public String getRoom() { return room; }
        public void setRoom(String room) { this.room = room; }

        /**
         * Retourne le mode d'enseignement (présentiel, en ligne, etc.).
         *
         * @return mode d'enseignement
         */
        public String getMode() { return mode; }
        public void setMode(String mode) { this.mode = mode; }
    }

    @JsonProperty("cycle")
    private String cycle;  // "bachelor", "master", etc.

    public String getCycle() { return cycle; }
    public void setCycle(String cycle) { this.cycle = cycle; }

}
