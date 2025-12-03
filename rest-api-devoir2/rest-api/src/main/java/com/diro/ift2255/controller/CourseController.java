package com.diro.ift2255.controller;

import io.javalin.http.Context;
import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.CourseDetails;
import com.diro.ift2255.service.CourseService;
import com.diro.ift2255.util.ResponseUtil;

import java.util.*;


public class CourseController {
    // Service qui contient la logique métier pour la manipulation des cours et la communication avec les services externes
    private final CourseService service;

    public CourseController(CourseService service) {
        this.service = service;
    }

    /**
     * Récupère la liste de tous les cours.
     *
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
     */
    public void getAllCourses(Context ctx) {
        Map<String, String> queryParams = extractQueryParams(ctx);

        List<Course> courses = service.getAllCourses(queryParams);
        ctx.json(courses);
    }

    /**
     * Récupère un cours spécifique par son ID.
     *
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
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
     * Vérifie que l'ID du cours est bien formé
     *
     * @param courseId L'ID du cours à valider
     * @return Valeur booléeene indiquant si l'ID est valide
     */
    private boolean validateCourseId(String courseId) {
        return courseId != null && courseId.trim().length() >= 6;
    }

    /**
     * Récupère tous les paramètres de requête depuis l'URL et les met dans une Map
     *
     * @param ctx Contexte Javalin représentant la requête HTTP
     * @return Map contenant les paramètres de requête et leurs valeurs
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
     * Récupère un cours spécifique par mot-clés
     *
     * @param ctx Contexte Javalin représentant la requête et la réponse HTTP
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

    public void getCourseDetails(Context ctx) {
        String id = ctx.pathParam("id");
        if (!validateCourseId(id)) {
            ctx.status(400).json(ResponseUtil.formatError("Le paramètre id n'est pas valide."));
            return;
        }

        Optional<CourseDetails> courseDetails = service.getCourseDetails(id);

        if (courseDetails.isEmpty()) {
            ctx.status(404).json(ResponseUtil.formatError("Il n'y a pas plus de details pour cours " + id));
            return;
        }

        CourseDetails details = courseDetails.get();

        Map<String, Object> data = new LinkedHashMap<>();

        data.put("id", details.getId());
        data.put("name", details.getName());
        data.put("description", details.getDescription());
        data.put("credits", details.getCredits());
        data.put("availableTerms", details.getAvailableTermsString());
        data.put("prerequis", details.getPrerequis());

        ctx.json(data);
    }
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

    private List<String> getCommonPrerequisites(CourseDetails c1, CourseDetails c2) {
        List<String> common = new ArrayList(c1.getPrerequisiteCourses());
        common.retainAll(c2.getPrerequisiteCourses());
        return common;
    }
}

