package com.example.chessai;

//this class contains the result from the pruning. We won't to keep track of the best state, nodes explored and function values
public class PruningResult {
    protected double functionValue;
    protected int nodesExplored;
    protected GameState bestState;

    public double getFunctionValue() {
        return functionValue;
    }

    public void setFunctionValue(double functionValue) {
        this.functionValue = functionValue;
    }

    public int getNodesExplored() {
        return nodesExplored;
    }

    public void setNodesExplored(int nodesExplored) {
        this.nodesExplored = nodesExplored;
    }

    public GameState getBestState() {
        return bestState;
    }

    public void setBestState(GameState bestState) {
        this.bestState = bestState;
    }



    public PruningResult(double x, GameState z, int y){
        functionValue=x;
        nodesExplored=y;
        bestState=z;
    }

}
