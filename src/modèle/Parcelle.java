package modèle;
import gui.ImgParcelle;
 // @author jawad
public class Parcelle {
    TypeParcelle type; // Type de la parcelle

    final int x; // Coordonnée X de la parcelle dans la grille

    final int y; // Coordonnée Y de la parcelle dans la grille
    Parcelle[][] grille; // Grille en elle même
    ImgParcelle img; // Représentation graphique de la grille
    private boolean contientDonnee; // Booléan indiquant si la parcelle contient la donnée

    public Parcelle() {
        x = y = 0;
    }

    // Constructeur initialisant une parcelle avec sa position, sa grille, son type et si elle contient une donnée
    public Parcelle(Parcelle[][] grille, int x, int y, TypeParcelle type, boolean contientDonnee) {
        this.x = x;
        this.y = y;
        this.grille = grille;
        this.type = type;
        this.contientDonnee = contientDonnee;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setType(TypeParcelle type) {

        this.type = type;
    }


    public void setImg(ImgParcelle img) {
        this.img = img;
    }

    public TypeParcelle getType() { return type; }

    public boolean contientDonnee() {
        return contientDonnee;
    }

    public void setContientDonnee(boolean contientDonnee) {
        this.contientDonnee = contientDonnee;
    }

    @Override
    public String toString() {
        return "Parcelle{" +
                "type=" + type +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}