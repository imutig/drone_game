package gui;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import modèle.Parcelle;
import modèle.TypeParcelle;

/**image d'une parcelle de terrain
 * une image contient un carre representant sa nature (terrain, nid, nourriture)
 * et un carre blanc le recouvrant representant la dose de pheromone
 * le carre de pheromone est plus ou moins opaque selon la dose de pheromone
 *
 * @author emmanueladam */
public class ImgParcelle  {
    // des couleurs
    static final public Color coulFond = Color.DARKGREY;

    /**parcelle liee a cette image*/
    private Parcelle parcelle;
    public Rectangle elt;
    /**carre liee au type*/
    /**
     * creation d'une image de parcelle
     * le carre elt porte la couleur du type de la parcelle
     * le carre phero est plus ou moins opaque selon la dose de pheromone
     * initialement ce carre est transparent (opacity = 0)
     * @param troupe troupe des elements graphiques a laquelle doivent se raccrocher elt et phero
     * @param parcelle parcelle liee a cette image
     * @param x position x en pixel
     * @param y position y en pixel
     * @param dim taille de la case en pixel
     * */
    public ImgParcelle(Group troupe, Parcelle parcelle, int x, int y, int dim) {
        this.parcelle = parcelle;
        elt = new Rectangle(x,y,dim, dim);
        troupe.getChildren().add(elt);
    }

    /**definit l'opacite de la case phero selon le degre de pheromone de la cellule*/

    /**choisit la couleur de l'elt selon la nature de la parcelle */
    public void choisirCouleur() {
        //System.out.println("Choix de couleur pour la parcelle (" + parcelle.getX() + ", " + parcelle.getY() + ")");
        switch (parcelle.getType()) {
            case Terrain -> elt.setFill(coulFond);
            case Arbre -> elt.setFill(Color.FORESTGREEN);
            case Donnee -> elt.setFill(Color.BLUE);
            case Sortie -> elt.setFill(Color.WHITE);
        }
    }
//        elt.setOpacity(parcelle.getOdeurNid());

    public Parcelle getParcelle() { return parcelle; }

    public void setOpacity(double i) {
        elt.setOpacity(i);
    }
}





