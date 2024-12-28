package characters_classes;

import myclasses.My2DSyncArray;
import tiles_classes.Tile;
import tiles_classes.WallTile;

public class PinkGhost extends Ghost {
    public PinkGhost(My2DSyncArray charactersPosition, Tile[][] tiles, Pacman pacman, int colour){
        super(charactersPosition, tiles, pacman, colour);
        x=14;
        y=17;
        status=1;
        nGhost=3;
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
    }

    @Override
    public void chase() {
        if(status!=0) {
            //quando cambia status inverte la direzione
            targetReached=true;
            turnAround();
            status = 0;
        }

        //controlla se il target è stato raqggiunto
        if(x==xTarget&&y==yTarget) targetReached=true;

        //se è stato raggiunto acquisice un nuovo target
        if(targetReached){
            targetReached=false;

            //prende come obbiettivo la casella di pacman
            xTarget=charactersPosition.getX(0);
            yTarget=charactersPosition.getY(0);

            //a seconda della direzione di pacman, targhetta la quarta casella in avanti
            //prova a puntare alla quarta in avanti, se è un muro o fuori dalla griglia prova con quella prima
            switch (pacman.getDirection()){
                case 2: {
                    for(int i=4; i>=0; i--)  {
                        if( yTarget-i>=0 && !(tiles[yTarget-i][xTarget] instanceof WallTile) ) {
                                yTarget = yTarget - i;
                                break;
                        }
                    }
                    break;
                }
                case 1: {
                    for(int i=4; i>=0; i--)  {
                        if( xTarget-i>=0 && !(tiles[yTarget][xTarget-i] instanceof WallTile) ){
                            xTarget=xTarget-i;
                            break;
                        }
                    }
                    break;
                }
                case 3: {
                    for(int i=4; i>=0; i--)  {
                        if( yTarget+i<=35 && !(tiles[yTarget+i][xTarget] instanceof WallTile) ){
                            yTarget=yTarget+i;
                            break;
                        }
                    }
                    break;
                }
                case 0: {
                    for(int i=4; i>=0; i--)  {
                        if( xTarget+i<=27 && !(tiles[yTarget][xTarget+i] instanceof WallTile) ){
                            xTarget=xTarget+i;
                            break;
                        }
                    }
                    break;
                }
            }
        }
        //si muove in quella direzione
        reachTarget(xTarget, yTarget);
    }
    @Override
    public void scatter() {
        if(status!=1) {
            turnAround();
            status = 1;
        }
        int xTarget=1;
        int yTarget=0;
        reachTarget(xTarget, yTarget);
    }
    @Override
    public void startGame() {
        try {
            Thread.sleep(waitingTime* 7L);
        } catch (InterruptedException ignored) {}
        move(2);
        move(2);
        move(2);
        status=1;
    }
    @Override
    public void restorePosition(){
        //riporta pacman nella casetta
        if(x!=14 || y!=17) {
            eventManager.clearGhostPosition(this);
        }
        x=14;
        y=17;
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
        eventManager.updateGhostPosition(this);
    }
}

