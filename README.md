# Drone Game

Drone Game est un jeu développé en Java pour un projet de TP d'université.

## Installation

Pour y jouer, il suffira d'installer les fichiers et d'ouvrir le projet via un IDE adapté, puis l'exécuter.
Par exemple, utilisez [IntelliJIdea](https://www.jetbrains.com/idea/)

## Gameplay

Utilisez les touches Z, Q, S, D pour vous déplacer. Le principe est simple :
- Vous êtes un intrus dans une zone protégé à la recherche d'une donnée secrète
- Vous ne voyez qu'à quelques mètres autour de vous et vous oubliez rapidement (3 secondes) ce que vous avez précédemment vu
- Cette donnée secrète se trouve sur la carte sous la forme d'une case bleue
- Marchez dessus pour la récupérer, puis empruntez l'une des sorties aux 4 côtés de la map pour sortir
- Faufilez vous entre les arbres pour vous en sortir
- Attention, des drones patrouillent dans la zone (au nombre de 10, modifiable via la variable nb_drone modifiable)
  ```java
  public class SimuDrones extends Application {
  public static int nb_drones = 10;
  }
  ```
- Lorsqu'un drone est suffisamment proche, il vous détecte et envoie votre position à tous les autres drones
- Une fois détecté, vous avez 10 secondes pour sortir du champ de détection des drones pour ne pas perdre
- Une fois sorti, après un temps, les drones recommencent à patrouiller
- Si vous parvenez à sortir avec la donnée, vous gagnez

## Auteurs

Ce projet a été réalisé en binôme par :
- Moreau Jawad
- Halle Julien


## Lien vers le TP

[TP_Drones](https://github.com/EmmanuelADAM/coursJavaAvance/tree/master/tps/poursuiteDrones)
