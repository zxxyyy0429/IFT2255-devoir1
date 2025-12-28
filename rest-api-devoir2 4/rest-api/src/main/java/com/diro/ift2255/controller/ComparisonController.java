package com.diro.ift2255.controller;

import java.util.Optional;

import com.diro.ift2255.model.CourseResult;
import com.diro.ift2255.service.CourseComparisonService;
import com.diro.ift2255.service.CourseResultService;
import com.diro.ift2255.util.ResponseUtil;

import io.javalin.http.Context;

/**
 * Contrôleur REST responsable de la comparaison entre deux cours.
 *
 * <p>
 * Cet endpoint permet de comparer :
 * </p>
 * <ul>
 *     <li>la charge de travail moyenne (provenant des avis étudiants)</li>
 *     <li>la difficulté estimée (calculée à partir des résultats académiques)</li>
 *     <li>les métadonnées associées aux deux cours</li>
 * </ul>
 *
 * <h3>Endpoint</h3>
 * <pre>
 * GET /compare?course1=IFT2255&course2=IFT3225
 * </pre>
 *
 * <h3>Paramètres</h3>
 * <ul>
 *     <li><strong>course1</strong> – ID ou sigle du premier cours</li>
 *     <li><strong>course2</strong> – ID ou sigle du second cours</li>
 * </ul>
 *
 * <h3>Codes de réponse</h3>
 * <ul>
 *     <li>200 – comparaison réussie</li>
 *     <li>400 – paramètres manquants</li>
 *     <li>404 – si un cours n'existe pas (géré dans le service)</li>
 * </ul>
 *
 * <h3>Structure de la réponse</h3>
 * L’objet retourné est un {@link com.diro.ift2255.model.ComparisonResult}
 * contenant : course1, course2, workload1, workload2, difficulty1, difficulty2.
 */
public class ComparisonController {

    /** Service responsable de la logique de comparaison. */
    private final CourseComparisonService service;
    /** Service pour récupérer les résultats académiques (score ∈ [0,4.3]). */
    private final CourseResultService courseResultService;

    /**
     * Constructeur du contrôleur.
     *
     * @param service service métier utilisé pour comparer deux cours
     */
    public ComparisonController(CourseComparisonService service) {
        this.service = service;
        courseResultService = new CourseResultService();
    }

    /**
     * Endpoint HTTP permettant de comparer deux cours.
     *
     * <p>
     * URL : <pre>/compare?course1=X&course2=Y</pre>
     * </p>
     *
     * <p>
     * Étapes :
     * </p>
     * <ol>
     *     <li>Lire les identifiants des deux cours</li>
     *     <li>Valider qu’ils sont présents</li>
     *     <li>Récupérer leur score académique</li>
     *     <li>Transmettre au service de comparaison</li>
     *     <li>Retourner un {@code ComparisonResult} en JSON</li>
     * </ol>
     *
     * @param ctx contexte HTTP Javalin contenant les paramètres de requête
     */
    public void compareCourses(Context ctx) {
        // Lire les paramètres ?course1=...&course2=...
        String courseId1 = ctx.queryParam("course1");
        String courseId2 = ctx.queryParam("course2");

        // Vérification des paramètres obligatoires
        if (courseId1 == null || courseId2 == null) {
            ctx.status(400).json(ResponseUtil.formatError("Deux identifiants de cours sont requis"));
            return;
        }

        // Cherche les résultats académiques des deux cours (Optionals)
        Optional<CourseResult> result1 = courseResultService.getResultsBySigle(courseId1);
        Optional<CourseResult> result2 = courseResultService.getResultsBySigle(courseId2);

        // Score académique (0 si non trouvé)
        double score1 = result1.map(CourseResult::getScore).orElse(0.0);
        double score2 = result2.map(CourseResult::getScore).orElse(0.0);

        // Appel au service pour construire un ComparisonResult complet
        ctx.json(service.compareCourses(courseId1, score1, courseId2, score2));
    }
}
