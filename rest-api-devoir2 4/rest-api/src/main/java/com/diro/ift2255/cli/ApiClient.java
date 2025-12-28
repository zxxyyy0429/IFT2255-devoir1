package com.diro.ift2255.cli;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ApiClient {
    private static String BASE_URL = "http://localhost:7070";

    private static HttpClient CLIENT = HttpClient.newHttpClient();

    public static String get(String path) {
        try {
            HttpRequest requete = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).GET().build();

            HttpResponse<String> reponse = CLIENT.send(requete, HttpResponse.BodyHandlers.ofString());

            return reponse.body();

        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String post(String path, String body) {
        try {
            HttpRequest requete = HttpRequest.newBuilder().uri(URI.create(BASE_URL + path)).header("Content-Type", "application/json").POST(HttpRequest.BodyPublishers.ofString(body)).build();

            HttpResponse<String> response = CLIENT.send(requete, HttpResponse.BodyHandlers.ofString());

            return response.body();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    public static String encode(String value) {
            return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}