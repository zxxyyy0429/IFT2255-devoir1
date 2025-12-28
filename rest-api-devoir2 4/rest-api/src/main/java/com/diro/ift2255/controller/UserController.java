package com.diro.ift2255.controller;

import java.util.List;
import java.util.Optional;

import com.diro.ift2255.model.User;
import com.diro.ift2255.service.UserService;
import com.diro.ift2255.util.ResponseUtil;
import com.diro.ift2255.util.ValidationUtil;

import io.javalin.http.Context;


/**
 * Contrôleur REST permettant de gérer les utilisateurs.
 *
 * <p>
 * Cette partie de l'application n'est pas obligatoire dans les exigences du projet,
 * mais elle permet d’illustrer un CRUD complet et constitue une base extensible
 * si un système d'authentification est ajouté plus tard.
 * </p>
 *
 * <h3>Endpoints exposés :</h3>
 * <ul>
 *     <li><strong>GET /users</strong> — récupérer tous les utilisateurs</li>
 *     <li><strong>GET /users/{id}</strong> — récupérer un utilisateur par ID</li>
 *     <li><strong>POST /users</strong> — créer un utilisateur</li>
 *     <li><strong>PUT /users/{id}</strong> — modifier un utilisateur</li>
 *     <li><strong>DELETE /users/{id}</strong> — supprimer un utilisateur</li>
 * </ul>
 *
 * <p>
 * Toutes les opérations reposent sur {@link UserService} qui gère la logique métier et la
 * structure de stockage interne (HashMap).
 * </p>
 */
public class UserController {
    // Service qui contient la logique métier pour la manipulation des utilisateurs (users) et la communication avec les services externes
    private final UserService service;

    /**
     * Constructeur du contrôleur.
     *
     * @param service service utilisateur injecté
     */
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Endpoint : récupérer la liste complète des utilisateurs.
     *
     * <p>URL : {@code GET /users}</p>
     *
     * <h3>Code de réponse :</h3>
     * <ul>
     *     <li>200 — liste retournée (peut être vide)</li>
     * </ul>
     *
     * @param ctx contexte HTTP
     */
    public void getAllUsers(Context ctx) {
        List<User> users = service.getAllUsers();
        ctx.json(users);
    }

    /**
     * Endpoint : récupérer un utilisateur par son ID.
     *
     * <p>URL : {@code GET /users/{id}}</p>
     *
     * @param ctx contexte HTTP
     */
    public void getUserById(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));

        Optional<User> user = service.getUserById(id);
        if (user.isPresent()) {
            ctx.json(user.get());
        } else {
            ctx.status(404).json(ResponseUtil.formatError("Aucun utilisateur ne correspond à l'ID: " + id));
        }
    }

    /**
     * Endpoint : créer un nouvel utilisateur.
     *
     * <p>URL : {@code POST /users}</p>
     *
     * <h3>Exemple de corps JSON :</h3>
     * <pre>
     * {
     *   "name": "Alice",
     *   "email": "alice@example.com"
     * }
     * </pre>
     *
     * <h3>Codes de réponse :</h3>
     * <ul>
     *     <li><strong>201</strong> — utilisateur créé</li>
     *     <li><strong>400</strong> — email invalide</li>
     * </ul>
     *
     * @param ctx contexte HTTP
     */
    public void createUser(Context ctx) {
        User user = ctx.bodyAsClass(User.class);
        if (!ValidationUtil.isEmail(user.getEmail())) {
            ctx.status(400).json("Invalid email format");
            return;
        }
        service.createUser(user);
        ctx.status(201).json(user);
    }

    /**
     * Endpoint : mettre à jour un utilisateur existant.
     *
     * <p>URL : {@code PUT /users/{id}}</p>
     *
     * <p>Si l'utilisateur n'existe pas déjà, il sera créé avec cet ID.</p>
     *
     * @param ctx contexte HTTP
     */
    public void updateUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        User updated = ctx.bodyAsClass(User.class);
        service.updateUser(id, updated);
        ctx.json(updated);
    }

    /**
     * Endpoint : supprimer un utilisateur.
     *
     * <p>URL : {@code DELETE /users/{id}}</p>
     *
     * <h3>Codes de réponse :</h3>
     * <ul>
     *     <li>204 — suppression réussie</li>
     * </ul>
     *
     * @param ctx contexte HTTP
     */
    public void deleteUser(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        service.deleteUser(id);
        ctx.status(204); // No Content
    }
}
