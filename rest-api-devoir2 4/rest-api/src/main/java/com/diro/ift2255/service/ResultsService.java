package com.diro.ift2255.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * Service responsable de charger et fournir les scores académiques agrégés d’un cours.
 *
 * <p>
 * Les données proviennent d’un fichier TSV (séparé par tabulations) contenant,
 * pour chaque cours :
 * </p>
 *
 * <pre>
 * sigle    nom    moyenne    score    participants    trimestres
 * </pre>
 *
 * <ul>
 *     <li><strong>sigle</strong> : identifiant du cours (IFT2255)</li>
 *     <li><strong>score</strong> : indice de réussite (1 = faible, 5 = fort)</li>
 * </ul>
 *
 * <p>
 * Ce service charge uniquement le score, qui est ensuite converti en estimation
 * de difficulté (inversée) via {@link #getAverageDifficulty(String)}.
 * </p>
 */
public class ResultsService {

    /** Table associant un sigle de cours à son score académique. */
    private final Map<String, Double> courseScores = new HashMap<>();

    /**
     * Construit le service et charge immédiatement le contenu du fichier TSV.
     *
     * @param csvStream flux d’entrée vers le fichier TSV (souvent dans /resources)
     */
    public ResultsService(InputStream csvStream) {
        loadCsv(csvStream);
    }

    /**
     * Charge le fichier TSV ligne par ligne et remplit la table {@code courseScores}.
     *
     * <p>
     * Le fichier est attendu au format tabulé. Exemple :
     * </p>
     * <pre>
     * IFT2255    Conception OO    B+    3.8    240    5
     * </pre>
     *
     * @param csvStream flux d’entrée vers le fichier TSV
     */
    private void loadCsv(InputStream csvStream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvStream))) {
            String line = br.readLine(); // En-tête ignorée

            while ((line = br.readLine()) != null) {
                String[] parts = line.split("\t"); 
                if (parts.length < 6) continue;

                String sigle = parts[0].trim();
                String scoreStr = parts[3].trim();

                try {
                    double score = Double.parseDouble(scoreStr);
                    courseScores.put(sigle, score);   

                } catch (NumberFormatException e) {
                    // Score invalide → on ignore simplement la ligne

                    continue;
                }
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Retourne une estimation de la difficulté du cours sur une échelle 0 → 5.
     *
     * <p>
     * Le fichier fournit un score (1 → 5) où :
     * <ul>
     *     <li>5 = cours où les étudiants réussissent très bien (peu difficile)</li>
     *     <li>1 = cours où les étudiants réussissent difficilement</li>
     * </ul>
     * </p>
     *
     * <p>
     * Pour convertir ce score en difficulté perçue, on utilise une inversion simple :
     * </p>
     * <pre>
     * difficulté = 5.0 - score
     * </pre>
     *
     * <p>Exemples :</p>
     * <pre>
     * score = 5   → difficulté = 0 (très facile)
     * score = 3   → difficulté = 2
     * score = 1   → difficulté = 4 (très difficile)
     * </pre>
     *
     * @param courseId sigle du cours (IFTxxxx)
     * @return difficulté estimée sur une échelle 0–5, ou 0 si aucune donnée
     */
    public double getAverageDifficulty(String courseId) {
        Double score = courseScores.get(courseId);
        if (score == null) return 0.0;
        return 5.0 - score;
    }
}

