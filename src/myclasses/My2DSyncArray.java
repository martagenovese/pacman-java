package myclasses;

import java.awt.*;

public class My2DSyncArray {
    private Point[] array;

    public My2DSyncArray(int rows) {
        array = new Point[rows];
        for (int i = 0; i < rows; i++) {
            array[i] = new Point();
        }
    }
    public synchronized void setX(int row, int value) {
        array[row].x = value;
    }
    public synchronized void setY(int row, int value) {
        array[row].y = value;
    }
    public synchronized int getX(int row) {
        return array[row].x;
    }
    public synchronized int getY(int row) {
        return array[row].y;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Point point : array) {
            sb.append("[").append(point).append("]\n");
        }
        return sb.toString();
    }
}
