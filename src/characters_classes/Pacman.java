package characters_classes;

import mcv.EventManager;
import myclasses.My2DSyncArray;
import javax.swing.*;
import java.awt.*;

public class Pacman extends ImageIcon {
    protected int x, y;
    private My2DSyncArray charactersPosition;
    protected boolean isSuper;
    protected EventManager eventManager;
    //0->right, 1->left, 2->up, 3->down
    private Image[] pacmanImages = new Image[4];
    private Image[] superPacmanImages = new Image[4];
    protected int direction;

    public Pacman(My2DSyncArray charactersPosition) {
        pacmanImages[0] = createImage("right.png");
        pacmanImages[1] = createImage("left.png");
        pacmanImages[2] = createImage("up.png");
        pacmanImages[3] = createImage("down.png");
        superPacmanImages[0] = createImage("super/right.png");
        superPacmanImages[1] = createImage("super/left.png");
        superPacmanImages[2] = createImage("super/up.png");
        superPacmanImages[3] = createImage("super/down.png");
        setImage(pacmanImages[0]);
        direction=0;

        this.charactersPosition=charactersPosition;
        x=13;
        y=26;
    }
    private Image createImage(String imagePath){
        ImageIcon originalIcon = new ImageIcon("src/images/pacman/"+imagePath);
        Image originalImage = originalIcon.getImage();
        Image scaledImageDot = originalImage.getScaledInstance(25, 23, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImageDot).getImage();
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
    }
    private void setDirection(int direction) {
        if (isSuper) setImage(superPacmanImages[direction]);
        else setImage(pacmanImages[direction]);
        this.direction = direction;
    }
    public void setSuper(boolean isSuper) {
        this.isSuper = isSuper;
        if (isSuper) {
            eventManager.muchoMachoPacman();
            setImage(superPacmanImages[direction]);
        }
    }

    public boolean isSuper() {
        return isSuper;
    }
    public int getDirection() {
        return direction;
    }
    public synchronized int getX() {
        return x;
    }
    public synchronized int getY() {
        return y;
    }

    public void move(int direction) {
        setDirection(direction);
        switch (direction) {
            case 1 : {
                if (x == 0) x = 27;
                else x-=1;
                break;
            }
            case 0 : {
                if (x == 27) x = 0;
                else x++;
                break;
            }
            case 2 : {
                y-=1;
                break;
            }
            case 3 : {
                y++;
                break;
            }
        }
        charactersPosition.setX(0, x);
        charactersPosition.setY(0, y);
    }
    public void eaten() {
        //se è stato mangiato torna alla posizione di partenza
        eventManager.getTable().clearPacman(x,y);
        x=13;
        y=26;
        charactersPosition.setX(0, x);
        charactersPosition.setY(0, y);
        setDirection(0);
        eventManager.getTable().updatePosition();
    }
}