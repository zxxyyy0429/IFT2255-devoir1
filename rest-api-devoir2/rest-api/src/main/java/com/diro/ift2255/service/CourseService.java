package com.diro.ift2255.service;

import com.diro.ift2255.model.Course;
import com.diro.ift2255.model.CourseDetails;
import com.diro.ift2255.util.HttpClientApi;
import com.fasterxml.jackson.core.type.TypeReference;
import java.net.URI;
import java.util.*;

public class CourseService {
    private final HttpClientApi clientApi;
    private static final String BASE_URL= "https://planifium-api.onrender.com/api/v1/courses";

    public CourseService(HttpClientApi clientApi) {
        this.clientApi = clientApi;
    }

    /**
     * Fetch all courses
     */
    public List<Course> getAllCourses(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;

        URI uri = HttpClientApi.buildUri(BASE_URL, params);
        List<Course> courses = clientApi.get(uri, new TypeReference<List<Course>>() {
        });

        return courses;
    }

    /**
     * Fetch a course by ID
     */
    public Optional<Course> getCourseById(String courseId) {
        return getCourseById(courseId, null);
    }

    /**
     * Fetch a course by ID with optional query params
     */
    public Optional<Course> getCourseById(String courseId, Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(BASE_URL+ "/" + courseId, params);

        try {
            Course course = clientApi.get(uri, Course.class);
            return Optional.of(course);
        } catch (RuntimeException e) {
            return Optional.empty();
        }
    }

    /**
     * Recherche des cours selon un mot-clé
     */
    public List<Course> getCoursesByMotCle(Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(BASE_URL, params);

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

    public Optional<CourseDetails> getCourseDetails(String courseId) {
        return getCourseDetails(courseId, null);
    }

    public Optional<CourseDetails> getCourseDetails(String id, Map<String, String> queryParams) {
        Map<String, String> params = (queryParams == null) ? Collections.emptyMap() : queryParams;
        URI uri = HttpClientApi.buildUri(BASE_URL + "/" + id, params);

        try {
            CourseDetails courseDetails = clientApi.get(uri, CourseDetails.class);
            return Optional.of(courseDetails);
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Optional.empty();
        }
}}