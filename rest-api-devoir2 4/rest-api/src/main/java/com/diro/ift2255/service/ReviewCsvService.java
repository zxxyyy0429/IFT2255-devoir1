package com.diro.ift2255.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import com.diro.ift2255.model.Review;

/**
 * Service responsable de la persistance des avis étudiants dans un fichier TSV.
 *
 * <p>
 * Ce service :
 * </p>
 * <ul>
 *     <li>crée automatiquement le fichier et son répertoire au besoin ;</li>
 *     <li>charge en mémoire (cache) tous les avis existants au démarrage ;</li>
 *     <li>permet d'ajouter un nouvel avis (append dans le fichier + cache) ;</li>
 *     <li>permet de récupérer les avis et la charge de travail moyenne par cours.</li>
 * </ul>
 *
 * <p>
 * Le fichier est au format TSV (tabulation comme séparateur) avec les colonnes :
 * </p>
 * <pre>
 * courseId    studentName    workload    comment
 * </pre>
 *
 * <p>
 * Ce service est typiquement utilisé par le contrôleur REST et/ou le bot Discord
 * pour stocker les avis soumis par les étudiants.
 * </p>
 */
public class ReviewCsvService {
    /** Séparateur utilisé dans le fichier (tabulation). */
    private static String SEPARATOR = "\t";
    /** Ligne d'entête du fichier TSV. */
    private static String HEADER = String.join(SEPARATOR, "courseId", "studentName", "workload", "comment");
    /** Chemin vers le fichier de stockage des avis. */
    private Path filePath;
    /**
     * Cache en mémoire des avis déjà chargés.
     * Tous les accès de lecture se font sur ce cache.
     */
    private List<Review> cache = new ArrayList<>();

    /**
     * Construit le service et initialise le fichier de stockage.
     * <p>
     * Cette méthode :
     * </p>
     * <ul>
     *     <li>crée le répertoire et le fichier s'ils n'existent pas ;</li>
     *     <li>écrit l'en-tête si le fichier est vide ;</li>
     *     <li>charge tous les avis existants dans le cache.</li>
     * </ul>
     *
     * @param filePath chemin vers le fichier TSV des avis (ex. "data/avis.tsv")
     */
    public ReviewCsvService(String filePath) {
        this.filePath = Paths.get(filePath);
        initFile();
        loadFile();
    }

    /**
     * Initialise le fichier de stockage :
     * <ul>
     *     <li>crée le répertoire parent si nécessaire ;</li>
     *     <li>crée le fichier s'il n'existe pas ;</li>
     *     <li>écrit l'en-tête si le fichier est vide.</li>
     * </ul>
     */
    private void initFile() {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            if (Files.notExists(filePath)) {
                Files.createFile(filePath);
            }

            if (Files.size(filePath) == 0) {
                try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
                    bw.write(HEADER);
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Charge le contenu du fichier TSV en mémoire dans le cache.
     *
     * <p>Les lignes invalides ou incomplètes sont ignorées. On vérifie notamment :</p>
     * <ul>
     *     <li>présence des 4 colonnes</li>
     *     <li>courseId et studentName non vides</li>
     *     <li>workload compris entre 1 et 5</li>
     * </ul>
     */
    private void loadFile() {
        cache.clear();

        try (BufferedReader br = Files.newBufferedReader(filePath, StandardCharsets.UTF_8)) {
            String line = br.readLine();

            while ((line = br.readLine()) != null) {
                if (line.isBlank()){
                    continue;
                }

                String[] parts = line.split(SEPARATOR, -1);
                if (parts.length < 4){
                    continue;
                }

                String courseId = parts[0].trim();
                String studentName = parts[1].trim();
                int workload = safeParseInt(parts[2].trim(), 0);
                String comment = unescape(parts[3]);

                if (courseId.isEmpty() || studentName.isEmpty()){
                    continue;
                }
                if (workload < 1 || workload > 5){
                    continue;
                }

                cache.add(new Review(courseId, studentName, workload, comment));
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Ajoute un nouvel avis dans le fichier et dans le cache.
     *
     * <p>
     * Le commentaire est d'abord "échappé" (tabulations et sauts de ligne
     * remplacés par des séquences sécurisées), puis la ligne est ajoutée au
     * fichier TSV. Ensuite, un objet {@link Review} équivalent est ajouté au cache.
     * </p>
     *
     * <p>
     * Cette méthode est {@code synchronized} pour éviter des écritures
     * concurrentes sur le fichier et le cache.
     * </p>
     *
     * @param review avis à persister
     * @return l'avis tel qu'il a été reçu
     */
    public synchronized Review submitReview(Review review) {
        String safeComment = escape(review.getComment());

        String line = String.join(SEPARATOR,
                nullToEmpty(review.getCourseId()).trim(),
                nullToEmpty(review.getStudentName()).trim(),
                Integer.toString(review.getWorkload()),
                safeComment
        );

        try (BufferedWriter bw = Files.newBufferedWriter(filePath, StandardCharsets.UTF_8, StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        cache.add(new Review(review.getCourseId(), review.getStudentName(), review.getWorkload(), review.getComment()
        ));

        return review;
    }

    /**
     * Retourne la liste des avis associés à un cours donné.
     *
     * <p>
     * La recherche se fait dans le cache en mémoire, et non en relisant le fichier.
     * </p>
     *
     * @param courseId sigle du cours (IFTxxxx)
     * @return liste des avis (copie des objets, pas les instances du cache)
     */
    public List<Review> getReviewsByCourse(String courseId) {
        String id = courseId == null ? "" : courseId.trim();

        List<Review> avis = new ArrayList<>();
        for (Review review : cache) {
            if (review.getCourseId().equals(id)) {
                avis.add(new Review(review.getCourseId(), review.getStudentName(), review.getWorkload(), review.getComment()));
            }
        }
        return avis;
    }

    /**
     * Calcule la charge de travail moyenne (workload) pour un cours donné.
     *
     * <p>
     * Si aucun avis n'est disponible pour ce cours, la valeur retournée est 0.0.
     * </p>
     *
     * @param courseId sigle du cours
     * @return charge de travail moyenne (entre 1.0 et 5.0), ou 0.0 si aucun avis
     */
    public double getAverageWorkload(String courseId) {
        String id = courseId == null ? "" : courseId.trim();

        long sum = 0;
        long count = 0;

        for (Review review : cache) {
            if (review.getCourseId().equals(id)) {
                sum += review.getWorkload();
                count++;
            }
        }
        if (count == 0) return 0.0;
        return sum * 1.0 / count;
    }

    /**
     * Remplace une valeur {@code null} par une chaîne vide.
     *
     * @param s chaîne potentiellement null
     * @return chaîne non null (éventuellement vide)
     */
    private static String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    /**
     * Parse un entier de manière sécurisée.
     *
     * @param s        chaîne à parser
     * @param fallback valeur de repli en cas d'erreur
     * @return entier parsé ou valeur de repli
     */
    private static int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s);
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Échappe les caractères spéciaux (tabulations, retours à la ligne, backslash)
     * pour pouvoir stocker le commentaire en une seule colonne TSV.
     *
     * @param s texte original
     * @return texte échappé
     */
    private static String escape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\", "\\\\")
                .replace("\t", "\\t")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
    }

    /**
     * Opération inverse de {@link #escape(String)} :
     * convertit les séquences échappées en caractères normaux.
     *
     * @param s texte échappé
     * @return texte original (ou chaîne vide si null)
     */
    private static String unescape(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\\n", "\n")
                .replace("\\r", "\r")
                .replace("\\t", "\t")
                .replace("\\\\", "\\");
    }
}

