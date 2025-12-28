package com.diro.ift2255.service;

import java.util.List;
import com.diro.ift2255.model.Review;

/**
 * Service façade permettant d'interagir avec les avis étudiants.
 *
 * <p>
 * Cette classe encapsule {@link ReviewCsvService} pour offrir une interface
 * simplifiée au reste de l'application (contrôleurs REST, CLI, services de comparaison, etc.).
 * </p>
 *
 * <p>
 * Le rôle principal de ce service est :
 * </p>
 * <ul>
 *     <li>soumettre un avis (persisté dans un fichier TSV)</li>
 *     <li>récupérer les avis d'un cours</li>
 *     <li>calculer la charge de travail moyenne d'un cours</li>
 * </ul>
 *
 * <p>
 * Le fait d'avoir un service intermédiaire permet de remplacer facilement le backend
 * de stockage (ex. TSV → base de données) sans modifier le reste du code.
 * </p>
 */
public class ReviewService {

    /** Service bas niveau responsable de la persistance des avis. */
    private final ReviewCsvService csv;

    /**
     * Constructeur permettant d'injecter le service CSV.
     *
     * @param csv instance de {@link ReviewCsvService}
     */
    public ReviewService(ReviewCsvService csv) {
        this.csv = csv;
    }

    /**
     * Soumet un nouvel avis étudiant et le persiste dans le fichier TSV.
     *
     * @param review avis à enregistrer
     * @return l'avis tel que persistant
     */
    public Review submitReview(Review review) {
        return csv.submitReview(review);
    }

    /**
     * Retourne tous les avis associés à un cours.
     *
     * @param courseId sigle du cours (par ex. "IFT2255")
     * @return liste des avis pour ce cours (jamais null)
     */
    public List<Review> getReviewsByCourse(String courseId) {
        return csv.getReviewsByCourse(courseId);
    }

    /**
     * Calcule la charge de travail moyenne pour un cours donné.
     *
     * <p>
     * Si aucun avis n'est trouvé, retourne 0.0.
     * </p>
     *
     * @param courseId sigle du cours
     * @return charge de travail moyenne (entre 1 et 5), ou 0.0
     */
    public double getAverageWorkload(String courseId) {
        return csv.getAverageWorkload(courseId);
    }
}

