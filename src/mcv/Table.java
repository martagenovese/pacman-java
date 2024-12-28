package mcv;
//
import javax.swing.*;
import java.awt.*;
import characters_classes.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.net.URL;

public class Table extends JFrame {
    protected JLabel[][] tiles;
    protected EventManager eventManager;
    protected Pacman character;
    protected Ghost redGhost, cyanGhost, pinkGhost, orangeGhost;
    protected Image scaledImageDot, scaledImageSFood, scaledImageTardis, scaledImageFruit, scaledImageBrick, scaledImagePacman;


    public Table() {
        setTitle("pacman");
        tiles = new JLabel[36][28];
        setLayout(new GridLayout(36, 28));
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {
                tiles[i][j] = new JLabel();
                tiles[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tiles[i][j].setVerticalAlignment(SwingConstants.CENTER);
                tiles[i][j].setOpaque(true);
                add(tiles[i][j]);
            }
        }
        setSize(224 * 3, 288 * 3);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


        ImageIcon originalIcon = new ImageIcon("src/images/dot.png");
        Image originalImage = originalIcon.getImage();
        scaledImageDot = originalImage.getScaledInstance(10, 10, Image.SCALE_SMOOTH);
        scaledImageSFood = originalImage.getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        originalIcon = new ImageIcon("src/meme/Tardis.png");
        scaledImageTardis = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        originalIcon = new ImageIcon("src/meme/Brick.png");
        scaledImageBrick = originalIcon.getImage().getScaledInstance(25, 25, Image.SCALE_SMOOTH);

        originalIcon = new ImageIcon("src/images/fruit.png");
        scaledImageFruit = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);

        originalIcon = new ImageIcon("src/images/pacman/right.png");
        scaledImagePacman = originalIcon.getImage().getScaledInstance(20, 20, Image.SCALE_SMOOTH);
    }

    public void setEventManager(EventManager eventManager) {
        this.eventManager = eventManager;
        addKeyListener(eventManager);
    }


    public void setDot(int y, int x) {
        tiles[y][x].setIcon(new ImageIcon(scaledImageDot));
    }
    public void setSuperFood(int y, int x) {
        tiles[y][x].setIcon(new ImageIcon(scaledImageSFood));
    }
    public void setTardis(int x, int y){
        tiles[y][x].setIcon(new ImageIcon(scaledImageTardis));
    }
    public void setBrick(int x, int y){
        tiles[y][x].setIcon(new ImageIcon(scaledImageBrick));
    }
    public void clearPacman(int x, int y) {
        tiles[y][x].setIcon(null);
    }

    public void setCharacter(Pacman character) {
        this.character = character;
        tiles[26][13].setIcon(character);
    }
    public void setRedGhost(RedGhost redGhost) {
        this.redGhost = redGhost;
        tiles[17][12].setIcon(redGhost);
    }
    public void setCyanGhost(CyanGhost cyanGhost) {
        this.cyanGhost = cyanGhost;
        tiles[17][13].setIcon(cyanGhost);
    }
    public void setPinkGhost(PinkGhost pinkGhost) {
        this.pinkGhost = pinkGhost;
        tiles[17][14].setIcon(pinkGhost);
    }
    public void setOrangeGhost(OrangeGhost orangeGhost) {
        this.orangeGhost = orangeGhost;
        tiles[17][15].setIcon(orangeGhost);
    }
    public void setScoreBar() {
        String score = "SCORE";
        int xTile = 9;
        for (int i = 0; i < score.length(); i++) {
            tiles[1][xTile].setForeground(Color.WHITE);
            tiles[1][xTile].setFont(new Font("Arial", Font.BOLD, 25));
            tiles[1][xTile].setText(score.charAt(i) + "");
            xTile++;
        }
        tiles[1][18].setForeground(Color.WHITE);
        tiles[1][18].setFont(new Font("Arial", Font.BOLD, 25));
        tiles[1][18].setText("0");
    }
    public void setLives(){
        tiles[35][2].setIcon(new ImageIcon(scaledImagePacman));
        tiles[35][4].setIcon(new ImageIcon(scaledImagePacman));
        tiles[35][6].setIcon(new ImageIcon(scaledImagePacman));
    }
    public void  setFruit() {
        tiles[35][25].setIcon(new ImageIcon(scaledImageFruit));
        tiles[35][23].setIcon(new ImageIcon(scaledImageFruit));
    }
    public void setFruitInTable(int x, int y) {
        if (x==9) tiles[35][23].setIcon(null);
        else tiles[35][25].setIcon(null);
        tiles[y][x].setIcon(new ImageIcon(scaledImageFruit));
    }

    public void playSound(String soundFileName) {
        try {
            URL url = this.getClass().getClassLoader().getResource(soundFileName);
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
    public void endGame(String message, String text, String imagePath) {
        ImageIcon originalIcon = new ImageIcon(imagePath);
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(500, 500, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        JLabel label = new JLabel(text, resizedIcon, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setForeground(Color.BLACK);
        label.setHorizontalTextPosition(JLabel.CENTER);
        label.setVerticalTextPosition(JLabel.BOTTOM);

        JOptionPane optionPane = new JOptionPane(label, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object[]{}, null);

        JDialog dialog = new JDialog();
        dialog.setTitle(message);
        dialog.setModal(true);
        dialog.setContentPane(optionPane);
        dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        dialog.pack();

        Timer timer = new Timer((message.equals("Victory"))?3500:8100, e -> dialog.setVisible(false));
        timer.setRepeats(false);
        timer.start();
        playSound("meme/audio/"+message+".wav");
        dialog.setVisible(true);
        System.exit(0);
    }

    public void updatePosition() {
        tiles[character.getY()][character.getX()].setIcon(character);
    }
    public void clearGhost(int x, int y, boolean isDot, boolean isSuperFood, boolean isFruit, boolean isTardis) {
        if (!isDot && !isSuperFood && !isFruit && !isTardis) tiles[y][x].setIcon(null);
        else if (isSuperFood) setSuperFood(y, x);
        else if (isFruit) setFruitInTable(x, y);
        else if (isTardis) setTardis(x, y);
        else setDot(y, x);
    }
    public void updateGhost(Ghost ghost) {
        tiles[ghost.getY()][ghost.getX()].setIcon(ghost);
    }
    public void updateScore(int score) {
        String scoreString = score+"";
        int xTile = 18;
        for (int i = scoreString.length()-1; i>=0; i--) {
            tiles[1][xTile].setForeground(Color.WHITE);
            tiles[1][xTile].setFont(new Font("Arial", Font.BOLD, 25));
            tiles[1][xTile].setText(scoreString.charAt(i)+"");
            xTile--;
        }
    }
    public void clearTile(int x, int y){
        tiles[y][x].setIcon(null);
    }
}
