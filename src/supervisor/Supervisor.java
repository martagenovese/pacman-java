package supervisor;

import mcv.EventManager;
import mcv.Model;
import myclasses.My2DSyncArray;

public class Supervisor implements Runnable{
    My2DSyncArray charactersPosition;
    int ghostsEaten;
    Model model;
    EventManager eventManager;

    public Supervisor(My2DSyncArray charactersPosition, Model model){
        this.charactersPosition = charactersPosition;
        this.model = model;
    }
    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
    private int nGhostsEaten(){
        int g = 1, n = 0;
        for (int i = 12; i < 16; i++) {
            if (charactersPosition.getY(g) == 17 && charactersPosition.getX(g) == i) {
                n++;
            }
            g++;
        }
        return n;
    }

    @Override
    public void run() {
        int nGhostsBefore = nGhostsEaten();
        while (true) {
            int n = model.collision();
            if (n>=0) {
                try {
                    boolean isPacmanAlive = model.collisionProcedure(n);
                    if (!isPacmanAlive) {
                        eventManager.setStartGhost(2);
                        model.lives--;
                        if (model.lives==2) eventManager.getTable().clearTile(6, 35);
                        else if (model.lives==1) eventManager.getTable().clearTile(4, 35);
                        else if (model.lives==0) eventManager.getTable().clearTile(2, 35);
                        if (model.lives < 0) {
                            System.out.println("Game Over");
                            eventManager.stopGame(false);
                        } else eventManager.getTable().playSound("meme/audio/morte.wav");
                    } else {
                        eventManager.getTable().playSound("meme/audio/urlo.wav");
                        eventManager.getTable().clearPacman(model.getPacman().getX(), model.getPacman().getY());
                        eventManager.getTable().updatePosition();
                        ghostsEaten = nGhostsEaten();
                        if (ghostsEaten!=0 && ghostsEaten!=nGhostsBefore) {
                            model.score += 100 * (int) Math.pow(2, ghostsEaten);
                            nGhostsBefore = ghostsEaten;
                        }
                    }
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }
}