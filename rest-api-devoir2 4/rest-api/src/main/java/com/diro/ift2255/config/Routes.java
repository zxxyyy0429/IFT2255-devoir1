package com.diro.ift2255.config;

import java.io.InputStream;

import com.diro.ift2255.controller.ComparisonController;
import com.diro.ift2255.controller.CourseController;
import com.diro.ift2255.controller.CourseSetController;
import com.diro.ift2255.controller.ReviewController;
import com.diro.ift2255.controller.UserController;
import com.diro.ift2255.service.*;
import com.diro.ift2255.util.HttpClientApi;

import io.javalin.Javalin;

public class Routes {
    static ReviewCsvService reviewCsv = new ReviewCsvService("data/reviews.tsv"); // 或 data/reviews.csv
    static ReviewService reviewService = new ReviewService(reviewCsv);

    public static void register(Javalin app) {
        registerUserRoutes(app);
        registerCourseRoutes(app);
        registerReviewRoutes(app);
        registerCourseComparisonRoutes(app);
        registerCourseSetRoutes(app);
    }

    private static void registerUserRoutes(Javalin app) {
        UserService userService = new UserService();
        UserController userController = new UserController(userService);

        app.get("/users", userController::getAllUsers);
        app.get("/users/{id}", userController::getUserById);
        app.post("/users", userController::createUser);
        app.put("/users/{id}", userController::updateUser);
        app.delete("/users/{id}", userController::deleteUser);
    }

    private static void registerCourseRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseController courseController = new CourseController(courseService);

        app.get("/courses/semester", courseController::getCoursesBySemester);
        // http://localhost:7070/courses/semester?semester=H25 -- good

        app.get("/courses", courseController::getAllCourses);
        // http://localhost:7070/courses -- good

        app.get("/courses/search", courseController::getCourseByMotCle);
        

        app.get("/courses/compare", courseController::compareCourses);
        // http://localhost:7070/courses/compare?id1=IFT1025&id2=IFT2255 -- not done

        app.get("/courses/{id}", courseController::getCourseById);
        // http://localhost:7070/courses/IFT1005 -- good

        app.get("/courses/{id}/details", courseController::getCourseDetails);
        // http://localhost:7070/courses/IFT2255/details -- good

        app.get("/courses/search/sigle", courseController::searchByPartialSigle);
        // http://localhost:7070/courses/search?sigle=IFT -- good

        app.get("/courses/search/description", courseController::searchByDescription);
        // http://localhost:7070/courses/search/description?description=java -- good

        app.get("/programs/{programId}/courses", courseController::getCoursesByProgram);
        // http://localhost:7070/programs/117510/courses -- good

        app.get("/courses/{courseId}/schedule", courseController::getCourseSchedule);
        // http://localhost:7070/courses/IFT1015/schedule?semester=A25 -- good

        app.post("/courses/{id}/eligibility", courseController::checkEligibility);
        // http://localhost:7070/courses/IFT2255/eligibility
        // uses POST
        // exmaple in terminal
        // curl -X POST http://localhost:7070/courses/IFT2255/eligibility \
        // -H "Content-Type: application/json" \
        // -d '{"completedCourses":["IFT1005","IFT1025","IFT1215"],"cycle":"bachelor"}'

        // expected output:
        // {"courseId":"IFT2255","eligible":true}

        app.get("/courses/{sigle}/results", courseController::getCourseResults);
        // http://localhost:7070/courses/IFT2255/results -- good
    }

    private static void registerReviewRoutes(Javalin app) {
        //ReviewService reviewService = new ReviewService();
        ReviewController reviewController = new ReviewController(reviewService);

        // Route to submit a review for a course
        app.post("/courses/{id}/review", reviewController::submitReview);
        // review 1 ift2255, paste in terminal
        // curl -X POST http://localhost:7070/courses/IFT2255/review \
        //-H "Content-Type: application/json" \
        //-d '{"courseId":"IFT2255","studentName":"Alice","workload":3,"comment":"Bon cours"}'


        // Route to get all reviews for a course
        app.get("/courses/{courseId}/reviews", reviewController::getReviews);
        // http://localhost:7070/courses/IFT2255/reviews -- good

    }

    private static void registerCourseComparisonRoutes(Javalin app) {

        // Create the services needed
        //ReviewService reviewService = new ReviewService();

        // Load CSV from resources
        InputStream csvStream = ResultsService.class.getClassLoader().getResourceAsStream("data/resultats.csv");

        if (csvStream == null) {
            throw new RuntimeException("CSV file not found in classpath: data/resultats.csv");
        }

        ResultsService resultsService = new ResultsService(csvStream);
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseComparisonService comparisonService = new CourseComparisonService(reviewService, resultsService, courseService);
        ComparisonController comparisonController = new ComparisonController(comparisonService);

        app.get("/compare", comparisonController::compareCourses);
        // http://localhost:7070/compare?course1=IFT1025&course2=IFT2255 -- good

        // review 2 ift2255
        // curl -X POST http://localhost:7070/courses/IFT2255/review \
        // -H "Content-Type: application/json" \
        // -d '{"courseId": "IFT2255","studentName": "Charles","workload": 4,"comment": "Beaucoup de travaux d’équipe."}'

        // review 1 ift1025
        // curl -X POST http://localhost:7070/courses/IFT1025/review \
        //-H "Content-Type: application/json" \
        //-d '{"courseId": "IFT1025", "studentName": "Diana", "workload": 3, "comment": "Charge de travail modérée."}'

        // review 2 ift1025
        //curl -X POST http://localhost:7070/courses/IFT1025/review \
        //-H "Content-Type: application/json" \
        //-d '{"courseId": "IFT1025", "studentName": "Eric", "workload": 2, "comment": "Cours assez léger."}'

    }
    
    private static void registerCourseSetRoutes(Javalin app) {
        CourseService courseService = new CourseService(new HttpClientApi());
        CourseSetService courseSetService = new CourseSetService(courseService);
        CourseSetController courseSetController = new CourseSetController(courseSetService);

        
        app.post("/courseset/schedule", courseSetController::getSchedule);
        // http://localhost:7070/courseset/schedule

        //curl -X POST http://localhost:7070/courseset/schedule \
        //-H "Content-Type: application/json" \
        //-d '{"courseIds": ["IFT2255", "IFT2035"], "semester": "A24"}'

        app.get ("/courseset/schedule", courseSetController::getScheduleFromQuery);
        // http://localhost:7070/courseset/schedule?courses=IFT2255,IFT2035&semester=A24

        


    }


}

    

