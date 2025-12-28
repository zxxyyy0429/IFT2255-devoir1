package com.diro.ift2255.controller;

import java.util.List;

import com.diro.ift2255.model.Review;
import com.diro.ift2255.service.ReviewService;
import com.diro.ift2255.util.ResponseUtil;

import io.javalin.http.Context;

/**
 * Contrôleur REST permettant de gérer les avis étudiants (reviews).
 *
 * <p>
 * Ce contrôleur expose deux endpoints principaux :
 * </p>
 *
 * <ul>
 *     <li><strong>POST /reviews</strong> — Soumettre un avis étudiant (utilisé aussi par le bot Discord)</li>
 *     <li><strong>GET /reviews/{courseId}</strong> — Récupérer tous les avis d’un cours donné</li>
 * </ul>
 *
 * <p>
 * Les avis incluent :
 * </p>
 * <ul>
 *     <li>courseId — le sigle du cours</li>
 *     <li>studentName — nom ou pseudonyme de l'étudiant</li>
 *     <li>workload — charge de travail perçue (1 à 5)</li>
 *     <li>comment — commentaire optionnel</li>
 * </ul>
 *
 * <p>
 * Les données sont persistées via {@link ReviewService} (fichier TSV).
 * </p>
 */
public class ReviewController {

    /** Service responsable de la gestion et persistance des avis. */
    private final ReviewService service;

    /**
     * Constructeur du contrôleur.
     *
     * @param service service métier utilisé pour soumettre et récupérer les avis
     */
    public ReviewController(ReviewService service) {
        this.service = service;
    }


    /**
     * Endpoint : soumettre un avis étudiant.
     *
     * <p>URL : {@code POST /reviews}</p>
     *
     * <h3>Exemple de corps JSON :</h3>
     * <pre>
     * {
     *   "courseId": "IFT2255",
     *   "studentName": "Alice",
     *   "workload": 4,
     *   "comment": "Cours exigeant mais très intéressant"
     * }
     * </pre>
     *
     * <h3>Codes de réponse :</h3>
     * <ul>
     *     <li><strong>201</strong> — avis enregistré</li>
     *     <li><strong>400</strong> — données invalides</li>
     * </ul>
     *
     * <p>
     * Ce endpoint est aussi utilisé par le bot Discord pour soumettre automatiquement des avis.
     * </p>
     *
     * @param ctx contexte HTTP Javalin contenant le JSON de l'avis
     */
    // Submit a review
    public void submitReview(Context ctx) {
        Review review = ctx.bodyAsClass(Review.class);

        // Validation des champs obligatoires
        if (review.getCourseId() == null || review.getCourseId().isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("courseId est requis"));
            return;
        }

        if (review.getStudentName() == null || review.getStudentName().isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("studentName est requis"));
            return;
        }

        if (review.getWorkload() < 1 || review.getWorkload() > 5) {
            ctx.status(400).json(ResponseUtil.formatError("workload doit être entre 1 et 5"));
            return;
        }


        // Persistance de l'avis
        Review saved = service.submitReview(review);

        //System.out.println("Parsed Review object:");
        //System.out.println("  courseId      = " + review.getCourseId());
        //System.out.println("  studentName   = " + review.getStudentName());
        //System.out.println("  workload      = " + review.getWorkload());
        //System.out.println("  comment       = " + review.getComment());

        // Retourner l'avis avec code HTTP 201
        ctx.status(201).json(saved);
    }

    /**
     * Endpoint : récupérer tous les avis associés à un cours.
     *
     * <p>URL : {@code GET /reviews/{courseId}}</p>
     *
     * <h3>Exemple :</h3>
     * <pre>
     * GET /reviews/IFT2255
     * </pre>
     *
     * <h3>Codes de réponse :</h3>
     * <ul>
     *     <li><strong>200</strong> — succès (liste vide possible)</li>
     * </ul>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Get all reviews for a course
    public void getReviews(Context ctx) {
        String courseId = ctx.pathParam("courseId");
        List<Review> reviews = service.getReviewsByCourse(courseId);
        ctx.json(reviews);
    }
}
