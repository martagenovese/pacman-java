package construction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.Scanner;

public class BuildDots extends JFrame implements ActionListener {

    MyButton[][] tiles = new MyButton[36][28];

    public BuildDots() {
        //legge da file le posizioni dei dot e li inserisce nella schermata
        setTitle("Building dots");
        setLayout(new GridLayout(36, 28));
        InputStream f;
        try {
            f = new FileInputStream("src/construction/walls.csv");
            Scanner s = new Scanner(f);
            for (int i = 0; i < 36; i++) {
                for (int j = 0; j < 28; j++) {
                    tiles[i][j] = new MyButton();
                    tiles[i][j].addActionListener(this);
                    tiles[i][j].x = i;
                    tiles[i][j].y = j;
                    tiles[i][j].setBackground(Color.BLACK);
                }
            }
            while (s.hasNextLine()) {
                String[] coordinates = s.nextLine().split(";");
                int i = Integer.parseInt(coordinates[0]);
                int j = Integer.parseInt(coordinates[1]);
                tiles[i][j] = new MyButton();
                tiles[i][j].x = i;
                tiles[i][j].y = j;
                tiles[i][j].setBackground(Color.BLUE);
                //System.out.println(i + " " + j);
            }
            for (int i = 0; i < 36; i++) {
                for (int j = 0; j < 28; j++) {
                    add(tiles[i][j]);
                }
            }
        } catch (
                FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        setSize(224*3, 288*3);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MyButton source = (MyButton) e.getSource();
        source.setBackground(Color.RED);
        try {
            FileWriter fileWriter = new FileWriter("src/construction/dots.csv", true);
            PrintWriter printWriter = new PrintWriter(fileWriter);
            printWriter.printf("%d;%d\n", source.x, source.y);
            printWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BuildDots();
        PrintWriter writer = null;
        try {
            writer = new PrintWriter("src/construction/dots.csv");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        writer.close();
    }
}
