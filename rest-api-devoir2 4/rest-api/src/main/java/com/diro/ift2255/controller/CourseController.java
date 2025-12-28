package com.diro.ift2255.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.CourseDetails;
import com.diro.ift2255.model.CourseResult;
import com.diro.ift2255.service.CourseResultService;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.ResponseUtil;

import io.javalin.http.Context;


/**
 * Contrôleur REST principal pour toutes les opérations liées aux cours.
 *
 * <p>Ce contrôleur expose plusieurs endpoints pour :</p>
 * <ul>
 *     <li>rechercher des cours (par sigle, description, mot-clé)</li>
 *     <li>récupérer les détails d’un cours</li>
 *     <li>afficher les cours d’un programme / d’un trimestre</li>
 *     <li>récupérer l’horaire d’un cours</li>
 *     <li>vérifier l’éligibilité d’un étudiant à un cours</li>
 *     <li>afficher les résultats académiques d’un cours</li>
 *     <li>comparer deux cours (prérequis, crédits, etc.)</li>
 * </ul>
 *
 * <p>
 * Tous les endpoints utilisent {@link ResponseUtil} pour formater les messages d’erreur,
 * et s’appuient sur {@link CourseService} et {@link CourseResultService} pour la logique métier.
 * </p>
 */
public class CourseController {

    /** Service gérant la logique métier autour des cours et de Planifium. */
    // Service qui contient la logique métier pour la manipulation des cours et la communication avec les services externes
    private final CourseService service;


    public CourseController(CourseService service) {
        this.service = service;
        this.courseResultService = new CourseResultService();
    }

    /** Service permettant d'accéder aux résultats académiques agrégés. */
    private final CourseResultService courseResultService;

    /**
     * Constructeur utilisant une nouvelle instance de {@link CourseResultService}.
     * <p>Principalement utilisé dans un contexte où l'injection explicite de CourseResultService
     * n'est pas nécessaire.</p>
     *
     * @param service service de gestion des cours
     */
    public CourseController(CourseService courseService,CourseResultService courseResultService) {
        this.service = courseService;
        this.courseResultService = courseResultService;
    }

    /**
     * Endpoint : récupérer la liste de tous les cours.
     *
     * <p>Exemple : {@code GET /courses}</p>
     * Les paramètres de requête (s’il y en a) sont transmis tels quels à l’API Planifium.
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getAllCourses(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);

        List<Course> courses = service.getAllCourses(queryParams);
        ctx.json(courses);
    }

    /**
     * Endpoint : récupérer un cours spécifique par son ID.
     *
     * <p>Exemple : {@code GET /courses/IFT2255}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getCourseById(Context ctx) {
        String id = ctx.pathParam("id");

        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Optional<Course> course = service.getCourseById(id);
        if (course.isPresent()) {
            ctx.json(course.get());
        } else {
            ctx.status(404).json(ResponseUtil.formatError("Aucun cours ne correspond à l'ID: " + id));
        }
    }

    /**
     * Vérifie que l'ID du cours est bien formé.
     * <p>Validation simple : non null et longueur minimale de 6 caractères.</p>
     *
     * @param courseId ID du cours à valider
     * @return {@code true} si l'ID semble valide, {@code false} sinon
     */
    private boolean validateCourseId(String courseId) {
        return courseId != null && courseId.trim().length() >= 6;
    }

    /**
     * Récupère tous les paramètres de requête depuis l'URL et les met dans une {@link Map}.
     *
     * @param ctx contexte Javalin représentant la requête HTTP
     * @return map contenant les paramètres de requête et leurs valeurs
     */
    private Map<String, String> extractQueryParams(Context ctx) {
        Map<String, String> queryParams = new HashMap<>();

        ctx.queryParamMap().forEach((key, values) -> {
            if (!values.isEmpty()) {
                queryParams.put(key, values.get(0));
            }
        });

        return queryParams;
    }

    /**
     * Endpoint : rechercher des cours selon un ou plusieurs mots-clés.
     *
     * <p>
     * Exemple : {@code GET /courses/search?name=java} ou {@code ?description=logiciel}
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getCourseByMotCle(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);

        if (queryParams.isEmpty()) {
            ctx.status(400).json(ResponseUtil.formatError("Au moins un mot-clé est requis."));
            return;
        }

        List<Course> courses = service.getCoursesByMotCle(queryParams);

        ctx.json(courses);
    }

    /**
     * Endpoint : récupérer les détails complets d'un cours (prérequis, trimestres, horaires).
     *
     * <p>Exemple : {@code GET /courses/IFT2255/details}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Récupère les détails d'un cours
    public void getCourseDetails(Context ctx) {
        String id = ctx.pathParam("id");
        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("include_schedule", "true");

        Optional<CourseDetails> courseDetails = service.getCourseDetails(id, queryParams);

        if (courseDetails.isEmpty()) {
            ctx.status(404).json(ResponseUtil.formatError("Il n'y a pas plus de détails pour le cours " + id));
            return;
        }

        ctx.json(courseDetails.get());
    }

    /**
     * Endpoint : comparer deux cours sur la base de leurs prérequis et crédits.
     *
     * <p>Exemple : {@code GET /courses/compare?id1=IFT2255&id2=IFT3225}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Compare deux cours
    public void compareCourses(Context ctx) {
        String id1 = ctx.queryParam("id1");
        String id2 = ctx.queryParam("id2");
        if (this.validateCourseId(id1) && this.validateCourseId(id2)) {
            Optional<CourseDetails> course1Opt = this.service.getCourseDetails(id1);
            Optional<CourseDetails> course2Opt = this.service.getCourseDetails(id2);
            if (!course1Opt.isEmpty() && !course2Opt.isEmpty()) {
                CourseDetails c1 = (CourseDetails)course1Opt.get();
                CourseDetails c2 = (CourseDetails)course2Opt.get();
                Map<String, Object> comparison = new LinkedHashMap();
                comparison.put("id1", c1.getId());
                comparison.put("name1", c1.getName());
                comparison.put("credits1", c1.getCredits());
                comparison.put("prerequisites1", c1.getPrerequis());
                comparison.put("id2", c2.getId());
                comparison.put("name2", c2.getName());
                comparison.put("credits2", c2.getCredits());
                comparison.put("prerequisites2", c2.getPrerequis());
                comparison.put("sameCredits", c1.getCredits() == c2.getCredits());
                comparison.put("commonPrerequisites", this.getCommonPrerequisites(c1, c2));
                ctx.json(comparison);
            } else {
                ctx.status(404).json(ResponseUtil.formatError("Un ou les deux cours n'ont pas été trouvés."));
            }
        } else {
            ctx.status(400).json(ResponseUtil.formatError("Les paramètres id1 et id2 doivent être valides."));
        }
    }

    /**
     * Calcule la liste des prérequis communs entre deux cours.
     *
     * @param c1 premier cours
     * @param c2 second cours
     * @return liste des sigles de cours demandés par les deux comme prérequis
     */
    private List<String> getCommonPrerequisites(CourseDetails c1, CourseDetails c2) {
        List<String> common = new ArrayList(c1.getPrerequisiteCourses());
        common.retainAll(c2.getPrerequisiteCourses());
        return common;
    }


    /**
     * Endpoint : recherche de cours par sigle partiel.
     *
     * <p>Exemple : {@code GET /courses/searchBySigle?sigle=IFT}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Recherche par sigle partiel -- IFT
    public void searchByPartialSigle(Context ctx) {

        String sigle = ctx.queryParam("sigle");

        if (sigle == null || sigle.isBlank()) {
            ctx.status(400)
            .json(ResponseUtil.formatError("Le paramètre 'sigle' est requis."));
            return;
        }

        List<Course> courses = service.searchByPartialSigle(sigle);
        ctx.json(courses);
    }

    /**
     * Endpoint : recherche de cours par description.
     *
     * <p>Exemple : {@code GET /courses/searchByDescription?description=java}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void searchByDescription(Context ctx) {

        String description = ctx.queryParam("description");

        if (description == null || description.isBlank()) {
            ctx.status(400)
            .json(ResponseUtil.formatError("Le paramètre 'description' est requis."));
            return;
        }

        List<Course> courses = service.searchByDescription(description);
        ctx.json(courses);
    }

    /**
     * Endpoint : voir tous les cours offerts dans un programme donné.
     *
     * <p>Exemple : {@code GET /programs/117510/courses}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Voir cours offerts d'un programme
    public void getCoursesByProgram(Context ctx) {
        String programId = ctx.pathParam("programId");  
        List<Course> courses = service.getCoursesByProgram(programId);  

        if (courses.isEmpty()) {
            ctx.status(404).json(Map.of("error", "Aucun cours trouvé pour ce programme."));
        } else {
            ctx.json(courses);  
        }
    }

    /**
     * Endpoint : voir les cours offerts pour un trimestre donné.
     *
     * <p>
     * Exemple : {@code GET /courses/semester?semester=H25&programId=117510}
     * </p>
     *
     * <p>
     * Si {@code programId} est fourni, la recherche est restreinte au programme;
     * sinon, tous les cours du trimestre sont retournés (si supporté par le service).
     * </p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Voir les cours offerts pour un semestre
    public void getCoursesBySemester(Context ctx) {

        String semester = ctx.queryParam("semester");
        String programId = ctx.queryParam("programId"); 

        if (semester == null || semester.isBlank()) {
            ctx.status(400).json(
                ResponseUtil.formatError("Le paramètre 'semester' est requis (ex: H25, A24, E24).")
            );
            return;
        }

        // Validation simple du format, ex. H25 / A24 / E24
        if (!semester.matches("[HAE][0-9]{2}")) {
            ctx.status(400).json(
                ResponseUtil.formatError("Format de trimestre invalide. Utilisez H25, A24 ou E24.")
            );
            return;
        }

        List<Course> courses;

        if (programId != null && !programId.isBlank()) {
            courses = service.getCoursesBySemester(programId, semester);
        } else {
            courses = service.getAllCourses(Map.of()); 
        }

        if (courses.isEmpty()) {
            ctx.status(404).json(
                ResponseUtil.formatError("Aucun cours trouvé pour ce trimestre.")
            );
            return;
        }

        ctx.json(courses);
    }

    /**
     * Endpoint : récupérer l'horaire d'un cours pour un trimestre donné.
     *
     * <p>Exemple : {@code GET /courses/IFT2255/schedule?semester=H25}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Récupère l'horaire d'un cours pour un trimestre donné
    public void getCourseSchedule(Context ctx) {
        String courseId = ctx.pathParam("courseId");
        String semester = ctx.queryParam("semester");

        if (courseId == null || courseId.isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre 'courseId' est requis."));
            return;
        }

        if (semester == null || semester.isBlank()) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre 'semester' est requis."));
            return;
        }

        Optional<CourseDetails> courseScheduleOpt = service.getCourseSchedule(courseId, semester);

        if (courseScheduleOpt.isEmpty()) {
            ctx.status(404).json(ResponseUtil.formatError(
                "Aucune information d'horaire trouvée pour le cours " + courseId + " au trimestre " + semester
            ));
            return;
        }

        ctx.json(courseScheduleOpt.get());
    }

    /**
     * Endpoint : vérifier l'éligibilité d'un étudiant pour un cours.
     *
     * <p>
     * Exemple : {@code POST /courses/{id}/eligibility}
     * </p>
     *
     * Corps JSON attendu :
     * <pre>
     * {
     *   "completedCourses": ["IFT1025", "IFT2255"],
     *   "cycle": "bachelor"
     * }
     * </pre>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Vérification d'éligibilité à un cours
    public void checkEligibility(Context ctx) {
        String courseId = ctx.pathParam("id");

        if (!validateCourseId(courseId)) {
            ctx.status(400).json(ResponseUtil.formatError("ID de cours invalide."));
            return;
        }

        Map<String, Object> body = ctx.bodyAsClass(Map.class);

        List<String> completedCourses =(List<String>) body.get("completedCourses");
        String cycle = (String) body.get("cycle");

        if (completedCourses == null || cycle == null) {
            ctx.status(400).json(ResponseUtil.formatError(
                "completedCourses et cycle sont requis."
            ));
            return;
        }

        boolean eligible = service.checkEligibility(
                courseId,
                completedCourses,
                cycle
        );

        ctx.json(Map.of(
            "courseId", courseId,
            "eligible", eligible
        ));
    }

    /**
     * Endpoint : voir les résultats académiques agrégés d'un cours.
     *
     * <p>Exemple : {@code GET /courses/IFT2255/results}</p>
     *
     * @param ctx contexte HTTP Javalin
     */
    // Voir résultats académiques d'un cours
    public void getCourseResults(Context ctx) {

        String sigle = ctx.pathParam("sigle");

        if (sigle == null || sigle.isBlank()) {
            ctx.status(400).json(
                ResponseUtil.formatError("Le sigle du cours est requis.")
            );
            return;
        }

        Optional<CourseResult> result = courseResultService.getResultsBySigle(sigle);

        if (result.isEmpty()) {
            ctx.status(404).json(
                ResponseUtil.formatError(
                    "Aucun résultat académique trouvé pour le cours " + sigle
                )
            );
            return;
        }

        ctx.json(result.get());
    }




}




    


