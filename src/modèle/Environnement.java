package modèle;

import application.SimuDrones;

import java.awt.geom.Point2D;
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import modèle.Intrus;



public class Environnement {
    /**grille des parcelles de terrain*/
    Parcelle[][] grille;
    /** taille de la grille */
    public int taille;

    /**les fourmis presentes*/
    ArrayList<Drone> lesDrones;

    /**lien vers l'application graphiquue*/
    SimuDrones application;


    /**
     * constructeur initialisant l'application et la taille,
     * la grille et la liste des fourmis
     */
    public Environnement(SimuDrones application, int taille) {
        this.application = application;
        this.taille = taille;
        grille = new Parcelle[taille][taille];
        lesDrones = new ArrayList<>();
        init();
    }


    /**
     * remplit la grille de parcelles de type terrain
     */
    void init() {
        for (int i = 0; i < taille; i++)
            for (int j = 0; j < taille; j++) {
                if (Math.random() < 0.1) { // 5% de chance d'avoir un arbre dans une case
                    grille[i][j] = new Parcelle(grille, i, j, TypeParcelle.Arbre, false);
                } else {
                    grille[i][j] = new Parcelle(grille, i, j, TypeParcelle.Terrain, false);
                }
            }

        int xDonnee = (int) (Math.random() * taille);
        int yDonnee = (int) (Math.random() * taille);
        grille[xDonnee][yDonnee] = new Parcelle(grille, xDonnee, yDonnee, TypeParcelle.Donnee, true);

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