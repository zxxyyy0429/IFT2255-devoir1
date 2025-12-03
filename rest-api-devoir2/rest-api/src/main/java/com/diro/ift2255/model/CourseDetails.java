package com.diro.ift2255.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CourseDetails {
    private String id;
    private String name;
    private String description;
    private int credits;
    @JsonProperty("available_terms")
    private AvailableTerms availableTerms;
    //private String horaire;
    //private String professeur= null;
    @JsonProperty("prerequisite_courses")
    private List<String> prerequisiteCourses;

    public CourseDetails() {}

    public CourseDetails(String id, String name, String description, int credits, AvailableTerms availableTerms, List<String> prerequis) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.credits = credits;
        this.availableTerms = availableTerms;
        this.prerequisiteCourses = prerequis; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String email) { this.description = email; }

    public int getCredits() {return credits;}
    public void setCredits(int credits) {this.credits = credits;}

    public AvailableTerms getAvailableTerms() { return availableTerms; }
    public void setAvailableTerms(AvailableTerms availableTerms) { this.availableTerms = availableTerms; }

    //public String getHoraire() {return horaire;}
    //public void setHoraire(String horaire) { this.horaire = horaire; }

    //public String getProfesseur() {return professeur;}
    //public void setProfesseur(String professeur) {this.professeur = professeur;}

    public List<String> getPrerequisiteCourses() {return prerequisiteCourses;}
    public void setPrerequisiteCourses(List<String> prerequis) {this.prerequisiteCourses = prerequis;}

    @JsonIgnore
    public String getPrerequis(){
        if (prerequisiteCourses != null && !prerequisiteCourses.isEmpty()) {
            return String.join(", ", prerequisiteCourses);
        }
        return "Aucun";
    }

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

    public static class AvailableTerms {
        private boolean autumn;
        private boolean winter;
        private boolean summer;

        public boolean isAutumn() { return autumn; }
        public void setAutumn(boolean autumn) { this.autumn = autumn; }

        public boolean isWinter() { return winter; }
        public void setWinter(boolean winter) { this.winter = winter; }

        public boolean isSummer() { return summer; }
        public void setSummer(boolean summer) { this.summer = summer; }

    }
}
