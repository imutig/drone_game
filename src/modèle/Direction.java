package modèle;

import javax.xml.stream.events.NotationDeclaration;
import java.util.Objects;
import java.util.Random;

/**
 * enumeration codant les directions d'une rose des vents
 * N, NE, E, SE, S, SO, O, NO
 * chaque enumeration contient son vecteur directeur
 * (ex. un deplacement N implique un déplacement de 0 en x, -1 en y
 * un deplacement SE implique un déplacement de 1 en x, 1 en y)
 */
public enum Direction {N(0,-1), NE(1,-1), E(1,0), SE(1,1), S(0,1), SO(-1,1), O(-1,0), NO(-1,-1);
    int x;
    int y;
    /**objet utilise pour choix aleatoire*/
    static Random r= new Random();

    /**constructeur initalisant les donnees du vecteur directeur*/
    Direction(int x, int y) {this.x = x; this.y = y;}

    /**retourne une direction aleatoire autour de la direction courante
     * ex. si direction = N, retourne soit NO, soit N, soit NE*/
    Direction nextDirection()
    {
        int h = r.nextInt(3)-1;
        int no = this.ordinal();
        no = (no +h+ 8)%8;
        return Direction.values()[no];
    }

    /**retourne une direction au hasard*/
    static Direction getRandom() {
        return N;    }



    /**retourne 5 directions autour de la direction actuelle
     * ex. si direction = N, alors retourne [O, NO, N, NE, E]*/
    Direction[] get5(){
        var tab = new Direction[3];
        return tab;
    }
    public static Direction getDirectionTo(int currentX, int currentY, int x, int y) { // permet d'obtenir la direction vers laquelle se diriger en fonction de la position X,Y actuelle vers une position X,Y autre
        int deltaX = x - currentX;
        int deltaY = y - currentY;

        if (deltaX == 0) {
            // Même colonne, déplacer verticalement
            if (deltaY > 0) {
                return S;
            } else {
                return N;
            }
        } else if (deltaY == 0) {
            // Même ligne, déplacer horizontalement
            if (deltaX > 0) {
                return E;
            } else {
                return O;
            }
        } else {
            // Déplacer en diagonale
            if (deltaX > 0 && deltaY > 0) {
                return SE;
            } else if (deltaX > 0 && deltaY < 0) {
                return NE;
            } else if (deltaX < 0 && deltaY > 0) {
                return SO;
            } else {
                return NO;
            }
        }
    }

}