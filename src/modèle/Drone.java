package modèle;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Drone {


    Parcelle parcelle;// Parcelle où se trouve le drone
    Direction d; // Direction du drone
    Environnement evt; // Environnement sur lequel le drone est
    Circle circle; // Cercle représentant le drone

    // La dernière position connue de l'intrus est set à -1, pour indiquer qu'elle n'est pas encore connue
    public int lastPlayerPositionX = -1;
    public int lastPlayerPositionY = -1;
    // La dernière détection de l'intrus est set à 0 pour indiquer qu'elle n'est pas encore connue
    public long lastDetection = 0;
    public boolean estDesactive = false; // Un drone est actif par défaut
    public boolean isMega = false; // Un drone est non mega par défaut

    // Constructeur par défaut
    public Drone() {
    }

    // Constructeur du drone avec son environnement, sa position, et l'initialisation aléatoire de sa position
    public Drone(Environnement evt, int x, int y) {
        this.evt = evt;
        this.parcelle = evt.getGrille()[x][y];
        this.d = Direction.getRandom();
    }

    public Parcelle getParcelleDir(Direction dir) { // Retourne la parcelle suivant la direction du drone
        int x = Math.max(0, Math.min(evt.taille - 1, parcelle.x + dir.x));
        int y = Math.max(0, Math.min(evt.taille - 1, parcelle.y + dir.y));
        return evt.grille[x][y];
    }

    public Parcelle getParcelle() {
        return parcelle;
    }

    void errer() { // Fonction de déplacement du drone
    if (estDesactive == false) { // Le drone ne bouge que si il est actif, si désactivé alors il ne bouge plus
        Direction nouvelleDirection = d.nextDirection();
        Parcelle nouvelleParcelle = getParcelleDir(nouvelleDirection);

        // Si la nouvelle parcelle est un obstacle le drone bouge dans une autre direction
        if (nouvelleParcelle.getType().equals(TypeParcelle.Arbre)) {
            // Déplacement en fonction de la direction actuelle
            int newX = parcelle.x + d.x;
            int newY = parcelle.y + d.y;

            // On vérifie que les nouvelles coordonnées sont bien incluses dans la grille
            newX = Math.max(0, Math.min(evt.taille - 1, newX));
            newY = Math.max(0, Math.min(evt.taille - 1, newY));

            // Mise à jour de la position du drone
            parcelle = evt.grille[newX][newY];
            evt.bougerDrone(this);
            d = nouvelleDirection;
        } else {
            // Si la nouvelle parcelle n'est pas un obstacle, la position du drone est mise à jour
            if (lastPlayerPositionX == -1 && lastPlayerPositionY == -1) { // Aléatoirement, si la position de l'intrus n'est pas connue
                // Déplacement en fonction de la direction actuelle
                int newX = parcelle.x + d.x;
                int newY = parcelle.y + d.y;

                // On vérifie que les nouvelles coordonnées sont bien incluses dans la grille
                newX = Math.max(0, Math.min(evt.taille - 1, newX));
                newY = Math.max(0, Math.min(evt.taille - 1, newY));

                // Mise à jour de la position du drone
                parcelle = evt.grille[newX][newY];
                evt.bougerDrone(this);
                d = nouvelleDirection;
            } else { // Sinon, si l'intrus est détecté
                d = Direction.getDirectionTo(parcelle.x, parcelle.y, lastPlayerPositionX, lastPlayerPositionY); // On récupère la direction vers sa position
                if (getParcelleDir(d).getType().equals(TypeParcelle.Arbre)) { // Si la nouvelle direction est un obstacle
                    // Je n'ai pas réussi à réagir à cette situation, alors le drone ignore l'obstacle s'il connait la position de l'intrus
                } else { // Si ce n'est pas un obstacle
                    int newX = parcelle.x + d.x;
                    int newY = parcelle.y + d.y;
                    newX = Math.max(0, Math.min(evt.taille - 1, newX));
                    newY = Math.max(0, Math.min(evt.taille - 1, newY));

                    // Mise à jour de la position dans la bonne direction
                    parcelle = evt.grille[newX][newY];
                    evt.bougerDrone(this);
                    d = nouvelleDirection;
                }
            }
        }
    }

    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public void setLastPlayerPositionX(int x) {
        lastPlayerPositionX = x;
    }

    public void setLastPlayerPositionY(int y) {
        lastPlayerPositionY = y;
    }

    public void setLastDetection (long time) {
        lastDetection = time;
    }

    public long getLastDetection() {
        return lastDetection;
    }

    public boolean getEstDesactive() {
        return estDesactive;
    }

    public void setEstDesactive(boolean estDesactive) {
        this.estDesactive = estDesactive;
    }

    public int getLastPlayerPositionX() {
        return lastPlayerPositionX;
    }

    public int getLastPlayerPositionY() {
        return lastPlayerPositionY;
    }

    public boolean getMega() {
        return isMega;
    }

    public void setMega(boolean mega) {
        isMega = mega;
    }

    public Object getDirection() {
        return d;
    }

    public void setDirection(Object direction) {
        d = (Direction) direction;
    }




    public static int getDistance(Intrus intrus) { // Récupère la distance entre le drone est l'intrus
        return Math.abs(intrus.getPosition().getX()) + Math.abs(intrus.getPosition().getY());
    }



    public void setCouleur(Color couleur) { // Change la couleur du drone
        circle.setFill(couleur);
    }


}
