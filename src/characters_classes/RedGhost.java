package characters_classes;

import myclasses.My2DSyncArray;
import tiles_classes.*;

public class RedGhost extends Ghost {
    public RedGhost(My2DSyncArray charactersPosition, Tile[][] tiles, Pacman pacman, int colour){
        super(charactersPosition, tiles, pacman, colour);
        x=12;
        y=17;
        status=1;
        nGhost=1;
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
    }
    public void startGame() {
        move(2);
        move(0);
        move(2);
        move(2);
        status=1;
    }
    @Override
    public void scatter(){
        if(status!=1) {
            turnAround();
            status = 1;
        }
        int xTarget=25;
        int yTarget=0;
        reachTarget(xTarget, yTarget);
    }
    @Override
    public void chase() {
        if(status!=0) {
            targetReached=true;
            turnAround();
            status = 0;
        }
        //controlla se il target è stato raqggiunto
        if(x==xTarget&&y==yTarget) targetReached=true;

        //se è stato raggiunto acquisice un nuovo target
        if(targetReached){
            targetReached=false;
            xTarget=charactersPosition.getX(0);
            yTarget=charactersPosition.getY(0);
        }
        reachTarget(xTarget, yTarget);
    }
    @Override
    public void restorePosition(){
        if(x!=12 || y!=17) {
            eventManager.clearGhostPosition(this);
        }
        x=12;
        y=17;
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
        eventManager.updateGhostPosition(this);
    }
}