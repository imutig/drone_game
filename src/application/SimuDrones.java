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
    int vitesse_intrus = 1; // Vitesse de l'intrus
    public static int tempo = 150;
    /** troupe des elements graphiques*/
    public static int nb_drones = 10; // Nombre de drones présents sur le terrain (positions aléatoires)
    Group troupe;
    private Intrus intrus; // Déclaration de l'intrus
    ArrayList<Drone> lesDrones; // Array contenant les drones


    /**lancement de l'application*/
    @Override
    public void start(Stage primaryStage) { // Fonction de démarrage
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
        scene.setOnKeyPressed(e -> { // lorsque l'utilisateur appuie sur une touche on déplace l'intrus
            KeyCode touche = e.getCode();
            if(!intrus.getHasLost() && !intrus.getHasWon()) { // seulement si l'intrus n'a ni perdu, ni gagné
            for(int i = 0; i < vitesse_intrus; i++) { // la vitesse de l'intrus affecte le nombre de cases de déplacement par pression de la touche
                intrus.déplacer(touche);
            }
            }
        });
        primaryStage.setTitle("Simu Drones...");
        primaryStage.setScene(scene);
        evt = new Environnement(this,taille);
        cases = new ImgParcelle[taille][taille]; // tableau où les cases sont stockées
        creerParcellesEtImg(); // creation des parcelles
        colorerParcelles(); // coloration des parcelles
        intrus = new Intrus(evt, taille / 2, taille / 2); // création de l'intrus

        long depart = System.currentTimeMillis(); // variable stockant le temps de démarrage de la simulation
        addDroneAndIntrus(nb_drones); // ajout des drones et de l'intrus sur la simulation
        evt.avancer(); // lancement de la simulation
        // Initialisation des différents textes à afficher en cas de victoire/défaite ou encore en HUD
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
        //afficher le theatre des opérations
        primaryStage.show();
        //-----lancer le timer pour faire vivre la simulation
        Timeline littleCycle = new Timeline(new KeyFrame(Duration.millis(tempo),
                event -> {
                    long time = (System.currentTimeMillis() - depart) / 1000; // variable qui récupère le temps de la simulation à partir du temps actuel - le temps de départ
                    int taille_champ = 5; // taille du champ de vision
                    int temps_oubli = 3000; // temps (en ms) après lequel l'intrus oublie l'environnement vu en champ de vision
                    if (intrus.surSortie() && intrus.isDonneeRecuperee()) { // si l'intrus est sur la sortie et qu'il a recupère la donnée alors il gagne
                        victoire.setText("Vous avez gagné !");
                        victoire.setVisible(true);
                        intrus.setHasWon(true);
                        taille_champ = 0; // son champ est mis à 0 pour que l'écran devienne noir
                        temps_oubli = 0; // son temps d'oubli à 0 pour que l'écran devienne noir
                        donne.setText("");
                        intrus.setCouleur(Color.TRANSPARENT); // l'intrus devient transparent pour que l'écran soit totalement noir
                        temps.setX(largeur/2 - 100);
                        temps.setY(hauteur/2 - 70);
                        for (Drone drone : lesDrones) {
                            drone.setCouleur(Color.TRANSPARENT); // les drones deviennent transparents pour que l'écran soit totalement noir
                        }

                    } else if (intrus.getTempsDeDetection() > 10000) { // si l'intrus est détecté pendant + du temps en ms indiqué, l'intrus perd
                        intrus.setDonneeRecuperee(false);
                        victoire.setX(largeur/2 - 100);
                        victoire.setText("Perdu...");
                        victoire.setFill(Color.DARKRED);
                        victoire.setVisible(true);
                        taille_champ = 0;
                        temps_oubli = 0;
                        donne.setText("");
                        temps.setX(largeur/2 - 100);
                        temps.setY(hauteur/2 - 70);
                        intrus.setCouleur(Color.TRANSPARENT);
                        intrus.setHasLost(true);
                        for (Drone drone : lesDrones) {
                            drone.setCouleur(Color.TRANSPARENT);
                        }
                    } else { // Si l'intrus n'a ni gagné, ni perdu, alors la simulation continue et le temps survécu se met à jour
                        evt.avancer();
                        colorerParcelles();
                        temps.setText("Temps survécu : " + time);
                    }

                    champvisionintrus(intrus, taille_champ, temps_oubli); // on update le champ de vision de l'intrus à chaque itération
                    if (intrus.isDonneeRecuperee()) {
                        donne.setVisible(true); // si l'intrus a récupéré la donnée on affiche un message pour l'indiquer en haut de l'écran

                    }

                    for (Drone drone1 : lesDrones) {
                        for (Drone drone2 : lesDrones) { // Pour chaque couple possible de drone
                            if (drone1 != drone2) { // On ne vérifie pas pour un drone avec lui même
                                if (drone1.lastPlayerPositionX == -1 && drone2.lastPlayerPositionX == -1) { // Si l'intrus n'est pas détecté
                                    if (drone1.getParcelle() == drone2.getParcelle()) { // Et que les drones sont sur la même case
                                        if ((drone1.isMega == true && drone2.isMega == true) || (drone1.isMega == false && drone2.isMega == false)) {
                                            // Et que les drones sont de même type (soit tous les deux MEGA, soit tous les deux non MEGA

                                            drone1.setEstDesactive(true); // Ils sont désactivés
                                            drone2.setEstDesactive(true);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    for (Drone drone : lesDrones) {
                        if (drone.getEstDesactive() == true && intrus.getHasWon() == false && intrus.getHasLost() == false) { // si le drone est desactivé, il devient grisé
                            drone.setCouleur(Color.GRAY);
                        }
                    }

                    if (System.currentTimeMillis() - depart >= 5000) { // Après les 5 premières secondes, les drones peuvent détecter l'intrus (pour que l'intrus ne soit pas détecté instantannément)
                        for (Drone drone : lesDrones) {
                            champvisiondrone(drone);
                            if (drone.getLastPlayerPositionX() != -1 && drone.getLastPlayerPositionY() != -1) {
                                //System.out.println(System.currentTimeMillis() - drone.getLastDetection());
                                intrus.setTempsDeDetection(System.currentTimeMillis() - intrus.getDerniereDetection());
                                //System.out.println(intrus.getTempsDeDetection());
                                if (System.currentTimeMillis() - drone.getLastDetection() >= 3000) { // Si aucun drone n'a détécté l'intrus depuis 3 secondes, la dernière position indiquée de tous les drones devient -1 ce qui leur indique de recommencer à patrouiller
                                    drone.setLastPlayerPositionX(-1);
                                    drone.setLastPlayerPositionY(-1);
                                    if (!intrus.getHasLost() && !intrus.getHasWon() && drone.getEstDesactive() == false) { // si l'intrus n'a ni perdu, ni gagné, on set la couleur des drones (différentes selon s'ils sont MEGA ou non
                                        if (drone.isMega) {
                                            drone.setCouleur(Color.BLUE);
                                        } else {
                                            drone.setCouleur(Color.LIGHTBLUE);
                                        }
                                    }

                                } else {
                                    if (!intrus.getHasLost() && !intrus.getHasWon() && drone.getEstDesactive() == false) { // si l'intrus n'a ni gagné ni perdu, et que l'intrus est détecté actuellement, ils deviennent rouges
                                        if (drone.isMega) {
                                            drone.setCouleur(Color.DARKRED);
                                        } else {
                                            drone.setCouleur(Color.PALEVIOLETRED);
                                        }
                                    }

                                }
                            }
                        }
                    }
                }));
        littleCycle.setCycleCount(Timeline.INDEFINITE);
        littleCycle.play();
        //ecoute de evenements claviers

            scene.setOnKeyTyped(e -> agirSelonTouche(e.getCharacter(), littleCycle));
    }
    void agirSelonTouche(String touche, Timeline chrono)
    {
            switch (touche) {
                case "f" -> chrono.play();
                case "p" -> chrono.stop();
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



    public void addDroneAndIntrus(int nb)
    {
        Color couleur = Color.LIGHTBLUE;
        lesDrones = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            if (Math.random() < 0.25) { // les drones ont 25% de chance d'être mega
                Drone d = evt.addDrone((int) (Math.random() * (95 - 5)), (int) (Math.random() * (95 - 5))); // La position des drones est aléatoire
                d.setMega(true);
                Circle c = new Circle(largeur / 2d + 3 * espace / 2d, largeur / 2d + 3 * espace / 2d, espace);
                d.setCircle(c);
                c.setRadius(7.0f); // les mega sont légèrement plus gros que les drones normaux
                troupe.getChildren().add(c);
                lesDrones.add(d);
                d.setCouleur(Color.BLUE); // ils sont également un peu plus foncés
            } else {
                Drone d = evt.addDrone((int) (Math.random() * (95 - 5)), (int) (Math.random() * (95 - 5))); // La position des drones est aléatoire
                Circle c = new Circle(largeur / 2d + 3 * espace / 2d, largeur / 2d + 3 * espace / 2d, espace);
                d.setCircle(c);
                c.setRadius(5.0f);
                troupe.getChildren().add(c);
                lesDrones.add(d);
                d.setCouleur(couleur);
            }
        }
        // création de l'intrus
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

    //l'intrus ne doit pas voir au delà de son champ de vision qui est une variable (les cases qui se trouvent dans son champ de vison ont une opacité de 1)
    public void champvisionintrus(Intrus intrus, int taille_champ, int temps_oubli){

        for (int i = 0; i < taille; i++) {
            for (int j = 0; j <taille; j++) {
                //le champ de vision doit etre circulaire
                double distance = Math.sqrt(Math.pow(intrus.getPosition().getX() - i, 2) + Math.pow(intrus.getPosition().getY() - j, 2));
                boolean[][] memoire = intrus.getMemoire(); // tableau de memoire
                long[][] memoireOubli = intrus.getMemoireOubli(); // tableau du temps de la dernière mémorisation pour pouvoir oublier après un temps
                // Si la distance est supérieure au champs de vision, les cases deviennent opaques
                if (distance > taille_champ && distance <= taille_champ+3 && !(intrus.getHasLost() || intrus.getHasWon())) {
                    if (!memoire[i][j]) {
                        cases[i][j].setOpacity(0.8);
                    }
                } else if (distance > taille_champ+3 && distance <= taille_champ+5 && !(intrus.getHasLost() || intrus.getHasWon())) {
                    if (!memoire[i][j]) {
                        cases[i][j].setOpacity(0.6);
                    }
                } else if (distance > taille_champ+5 && distance <= taille_champ+7 && !(intrus.getHasLost() || intrus.getHasWon())) {
                    if (!memoire[i][j]) {
                        cases[i][j].setOpacity(0.4);
                    }
                } else if (distance > taille_champ+7 || intrus.getHasWon() || intrus.getHasLost()) {
                    if (!memoire[i][j]) {
                        cases[i][j].setOpacity(0);
                    }
                }
                else { // sinon elles sont visibles et le temps de l'oubli est mis à jour
                    cases[i][j].setOpacity(1);
                    memoire[i][j] = true;
                    memoireOubli[i][j] = System.currentTimeMillis();
                }
                // si le temps de l'oubli est dépassé alors les cases redeviennent opaques
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
        // Les drones ont un champ de vision de 5
        if (distance < 5) {
            //System.out.println("Drone a détecté joueur\n");
            //System.out.println("Distance : " +distance);
            //System.out.println("Position du joueur : (" +intrus.getPosition().getX() + "," +intrus.getPosition().getY() + ")");
            //System.out.println("Position du drone : (" +drone.getParcelle().getX() + "," +drone.getParcelle().getY() + ")");
            updatePlayerPosition(intrus.getPosition().getX(), intrus.getPosition().getY());
        }
    }

    void updatePlayerPosition(int x, int y) { // Si un drone a un joueur dans son champ de vision, il informe tous les autres drones et leur donne la position du joueur
        lesDrones.forEach(d -> {
            if (d.lastPlayerPositionX == -1 && d.lastPlayerPositionY == -1) {
                intrus.setDerniereDetection(System.currentTimeMillis()); // quand un drone ne détecte plus, on récupère à quel moment il n'a plus détecté
            }
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