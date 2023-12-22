package modèle;

import application.SimuDrones;

import java.awt.geom.Point2D;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import modèle.Intrus;



public class Environnement {
    Parcelle[][] grille;
    public int taille; // Taille de la grille

    ArrayList<Drone> lesDrones; // ArrayList des drones présents

    SimuDrones application; // Lien vers l'application graphique

    // Constructeur initialisant l'application, la taille de la grille, la grille ainsi que les drones
    public Environnement(SimuDrones application, int taille) {
        this.application = application;
        this.taille = taille;
        grille = new Parcelle[taille][taille];
        lesDrones = new ArrayList<>();
        init();
    }

    void init() { // Associe à chaque case un type de terrain en fonction de règles bien spécifique
        for (int i = 0; i < taille; i++)
            for (int j = 0; j < taille; j++) {
                if (Math.random() < 0.1) { // 5% de chance d'avoir un arbre dans une case
                    grille[i][j] = new Parcelle(grille, i, j, TypeParcelle.Arbre, false);
                } else {
                    grille[i][j] = new Parcelle(grille, i, j, TypeParcelle.Terrain, false);
                }
            }

        int xDonnee = (int) (Math.random() * taille); // Un X aléatoire est associé à la donnée
        int yDonnee = (int) (Math.random() * taille); // Un Y aléatoire est associé à la donnée
        grille[xDonnee][yDonnee] = new Parcelle(grille, xDonnee, yDonnee, TypeParcelle.Donnee, true); // La parcelle aléatoire est update : sa variable "contientDonnee" devient true et son type devient donnee

        grille[0][taille / 2].setType(TypeParcelle.Sortie); // Sortie à gauche
        grille[taille - 1][taille / 2].setType(TypeParcelle.Sortie); // Sortie à droite
        grille[taille / 2][0].setType(TypeParcelle.Sortie); // Sortie en haut
        grille[taille / 2][taille - 1].setType(TypeParcelle.Sortie); // Sortie en bas
    }

    /**demande au cerle lie a la fourmi de se deplacer dans le point identifie par la parcelle
     * @param d fourmi qui se deplace*/

    public void bougerDrone(Drone d)
    {
        application.move(d.circle, d.parcelle.x, d.parcelle.y);
    }

    public void bougerIntrus(Intrus d)
    {
        application.move(d.circle, d.position.x, d.position.y);
    }

    public void avancer() {
        lesDrones.forEach(Drone::errer);
    }

    public Drone addDrone(int x, int y)
    {
        Drone d = new Drone(this, x, y);
        lesDrones.add(d);
        return d;
    }

    /**
     * @return la grille des parcelles
     */
    public Parcelle[][] getGrille() {
        return grille;
    }


    public int getTaille() {
        return taille;
    }
}