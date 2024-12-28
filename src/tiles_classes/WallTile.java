package tiles_classes;

public class WallTile extends Tile {
    public WallTile() {}

    @Override
    public boolean isWall() {
        return true;
    }

    @Override
    public boolean isPacman() {
        return false;
    }

    @Override
    public boolean isGhost() {
        return false;
    }

    @Override
    public boolean isDot() {
        return false;
    }

    @Override
    public boolean isSuperFood() {
        return false;
    }

    @Override
    public boolean isIntersection() {
        return false;
    }
    public boolean isTardis(){return false;}
}
