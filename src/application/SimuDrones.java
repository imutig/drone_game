package application;

import gui.ImgParcelle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import modèle.*;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;

import java.util.ArrayList;


public class SimuDrones extends Application {
    /**
     * environnement liee a cet objet graphique
     */
    Environnement evt;
    /**
     * elements graphiques représentant les parcelles de terrain
     */
    public ImgParcelle[][] cases;

    /** largeur du terrain en nb de cases  */
    private int taille;
    /** taille d'une parcelle en pixel*/
    int espace = 20;
    /** largeur du terrain en pixel*/
    int largeur = 800;
    /** heuteur du terrain en pixel*/
    int hauteur = 900;
    /** délai en ms entre chaque etape de simulation*/
    int vitesse_intrus = 1;
    public static int tempo = 150;
    /** troupe des elements graphiques*/
    public static int nb_drones = 5;
    Group troupe;
    private Intrus intrus;
    ArrayList<Drone> lesDrones;


    /**lancement de l'application*/
    @Override
    public void start(Stage primaryStage) {
        taille = 100;
        construireScene(primaryStage);
    }

    /**
     * construit le theatre des opérations, la fenetre, l'environnement, les acteurs (elements graphiques),
     * et cycle de simulation
     * */

    void construireScene(Stage primaryStage) {
        espace = largeur / taille;
        //definir la scene principale
        troupe = new Group();
        Scene scene = new Scene(troupe, largeur, hauteur, Color.BLACK);
        scene.setOnKeyPressed(e -> {
            KeyCode touche = e.getCode();
            for(int i = 0; i < vitesse_intrus; i++) {
                intrus.déplacer(touche);
                // test
            }
        });
        primaryStage.setTitle("Simu Drones...");
        primaryStage.setScene(scene);
        //definir les acteurs
        evt = new Environnement(this,taille);
        //definir les acteurs
        cases = new ImgParcelle[taille][taille];
        creerParcellesEtImg();
        colorerParcelles();
        intrus = new Intrus(evt, taille / 2, taille / 2);
        long depart = System.currentTimeMillis();
        addDrone(nb_drones);
        evt.avancer();
        Text temps = new Text("Temps survécu : 0");
        temps.setX(20);
        temps.setY((40));
        temps.setFill(Color.WHITE);
        temps.setStyle("-fx-font-family: 'Arial Black'; -fx-font-size: 18; -fx-font-weight: bold; -fx-border-color: black;");
        troupe.getChildren().add(temps);
        Text donne = new Text("Donnée récupérée !");
        donne.setX(20);
        donne.setY((60));
        donne.setFill(Color.YELLOW);
        donne.setStyle("-fx-font-family: 'Arial Black'; -fx-font-size: 18; -fx-font-weight: bold; -fx-border-color: black;");
        troupe.getChildren().add(donne);
        donne.setVisible(false);
        Text victoire = new Text("Victoire");
        victoire.setX(largeur / 2 - 150);
        victoire.setY(hauteur / 2 - 100);
        victoire.setFill(Color.LIGHTGREEN);
        victoire.setStyle("-fx-font-family: 'Arial Black'; -fx-font-size: 30; -fx-font-weight: bold; -fx-border-color: black;");
        troupe.getChildren().add(victoire);
        victoire.setVisible(false);
        //afficher le theatre
        primaryStage.show();
        //-----lancer le timer pour faire vivre la simulation
        Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo),
                event -> {
                    long time = (System.currentTimeMillis() - depart) / 1000;
                    int taille_champ = 100;
                    int temps_oubli = 3000;
                    if (intrus.surSortie() && intrus.isDonneeRecuperee()) {
                        victoire.setText("Vous avez gagné !");
                        victoire.setVisible(true);
                        taille_champ = 0;
                        temps_oubli = 0;
                        donne.setText("");
                        temps.setX(largeur/2 - 100);
                        temps.setY(hauteur/2 - 70);
                    } else {
                        evt.avancer();
                        colorerParcelles();
                        temps.setText("Temps survécu : " + time);
                    }
                    champvisionintrus(intrus, taille_champ, temps_oubli);
                    if (intrus.isDonneeRecuperee()) {
                        donne.setVisible(true);

                    }

                    if (System.currentTimeMillis() - depart >= 5000) {
                    for (Drone drone : lesDrones) {
                            champvisiondrone(drone);

                            if (drone.getLastPlayerPositionX() != -1 && drone.getLastPlayerPositionY() != -1) {
                                //System.out.println(System.currentTimeMillis() - drone.getLastDetection());
                                if (System.currentTimeMillis() - drone.getLastDetection() >= 3000) {
                                    drone.setLastPlayerPositionX(-1);
                                    drone.setLastPlayerPositionY(-1);
                                    if (drone.isMega) {
                                        drone.setCouleur(Color.BLUE);
                                    } else {
                                        drone.setCouleur(Color.LIGHTBLUE);
                                    }

                                } else {
                                    if (drone.isMega) {
                                        drone.setCouleur(Color.DARKRED);
                                    } else {
                                        drone.setCouleur(Color.PALEVIOLETRED);
                                    }

                                }
                            }
                        }
                    }
                }));
        littleCycle.setCycleCount(Timeline.INDEFINITE);
        littleCycle.play();
        //ecoute de evenements claviers
        scene.setOnKeyTyped(e->agirSelonTouche( e.getCharacter(), littleCycle));
    }
    void agirSelonTouche(String touche, Timeline chrono)
    {
        switch (touche)
        {
            case "f"->chrono.play();
            case "p"->chrono.stop();
        }
    }

    /**
     * creation des parcelles de la grille, et de leurs images
     */
    void creerParcellesEtImg() {
        Parcelle[][] grille = evt.getGrille();
        for (int i = 0; i < taille; i++)
            for (int j = 0; j < taille; j++) {
                Parcelle cell = grille[i][j];
                cases[i][j] = new ImgParcelle(troupe, cell, (i * espace + espace), (j * espace + espace), espace);
                cell.setImg(cases[i][j]);
                //System.out.println("Type de la parcelle (" + i + ", " + j + "): " + cell.getType());
            }
    }


    /**colorer les parcelles*/
    void colorerParcelles() {
        for (ImgParcelle[] ligne: cases)
            for (ImgParcelle img:ligne)
                img.choisirCouleur();
    }



    public void addDrone(int nb)
    {
        Color couleur = Color.LIGHTBLUE;
        lesDrones = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            if (Math.random() < 0.5) {
                Drone d = evt.addDrone((int) (Math.random() * (95 - 5)), (int) (Math.random() * (95 - 5)));
                d.setMega(true);
                Circle c = new Circle(largeur / 2d + 3 * espace / 2d, largeur / 2d + 3 * espace / 2d, espace);
                d.setCircle(c);
                c.setRadius(7.0f);
                troupe.getChildren().add(c);
                lesDrones.add(d);
                d.setCouleur(Color.BLUE);
            } else {
                Drone d = evt.addDrone((int) (Math.random() * (95 - 5)), (int) (Math.random() * (95 - 5)));
                Circle c = new Circle(largeur / 2d + 3 * espace / 2d, largeur / 2d + 3 * espace / 2d, espace);
                d.setCircle(c);
                c.setRadius(5.0f);
                troupe.getChildren().add(c);
                lesDrones.add(d);
                d.setCouleur(couleur);
            }
        }

        Circle intrusCircle = new Circle(intrus.getPosition().getX() * espace + espace / 2, intrus.getPosition().getY() * espace + espace / 2, espace / 2, Intrus.coulPleine);
        intrus.setCircle(intrusCircle);
        intrusCircle.setRadius(8.0f);
        troupe.getChildren().add(intrusCircle);
    }

    /**
     * deplace un cerle vers le point identifie en  xx,yy dans la grille
     * (donc necessité de calculer l'arrivee x,y en pixel)
     * */
    public void move(Circle c, int xx, int yy)
    {
        Timeline timeline = new Timeline();
        int xdest = (xx*largeur) / taille + 3*espace/2;
        int ydest = (yy*largeur) / taille + 3*espace/2;
        KeyFrame bougeDrone = new KeyFrame(new Duration(tempo),
                new KeyValue(c.centerXProperty(), xdest),
                new KeyValue(c.centerYProperty(), ydest));
        timeline.getKeyFrames().add(bougeDrone);
        timeline.play();
    }

    public int getEspace() {
        return espace;
    }

    public Group getTroupe() {
        return troupe;
    }

    //l'intrus ne doit pas voir au dela de son champ de vision qui est de 3 cases (les cases qui se trouvent dans son champ de vison ont une opacité de 1
    // Set the opacity of the cells outside the intruder's field of vision to 1
    public void champvisionintrus(Intrus intrus, int taille_champ, int temps_oubli){

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j <taille; j++) {
                //le champ de vision doit etre circulaire
                double distance = Math.sqrt(Math.pow(intrus.getPosition().getX() - i, 2) + Math.pow(intrus.getPosition().getY() - j, 2));
                boolean[][] memoire = intrus.getMemoire();
                long[][] memoireOubli = intrus.getMemoireOubli();
                // Check if the distance is greater than the field of vision radius
                if (distance > taille_champ) {
                    if (!memoire[i][j]) {
                        cases[i][j].setOpacity(0);
                    }
                }
                else{
                    cases[i][j].setOpacity(1);
                    memoire[i][j] = true;
                    memoireOubli[i][j] = System.currentTimeMillis();
                }

                if (System.currentTimeMillis() - memoireOubli[i][j] >=  temps_oubli) {
                    memoire[i][j] = false;
                    memoireOubli[i][j] = 0;
                }
            }
        }
    }

    //les drones ont un champ de vision de 5 cases si l'intrus se trouve dans leur champ de vision le drone envoie la position de l'intrus aux autres drones, quand l'intrus n'est pas dans un champ de vision d'un drone ils se déplacent aléatroiement avec la méthode errer
    public void champvisiondrone(Drone drone) {


        //le champ de vision doit etre circulaire
        double distance = Math.sqrt(Math.pow(intrus.getPosition().getX() - drone.getParcelle().getX(), 2) + Math.pow(intrus.getPosition().getY() - drone.getParcelle().getY(), 2));
        // Check if the distance is greater than the field of vision radius
        if (distance < 5) {
            //System.out.println("Drone a détecté joueur\n");
            //System.out.println("Distance : " +distance);
            //System.out.println("Position du joueur : (" +intrus.getPosition().getX() + "," +intrus.getPosition().getY() + ")");
            //System.out.println("Position du drone : (" +drone.getParcelle().getX() + "," +drone.getParcelle().getY() + ")");
            updatePlayerPosition(intrus.getPosition().getX(), intrus.getPosition().getY());
        }
    }

    void updatePlayerPosition(int x, int y) {
        lesDrones.forEach(d -> {
            Direction nouvelleDirection = Direction.getDirectionTo(d.getParcelle().getX(), d.getParcelle().getY(), intrus.getPosition().getX(), intrus.getPosition().getY());
            if (d.isMega == true) {
                d.setLastPlayerPositionX(intrus.getPosition().getX());
                d.setLastPlayerPositionY(intrus.getPosition().getY());
                d.setLastDetection(System.currentTimeMillis());

            } else {
                d.setLastPlayerPositionX(intrus.getPosition().getX());
                d.setLastPlayerPositionY(intrus.getPosition().getY());
                d.setLastDetection(System.currentTimeMillis());}
        });
    }


    /**methode principale*/
    public static void main(String[] args) {
        launch(args);
    }
}