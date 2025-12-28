package com.diro.ift2255.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import com.diro.ift2255.model.CourseResult;

/**
 * Service permettant de lire et d'extraire les statistiques académiques d'un cours
 * à partir du fichier CSV fourni dans le répertoire {@code /resources/data}.
 *
 * <p>Les données proviennent de {@code resultats.csv}, un fichier contenant
 * les informations suivantes pour chaque cours :</p>
 *
 * <pre>
 * sigle, nom, moyenne, score, participants, trimestres
 * </pre>
 *
 * <ul>
 *     <li><strong>sigle</strong> : identifiant du cours (IFT2255)</li>
 *     <li><strong>nom</strong> : nom complet du cours</li>
 *     <li><strong>moyenne</strong> : moyenne littérale (A+, A, A-, B+, ...)</li>
 *     <li><strong>score</strong> : score de réussite entre 1 et 5</li>
 *     <li><strong>participants</strong> : nombre d'étudiants</li>
 *     <li><strong>trimestres</strong> : nombre de trimestres d’enseignement</li>
 * </ul>
 *
 * <p>
 * Ce service est principalement utilisé pour :
 * <ul>
 *     <li>afficher les statistiques d’un cours</li>
 *     <li>alimenter la comparaison entre deux cours</li>
 * </ul>
 * </p>
 */
public class CourseResultService {
    /** Chemin vers le fichier CSV contenant les résultats académiques. */
    private static final String CSV_PATH = "/data/resultats.csv";

    /**
     * Recherche les résultats académiques correspondant à un sigle donné.
     *
     * <p>Lecture du fichier CSV ligne par ligne. Dès qu’un sigle correspond
     * (comparaison insensible à la casse), un {@link CourseResult} est construit
     * et retourné.</p>
     *
     * @param sigle le code du cours recherché (ex. "IFT2255")
     * @return un {@link Optional} contenant les résultats si trouvés,
     *         sinon {@code Optional.empty()}
     *
     * @throws RuntimeException si le fichier {@code resultats.csv} est introuvable
     */
    public Optional<CourseResult> getResultsBySigle(String sigle) {

        InputStream is = getClass().getResourceAsStream(CSV_PATH);

        if (is == null) {
            throw new RuntimeException("Fichier resultats.csv introuvable dans resources");
        }

        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(is, StandardCharsets.UTF_8))) {

            // La première ligne contient souvent les en-têtes → on l’ignore
            String line = br.readLine(); 

            while ((line = br.readLine()) != null) {

                String[] cols = line.split(",");

                // Nettoyage du sigle (certains CSV contiennent des espaces insécables)
                String csvSigle = cols[0].trim().replace("\u00A0", "");
                String requestSigle = sigle.trim();

                if (csvSigle.equalsIgnoreCase(requestSigle)) {

                    CourseResult result = new CourseResult();
                    result.setSigle(cols[0]);
                    result.setNom(cols[1]);
                    result.setMoyenne(cols[2]);
                    result.setScore(Double.parseDouble(cols[3]));
                    result.setParticipants(Integer.parseInt(cols[4]));
                    result.setTrimestres(Integer.parseInt(cols[5]));

                    return Optional.of(result);
                }
            }

        } catch (Exception e) {
            // L’erreur est affichée pour faciliter le debug, mais on ne remonte pas d’exception
            e.printStackTrace();
        }

        return Optional.empty();
    }
}
