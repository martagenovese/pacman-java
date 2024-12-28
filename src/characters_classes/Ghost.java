 package characters_classes;

import myclasses.My2DSyncArray;
import tiles_classes.*;
import javax.swing.*;
import mcv.*;
import java.awt.*;

 public abstract class Ghost extends ImageIcon implements Runnable {

    protected int x, y;
    protected String direction;
    //0->right, 1->left, 2->up, 3->down
    protected int directionInt;
    //0-> chase, 1->scatter, 2->frightened, 3->eaten, 4->pacmanEaten
    protected int status, nGhost, waitingTime;
    //0->pacman 1->red 2->cyan 3->pink 4->orange
    protected My2DSyncArray charactersPosition;
    protected Tile[][] tiles;
    protected EventManager eventManager;
    protected Pacman pacman;
    protected boolean targetReached;
    protected int xTarget, yTarget;
    protected int colour;
    protected Image normal, scared;

    public Ghost(My2DSyncArray charactersPosition, Tile[][] tiles, Pacman pacman, int colour){
        this.charactersPosition=charactersPosition;
        this.tiles=tiles;
        this.pacman=pacman;
        this.colour=colour;
        normal = createImage("src/images/ghosts/"+switchColor()+".png");
        setImage(normal);
        scared = createImage("src/images/ghosts/scared.png");
        targetReached=true;
        xTarget=0;
        yTarget=0;
        waitingTime=400;
    }
    private Image createImage(String imagePath){
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImageDot = originalImage.getScaledInstance(25, 23, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImageDot).getImage();
    }
    private String switchColor(){
        switch(colour){
            case 1:
                return "red";
            case 2:
                return "cyan";
            case 3:
                return "pink";
            case 4:
                return "orange";
            default:
                return "scared";
        }
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
    public void setScared(boolean isScared){
        if (isScared) {
            setImage(scared);
            status=2;
        } else {
            setImage(normal);
            status=0;
        }
    }
    public void setStatus(int n){
         status=n;
    }
    public int getStatus() {
         return status;
    }
    public int getX() {
         return x;
    }
    public int getY() {
         return y;
    }

    public void move(int dir) {
        eventManager.clearGhostPosition(this);
        directionInt=dir;
        switch (dir) {
            case 1 : {
                if (x == 0) x = 27;
                else x--;
                break;
            }
            case 0 : {
                if (x == 27) x = 0;
                else x++;
                break;
            }
            case 2 : {
                y--;
                break;
            }
            case 3 : {
                y++;
                break;
            }
        }
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
        eventManager.updateGhostPosition(this);
        try {
            Thread.sleep(waitingTime);
        } catch (InterruptedException ignored) {}
    }
    public void reachTarget(int xTarget, int yTarget){
        //{ Up, Left, Down, Right }
        int[][] directions = {{y - 1, x}, {y, x - 1}, {y + 1, x}, {y, x + 1}};
        if (directions[1][1] == -1) directions[1][1] = 27;
        if (directions[3][1] == 28) directions[3][1] = 0;
        double distance;
        int chosenDirection;
        int back;

        //salvo la direzione in cui sta andando pacman e la direzione opposta
        switch(directionInt){
            case 2 :
                chosenDirection=0;
                back=2;
                break;
            case 1 :
                chosenDirection=1;
                back=3;
                break;
            case 3 :
                chosenDirection=2;
                back=0;
                break;
            case 0 :
                chosenDirection=3;
                back=1;
                break;
            default:
                chosenDirection=2;
                back=0;
                break;
        }

        //se è un incrocio o se continuando su quella direzione trova un muro allora cambio choosenDirection
        if ( (tiles[y][x].isIntersection()) || (tiles[directions[chosenDirection][0]][directions[chosenDirection][1]] instanceof WallTile) ) {
            double distanceMin = 100;
            for (int i = 0; i < directions.length; i++) {
                if (!(tiles[directions[i][0]][directions[i][1]] instanceof WallTile) && i != back) {
                    distance = Math.sqrt(Math.pow(yTarget - directions[i][0], 2) + Math.pow(xTarget - directions[i][1], 2));
                    if (distance < distanceMin) {
                        distanceMin = distance;
                        chosenDirection = i;
                    }
                }
            }
        }

        //converto la direzione da int in stringa
        switch (chosenDirection) {
            case 0:
                move(2);
                break;
            case 1:
                move(1);
                break;
            case 2:
                move(3);
                break;
            case 3:
                move(0);
                break;
        }
    }
    public abstract void startGame();
    public abstract void chase();
    public abstract void scatter();
    public void frightened()  {
         //quando cambia modalità inverte la direzione
         if(status!=2){
             targetReached=true;
             turnAround();
             status=2;
         }

         //controllo se il target è stato raggiunto
         if(x==xTarget&&y==yTarget) {
             targetReached=true;
         }

         //se è stato raggiunto acquisice randomicamente un nuovo target
         if(targetReached){
             do{
                 xTarget=(int)(Math.random()*26+1);
                 yTarget=(int)(Math.random()*29+4);
             } while(tiles[yTarget][xTarget] instanceof WallTile);
         }
         reachTarget(xTarget, yTarget);
    }
    public void eaten(){
         restorePosition();
         if(!pacman.isSuper()){
             startGame();
         }
    }
    protected abstract void restorePosition();
    public void turnAround(){
        switch (directionInt) {
            case 2 :
                if(!(tiles[y-1][x] instanceof WallTile)){move(3);}
                break;
            case 1 :
                if(!(tiles[y][x-1] instanceof WallTile)){move(0);}
                break;
            case 3 :
                if(!(tiles[y+1][x] instanceof WallTile)){move(2);}
                break;
            case 0 :
                if(!(tiles[y][x+1] instanceof WallTile)){move(1);}
                break;
        }
    }
     public void pacmanEaten(){
         restorePosition();
         status=4;
         eventManager.updateGhostPosition(this);
         eventManager.clearGhostPosition(this);
    }

     @Override
     public void run() {
         startGame();
         while(true){
             //0-> chase, 1->scatter, 2->frightened, 3->eaten, 4->gameLost
             switch(status){
                 case 0: {
                     chase();
                     break;
                 }
                 case 1: {
                     scatter();
                     break;
                 }
                 case 2: {
                     frightened();
                     break;
                 }
                 case 3: {
                     eaten();
                     break;
                 }
                 case 4:{
                     restorePosition();
                     break;
                 }
                 case 5:{
                     startGame();
                     break;
                 }
             }
         }
     }
}
