package com.diro.ift2255.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.*;

import com.diro.ift2255.model.CourseDetails;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.service.CourseService;

import io.javalin.http.Context;

@ExtendWith(MockitoExtension.class) // ← Active Mockito pour ce test
public class CourseControllerTest {

    @Mock // ← Crée un FAUX CourseService
    private CourseService mockService;

    @Mock // ← Crée un FAUX Context Javalin
    private Context mockContext;

    private CourseController controller; // ← Le VRAI contrôleur à tester

    private long testStartTime;

    @BeforeAll
    static void printHeader() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("CourseController Tests");
        System.out.println("=".repeat(80));
    }

    @BeforeEach
    void setup(TestInfo testInfo) {
        // On injecte les FAUX objets dans le VRAI contrôleur
        controller = new CourseController(mockService);
        testStartTime = System.currentTimeMillis();

        System.out.println("\nTEST: " + testInfo.getDisplayName());
        System.out.println("    ├─ Method: " + testInfo.getTestMethod().get().getName());
        System.out.println("    ├─ Assertions:");
    }

    @AfterEach
    void tearDown(TestInfo testInfo) {
        long duration = System.currentTimeMillis() - testStartTime;
        System.out.println("    └─ Duration: " + duration + " ms");
    }

    /**************************************************************************
     * Tests for getAllCourses method
     *************************************************************************/

    @Test
    @DisplayName("Get all courses should return all courses when no query parameters")
    void testGetAllCoursesWithoutQueryParams() {
        // ARRANGE
        List<Course> mockCourses = Arrays.asList(
                new Course("IFT1015", "Programmation I"),
                new Course("IFT1025", "Programmation II"));

        when(mockContext.queryParamMap()).thenReturn(new HashMap<>());
        when(mockService.getAllCourses(any())).thenReturn(mockCourses);

        // ACT
        controller.getAllCourses(mockContext);

        // ASSERT
        try {
            verify(mockContext).queryParamMap();
            OK("Query params extracted from context", false);

            verify(mockService).getAllCourses(any(Map.class));
            OK("Service called with query params", false);

            verify(mockContext).json(mockCourses);
            OK("Response returned with " + mockCourses.size() + " courses");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get all courses should pass query parameters to service")
    void testGetAllCoursesWithQueryParameters() {
        // ARRANGE
        Map<String, List<String>> queryParamMap = new HashMap<>();
        queryParamMap.put("session", Arrays.asList("A2025"));

        List<Course> mockCourses = Arrays.asList(
                new Course("IFT1015", "Programmation I"));

        when(mockContext.queryParamMap()).thenReturn(queryParamMap);
        when(mockService.getAllCourses(any())).thenReturn(mockCourses);

        // ACT
        controller.getAllCourses(mockContext);

        // ASSERT
        try {
            verify(mockService).getAllCourses(argThat(params -> 
                    params.containsKey("session") &&
                    params.get("session").equals("A2025")));
            OK("Service called with correct query parameters", false);

            verify(mockContext).json(mockCourses);
            OK("Response returned successfully");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    /**************************************************************************
     * Tests for getCourseById method
     *************************************************************************/

    @Test
    @DisplayName("Get course by ID should return course when ID exists")
    void testGetCourseByIdWhenIdExists() {
        // ARRANGE
        String courseId = "IFT2255";
        Course mockCourse = new Course(courseId, "Génie logiciel");

        when(mockContext.pathParam("id")).thenReturn(courseId);
        when(mockService.getCourseById(courseId)).thenReturn(Optional.of(mockCourse));

        // ACT
        controller.getCourseById(mockContext);

        // ASSERT
        try {
            verify(mockContext).pathParam("id");
            OK("Path parameter 'id' extracted", false);

            verify(mockService).getCourseById(courseId);
            OK("Service called with ID: " + courseId, false);

            verify(mockContext).json(mockCourse);
            OK("Course returned successfully", false);

            verify(mockContext, never()).status(anyInt());
            OK("No error status set");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get course by ID should return 404 when course not found")
    void testGetCourseByIdWhenCourseNotFound() {
        // ARRANGE
        String courseId = "IFT1234";

        when(mockContext.pathParam("id")).thenReturn(courseId);
        when(mockService.getCourseById(courseId)).thenReturn(Optional.empty());
        when(mockContext.status(404)).thenReturn(mockContext);

        // ACT
        controller.getCourseById(mockContext);

        // ASSERT
        try {
            verify(mockService).getCourseById(courseId);
            OK("Service called with ID: " + courseId, false);

            verify(mockContext).status(404);
            OK("Status 404 set", false);

            verify(mockContext).json(argThat(map -> map instanceof Map &&
                    ((Map<?, ?>) map).containsKey("error")));
            OK("Error message returned");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get course by ID should return 400 when ID is null")
    void testGetCourseByIdWhenIdIsNull() {
        // ARRANGE
        when(mockContext.pathParam("id")).thenReturn(null);
        when(mockContext.status(400)).thenReturn(mockContext);

        // ACT
        controller.getCourseById(mockContext);

        // ASSERT
        try {
            verify(mockContext).status(400);
            OK("Status 400 set", false);

            verify(mockContext).json(argThat(map -> map instanceof Map &&
                    ((Map<?, ?>) map).containsKey("error")));
            OK("Error message returned", false);

            verify(mockService, never()).getCourseById(any());
            OK("Service was not called");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get course by ID should return 400 when ID is empty string")
    void testGetCourseByIdWhenIdIsEmpty() {
        // ARRANGE
        when(mockContext.pathParam("id")).thenReturn("");
        when(mockContext.status(400)).thenReturn(mockContext);

        // ACT
        controller.getCourseById(mockContext);

        // ASSERT
        try {
            verify(mockContext).status(400);
            OK("Status 400 set for empty ID", false);

            verify(mockService, never()).getCourseById(any());
            OK("Service was not called");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("HRX-1 — Comparer deux cours valides doit retourner un tableau comparatif")
    void testCompareCourses_ValidCourses() {
        CourseDetails c1 = new CourseDetails();
        c1.setId("ABC123");
        c1.setName("Programmation 2");
        c1.setCredits(3);
        c1.setPrerequisiteCourses(List.of("IFT1015", "IFT1001"));
        CourseDetails c2 = new CourseDetails();
        c2.setId("DEF456");
        c2.setName("Structures de données");
        c2.setCredits(3);
        c2.setPrerequisiteCourses(List.of("IFT1015"));
        Mockito.when(this.mockContext.queryParam("id1")).thenReturn("ABC123");
        Mockito.when(this.mockContext.queryParam("id2")).thenReturn("DEF456");
        Mockito.when(this.mockService.getCourseDetails("ABC123")).thenReturn(Optional.of(c1));
        Mockito.when(this.mockService.getCourseDetails("DEF456")).thenReturn(Optional.of(c2));
        this.controller.compareCourses(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((map) -> {
                Map<?, ?> m = (Map)map;
                return m.get("id1").equals("ABC123") && m.get("name1").equals("Programmation 2") && m.get("credits1").equals(3) && m.get("id2").equals("DEF456") && m.get("name2").equals("Structures de données") && m.get("credits2").equals(3) && (Boolean)m.get("sameCredits");
            }));
            this.OK("Comparaison retournée correctement");
        } catch (AssertionError e) {
            this.Err("Le JSON généré ne correspond pas au résultat attendu");
            throw e;
        }
    }

    @Test
    @DisplayName("HRX-2 — Vérifier les prérequis communs entre deux cours")
    void testCompareCourses_CommonPrerequisites() {
        CourseDetails c1 = new CourseDetails();
        c1.setId("AAA111");
        c1.setPrerequisiteCourses(List.of("IFT1015", "IFT2000"));
        CourseDetails c2 = new CourseDetails();
        c2.setId("BBB222");
        c2.setPrerequisiteCourses(List.of("IFT1015"));
        Mockito.when(this.mockContext.queryParam("id1")).thenReturn("AAA111");
        Mockito.when(this.mockContext.queryParam("id2")).thenReturn("BBB222");
        Mockito.when(this.mockService.getCourseDetails("AAA111")).thenReturn(Optional.of(c1));
        Mockito.when(this.mockService.getCourseDetails("BBB222")).thenReturn(Optional.of(c2));
        this.controller.compareCourses(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((map) -> {
                List<String> commons = (List)((Map)map).get("commonPrerequisites");
                return commons.size() == 1 && commons.contains("IFT1015");
            }));
            this.OK("Prérequis commun correctement détecté");
        } catch (AssertionError e) {
            this.Err("Erreur dans la détection des prérequis communs");
            throw e;
        }
    }

    @Test
    @DisplayName("HRX-3 — Comparaison impossible si un cours est introuvable")
    void testCompareCourses_CourseMissing() {
        Mockito.when(this.mockContext.queryParam("id1")).thenReturn("ABC123");
        Mockito.when(this.mockContext.queryParam("id2")).thenReturn("INVALID");
        Mockito.when(this.mockService.getCourseDetails("ABC123")).thenReturn(Optional.empty());
        Mockito.when(this.mockService.getCourseDetails("INVALID")).thenReturn(Optional.empty());
        Mockito.when(this.mockContext.status(404)).thenReturn(this.mockContext);
        this.controller.compareCourses(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).status(404);
            this.OK("404 retourné correctement", false);
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((map) -> ((Map)map).get("error").equals("Un ou les deux cours n'ont pas été trouvés.")));
            this.OK("Message d’erreur correct");
        } catch (AssertionError e) {
            this.Err("Le contrôleur n'a pas renvoyé l'erreur attendue");
            throw e;
        }
    }

    @Test
    @DisplayName("Get courses by keyword should return filtered list when keywords exist")
    void testGetCourseByMotCleSuccess() {
        // ARRANGE
        Map<String, List<String>> queryParamMap = new HashMap<>();
        queryParamMap.put("motCle", Arrays.asList("génie"));
        queryParamMap.put("cycle", Arrays.asList("premier cycle"));

        List<Course> mockCourses = Arrays.asList(
                new Course("IFT2255", "Génie Logiciel"),
                new Course("IFT3913", "Sujets spéciaux en génie"));

        when(mockContext.queryParamMap()).thenReturn(queryParamMap);
        when(mockService.getCoursesByMotCle(any())).thenReturn(mockCourses);

        // ACT
        controller.getCourseByMotCle(mockContext);

        // ASSERT
        try {
            // Verification que le service a été appelé avec les bons paramètres
            verify(mockService).getCoursesByMotCle(argThat(params ->
                    params.containsKey("motCle") && params.get("motCle").equals("génie") &&
                            params.containsKey("cycle") && params.get("cycle").equals("premier cycle")));
            OK("Service called with correct keywords and filters", false);

            // Verification que la liste de cours a été retournée en JSON
            verify(mockContext).json(mockCourses);
            OK("Filtered list returned successfully");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get course details should return formatted JSON when details exist")
    void testGetCourseDetailsSuccess() {
        String courseId = "IFT2255";
        CourseDetails.AvailableTerms terms = new CourseDetails.AvailableTerms();
        terms.setAutumn(true);
        terms.setWinter(false);
        terms.setSummer(true);
        CourseDetails mockDetails = new CourseDetails();
        mockDetails.setId(courseId);
        mockDetails.setName("Génie Logiciel");
        mockDetails.setDescription("Introduction au génie logiciel.");
        mockDetails.setCredits(3);
        mockDetails.setAvailableTerms(terms);
        mockDetails.setPrerequisiteCourses(Arrays.asList("IFT1015", "IFT1025"));
        Mockito.when(this.mockContext.pathParam("id")).thenReturn(courseId);
        Mockito.when(this.mockService.getCourseDetails(courseId)).thenReturn(Optional.of(mockDetails));
        this.controller.getCourseDetails(this.mockContext);

        try {
            ((CourseService)Mockito.verify(this.mockService)).getCourseDetails(courseId);
            this.OK("Service called to fetch details", false);
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((data) -> {
                Map<String, Object> map = (Map)data;
                Assertions.assertEquals(courseId, map.get("id"), "ID matches");
                Assertions.assertEquals("Génie Logiciel", map.get("name"), "Name matches");
                Assertions.assertEquals(3, map.get("credits"), "Credits match");
                Assertions.assertEquals("Automne, Ete", map.get("availableTerms"), "Terms are correctly formatted");
                Assertions.assertEquals("IFT1015, IFT1025", map.get("prerequis"), "Prerequis are correctly joined");
                return true;
            }));
            this.OK("Details returned in correct JSON format");
            ((Context)Mockito.verify(this.mockContext, Mockito.never())).status(Mockito.anyInt());
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("Get course details should return 400 when ID is invalid ")
    void testGetCourseDetailsInvalidId() {
        String invalidId = "IFT1";
        Mockito.when(this.mockContext.pathParam("id")).thenReturn(invalidId);
        Mockito.when(this.mockContext.status(400)).thenReturn(this.mockContext);
        this.controller.getCourseDetails(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).status(400);
            this.OK("Status 400 set", false);
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((map) -> map instanceof Map && ((Map)map).containsKey("error")));
            this.OK("Error message returned", false);
            ((CourseService)Mockito.verify(this.mockService, Mockito.never())).getCourseDetails((String)Mockito.any());
            this.OK("Service was not called");
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("ZXY 1-Afficher Aucun pour un cours qui n'a pas de condition prerequis")
    void testVoirDetailsAucunPrerequis() {
        String id = "IFT1015";
        CourseDetails mockDetails = new CourseDetails(id, "Programmation 1", "python", 3, (CourseDetails.AvailableTerms)null, Collections.emptyList());
        Mockito.when(this.mockContext.pathParam("id")).thenReturn(id);
        Mockito.when(this.mockService.getCourseDetails(id)).thenReturn(Optional.of(mockDetails));
        this.controller.getCourseDetails(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((obj) -> {
                Map<?, ?> map = (Map)obj;
                return "Aucun".equals(map.get("prerequis"));
            }));
            this.OK("Le champ de prerequis est Aucun pour un cours qui n'a pas de condition prerequis");
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("ZXY 2-Retourne 404 quand les donnes d'un cours sont manquants")
    void testComparerCoursDonneesManquants() {
        String id1 = "IFT1015";
        CourseDetails mockDetails1 = new CourseDetails(id1, "Programmation 1", "python", 3, (CourseDetails.AvailableTerms)null, Collections.emptyList());
        String id2 = "IFT1025";
        Mockito.when(this.mockContext.queryParam("id1")).thenReturn(id1);
        Mockito.when(this.mockContext.queryParam("id2")).thenReturn(id2);
        Mockito.when(this.mockService.getCourseDetails(id1)).thenReturn(Optional.of(mockDetails1));
        Mockito.when(this.mockService.getCourseDetails(id2)).thenReturn(Optional.empty());
        Mockito.when(this.mockContext.status(404)).thenReturn(this.mockContext);
        this.controller.compareCourses(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).status(404);
            this.OK("Status 404 set successfully.");
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }

    @Test
    @DisplayName("ZXY 3-L'ordre des cours est correct")
    public void testComparerCoursKeepOrder() {
        String id1 = "IFT1015";
        CourseDetails mockDetails1 = new CourseDetails(id1, "Programmation 1", "python", 3, (CourseDetails.AvailableTerms)null, Collections.emptyList());
        String id2 = "IFT1025";
        CourseDetails mockDetails2 = new CourseDetails(id2, "Programmation 2", "java", 3, (CourseDetails.AvailableTerms)null, Collections.emptyList());
        Mockito.when(this.mockContext.queryParam("id1")).thenReturn(id1);
        Mockito.when(this.mockContext.queryParam("id2")).thenReturn(id2);
        Mockito.when(this.mockService.getCourseDetails(id1)).thenReturn(Optional.of(mockDetails1));
        Mockito.when(this.mockService.getCourseDetails(id2)).thenReturn(Optional.of(mockDetails2));
        this.controller.compareCourses(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((obj) -> {
                Map<?, ?> map = (Map)obj;
                return id1.equals(map.get("id1")) && id2.equals(map.get("id2"));
            }));
            this.OK("Ordre des cours respecté");
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }

    // ZYW-1 - Recherche par mot-clé
    @Test
    @DisplayName("Recherche par mot-clé : doit retourner la liste des cours")
    void testGetCourseByMotCleValid() {
        // ARRANGE
        Map<String, List<String>> queryParams = new HashMap<>();
        queryParams.put("mot", Arrays.asList("programmation"));

        List<Course> mockCourses = Arrays.asList(
                new Course("IFT1015", "Prog I"),
                new Course("IFT1025", "Prog II"));

        when(mockContext.queryParamMap()).thenReturn(queryParams);
        when(mockService.getCoursesByMotCle(any())).thenReturn(mockCourses);

        // ACT
        controller.getCourseByMotCle(mockContext);

        // ASSERT
        try {
            verify(mockService).getCoursesByMotCle(argThat(map ->
                    map.containsKey("mot") &&
                            map.get("mot").equals("programmation")));
            OK("Service appelé avec le bon mot-clé", false);

            verify(mockContext).json(mockCourses);
            OK("Résultats retournés");
        } catch (AssertionError e) {
            Err(e.getMessage());
            throw e;
        }
    }

// ZYW-2 - Recherche : aucun mot-clé fourni


    @Test
    @DisplayName("Recherche par mot-clé : aucun paramètre → erreur 400")
    void testGetCourseByMotCleNoKeywords() {
        Mockito.when(this.mockContext.queryParamMap()).thenReturn(new HashMap());
        Mockito.when(this.mockContext.status(400)).thenReturn(this.mockContext);
        this.controller.getCourseByMotCle(this.mockContext);

        try {
            ((Context)Mockito.verify(this.mockContext)).status(400);
            this.OK("Status 400 envoyé", false);
            ((Context)Mockito.verify(this.mockContext)).json(Mockito.argThat((map) -> ((Map)map).containsKey("error")));
            this.OK("Message d’erreur retourné", false);
            ((CourseService)Mockito.verify(this.mockService, Mockito.never())).getCoursesByMotCle((Map)Mockito.any());
            this.OK("Service non appelé car aucun mot-clé fourni");
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }
// ZYW-3 - recherche sans résultat

    @Test
    @DisplayName("Recherche par mot-clé : aucun résultat")
    void testGetCourseByMotCleNoResults() {
        Map<String, List<String>> queryParams = new HashMap();
        queryParams.put("mot", Arrays.asList("xyz"));
        Mockito.when(this.mockContext.queryParamMap()).thenReturn(queryParams);
        Mockito.when(this.mockService.getCoursesByMotCle((Map)Mockito.any())).thenReturn(Collections.emptyList());
        this.controller.getCourseByMotCle(this.mockContext);

        try {
            ((CourseService)Mockito.verify(this.mockService)).getCoursesByMotCle((Map)Mockito.any());
            this.OK("Service appelé", false);
            ((Context)Mockito.verify(this.mockContext)).json(Collections.emptyList());
            this.OK("Retourne une liste vide");
        } catch (AssertionError e) {
            this.Err(e.getMessage());
            throw e;
        }
    }
    @AfterAll
    static void printFooter() {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("COMPLETED: CourseController Tests");
        System.out.println("=".repeat(80) + "\n");
    }

    private void printMessage(String message, boolean isOk, boolean isLast) {
        String symbol = isLast ? "└─" : "├─";
        String status = isOk ? "[PASS]" : "[FAIL]";
        System.out.println("    │   " + symbol + " " + status + " " + message);
    }

    private void OK(String message) {
        printMessage(message, true, true);
    }

    private void OK(String message, boolean isLast) {
        printMessage(message, true, isLast);
    }

    private void Err(String message) {
        printMessage(message, false, true);
    }

    private void Err(String message, boolean isLast) {
        printMessage(message, false, isLast);
    }
}
