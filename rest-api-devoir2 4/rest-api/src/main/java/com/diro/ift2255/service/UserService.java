package com.diro.ift2255.service;

import com.diro.ift2255.model.User;
import java.util.*;

/**
 * Service responsable de la gestion des utilisateurs.
 *
 * <p>
 * Ce service fournit un stockage en mémoire (mock) permettant de :
 * </p>
 * <ul>
 *     <li>créer un utilisateur</li>
 *     <li>récupérer la liste des utilisateurs</li>
 *     <li>consulter un utilisateur par identifiant</li>
 *     <li>modifier un utilisateur</li>
 <li>supprimer un utilisateur</li>
 * </ul>
 *
 * <p>
 * Bien que le projet n’exige pas la gestion de comptes utilisateurs,
 * ce service peut être utilisé pour :
 * </p>
 * <ul>
 *     <li>tester une fonctionnalité de personnalisation</li>
 *     <li>supporter un futur système d’authentification</li>
 *     <li>associer des préférences d’affichage à un utilisateur</li>
 * </ul>
 *
 * <p>
 * Le stockage est volontairement simple : une table en mémoire simulant un
 * mini-database. Aucun fichier ni base externe n'est utilisé ici.
 * </p>
 */
public class UserService {
    /** Map interne de stockage des utilisateurs (id → user). */
    private final Map<Integer, User> users = new HashMap<>();

    /** Prochain identifiant disponible pour un nouvel utilisateur. */
    private int nextId = 1;

    /**
     * Constructeur initialisant deux utilisateurs fictifs
     * afin d'avoir un dataset minimal pour les tests.
     */
    public UserService() {
        // Mock users
        users.put(nextId, new User(nextId++, "Alice", "alice@example.com"));
        users.put(nextId, new User(nextId++, "Bob", "bob@example.com"));
    }

    /**
     * Retourne la liste complète des utilisateurs.
     *
     * @return une nouvelle liste contenant tous les utilisateurs
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    /**
     * Recherche un utilisateur selon son identifiant.
     *
     * @param id identifiant de l'utilisateur
     * @return un {@link Optional} contenant l’utilisateur s’il existe,
     *         sinon {@code Optional.empty()}
     */
    public Optional<User> getUserById(int id) {
        try {
            User user = users.get(id);
            return Optional.of(user);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Crée un nouvel utilisateur dans le stockage.
     * <p>
     * L'identifiant est automatiquement généré.
     * </p>
     *
     * @param user l’utilisateur à enregistrer
     */
    public void createUser(User user) {
        user.setId(nextId++);
        users.put(user.getId(), user);
    }

    /**
     * Met à jour un utilisateur existant.
     *
     * @param id      identifiant de l’utilisateur à mettre à jour
     * @param updated données révisées de l’utilisateur
     */
    public void updateUser(int id, User updated) {
        updated.setId(id);
        users.put(id, updated);
    }

    /**
     * Supprime un utilisateur selon son identifiant.
     *
     * @param id identifiant de l’utilisateur à supprimer
     */
    public void deleteUser(int id) {
        users.remove(id);
    }
}
