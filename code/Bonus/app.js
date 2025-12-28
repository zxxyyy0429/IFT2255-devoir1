
// Descriptions de chaque cours sont pris du site d'admission UdeM.

const coursData = [
    {
        id: 1,
        code: "IFT2255",
        titre: "Génie logiciel",
        credits: 3,
        description: "Introduction au génie logiciel. Cycles de développement. Analyse, modélisation et spécification. Conception. Développement orienté objet. Mise au point. Outils et environnements de développement.",
        difficulte: 5,
        charge_travail: 6,
        moyenne_generale: 3.0,
        taux_reussite: 75,
    },
    {
        id: 2,
        code: "IFT1015",
        titre: "Programmation 1",
        credits: 3,
        description: "Éléments de base d'un langage de programmation : types, expressions, énoncés conditionnels et itératifs, procédures, fonctions, paramètres, récursivité, tableaux, enregistrements, pointeurs et fichiers.",
        prerequis: "Aucun",
        difficulte: 4,
        charge_travail: 6,
        moyenne_generale: 2.7,
        taux_reussite: 71,
    },
    {
        id: 3,
        code: "IFT1065",
        titre: "Mathématiques discrètes",
        credits: 3,
        description: "Éléments de logique propositionnelle. Ensembles. Suites et fonctions. Algorithmes. Matrices booléennes. Raisonnement mathématique. Induction. Combinatoire. Relations de récurrence. Graphes, Arbres.",
        prerequis: "Aucun",
        difficulte: 9,
        charge_travail: 7,
        moyenne_generale: 2.2,
        taux_reussite: 65,
    },
    {
        id: 4,
        code: "MAT1400",
        titre: "Calcul 1",
        credits: 4,
        description: "Suites, séries. Fonctions de plusieurs variables, continuité, dérivées partielles, différentielles, plan tangent, dérivation en chaîne. Gradient, surfaces de niveau, extremums. Intégrales multiples, changement de variables, jacobien.",
        prerequis: "Aucun",
        difficulte: 7,
        charge_travail: 8,
        moyenne_generale: 2.0,
        taux_reussite: 63,
    },
    {
        id: 5,
        code: "IFT1215",
        titre: "Introduction aux système informatiques",
        credits: 3,
        description: "Composantes d'un ordinateur. Codage des données et des instructions. Langages machine et de haut niveau. Concepts et utilisation d'un système d'exploitation. Introduction à l'Internet. Conséquences sociales de l'informatique.",
        prerequis: "Aucun",
        difficulte: 6,
        charge_travail: 5,
        moyenne_generale: 3.0,
        taux_reussite: 74,


    }
];

let comparisonCart = [];

// Fonction de recherche
function searchCourses() {
    
    const searchInput = document.getElementById('searchInput');
    if (!searchInput) {
        return;
    }
    
    const searchTerm = searchInput.value.toLowerCase();
    
    const results = coursData.filter(cours => {
        const matchCode = cours.code.toLowerCase().includes(searchTerm);
        const matchTitre = cours.titre.toLowerCase().includes(searchTerm);
        const matchDescription = cours.description.toLowerCase().includes(searchTerm);
        
        return matchCode || matchTitre || matchDescription;
    });
    
    displaySearchResults(results);
}

function displaySearchResults(results) {
    const container = document.getElementById('searchResults');
    if (!container) {
        return;
    }
    
    if (results.length === 0) {
        container.innerHTML = '<p class="no-results"> Aucun cours trouvé.</p>';
        return;
    }
    
    container.innerHTML = results.map(cours => `
        <div class="course-card" style="border: 1px solid #ddd; padding: 15px; margin: 10px 0; border-radius: 8px;">
            <div style="display: flex; justify-content: space-between;">
                <div class="course-code" style="font-weight: bold; color: #2c3e50;">${cours.code}</div>
                <div class="course-credits" style="color: #7f8c8d;">${cours.credits} crédits</div>
            </div>
            <h3 style="color: #34495e; margin: 10px 0;">${cours.titre}</h3>
            <p style="color: #666; margin-bottom: 15px;">${cours.description}</p>
            
            <div style="display: grid; grid-template-columns: 1fr 1fr 1fr; gap: 10px; margin: 15px 0;">
                <div style="text-align: center; padding: 8px; background: #f8f9fa; border-radius: 6px;">
                    <div style="font-weight: bold;">${cours.difficulte}/10</div>
                    <div style="font-size: 0.8em; color: #7f8c8d;">Difficulté</div>
                </div>
                <div style="text-align: center; padding: 8px; background: #f8f9fa; border-radius: 6px;">
                    <div style="font-weight: bold;">${cours.charge_travail}/10</div>
                    <div style="font-size: 0.8em; color: #7f8c8d;">Charge</div>
                </div>
                <div style="text-align: center; padding: 8px; background: #f8f9fa; border-radius: 6px;">
                    <div style="font-weight: bold;">${cours.moyenne_generale}/4.3</div>
                    <div style="font-size: 0.8em; color: #7f8c8d;">Moyenne</div>
                </div>
            </div>
            
            <button onclick="addToComparison(${cours.id})" style="padding: 8px 15px; background: #3498db; color: white; border: none; border-radius: 6px; cursor: pointer;">
                + Ajouter à la comparaison
            </button>
        </div>
    `).join('');
}

function addToComparison(courseId) {
    const course = coursData.find(c => c.id === courseId);
    
    if (!comparisonCart.find(c => c.id === courseId)) {
        comparisonCart.push(course);
        updateComparisonCart();
        alert(`${course.code} ajouté à la comparaison`);
    } else {
        alert('Ce cours est déjà dans la comparaison');
    }
}

function updateComparisonCart() {
    const cart = document.getElementById('comparisonCart');
    const btn = document.querySelector('.compare-btn');
    
    if (!cart || !btn) {
        return;
    }
    
    if (comparisonCart.length === 0) {
        cart.innerHTML = '<p>Aucun cours sélectionné pour comparaison</p>';
        btn.disabled = true;
    } else {
        cart.innerHTML = `
            <h4>Cours sélectionnés (${comparisonCart.length}/5):</h4>
            <div style="display: flex; flex-direction: column; gap: 5px;">
                ${comparisonCart.map(c => `
                    <div style="display: flex; justify-content: space-between; align-items: center; padding: 5px 10px; background: white; border: 1px solid #ddd; border-radius: 4px;">
                        <span><strong>${c.code}</strong> - ${c.titre}</span>
                        <button onclick="removeFromComparison(${c.id})" style="background: none; border: none; cursor: pointer; color: red;"></button>
                    </div>
                `).join('')}
            </div>
        `;
        btn.disabled = comparisonCart.length < 2;
    }
}

function removeFromComparison(courseId) {
    comparisonCart = comparisonCart.filter(c => c.id !== courseId);
    updateComparisonCart();
}

function viewComparison() {
    if (comparisonCart.length < 2) {
        alert("Sélectionnez au moins 2 cours pour comparer");
        return;
    }
    
    const container = document.getElementById('searchResults');
    container.innerHTML = `
        <div style="background: #f8f9fa; padding: 20px; border-radius: 8px;">
            <h2>Comparaison de ${comparisonCart.length} cours</h2>
            <table style="width: 100%; border-collapse: collapse; margin: 20px 0;">
                <thead>
                    <tr>
                        <th style="padding: 12px; border: 1px solid #ddd; background: #34495e; color: white;">Critères</th>
                        ${comparisonCart.map(cours => `
                            <th style="padding: 12px; border: 1px solid #ddd; background: #34495e; color: white;">${cours.code}</th>
                        `).join('')}
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td style="padding: 12px; border: 1px solid #ddd;"><strong>Titre</strong></td>
                        ${comparisonCart.map(cours => `
                            <td style="padding: 12px; border: 1px solid #ddd;">${cours.titre}</td>
                        `).join('')}
                    </tr>
                    <tr>
                        <td style="padding: 12px; border: 1px solid #ddd;"><strong>Crédits</strong></td>
                        ${comparisonCart.map(cours => `
                            <td style="padding: 12px; border: 1px solid #ddd;">${cours.credits}</td>
                        `).join('')}
                    </tr>
                    <tr>
                        <td style="padding: 12px; border: 1px solid #ddd;"><strong>Difficulté</strong></td>
                        ${comparisonCart.map(cours => `
                            <td style="padding: 12px; border: 1px solid #ddd;">${cours.difficulte}/10</td>
                        `).join('')}
                    </tr>
                    <tr>
                        <td style="padding: 12px; border: 1px solid #ddd;"><strong>Charge travail</strong></td>
                        ${comparisonCart.map(cours => `
                            <td style="padding: 12px; border: 1px solid #ddd;">${cours.charge_travail}/10</td>
                        `).join('')}
                    </tr>
                </tbody>
            </table>
            <button onclick="location.reload()" style="padding: 10px 20px; background: #3498db; color: white; border: none; border-radius: 6px; cursor: pointer;">
                Nouvelle recherche
            </button>
        </div>
    `;
}

// Initialisation au chargement
document.addEventListener('DOMContentLoaded', function() {
    
    // Affiche tous les cours au départ
    displaySearchResults(coursData);
    
    // Recherche avec la touche Enter
    const searchInput = document.getElementById('searchInput');
    if (searchInput) {
        searchInput.addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchCourses();
            }
        });
    }
});