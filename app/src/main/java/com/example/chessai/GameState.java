package com.example.chessai;

public class GameState {

    private GameState parent;
    private Character[][] matrix;
    private Boolean whiteTurn;
    private int depth;


    public GameState(){
        parent = null;
        matrix = new Character[8][8];
        whiteTurn = Boolean.TRUE;
        depth = 0;
    }

    public GameState(GameState mparent,Character[][] mmatrix,Boolean mwhiteTurn,int mdepth){
        parent = mparent;
        matrix = mmatrix;
        whiteTurn = mwhiteTurn;
        depth=mdepth;
    }

    public GameState getParent() {
        return parent;
    }

    public Character[][] getMatrix() {
        return matrix;
    }

    public Boolean getWhiteTurn() {
        return whiteTurn;
    }

    public int getDepth() {
        return depth;
    }

    public Boolean nextTurn(){
        return(!whiteTurn);
    }


}
