package characters_classes;

import myclasses.My2DSyncArray;
import tiles_classes.Tile;

public class OrangeGhost extends Ghost {
    public OrangeGhost(My2DSyncArray charactersPosition, Tile[][] tiles, Pacman pacman, int colour){
        super(charactersPosition, tiles, pacman, colour);
        x=15;
        y=17;
        status=1;
        nGhost=4;
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
    }

    @Override
    public void chase() {
        double distanceFromPacman;
        if(status!=0) {
            targetReached=true;
            turnAround();
            status = 0;
        }

        distanceFromPacman =  Math.sqrt(Math.pow(y - charactersPosition.getY(0), 2) + Math.pow(x - charactersPosition.getX(0), 2));
        if (distanceFromPacman>8) {

            if(x==xTarget&&y==yTarget) targetReached=true;

            if(targetReached){
                targetReached = false;
                xTarget=charactersPosition.getX(0);
                yTarget=charactersPosition.getY(0);
            }
            xTarget=charactersPosition.getX(0);
            yTarget=charactersPosition.getY(0);
        }else{
            targetReached= false;
            //altrimenti punta al suo target in scatter
            xTarget=27;
            yTarget=35;
        }
        //controlla se il target è stato raqggiunto
        if(x==xTarget&&y==yTarget) {
            targetReached=true;
        }
        //se è stato raggiunto acquisice un nuovo target
        reachTarget(xTarget, yTarget);
    }
    @Override
    public void scatter() {
        if(status!=1) {
            turnAround();
            status = 1;
        }
        xTarget=27;
        yTarget=35;
        reachTarget(xTarget, yTarget);
    }
    @Override
    public void startGame() {
        try {
            Thread.sleep(waitingTime* 10L);
        } catch (InterruptedException ignored) {}
        move(2);
        move(1);
        move(2);
        move(2);
        status=1;
    }
    @Override
    public void restorePosition(){
        if(x!=15 || y!=17) {
            eventManager.clearGhostPosition(this);
        }
        x=15;
        y=17;
        charactersPosition.setX(nGhost, x);
        charactersPosition.setY(nGhost, y);
        eventManager.updateGhostPosition(this);
    }
}
