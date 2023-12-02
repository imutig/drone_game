package modèle;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Drone {

    /**
     * parcelle ou se trouve la fourmi
     */
    Parcelle parcelle;

    /**
     * direction actuelle de la fourmis
     */
    Direction d;

    /**
     * reference a l'environnement
     */
    Environnement evt;

    /**
     * sa representation graphique associee (simple cercle)
     */
    Circle circle;
    public int lastPlayerPositionX = -1;
    public int lastPlayerPositionY = -1;
    public long lastDetection = 0;
    public boolean isMega = false;

    /**
     * constructeur par defaut, inutilise
     */
    public Drone() {
    }

    /**
     * constructeur le lien vers l'environnement, la parcelle en x,y et une direction aleatoire
     * initialement la fourmi est vide et peut deposer une dose de 1 pheromone
     */
    public Drone(Environnement evt, int x, int y) {
        this.evt = evt;
        this.parcelle = evt.getGrille()[x][y];
        this.d = Direction.getRandom();
    }

    public Parcelle getParcelleDir(Direction dir) {
        int x = Math.max(0, Math.min(evt.taille - 1, parcelle.x + dir.x));
        int y = Math.max(0, Math.min(evt.taille - 1, parcelle.y + dir.y));
        return evt.grille[x][y];
    }

    public Parcelle getParcelle() {
        return parcelle;
    }

    void errer() {
        Direction nouvelleDirection = d.nextDirection();
        Parcelle nouvelleParcelle = getParcelleDir(nouvelleDirection);

        // Si la nouvelle parcelle est un obstacle le dront bouge dans une autre direction
        if (nouvelleParcelle.getType().equals(TypeParcelle.Arbre)) {
            // Déplacement en fonction de la direction actuelle
            int newX = parcelle.x + d.x;
            int newY = parcelle.y + d.y;

            // Assurez-vous que les nouvelles coordonnées sont valides
            newX = Math.max(0, Math.min(evt.taille - 1, newX));
            newY = Math.max(0, Math.min(evt.taille - 1, newY));

            // Mettez à jour la position du drone
            parcelle = evt.grille[newX][newY];
            evt.bougerDrone(this);
            d = nouvelleDirection;
        } else {
            // Si la nouvelle parcelle n'est pas un obstacle, mettre à jour la position du drone
            if (lastPlayerPositionX == -1 && lastPlayerPositionY == -1) {
                        // Déplacement en fonction de la direction actuelle
                        int newX = parcelle.x + d.x;
                        int newY = parcelle.y + d.y;

                        // Assurez-vous que les nouvelles coordonnées sont valides
                        newX = Math.max(0, Math.min(evt.taille - 1, newX));
                        newY = Math.max(0, Math.min(evt.taille - 1, newY));

                        // Mettez à jour la position du drone
                        parcelle = evt.grille[newX][newY];
                        evt.bougerDrone(this);
                        d = nouvelleDirection;
                    }
            else {
                d = Direction.getDirectionTo(parcelle.x, parcelle.y, lastPlayerPositionX, lastPlayerPositionY);
                // Déplacement en fonction de la direction actuelle
                if(getParcelleDir(d).getType().equals(TypeParcelle.Arbre)) {
                    return;
                } else {
                    int newX = parcelle.x + d.x;
                    int newY = parcelle.y + d.y;
                    // Assurez-vous que les nouvelles coordonnées sont valides
                    newX = Math.max(0, Math.min(evt.taille - 1, newX));
                    newY = Math.max(0, Math.min(evt.taille - 1, newY));

                    // Mettez à jour la position du drone
                    parcelle = evt.grille[newX][newY];
                    evt.bougerDrone(this);
                    d = nouvelleDirection;
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




    public static int getDistance(Intrus intrus) {
        return Math.abs(intrus.getPosition().getX()) + Math.abs(intrus.getPosition().getY());
    }



    public void setCouleur(Color couleur) {
        circle.setFill(couleur);
    }


}
