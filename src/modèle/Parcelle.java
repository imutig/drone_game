package modèle;
import gui.ImgParcelle;
/**
 * cette classe represente une parcelle de terrain
 * La parcelle peut etre de type herbe, nid, nourriture, et contenir de la pheromone
 *
 * @author Emmanuel Adam
 */
public class Parcelle {
    static final double tauxOubli = 0.01;

    /**type de la parcelle*/
    TypeParcelle type;
    /**coordonnee x de la parcelle dans la grille*/
    final int x;
    /**coordonnee y de la parcelle dans la grille*/
    final int y;
    Parcelle[][] grille;
    /**representation graphique associee*/
    ImgParcelle img;
    private boolean contientDonnee;

    /**
     * constructeur par defaut, inutilise
     */
    public Parcelle() {
        x = y = 0;
    }

    /**
     * constructeur initialisant la grille, les coordonnees et le type de la parcelle
     */
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