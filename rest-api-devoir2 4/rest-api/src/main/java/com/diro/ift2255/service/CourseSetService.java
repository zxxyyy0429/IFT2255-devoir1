package com.diro.ift2255.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.diro.ift2255.model.CourseDetails;
import com.diro.ift2255.model.CourseDetails.Schedule;
import com.diro.ift2255.model.CourseSet;


/**
 * Service responsable de la gestion des ensembles de cours ({@link CourseSet}).
 *
 * <p>Il permet notamment de :</p>
 * <ul>
 *     <li>valider un ensemble de cours (taille maximale, existence des cours)</li>
 *     <li>récupérer l'horaire des cours pour un trimestre donné</li>
 *     <li>générer une représentation textuelle de l'horaire combiné</li>
 * </ul>
 *
 * <p>
 * Ce service s'appuie sur {@link CourseService} pour interroger l'API Planifium
 * et récupérer les détails de chaque cours (incluant les horaires).
 * </p>
 */
public class CourseSetService {
    /** Service pour récupérer les informations de cours et leurs horaires. */
    private final CourseService courseService;

    /**
     * Constructeur permettant d'injecter le {@link CourseService}.
     *
     * @param courseService service d'accès aux données de cours
     */
    public CourseSetService(CourseService courseService) {
        this.courseService = courseService;
    }

    /**
     * Valide un ensemble de cours et génère une représentation textuelle
     * de l'horaire pour le trimestre demandé.
     *
     * <p>Pour chaque cours de l'ensemble :</p>
     * <ol>
     *     <li>on vérifie que le cours existe dans Planifium ;</li>
     *     <li>on récupère ses horaires ({@code include_schedule=true});</li>
     *     <li>on filtre les horaires pour le trimestre de l'ensemble
     *         (voir {@link CourseSet#getSemester()});</li>
     *     <li>on formate l'horaire en chaîne lisible (voir {@link #formatSchedule(CourseDetails.Schedule)}).</li>
     * </ol>
     *
     * <p>Chaque entrée de la liste retournée est de la forme :
     * {@code "IFT2255: Section 10 | Théorie: Lundi 09:00-12:00, ..."}</p>
     *
     * @param set ensemble de cours contenant la liste des sigles et le trimestre
     * @return liste de descriptions d'horaires, une par cours
     *
     * @throws IllegalArgumentException si :
     *         <ul>
     *             <li>l'ensemble est vide</li>
     *             <li>l'ensemble contient plus de 6 cours</li>
     *             <li>un cours de l'ensemble est introuvable</li>
     *         </ul>
     */
    // Validate a course set and return resulting schedule
    public List<String> createCourseSet(CourseSet set) {
        List<String> scheduleResult = new ArrayList<>();

        if (set.getCourseIds() == null || set.getCourseIds().isEmpty()) {
            throw new IllegalArgumentException("Le set de cours est vide.");
        }

        if (set.getCourseIds().size() > 6) {
            throw new IllegalArgumentException("Un ensemble de cours ne peut pas dépasser 6 cours.");
        }

        Map<String, String> queryParams = new HashMap<>();
        queryParams.put("include_schedule", "true");

        for (String courseId : set.getCourseIds()) {
            
            Optional<CourseDetails> courseOpt = courseService.getCourseDetails(courseId, queryParams);

            if (courseOpt.isEmpty()) {
                throw new IllegalArgumentException("Cours introuvable: " + courseId);
            }

            CourseDetails course = courseOpt.get();
            List<Schedule> schedules = course.getSchedules();
            String scheduleStr = "pas de cours pour ce trimestre";

            if (schedules != null && !schedules.isEmpty()) {
                
                for (Schedule s : schedules) {

                    if (s.getSemester() != null && s.getSemester().trim().equalsIgnoreCase(set.getSemester().trim())) {
                        scheduleStr = formatSchedule(s);
                        break;
                    }

                }
            }

            scheduleResult.add(courseId + ": " + scheduleStr);
        }

        return scheduleResult;
    }

    /**
     * Construit une chaîne lisible représentant l'horaire d'un cours pour un trimestre.
     *
     * <p>L'horaire est organisé par section, puis par volet (Théorie, Laboratoire, etc.).
     * Pour chaque volet, on déduplique les créneaux d'activités (jour + heure de début/fin).</p>
     *
     * <p>Exemple de résultat :</p>
     * <pre>
     * Section 10 | Théorie: [Lundi 09:00-12:00, Mercredi 09:00-10:00] | Laboratoire: [...]
     * </pre>
     *
     * @param s horaire d'un cours pour un trimestre donné
     * @return représentation textuelle de l'horaire
     */
    private String formatSchedule(CourseDetails.Schedule s) {
        if (s.getSections() == null || s.getSections().isEmpty()) {
            return "Aucune section";
        }

        StringBuilder result = new StringBuilder();

        for (CourseDetails.Section section : s.getSections()) {
            result.append("Section ")
                .append(section.getNumberInscription())
                .append(" | ");

            if (section.getVolets() == null || section.getVolets().isEmpty()) {
                result.append("Aucun horaire");
                continue;
            }

            boolean firstVolet = true;

            for (CourseDetails.Volet volet : section.getVolets()) {
                if (volet.getActivities() == null || volet.getActivities().isEmpty()) {
                    continue;
                }

                // Déduplication des créneaux horaires
                List<String> slots = new ArrayList<>();

                for (CourseDetails.Activity a : volet.getActivities()) {
                    String slot = a.getDays() + " " +
                                a.getStartTime() + "-" +
                                a.getEndTime();

                    if (!slots.contains(slot)) {
                        slots.add(slot);
                    }
                }

                if (slots.isEmpty()) {
                    continue;
                }

                if (!firstVolet) {
                    result.append(" | ");
                }

                result.append(volet.getName())
                    .append(": ")
                    .append(String.join(", ", slots));

                firstVolet = false;
            }
        }

        return result.toString();
    }




}
