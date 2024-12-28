package tiles_classes;

import javax.swing.*;

public class CrossableTile extends Tile {
    public int x, y;
    boolean isPacman, isGhost, isDot, isSuperFood, isIntersection, isFruit, isTardis;

    public CrossableTile(int x, int y) {
        this.x = x;
        this.y = y;
        this.isPacman = false;
        this.isGhost = false;
        this.isDot = false;
        this.isSuperFood = false;
        this.isIntersection = false;
        this.isFruit = false;
        this.isTardis=false;
    }

    public void setPacman(boolean isPacman) {
        this.isPacman = isPacman;
    }
    public void setIntersection(boolean isIntersection) {
        this.isIntersection = isIntersection;
    }
    public void setGhost(boolean isGhost) {
        this.isGhost = isGhost;
    }
    public void setDot(boolean isDot) {
        this.isDot = isDot;
    }
    public void setSuperFood(boolean isSuperFood) {
        this.isSuperFood = isSuperFood;
    }
    public void setTardis(boolean isTardis){
        this.isTardis=isTardis;
    }

    @Override
    public boolean isWall() {
        return false;
    }

    @Override
    public boolean isPacman() {
        return isPacman;
    }

    @Override
    public boolean isGhost() {
        return isGhost;
    }

    @Override
    public boolean isDot() {
        return isDot;
    }

    @Override
    public boolean isSuperFood() {
        return isSuperFood;
    }

    public boolean isIntersection() {
        return isIntersection;
    }
    @Override
    public boolean isTardis(){return isTardis;}

    @Override
    public boolean isFruit() {
        return isFruit;
    }
    @Override
    public void setFruit(boolean isFruit) {
        this.isFruit = isFruit;
    }
}
