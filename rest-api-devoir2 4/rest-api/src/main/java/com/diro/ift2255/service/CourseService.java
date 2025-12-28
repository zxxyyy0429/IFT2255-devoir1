package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.CourseDetails;
import com.diro.ift2255.model.Program;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.util.*;


/**
 * Service central pour interagir avec l'API Planifium concernant les cours.
 *
 * <p>Ce service agit comme une façade entre l'API externe et le reste de
 * l'application. Il est responsable de :</p>
 * <ul>
 *     <li>récupérer la liste des cours (avec différents filtres)</li>
 *     <li>chercher un cours par identifiant</li>
 *     <li>récupérer les détails complets d'un cours (prérequis, trimestres, horaires)</li>
 *     <li>lister les cours d'un programme</li>
 *     <li>lister les cours offerts à un trimestre donné</li>
 *     <li>obtenir l'horaire d'un cours pour un trimestre spécifique</li>
 *     <li>vérifier l'éligibilité d'un étudiant à un cours donné</li>
 * </ul>
 *
 * <p>
 * Toutes les requêtes HTTP passent par {@link HttpClientApi} et utilisent
 * des URLs de base vers l’API Planifium.
 * </p>
 */
public class CourseService {
    /** Client HTTP utilisé pour appeler l’API Planifium. */
    private final HttpClientApi clientApi;
    //private static final String BASE_URL= "https://planifium-api.onrender.com/api/v1/courses";
    /** URL de base de l'API Planifium. */
    private static final String BASE_URL = "https://planifium-api.onrender.com/api/v1";
    /** Endpoint des cours. */
    private static final String COURSES_ENDPOINT = BASE_URL + "/courses";
    /** Endpoint des programmes. */
    private static final String PROGRAMS_ENDPOINT = BASE_URL + "/programs";
    /** Service utilisé pour vérifier l'éligibilité d'un étudiant à un cours. */
    private final CourseEligibilityService eligibilityService = new CourseEligibilityService();

    /**
     * Constructeur permettant d'injecter le client HTTP utilisé pour l'API.
     *
     * @param clientApi client HTTP abstrait pour exécuter les requêtes REST
     */
    
    //private static final String COURSES_ENDPOINT  = BASE_URL + "/courses";
    //private static final String PROGRAMS_ENDPOINT = BASE_URL + "/programs";

    public CourseService(HttpClientApi clientApi) {
        this.clientApi = clientApi;
    }

    /**
     * Récupère la liste des cours en utilisant les paramètres de requête fournis.
     * <p>
     * Cette méthode correspond généralement à un appel vers
     * {@code GET /courses} de l'API Planifium, avec les filtres appropriés.
     * </p>
     *
     * @param queryParams paramètres de requête (peuvent être {@code null})
     * @return liste des cours correspondant aux filtres
     */
    public List<Course> getAllCourses(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;

        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT, params);
        List<Course> courses = clientApi.get(uri, new TypeReference<List<Course>>() {
        });

        return courses;
    }

    /**
     * Récupère un cours par son identifiant sans paramètres additionnels.
     *
     * @param courseId identifiant (sigle) du cours
     * @return un {@link Optional} contenant le cours s'il existe,
     *         sinon {@code Optional.empty()}
     */
    public Optional<Course> getCourseById(String courseId) {
        return getCourseById(courseId, null);
    }


    /**
     * Récupère un cours par son identifiant, avec des paramètres additionnels.
     * <p>
     * Cette méthode correspond à un appel de type :
     * {@code GET /courses/{id}?include_schedule=true&...}
     * </p>
     *
     * @param courseId    identifiant (sigle) du cours
     * @param queryParams paramètres de requête (peuvent être {@code null})
     * @return un {@link Optional} contenant le cours s'il existe,
     *         sinon {@code Optional.empty()}
     */
    public Optional<Course> getCourseById(String courseId, Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT + "/" + courseId, params);

        try {
            Course course = clientApi.get(uri, Course.class);
            return Optional.of(course);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    /**
     * Recherche des cours à partir de paramètres (nom, description, etc.).
     * <p>
     * Les paramètres sont directement passés à l'endpoint {@code /courses}.
     * </p>
     *
     * @param queryParams paramètres de recherche (ex. "name", "description")
     * @return liste des cours trouvés, ou une liste vide en cas d'erreur
     */
    public List<Course> getCoursesByMotCle(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT, params);

        try {
            List<Course> courseList = new ArrayList<>();
            Course[] courses = clientApi.get(uri, Course[].class);
            for (Course course : courses) {
                courseList.add(course);
            }

            return courseList;
        } catch (RuntimeException e) {
            return Collections.emptyList();
        }
    }

    /**
     * Récupère les détails complets d'un cours (prérequis, trimestres, etc.).
     *
     * @param courseId identifiant du cours
     * @return {@link Optional} contenant les détails s'ils sont trouvés,
     *         sinon {@code Optional.empty()}
     */
    public Optional<CourseDetails> getCourseDetails(String courseId) {
        return getCourseDetails(courseId, null);
    }

    /**
     * Récupère les détails complets d'un cours, avec paramètres supplémentaires.
     *
     * @param id          identifiant du cours
     * @param queryParams paramètres additionnels (ex. include_schedule)
     * @return {@link Optional} contenant les détails s'ils sont trouvés,
     *         sinon {@code Optional.empty()}
     */
    public Optional<CourseDetails> getCourseDetails(String id, Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT + "/" + id, params);

        try {
            CourseDetails courseDetails = clientApi.get(uri, CourseDetails.class);
            return Optional.of(courseDetails);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Recherche des cours à partir d'un sigle partiel.
     * <p>
     * Exemple : {@code "IFT"} retourne tous les cours dont le sigle commence
     * par IFT.
     * </p>
     *
     * @param partialSigle préfixe du sigle (ex. "IFT")
     * @return liste des cours correspondants
     */
    // Recherche par sigle partiel
    public List<Course> searchByPartialSigle(String partialSigle) {

        Map<String, String> params = Map.of(
            "courses_sigle", partialSigle
        );

        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT, params);

        return clientApi.get(uri, new TypeReference<List<Course>>() {});

        
    }

    /**
     * Recherche des cours à partir d'un texte dans la description.
     *
     * @param description texte ou mot-clé à chercher dans la description
     * @return liste des cours trouvés, ou une liste vide en cas d'erreur
     */
    public List<Course> searchByDescription(String description) {

        Map<String, String> params = Map.of(
            "description", description
        );

        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT, params);

        try {
            return clientApi.get(uri, new TypeReference<List<Course>>() {});
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Retourne la liste des cours d'un programme donné.
     *
     * <p>
     * Cette méthode interroge l'endpoint {@code /programs} avec le paramètre
     * {@code programs_list} et {@code include_courses_detail=true}, puis
     * retourne la liste de cours associée au programme.
     * </p>
     *
     * @param programId identifiant du programme (ex. 117510)
     * @return liste des cours du programme, ou une liste vide en cas d'erreur
     */
    // Voir cours offerts d'un programme
    public List<Course> getCoursesByProgram(String programId) {

        Map<String, String> params = Map.of(
            "programs_list", programId,
            "include_courses_detail", "true"
        );

        
        URI uri = HttpClientApi.buildUri(PROGRAMS_ENDPOINT, params);

        //System.out.println("DEBUG - Calling URL: " + uri);


        try {
            Program program = clientApi.get(uri, new TypeReference<Program>() {});

            if (program == null) {
                return Collections.emptyList();
            }

            if (program.getCourses() == null) {
               return Collections.emptyList();
            }

           return program.getCourses();

        } catch (RuntimeException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    /**
     * Retourne les cours d'un programme qui sont offerts à un trimestre donné.
     *
     * <p>
     * Le trimestre est donné sous la forme {@code H25}, {@code A24}, {@code E24}, etc.
     * La lettre est convertie en saison interne ("winter", "autumn", "summer") et
     * comparée aux champs {@link CourseDetails.AvailableTerms}.
     * </p>
     *
     * @param programId identifiant du programme
     * @param semester  code du trimestre (H, A ou E + année)
     * @return liste des cours offerts pour ce trimestre dans ce programme
     */
    // Cours offerts d'un semestre
    public List<Course> getCoursesBySemester(String programId, String semester) {

        List<Course> courses = getCoursesByProgram(programId);
        if (courses.isEmpty()) return Collections.emptyList();

        String season;
        switch (semester.charAt(0)) {
            case 'H': season = "winter"; break;
            case 'A': season = "autumn"; break;
            case 'E': season = "summer"; break;
            default: return Collections.emptyList();
        }

        List<Course> result = new ArrayList<>();

        for (Course c : courses) {
            Optional<CourseDetails> detailsOpt = getCourseDetails(c.getId());
            if (detailsOpt.isEmpty()) continue;

            CourseDetails details = detailsOpt.get();
            CourseDetails.AvailableTerms terms = details.getAvailableTerms();
            if (terms == null) continue;

            boolean offered =
                (season.equals("autumn") && terms.isAutumn()) ||
                (season.equals("winter") && terms.isWinter()) ||
                (season.equals("summer") && terms.isSummer());

            if (offered) {
                result.add(c);
            }
        }

        return result;
    }


    /**
     * Récupère l'horaire détaillé d'un cours pour un trimestre spécifique.
     *
     * <p>
     * Cette méthode équivaut à un appel de type :
     * {@code GET /courses/{id}?include_schedule=true&schedule_semester=a25}
     * </p>
     *
     * @param courseId identifiant du cours
     * @param semester code du trimestre (ex. "A25", "H24")
     * @return {@link Optional} contenant les détails du cours avec horaire,
     *         sinon {@code Optional.empty()}
     */
    // Horaire d'un cours pour un trimestre
    public Optional<CourseDetails> getCourseSchedule(String courseId, String semester) {
        Map<String, String> params = Map.of(
            "include_schedule", "true",
            "schedule_semester", semester.toLowerCase()
        );

        URI uri = HttpClientApi.buildUri(COURSES_ENDPOINT + "/" + courseId, params); 

        try {
            CourseDetails courseDetails = clientApi.get(uri, CourseDetails.class);
            return Optional.of(courseDetails);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Vérifie si un étudiant est éligible à un cours donné.
     *
     * <p>
     * Cette méthode récupère d'abord les détails du cours, puis délègue la
     * logique d'éligibilité à {@link CourseEligibilityService}.
     * </p>
     *
     * @param courseId         identifiant du cours
     * @param completedCourses liste des sigles de cours complétés par l'étudiant
     * @param studentCycle     cycle de l'étudiant (ex. "bachelor", "master")
     * @return {@code true} si l'étudiant est éligible, {@code false} sinon
     */
    // Vérification d'éligibilité à un cours
    public boolean checkEligibility(String courseId, List<String> completedCourses, String studentCycle) {
    Optional<CourseDetails> courseOpt = getCourseDetails(courseId);

    if (courseOpt.isEmpty()) {
        return false;
    }

    return eligibilityService.isEligible(
            courseOpt.get(),
            completedCourses,
            studentCycle
    );
}


    



}