package com.diro.ift2255.controller;

import java.util.List;

import com.diro.ift2255.model.CourseSet;
import com.diro.ift2255.service.CourseSetService;

import io.javalin.http.Context;

/**
 * Contrôleur REST permettant de gérer les ensembles de cours (CourseSet).
 *
 * <p>
 * Un CourseSet représente un ensemble de sigles (max 6 cours) ainsi qu’un trimestre visé.
 * L’objectif principal est de générer un horaire résultant pour l’ensemble.
 * </p>
 *
 * <h3>Fonctionnalités exposées :</h3>
 * <ul>
 *     <li>obtenir l’horaire d’un ensemble de cours via JSON (POST /courseset)</li>
 *     <li>obtenir l’horaire via paramètres d’URL (GET /courseset?courses=A,B,C&semester=H25)</li>
 * </ul>
 *
 * <p>
 * Un horaire est retourné sous forme de liste de chaînes de caractères correspondant
 * à chaque cours et à son horaire pour le trimestre demandé.
 * </p>
 */
public class CourseSetController {

    /** Service gérant la logique métier relative aux ensembles de cours. */
    private final CourseSetService service;

    /**
     * Constructeur du contrôleur.
     *
     * @param service service utilisé pour générer les horaires
     */
    public CourseSetController(CourseSetService service) {
        this.service = service;
    }

    /**
     * Endpoint : générer un horaire à partir d’un CourseSet envoyé en JSON.
     *
     * <p>Exemple d’appel (POST) :</p>
     * <pre>
     * POST /courseset
     * {
     *   "courseIds": ["IFT2255", "IFT1025"],
     *   "semester": "H25"
     * }
     * </pre>
     *
     * <p>Codes de réponse :</p>
     * <ul>
     *     <li>200 – horaire généré avec succès</li>
     *     <li>400 – données invalides ou ensemble trop grand</li>
     * </ul>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getSchedule(Context ctx) {
        CourseSet courseSet = ctx.bodyAsClass(CourseSet.class);
        try {
            ctx.json(service.createCourseSet(courseSet));
        } catch (IllegalArgumentException e) {
            ctx.status(400).json("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Endpoint alternatif utilisant des paramètres d’URL.
     *
     * <p>Exemple :</p>
     * <pre>
     * GET /courseset/query?courses=IFT2255,IFT1025&semester=H25
     * </pre>
     *
     * <p>Paramètres requis :</p>
     * <ul>
     *     <li><strong>courses</strong> = liste séparée par virgules</li>
     *     <li><strong>semester</strong> = code du trimestre (H25, A24, E24)</li>
     * </ul>
     *
     * <p>Codes de réponse :</p>
     * <ul>
     *     <li>200 – horaire généré</li>
     *     <li>400 – paramètres manquants ou invalides</li>
     * </ul>
     *
     * @param ctx contexte HTTP Javalin
     */
    public void getScheduleFromQuery(Context ctx) {
        try {
            String coursesParam = ctx.queryParam("courses");
            String semester = ctx.queryParam("semester");

            if (coursesParam == null || semester == null) {
                ctx.status(400).result("Missing query parameters");
                return;
            }

            CourseSet set = new CourseSet(
                List.of(coursesParam.split(",")),
                semester
            );

            ctx.json(service.createCourseSet(set));

        } catch (IllegalArgumentException e) {
            ctx.status(400).json("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }


}
