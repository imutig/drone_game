package modèle;

import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class Intrus {
    static final public Color coulPleine = Color.color(1, 0, 0);
    Parcelle position;
    Environnement environnement;
    Circle circle;
    private boolean donneeRecuperee = false;
    private boolean surSortie = false;
    private boolean hasLost = false;
    private boolean hasWon = false;
    private long tempsDeDetection = 0;

    private long derniereDetection = 0;
    public Circle champVision; // Champ de vision
    private boolean[][] memoire;
    private long[][] memoireOubli;



    public Intrus(Environnement environnement, int x, int y) {
        this.environnement = environnement;
        this.position = environnement.getGrille()[x][y];
        memoire = new boolean[environnement.getTaille()][environnement.getTaille()];
        memoireOubli = new long[environnement.getTaille()][environnement.getTaille()];
    }
    public boolean[][] getMemoire() {
        return memoire;
    }

    public long[][] getMemoireOubli() {
        return memoireOubli;
    }

    public Parcelle getPosition() {
        return position;
    }

    public Circle getCircle() {
        return circle;
    }

    public void setCircle(Circle circle) {
        this.circle = circle;
    }

    public boolean getHasLost() {
        return hasLost;
    }

    public boolean getHasWon() {
        return hasWon;
    }

    public void setHasWon(boolean hasWon) {
        this.hasWon = hasWon;
    }
    public boolean isDonneeRecuperee() {
        return donneeRecuperee;
    }

    public void setTempsDeDetection(long tempsDeDetection) {
        this.tempsDeDetection = tempsDeDetection;
    }

    public long getTempsDeDetection() {
        return tempsDeDetection;
    }

    public long getDerniereDetection() {
        return derniereDetection;
    }

    public void setDerniereDetection(long time) {
        derniereDetection = time;
    }
    public boolean hasLost() { return hasLost; }

    public void setHasLost(boolean hasLost) { this.hasLost = hasLost; }

    public boolean surSortie() { return surSortie;}

    public void setCouleur(Color couleur) {
        circle.setFill(couleur);
    }
    public void setDonneeRecuperee(boolean donneeRecuperee) {
        this.donneeRecuperee = donneeRecuperee;
    }

    public void déplacer(KeyCode touche) {
        Parcelle nouvellePosition = position; // Initialise avec la position actuelle

        // Déplace l'intrus en fonction de la touche
        switch (touche) {
            case UP:
                nouvellePosition = getParcelleDir(Direction.N);
                break;
            case DOWN:
                nouvellePosition = getParcelleDir(Direction.S);
                break;
            case LEFT:
                nouvellePosition = getParcelleDir(Direction.O);
                break;
            case RIGHT:
                nouvellePosition = getParcelleDir(Direction.E);
                break;
            case A:
                nouvellePosition = getParcelleDir(Direction.NO);
                break;
            case E:
                nouvellePosition = getParcelleDir(Direction.NE);
                break;
            case Q:
                nouvellePosition = getParcelleDir(Direction.SO);
                break;
            case D:
                nouvellePosition = getParcelleDir(Direction.SE);
                break;
        }

        // Vérifie si la nouvelle position est valide (par exemple, pas un obstacle)
        if (nouvellePosition.getType().equals(TypeParcelle.Arbre)) {
            return;
        }

        if (!nouvellePosition.getType().equals(TypeParcelle.Arbre)) {
            // Déplace l'intrus uniquement si la nouvelle position est valide
            position = nouvellePosition;
            environnement.bougerIntrus(this);
        }
        if (nouvellePosition.getType() == TypeParcelle.Donnee) {
            // L'intrus marche sur la case bleue, le type de parcelle devient Terrain
            nouvellePosition.setType(TypeParcelle.Terrain);
            position = nouvellePosition;
            System.out.println("Donnée prise");
            setDonneeRecuperee(true);
        } else {
            position = nouvellePosition;
        }

        if (nouvellePosition.getType() == TypeParcelle.Sortie) {
            System.out.println("Intrus sur une sortie !\n");
            surSortie = true;
            if (isDonneeRecuperee()) {
                System.out.print("Victoire\n");
            } else {
                System.out.print("Sortie impossible\n");
            }
        } else {
            position = nouvellePosition;
        }
    }

    private Parcelle getParcelleDir(Direction dir) {
        int x = Math.max(0, Math.min(environnement.taille - 1, position.x + dir.x));
        int y = Math.max(0, Math.min(environnement.taille - 1, position.y + dir.y));
        return environnement.getGrille()[x][y];
    }



}



