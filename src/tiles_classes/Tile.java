package tiles_classes;

import javax.swing.*;

public abstract class Tile {
    public abstract boolean isWall();
    public abstract boolean isPacman();
    public abstract boolean isGhost();
    public abstract boolean isDot();
    public abstract boolean isSuperFood();
    public abstract boolean isTardis();
    public void setPacman(boolean b) {
    }
    public void setIntersection(boolean b) {
    }
    public void setTardis(boolean b){}

    public abstract boolean isIntersection();

    public void setFruit(boolean b) {
    }

    public boolean isFruit() {
        return false;
    }
}
