package com.example.chessai;

public class Coordinates {


    private int x;
    private int y;

    public Coordinates(){
        x = 0;
        y = 0;

    }

    public Coordinates(int row, int column){
        x = row;
        y = column;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }
}
