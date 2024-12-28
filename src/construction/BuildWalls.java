package construction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class BuildWalls extends JFrame implements ActionListener {
    MyButton[][] tiles = new MyButton[36][28];

    public BuildWalls() {
        setTitle("Building walls");

        setLayout(new GridLayout(36, 28));
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 28; j++) {
                tiles[i][j] = new MyButton();
                tiles[i][j].x = i;
                tiles[i][j].y = j;
                tiles[i][j].addActionListener(this);
                add(tiles[i][j]);
            }
        }

        setSize(224*2, 288*2);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);


    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MyButton source = (MyButton) e.getSource();
        source.setBackground(Color.BLUE);
        try {
            FileWriter fileWriter = new FileWriter("src/construction/walls.csv", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf("%d;%d\n", source.x, source.y);
            printWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    public static void main(String[] args) throws FileNotFoundException {
        new BuildWalls();
        PrintWriter writer = new PrintWriter("src/construction/walls.csv");
        writer.close();
    }
}
