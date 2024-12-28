package mcv;
//
import characters_classes.Ghost;
import myclasses.My2DSyncArray;
import tiles_classes.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;
import java.util.Timer;
import java.util.TimerTask;

public class EventManager implements KeyListener {
    My2DSyncArray charactersPosition;
    protected Table table;
    protected Model model;
    protected boolean isListenerActive;
    protected int startGhost;

    public EventManager() {
        isListenerActive = true;
        startGhost = 0;
    }

    public void setStartGhost(int n){
        startGhost=n;
    }
    public void setModel(Model model) {
        this.model = model;
        model.getRedGhost().setEventManager(this);
        model.getCyanGhost().setEventManager(this);
        model.getPinkGhost().setEventManager(this);
        model.getOrangeGhost().setEventManager(this);
        model.pacman.setEventManager(this);
        charactersPosition = model.charactersPosition;
        model.supervisor.setEventManager(this);
    }
    public void setTable(Table table) {
        this.table = table;
        //settiamo lo fondo a blu delle caselle che sono muri, tranne quelle al di fuori del campo di gioco
        //nelle caselle che possono essere attraversate da pacman mette i dot
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {
                if (model.tiles[i][j] instanceof WallTile) {
                    if (i<3) table.tiles[i][j].setBackground(Color.BLACK);
                    else if (( (i>12 && i<16) || (i>18 && i<22) ) && (j<5 || j>22)) table.tiles[i][j].setBackground(Color.BLACK);
                    else if (i>33) table.tiles[i][j].setBackground(Color.BLACK);
                    else if (i == 15 && (j == 13 || j == 14)) table.tiles[i][j].setBackground(Color.BLACK);
                    else if ((i>=16 && i<=18) && (j>=11 && j<=16)) table.tiles[i][j].setBackground(Color.BLACK);
                    else table.setBrick(j, i);
                } else if (model.tiles[i][j] instanceof CrossableTile) {
                    CrossableTile tile = (CrossableTile) model.tiles[i][j];
                    if (tile.isDot()) {
                        table.setDot(i, j);
                    } else if (tile.isSuperFood()) {
                        table.setSuperFood(i, j);
                    } else if(tile.isTardis()) {
                        table.setTardis(j,i);
                    }
                    table.tiles[i][j].setBackground(Color.BLACK);
                }
            }
        }
        table.setScoreBar();
        table.setLives();
        table.setFruit();
        table.setCharacter(model.getPacman());
        table.setRedGhost(model.getRedGhost());
        table.setCyanGhost(model.getCyanGhost());
        table.setPinkGhost(model.getPinkGhost());
        table.setOrangeGhost(model.getOrangeGhost());
    }
    public Table getTable() {
        return table;
    }

    public synchronized void disableListenerFor(int milliseconds) {
        isListenerActive = false;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isListenerActive = true;
            }
        }, milliseconds);
    }
    public void clearGhostPosition(Ghost ghost) {
        boolean isDot = model.tiles[ghost.getY()][ghost.getX()].isDot();
        boolean isSuperFood = model.tiles[ghost.getY()][ghost.getX()].isSuperFood();
        boolean isFruit = model.tiles[ghost.getY()][ghost.getX()].isFruit();
        boolean isTardis = model.tiles[ghost.getY()][ghost.getX()].isTardis();
        table.clearGhost(ghost.getX(), ghost.getY(), isDot, isSuperFood, isFruit, isTardis);
    }
    public void updateGhostPosition(Ghost ghost) {
        table.updateGhost(ghost);
    }
    public void muchoMachoPacman() {
        //quando pacman mangia il super food
        table.updatePosition();
        table.clearPacman(model.getPacman().getX(), model.getPacman().getY());
        model.getRedGhost().setScared(true);
        model.getCyanGhost().setScared(true);
        model.getPinkGhost().setScared(true);
        model.getOrangeGhost().setScared(true);
        model.ghostsEaten = 0;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                model.pacman.setSuper(false);
                model.getRedGhost().setScared(false);
                model.getCyanGhost().setScared(false);
                model.getPinkGhost().setScared(false);
                model.getOrangeGhost().setScared(false);
            }
        }, 7000);
    }
    public void stopGame(boolean victory) {
        charactersPosition.setX(0,0);
        charactersPosition.setY(0,0);
        if (victory) {
            table.endGame("Victory!", "<html>Hai vinto!<br>Adesso puoi rubare questo gatto</html>", "src/meme/vittoria.jpg");
        } else {
            table.endGame("Defeat!", "<html>Hai perso.<br>Ora il tipo del quadro di vienna è sotto al tuo letto</html>", "src/meme/sconfitta.jpg");
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (isListenerActive) {
            disableListenerFor(150);
            int key = e.getKeyCode();
            int d;
            
            if (key == KeyEvent.VK_LEFT) { d = 1;
            } else if (key == KeyEvent.VK_RIGHT) { d = 0;
            } else if (key == KeyEvent.VK_UP) { d = 2;
            } else if (key == KeyEvent.VK_DOWN) { d = 3;
            } else return;

            //la prima volta che viene mosso pacman partono anche i fantasmi
            if (startGhost==0) {
                table.playSound("meme/audio/musichetta.wav");
                model.startRedGhost();
                model.startCyanGhost();
                model.startPinkGhost();
                model.startOrangeGhost();
                model.startSupervisors();
                startGhost = 1;
            }else if(startGhost==2){
                //fa partire i fantasmi dopo che hanno mangiato pacman
                model.r.setStatus(5);
                model.c.setStatus(5);
                model.p.setStatus(5);
                model.o.setStatus(5);
                startGhost=1;
            }

            int fruit = model.getFruit();
            boolean pacmanStatus1 = model.getPacman().isSuper();
            int dotEaten = model.dotsCounter;
            table.clearPacman(model.getPacman().getX(), model.getPacman().getY());
            if(model.getPacman().getX()==0)  {
                table.playSound("meme/audio/Tardis.wav");
                model.setTardis(0,17);
                table.setTardis(0,17);
            } else if(model.getPacman().getX()==27) {
                table.playSound("meme/audio/Tardis.wav");
                model.setTardis(27,17);
                table.setTardis(27,17);
            }
            try {
                model.keepDirection(d);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException ex) {
                throw new RuntimeException(ex);
            }
            boolean pacmanStatus2 = model.getPacman().isSuper();
            if (!pacmanStatus1 && pacmanStatus2) table.playSound("meme/audio/cibo.wav");
            int fruitAfter = model.getFruit();
            if (fruit != fruitAfter) {
                if (fruitAfter == 0) {
                    model.setFruit(9, 17);
                    table.setFruitInTable(9, 17);
                } else {
                    model.setFruit(18, 17);
                    table.setFruitInTable(18, 17);
                }
            }
            if (model.isFruitEaten) table.playSound("meme/audio/crocchi.wav");
            if (Math.random()<0.025) table.playSound("meme/audio/bubii/"+(int) (Math.random()*7+1) + ".wav");
            table.updateScore(model.getScore());
            model.updatePosition();
            table.updatePosition();
            if (model.dotsCounter<0 && model.fruit==0) stopGame(true);
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
}
