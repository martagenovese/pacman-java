package mcv;

import characters_classes.*;
import myclasses.My2DSyncArray;
import tiles_classes.CrossableTile;
import tiles_classes.Tile;
import tiles_classes.WallTile;
import supervisor.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Scanner;

public class Model {
    protected Tile[][] tiles;
    protected Pacman pacman;
    public int score, lives, dotsCounter, fruit, eatenFruit, ghostsEaten;
    // 0 - pacman, 1 - red ghost, 2 - cyan ghost, 3 - pink ghost, 4 - orange ghost
    protected My2DSyncArray charactersPosition;
    protected Tile leftTile, rightTile, upTile, downTile, myTile;
    protected int lastDirection;
    protected Thread rThread, pThread, cThread, oThread, sThread, gThread;
    protected RedGhost r;
    protected PinkGhost p;
    protected CyanGhost c;
    protected OrangeGhost o;
    protected Supervisor supervisor;
    protected GhostSupervisor ghostSupervisor;
    protected boolean isFruitEaten;

    public Model() {
        charactersPosition = new My2DSyncArray(5);
        pacman = new Pacman(charactersPosition);
        tiles = new Tile[36][28];
        supervisor = new Supervisor(charactersPosition, this);
        sThread = new Thread(supervisor);
        ghostsEaten = 0;

        //tutte crossable all'inizio
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {
                tiles[i][j] = new CrossableTile(i, j);
            }
        }
        arrageWalls();
        arrangeIntersections();
        arrangeDots();
        tiles[26][13].setPacman(true);
        setTardis(0,17);
        setTardis(27,17);

        score = 0;
        lives = 3;
        dotsCounter = 0;
        fruit = 2;
        eatenFruit = 0;
        r = new RedGhost(charactersPosition, tiles, pacman, 1);
        c = new CyanGhost(charactersPosition, tiles, pacman, 2);
        p = new PinkGhost(charactersPosition, tiles, pacman, 3);
        o = new OrangeGhost(charactersPosition, tiles, pacman, 4);

        rThread = new Thread(r);
        pThread = new Thread(p);
        cThread = new Thread(c);
        oThread = new Thread(o);

        ghostSupervisor = new GhostSupervisor(r,c,p,o);
        gThread = new Thread(ghostSupervisor);
    }

    private void arrageWalls() {
        InputStream f;
        Scanner s;
        try {
            f = new FileInputStream("src/construction/walls.csv");
            s = new Scanner(f);
            while (s.hasNextLine()) {
                String[] coordinates = s.nextLine().split(";");
                int i = Integer.parseInt(coordinates[0]);
                int j = Integer.parseInt(coordinates[1]);
                tiles[i][j] = new WallTile();
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        for (int i=0; i<36; i++) {
            for (int j=0; j<28; j++) {
                if (i==3) i=13;
                else if ( ((i>12 && i<16) || (i>18 && i<22)) && j==5 ) j=23;
                else if (i==22) i=34;
                if (i<3) tiles[i][j] = new WallTile();
                else if (( (i>12 && i<16) || (i>18 && i<22) ) && (j<5 || j>22)) tiles[i][j] = new WallTile();
                else if (i>33) tiles[i][j] = new WallTile();
                else if ((i>=16 && i<=18) && (j>=11 && j<=16)) tiles[i][j] = new WallTile();
            }
        }
        tiles[15][13] = new WallTile();
        tiles[15][14] = new WallTile();
    }
    private void arrangeIntersections() {
        InputStream f;
        Scanner s;
        try {
            f = new FileInputStream("src/construction/intersections.csv");
            s = new Scanner(f);
            while (s.hasNextLine()) {
                String[] coordinates = s.nextLine().split(";");
                int i = Integer.parseInt(coordinates[0]);
                int j = Integer.parseInt(coordinates[1]);
                tiles[i][j].setIntersection(true);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    private void arrangeDots() {
        InputStream f;
        Scanner s;
        try {
            f = new FileInputStream("src/construction/dots.csv");
            s = new Scanner(f);
            while (s.hasNextLine()) {
                String[] coordinates = s.nextLine().split(";");
                int i = Integer.parseInt(coordinates[0]);
                int j = Integer.parseInt(coordinates[1]);
                if (i == 6 && j == 1 || i == 6 && j == 26 || i == 26 && j == 1 || i == 26 && j == 26) {
                    tiles[i][j] = new CrossableTile(i, j);
                    ((CrossableTile) tiles[i][j]).setSuperFood(true);
                } else ((CrossableTile) tiles[i][j]).setDot(true);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public void setFruit(int x, int y) {
        tiles[y][x].setFruit(true);
    }
    public void setTardis(int x, int y) {
        tiles[y][x].setTardis(true);
    }

    public Pacman getPacman() {
        return pacman;
    }

    public Tile getLeftTile() {
        try {
            leftTile = tiles[pacman.getY()][pacman.getX() - 1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            leftTile = tiles[pacman.getY()][27];
        }
        return leftTile;
    }
    public Tile getRightTile() {
        try {
            rightTile = tiles[pacman.getY()][pacman.getX() + 1];
        } catch (ArrayIndexOutOfBoundsException ex) {
            rightTile = tiles[pacman.getY()][0];
        }
        return rightTile;
    }
    public Tile getUpTile() {
        upTile = tiles[pacman.getY() - 1][pacman.getX()];
        return upTile;
    }
    public Tile getDownTile() {
        downTile = tiles[pacman.getY() + 1][pacman.getX()];
        return downTile;
    }
    public Tile getMyTile() {
        myTile = tiles[pacman.getY()][pacman.getX()];
        return myTile;
    }
    public RedGhost getRedGhost() {
        return  r;
    }
    public PinkGhost getPinkGhost() {
        return  p;
    }
    public CyanGhost getCyanGhost() {
        return c;
    }
    public OrangeGhost getOrangeGhost() {
        return o;
    }
    public int getScore() {
        return score;
    }
    public int getFruit() {
        return fruit;
    }

    protected void updatePosition() {
        charactersPosition.setX(0, pacman.getX());
        charactersPosition.setY(0, pacman.getY());
    }
    public void movePacman(int direction, Tile tile, Tile myTile) {
        isFruitEaten = false;
        if (tile==null) { return; }
        if (!(tile instanceof WallTile)) {
            if (dotsCounter == 70 || dotsCounter == 240) {
                if (fruit>0) fruit--;
            }
            if (tile.isSuperFood()) {
                ((CrossableTile) tile).setSuperFood(false);
                pacman.setSuper(true);
                score += 50;
            } else if (tile.isDot()) {
                ((CrossableTile) tile).setDot(false);
                score += 10;
                dotsCounter++;
            } else if (tile.isFruit()) {
                tile.setFruit(false);
                isFruitEaten = true;
                eatenFruit++;
                score += 100;
            }
            if (eatenFruit==2 && dotsCounter==240) dotsCounter=-1;
            myTile.setPacman(false);
            pacman.move(direction);
            tile.setPacman(true);
        }
    }
    public void pacmanHasBeenEaten() {
        pacman.eaten();
        r.pacmanEaten();
        c.pacmanEaten();
        p.pacmanEaten();
        o.pacmanEaten();
    }
    public int collision() {
        for (int i = 1; i < 5; i++) {
            if ((charactersPosition.getX(i) == charactersPosition.getX(0)) &&
                    (charactersPosition.getY(i) == charactersPosition.getY(0))) return i;
        }
        return -1;
    }
    public boolean collisionProcedure(int n) throws InterruptedException {
        //se pacman è super -> i fantasmi sono stati mangiati
        if (pacman.isSuper()) {
            switch (n) {
                case 1: {
                    r.setStatus(3);
                    break;
                }
                case 2: {
                    c.setStatus(3);
                    break;
                }
                case 3: {
                    p.setStatus(3);
                    break;
                }
                case 4: {
                    o.setStatus(3);
                    break;
                }
            }
            return true;
        } else {
            //altrimenti pacman è stato mangiato
            tiles[23][14].setPacman(true);
            pacmanHasBeenEaten();
            return false;
        }
    }

    public boolean isNextTileWall(int direction) {
        switch (direction) {
            case 1: return getLeftTile() instanceof WallTile;
            case 0: return getRightTile() instanceof WallTile;
            case 2: return getUpTile() instanceof WallTile;
            case 3: return getDownTile() instanceof WallTile;
            default: return false;
        }
    }
    public void keepDirection(int direction) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (isNextTileWall(direction)) direction = lastDirection;
        switch (direction) {
            case 1: {
                lastDirection = 1;
                movePacman(1, getLeftTile(), getMyTile());
                break;
            }
            case 0: {
                lastDirection = 0;
                movePacman(0, getRightTile(), getMyTile());
                break;
            }
            case 2: {
                lastDirection = 2;
                movePacman(2, getUpTile(), getMyTile());
                break;
            }
            case 3: {
                lastDirection = 3;
                movePacman(3, getDownTile(), getMyTile());
                break;
            }
        }
    }

    public void startRedGhost(){rThread.start();}
    public void startCyanGhost(){cThread.start();}
    public void startPinkGhost(){pThread.start();}
    public void startOrangeGhost(){oThread.start();}
    public void startSupervisors(){
        gThread.start();
        sThread.start();
    }
}
