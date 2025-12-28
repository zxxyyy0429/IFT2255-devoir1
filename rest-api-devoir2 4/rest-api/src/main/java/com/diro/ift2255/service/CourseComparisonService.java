package com.diro.ift2255.service;

import com.diro.ift2255.model.ComparisonResult;
import com.diro.ift2255.model.Course;

/**
 * Service responsable de comparer deux cours selon différents critères tels que :
 * <ul>
 *     <li>la charge de travail moyenne (basée sur les avis étudiants)</li>
 *     <li>la difficulté estimée (basée sur les résultats académiques agrégés)</li>
 *     <li>les métadonnées de cours (nom, description, etc.)</li>
 * </ul>
 *
 * <p>
 * Ce service combine les données provenant de :
 * <ul>
 *     <li>{@link ReviewService} – pour la charge de travail</li>
 *     <li>{@link ResultsService} – pour les scores/gpa</li>
 *     <li>{@link CourseService} – pour valider et récupérer les cours</li>
 * </ul>
 * </p>
 *
 * Le résultat de la comparaison est encapsulé dans un {@link ComparisonResult},
 * directement utilisable par le contrôleur REST et le CLI.
 */
public class CourseComparisonService {

    /** Service pour obtenir les avis étudiants et calculer la charge de travail. */
    private final ReviewService reviewService;
    /** Service pour récupérer les scores académiques (GPA agrégé). */
    private final ResultsService resultsService;
    /** Service pour récupérer les informations de cours. */
    private final CourseService courseService;

    /**
     * Constructeur permettant d'injecter les dépendances nécessaires.
     *
     * @param reviewService  service permettant d'obtenir les avis étudiants
     * @param resultsService service accédant aux résultats académiques agrégés
     * @param courseService  service fournissant les métadonnées des cours
     */
    public CourseComparisonService(ReviewService reviewService, ResultsService resultsService, CourseService courseService) {
        this.reviewService = reviewService;
        this.resultsService = resultsService;
        this.courseService = courseService;
    }

    /**
            * Compare deux cours et retourne un objet {@link ComparisonResult}
     * contenant :
            * <ul>
     *     <li>la charge de travail moyenne pour chaque cours</li>
            *     <li>la difficulté estimée pour chaque cours</li>
            *     <li>les métadonnées complètes des deux cours</li>
            * </ul>
            *
            * @param courseId1 identifiant du premier cours
     * @param score1    gpa ou score académique agrégé du premier cours (0.0 – 4.3)
     * @param courseId2 identifiant du deuxième cours
     * @param score2    gpa ou score académique agrégé du deuxième cours (0.0 – 4.3)
     * @return un objet {@link ComparisonResult} contenant toutes les mesures comparées
     *
             * @throws IllegalArgumentException si un cours n'est pas trouvé dans le référentiel
            */
    public ComparisonResult compareCourses(String courseId1, double score1, String courseId2, double score2) {
        // Charge de travail moyenne selon les avis Discord
        double workload1 = reviewService.getAverageWorkload(courseId1);
        double workload2 = reviewService.getAverageWorkload(courseId2);

        // Conversion GPA → difficulté sur une échelle 1–5
        int difficulty1 = gpaToDifficulty(score1);
        int difficulty2 = gpaToDifficulty(score2);

        // Récupération des cours (throw si absent)
        Course c1 = courseService.getCourseById(courseId1)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId1));
        Course c2 = courseService.getCourseById(courseId2)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseId2));

        // Création de l'objet de résultat
        ComparisonResult result = new ComparisonResult();
        result.setCourse1(c1);
        result.setCourse2(c2);
        result.setWorkload1(workload1);
        result.setWorkload2(workload2);
        result.setDifficulty1(difficulty1);
        result.setDifficulty2(difficulty2);

        return result;
    }

    /**
     * Convertit un gpa (0.0 à 4.3) en difficulté sur une échelle de 1 à 5.
     *
     * <p>Plus le GPA est élevé, plus la difficulté retournée est faible.
     * (On inverse l’échelle pour représenter la difficulté ressentie.)</p>
     *
     * <pre>
     * GPA 4.3 → Difficulté 1
     * GPA 3.0 → Difficulté ~2
     * GPA 2.0 → Difficulté ~3
     * GPA 1.0 → Difficulté ~4
     * GPA 0.0 → Difficulté 5
     * </pre>
     *
     * @param gpa valeur sur une échelle de 0.0 à 4.3
     * @return niveau de difficulté entre 1 et 5
     */
    private int gpaToDifficulty(double gpa) {
        // Clamp pour éviter valeurs hors plage
        double clamped = Math.max(0.0, Math.min(4.3, gpa));
        // Inversion de l’échelle 4.3 → 0.0
        double inverted = 4.3 - clamped;
        // Normalisation 0.0–4.3 → 1–5
        return (int) Math.round((inverted / 4.3) * 4 + 1);
    }
}
