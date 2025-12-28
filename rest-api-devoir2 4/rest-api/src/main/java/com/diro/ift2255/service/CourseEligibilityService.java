package com.diro.ift2255.service;

import java.util.List;

import com.diro.ift2255.model.CourseDetails;

/**
 * Service responsable de vérifier si un étudiant est éligible à suivre un cours.
 *
 * <p>L'éligibilité repose sur deux critères principaux :</p>
 * <ul>
 *     <li><strong>Les prérequis :</strong> l'étudiant doit avoir complété tous les cours prérequis.</li>
 *     <li><strong>Le cycle d'études :</strong> un cours peut être réservé à un cycle particulier
 *         (ex. baccalauréat, maîtrise).</li>
 * </ul>
 *
 * <p>
 * Ce service est utilisé par l'API REST et le CLI pour valider les choix
 * de cours avant l'inscription ou dans un tableau de bord étudiant.
 * </p>
 */
public class CourseEligibilityService {

    /**
     * Vérifie si un étudiant est éligible à suivre un cours donné.
     *
     * <p>La validation se déroule en deux étapes :</p>
     * <ol>
     *     <li><strong>Prérecquis :</strong> tous les cours prérequis doivent apparaître dans
     *         {@code completedCourses}. Si un prérequis est absent, l'étudiant n'est pas éligible.</li>
     *     <li><strong>Cycle :</strong> si le cours est associé à un cycle particulier
     *         (ex. "bachelor", "master"), celui-ci doit correspondre à celui de l'étudiant.</li>
     * </ol>
     *
     * @param targetCourse       cours dont on veut vérifier l'éligibilité
     * @param completedCourses   liste des sigles de cours que l'étudiant a déjà complétés
     * @param studentCycle       cycle actuel de l'étudiant (ex. "bachelor", "master")
     *
     * @return {@code true} si l'étudiant satisfait les prérequis et le cycle requis;
     *         {@code false} sinon
     */
    public boolean isEligible(CourseDetails targetCourse, List<String> completedCourses, String studentCycle) {
        // Vérification des prérequis
        List<String> prereqs = targetCourse.getPrerequisiteCourses();
        if (prereqs != null) {
            for (String prereq : prereqs) {
                if (!completedCourses.contains(prereq)) {
                    return false; // Prérequis manquant
                }
            }
        }

        // Vérifier le cycle
        String requiredCycle = targetCourse.getCycle();
        // Tous les prérequis et cycle valide
        return !(requiredCycle != null && !requiredCycle.equalsIgnoreCase(studentCycle)); 
    }
}
