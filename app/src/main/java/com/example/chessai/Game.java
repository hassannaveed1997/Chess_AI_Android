package com.example.chessai;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static com.example.chessai.MainActivity.getPuzzleSelection;
import static com.example.chessai.MainActivity.isAI;
import static com.example.chessai.MainActivity.isPuzzle;
import static com.example.chessai.MainActivity.puzzle;

public class Game extends AppCompatActivity {
    //the default board
    Character[][] board =  {{'r','n','b','q','k','b','n','r'},{'p','p','p','p','p','p','p','p'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','_','_','_'},{'P','P','P','P','P','P','P','P'},{'R','N','B','Q','K','B','N','R'}};

    //for the different puzzles and the number of moves left
    Character[][] puzzle1 = {{'_','_','_','_','_','_','q','k'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','P','_','p'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','_','Q','P'},{'_','_','_','_','_','P','P','_'},{'_','_','_','_','R','_','K','_'}};
    Character[][] puzzle2 = {{'_','_','B','_','_','_','_','_'},{'_','_','_','_','_','_','_','_'},{'_','_','_','K','_','_','_','_'},{'_','p','_','_','_','_','_','_'},{'_','_','k','_','_','_','_','_'},{'P','_','_','_','_','P','_','_'},{'_','B','_','_','_','_','_','_'},{'N','_','_','_','_','N','_','_'}};
    Character[][] puzzle3 = {{'_','_','_','_','_','_','_','_'},{'_','_','_','K','_','_','_','_'},{'_','_','R','_','P','_','_','_'},{'_','P','_','k','r','_','_','_'},{'_','_','_','N','p','b','_','_'},{'_','_','_','_','P','_','_','_'},{'_','_','_','_','_','_','_','_'},{'_','_','_','_','_','N','_','_'}};
    int movesLeft;

    final String mTAG = "ChessAI"; //for debugging purposes
    ArrayList<Coordinates> highlight; //the list of places currently highlighted
    Coordinates highlightedPiece; //the position of the piece currently highlighted

    boolean AI;
    int maxDepth = 3;

    GameState currentState;
    //initialize all buttons:
    ImageButton[][] buttons = new ImageButton[8][8];

    boolean whiteTurn = true; //whether its the white player's turn
    boolean selected = false; //whether a piece is currently selected
    boolean processingNextMove = false; //whether the AI is busy processing the next move

    TextView headingText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_layout);

        initializeButtons();

        headingText = findViewById(R.id.headingText);
        if(puzzle) {

            //if its a puzzle set the board with the puzzle state
            if(getPuzzleSelection().equals("1")){
                currentState = new GameState(null, puzzle1, true, 0);
                movesLeft = 3;
            }else if((getPuzzleSelection().equals("2"))){
                currentState = new GameState(null, puzzle2, true, 0);
                movesLeft = 3;
            }else if((getPuzzleSelection().equals("3"))){
                currentState = new GameState(null, puzzle3, true, 0);
                movesLeft = 3;
            }

            headingText.setText("You have "+ movesLeft+ " moves left");

        }else{
            //set the currentState with empty board
            currentState = new GameState(null, board, true, 0);
        }
        //refresh the board
        refreshGame(currentState);

        //get information whether it is a single player or multiplayer game
        AI = isAI();

        Button backButton = findViewById(R.id.backButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go back and destroy the ChangeCityController
                finish();
            }
        });
    }

    //method for initializing buttons
    private void initializeButtons(){
        buttons[0][0] =  findViewById(R.id.imageButton00);
        buttons[0][1] =  findViewById(R.id.imageButton01);
        buttons[0][2] =  findViewById(R.id.imageButton02);
        buttons[0][3] =  findViewById(R.id.imageButton03);
        buttons[0][4] =  findViewById(R.id.imageButton04);
        buttons[0][5] =  findViewById(R.id.imageButton05);
        buttons[0][6] =  findViewById(R.id.imageButton06);
        buttons[0][7] =  findViewById(R.id.imageButton07);

        buttons[1][0] =  findViewById(R.id.imageButton10);
        buttons[1][1] =  findViewById(R.id.imageButton11);
        buttons[1][2] =  findViewById(R.id.imageButton12);
        buttons[1][3] =  findViewById(R.id.imageButton13);
        buttons[1][4] =  findViewById(R.id.imageButton14);
        buttons[1][5] =  findViewById(R.id.imageButton15);
        buttons[1][6] =  findViewById(R.id.imageButton16);
        buttons[1][7] =  findViewById(R.id.imageButton17);

        buttons[2][0] =  findViewById(R.id.imageButton20);
        buttons[2][1] =  findViewById(R.id.imageButton21);
        buttons[2][2] =  findViewById(R.id.imageButton22);
        buttons[2][3] =  findViewById(R.id.imageButton23);
        buttons[2][4] =  findViewById(R.id.imageButton24);
        buttons[2][5] =  findViewById(R.id.imageButton25);
        buttons[2][6] =  findViewById(R.id.imageButton26);
        buttons[2][7] =  findViewById(R.id.imageButton27);

        buttons[3][0] =  findViewById(R.id.imageButton30);
        buttons[3][1] =  findViewById(R.id.imageButton31);
        buttons[3][2] =  findViewById(R.id.imageButton32);
        buttons[3][3] =  findViewById(R.id.imageButton33);
        buttons[3][4] =  findViewById(R.id.imageButton34);
        buttons[3][5] =  findViewById(R.id.imageButton35);
        buttons[3][6] =  findViewById(R.id.imageButton36);
        buttons[3][7] =  findViewById(R.id.imageButton37);

        buttons[4][0] =  findViewById(R.id.imageButton40);
        buttons[4][1] =  findViewById(R.id.imageButton41);
        buttons[4][2] =  findViewById(R.id.imageButton42);
        buttons[4][3] =  findViewById(R.id.imageButton43);
        buttons[4][4] =  findViewById(R.id.imageButton44);
        buttons[4][5] =  findViewById(R.id.imageButton45);
        buttons[4][6] =  findViewById(R.id.imageButton46);
        buttons[4][7] =  findViewById(R.id.imageButton47);

        buttons[5][0] =  findViewById(R.id.imageButton50);
        buttons[5][1] =  findViewById(R.id.imageButton51);
        buttons[5][2] =  findViewById(R.id.imageButton52);
        buttons[5][3] =  findViewById(R.id.imageButton53);
        buttons[5][4] =  findViewById(R.id.imageButton54);
        buttons[5][5] =  findViewById(R.id.imageButton55);
        buttons[5][6] =  findViewById(R.id.imageButton56);
        buttons[5][7] =  findViewById(R.id.imageButton57);

        buttons[6][0] =  findViewById(R.id.imageButton60);
        buttons[6][1] =  findViewById(R.id.imageButton61);
        buttons[6][2] =  findViewById(R.id.imageButton62);
        buttons[6][3] =  findViewById(R.id.imageButton63);
        buttons[6][4] =  findViewById(R.id.imageButton64);
        buttons[6][5] =  findViewById(R.id.imageButton65);
        buttons[6][6] =  findViewById(R.id.imageButton66);
        buttons[6][7] =  findViewById(R.id.imageButton67);

        buttons[7][0] =  findViewById(R.id.imageButton70);
        buttons[7][1] =  findViewById(R.id.imageButton71);
        buttons[7][2] =  findViewById(R.id.imageButton72);
        buttons[7][3] =  findViewById(R.id.imageButton73);
        buttons[7][4] =  findViewById(R.id.imageButton74);
        buttons[7][5] =  findViewById(R.id.imageButton75);
        buttons[7][6] =  findViewById(R.id.imageButton76);
        buttons[7][7] =  findViewById(R.id.imageButton77);

        buttons[0][0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(mTAG,"button pressed "+ 0+0);
                buttonPressed(0,0);
            }
        });
        buttons[0][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+1); buttonPressed(0,1);}});
        buttons[0][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+2); buttonPressed(0,2);}});
        buttons[0][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+3); buttonPressed(0,3);}});
        buttons[0][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+4); buttonPressed(0,4);}});
        buttons[0][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+5); buttonPressed(0,5);}});
        buttons[0][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+6); buttonPressed(0,6);}});
        buttons[0][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 0+7); buttonPressed(0,7);}});
        buttons[1][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+0); buttonPressed(1,0);}});
        buttons[1][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+1); buttonPressed(1,1);}});
        buttons[1][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+2); buttonPressed(1,2);}});
        buttons[1][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+3); buttonPressed(1,3);}});
        buttons[1][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+4); buttonPressed(1,4);}});
        buttons[1][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+5); buttonPressed(1,5);}});
        buttons[1][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+6); buttonPressed(1,6);}});
        buttons[1][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 1+7); buttonPressed(1,7);}});
        buttons[2][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+0); buttonPressed(2,0);}});
        buttons[2][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+1); buttonPressed(2,1);}});
        buttons[2][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+2); buttonPressed(2,2);}});
        buttons[2][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+3); buttonPressed(2,3);}});
        buttons[2][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+4); buttonPressed(2,4);}});
        buttons[2][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+5); buttonPressed(2,5);}});
        buttons[2][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+6); buttonPressed(2,6);}});
        buttons[2][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 2+7); buttonPressed(2,7);}});
        buttons[3][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+0); buttonPressed(3,0);}});
        buttons[3][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+1); buttonPressed(3,1);}});
        buttons[3][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+2); buttonPressed(3,2);}});
        buttons[3][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+3); buttonPressed(3,3);}});
        buttons[3][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+4); buttonPressed(3,4);}});
        buttons[3][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+5); buttonPressed(3,5);}});
        buttons[3][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+6); buttonPressed(3,6);}});
        buttons[3][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 3+7); buttonPressed(3,7);}});
        buttons[4][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+0); buttonPressed(4,0);}});
        buttons[4][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+1); buttonPressed(4,1);}});
        buttons[4][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+2); buttonPressed(4,2);}});
        buttons[4][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+3); buttonPressed(4,3);}});
        buttons[4][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+4); buttonPressed(4,4);}});
        buttons[4][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+5); buttonPressed(4,5);}});
        buttons[4][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+6); buttonPressed(4,6);}});
        buttons[4][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 4+7); buttonPressed(4,7);}});
        buttons[5][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+0); buttonPressed(5,0);}});
        buttons[5][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+1); buttonPressed(5,1);}});
        buttons[5][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+2); buttonPressed(5,2);}});
        buttons[5][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+3); buttonPressed(5,3);}});
        buttons[5][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+4); buttonPressed(5,4);}});
        buttons[5][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+5); buttonPressed(5,5);}});
        buttons[5][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+6); buttonPressed(5,6);}});
        buttons[5][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 5+7); buttonPressed(5,7);}});
        buttons[6][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+0); buttonPressed(6,0);}});
        buttons[6][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+1); buttonPressed(6,1);}});
        buttons[6][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+2); buttonPressed(6,2);}});
        buttons[6][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+3); buttonPressed(6,3);}});
        buttons[6][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+4); buttonPressed(6,4);}});
        buttons[6][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+5); buttonPressed(6,5);}});
        buttons[6][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+6); buttonPressed(6,6);}});
        buttons[6][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 6+7); buttonPressed(6,7);}});
        buttons[7][0].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+0); buttonPressed(7,0);}});
        buttons[7][1].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+1); buttonPressed(7,1);}});
        buttons[7][2].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+2); buttonPressed(7,2);}});
        buttons[7][3].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+3); buttonPressed(7,3);}});
        buttons[7][4].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+4); buttonPressed(7,4);}});
        buttons[7][5].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+5); buttonPressed(7,5);}});
        buttons[7][6].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+6); buttonPressed(7,6);}});
        buttons[7][7].setOnClickListener(new View.OnClickListener() {@Override public void onClick(View v) { Log.d(mTAG,"button pressed "+ 7+7); buttonPressed(7,7);}});

    }


    //method to make the board using a 2D array of characters
    private void refreshGame(GameState myCurrentState){
        Character[][] board = myCurrentState.getMatrix();
        for(int i=0; i < 8;i++){
            for(int j=0; j< 8; j++){
                //statement for each character to immage
                if(board[i][j] == 'k'){
                    buttons[i][j].setImageResource(R.drawable.kingblack);
                } else if(board[i][j] == 'b'){
                    buttons[i][j].setImageResource(R.drawable.bishopblack);
                }else if(board[i][j] == 'r') {
                    buttons[i][j].setImageResource(R.drawable.rookblack);
                }else if(board[i][j] == 'n') {
                    buttons[i][j].setImageResource(R.drawable.knightblack);
                }else if(board[i][j] == 'q') {
                    buttons[i][j].setImageResource(R.drawable.queenblack);
                }else if(board[i][j] == 'p') {
                    buttons[i][j].setImageResource(R.drawable.pawnblack);
                } else if(board[i][j] == 'K'){
                    buttons[i][j].setImageResource(R.drawable.kingwhite);
                } else if(board[i][j] == 'B'){
                    buttons[i][j].setImageResource(R.drawable.bishopwhite);
                }else if(board[i][j] == 'R') {
                    buttons[i][j].setImageResource(R.drawable.rookwhite);
                }else if(board[i][j] == 'N') {
                    buttons[i][j].setImageResource(R.drawable.knightwhite);
                }else if(board[i][j] == 'Q') {
                    buttons[i][j].setImageResource(R.drawable.queenwhite);
                }else if(board[i][j] == 'P') {
                    buttons[i][j].setImageResource(R.drawable.pawnwhite);
                }else if(board[i][j] == '_') {
                    buttons[i][j].setImageResource(R.drawable.blanks);
                }
            }
        }
    }

    //takes action when button is pressed
    private void buttonPressed(int i, int j){

        //first see if AI is busy processing the next move. if AI is busy, then method just returns.
        if(processingNextMove){
            return;
        }
        //get required information from the current state matrix
        Character charAtPos = currentState.getMatrix()[i][j];
        whiteTurn = currentState.getWhiteTurn();



    //if nothing has been selected yet see if player pressed a valid piece for turn
        if(!selected){
            //if it is the white's turn and one of its pieces is selected
            if(Character.isUpperCase(charAtPos) & whiteTurn) {
                highlight = validMoves(currentState,new Coordinates(i,j));
                highlightedPiece = new Coordinates(i,j);
                for(int index = 0; index < highlight.size(); index++){
                    int x = highlight.get(index).getX();
                    int y = highlight.get(index).getY();
                    buttons[x][y].setBackgroundColor(Color.BLUE);
                    //Log.d(mTAG,"highlighted"+x+y);
                }
                //highlight the selected piece
                buttons[highlightedPiece.getX()][highlightedPiece.getY()].setBackgroundColor(Color.GRAY);
                //change selected state
                selected = true;

            }
            //if it is the black players turn and one of its pieces are selected
            else if(Character.isLowerCase(charAtPos) & !whiteTurn) {
                highlight = validMoves(currentState,new Coordinates(i,j));
                highlightedPiece = new Coordinates(i,j);
                for(int index = 0; index < highlight.size(); index++){
                    int x = highlight.get(index).getX();
                    int y = highlight.get(index).getY();
                    buttons[x][y].setBackgroundColor(Color.BLUE);
                    //Log.d(mTAG,"highlighted"+x+y);
                }
                //highlight the selected piece
                buttons[highlightedPiece.getX()][highlightedPiece.getY()].setBackgroundColor(Color.GRAY);
                //change selected state
                selected = true;

            } else {
                Log.d(mTAG,"no piece exists at this position");
            }
        }
        //if something has been selected, see if player selected a valid move. if invalid, then selection resets
        else{
            boolean validSelection = false; //if the selection was valid or not
            for(int index = 0; index < highlight.size(); index++){
                if(i == highlight.get(index).getX() & j == highlight.get(index).getY()){
                    validSelection = true;
                }
            }

            if(validSelection){
                //if the selection was valid then move piece to the position and update states
                Character[][] matrix = currentState.getMatrix();
                matrix[i][j] = matrix[highlightedPiece.getX()][highlightedPiece.getY()];
                matrix[highlightedPiece.getX()][highlightedPiece.getY()] = '_';

                //see if the game ended or not. If the game ends, the method will print dialog and exit
                isEndGame(currentState);

                //increment state matrix
                currentState = new GameState(null,matrix,currentState.nextTurn(),0);
                refreshGame(currentState);
                selected = false;

                //if AI is enabled, then run the next move for the black player.
                if(AI) {
                    processingNextMove = true; //to prevent any changes while processing
                    PruningResult myResult = alpha_beta_min(currentState, -10000000, 10000000, 0);
                    Log.d(mTAG, "Nodes Explored: " + myResult.getNodesExplored());
                    Log.d(mTAG, "Score: " + myResult.getFunctionValue());
                    processingNextMove = false; //we are now done processing
                    currentState = myResult.getBestState();
                    refreshGame(currentState);

                    //see if the game ended or not. If the game ends, the method will print dialog and exit
                    if(isEndGame(currentState)){
                        return;
                    };

                    whiteTurn = currentState.getWhiteTurn(); //its white player's turn again

                    //if this is a puzzle, change the number of moves left
                    if(isPuzzle()){

                        //decrease the number of moves left and show it to the player. if we run out of moves, exit.
                        movesLeft = movesLeft-1;
                        headingText.setText("You have "+ movesLeft+ " moves left");
                        //if we run out of moves, make the player exit.
                        if(movesLeft == 0){
                            //dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("You ran out of moves");
                            builder.setTitle("Failed");
                            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // User clicked OK button, the game exits
                                    finish();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }

                    }
                }

            } else{
                //if selection was not valid
                selected = false;
            }
            //reset highlights for possible moves
            for(int index = 0; index < highlight.size(); index++){
                int x = highlight.get(index).getX();
                int y = highlight.get(index).getY();
                if((x+y)%2 ==0) {
                    buttons[x][y].setBackgroundColor(Color.WHITE);
                } else{
                    buttons[x][y].setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
                }
                //Log.d(mTAG,"unhighlighted"+x+y);
            }
            //reset highlights for selected piece
            int x = highlightedPiece.getX();
            int y = highlightedPiece.getY();
            if((x+y)%2 ==0) {
                buttons[x][y].setBackgroundColor(Color.WHITE);
            } else{
                buttons[x][y].setBackgroundColor(getResources().getColor(R.color.colorPrimary,getTheme()));
            }



        }

    }
    //method that explore all the available pieces
    private ArrayList<Coordinates> explore(GameState myCurrentState){
        //get required variables from state.
        ArrayList<Coordinates> pieces = new ArrayList<>();
        //the arraylist will contain locations of all of the player's pieces
        boolean myWhiteTurn = myCurrentState.getWhiteTurn();
        Character[][] myMatrix = myCurrentState.getMatrix();

        for(int i =0;i<8;i++){
            for(int j = 0;j<8;j++){
                if(myWhiteTurn){
                    if(Character.isUpperCase(myMatrix[i][j])){
                        pieces.add(new Coordinates(i,j));
                    }
                }
                else{
                    if(Character.isLowerCase(myMatrix[i][j])){
                        pieces.add(new Coordinates(i,j));
                    }
                }

            }
        }
        //randomize just so it doesn't keep moving the same piece back and forth when nothing is helpful
        Collections.shuffle(pieces);

        return pieces;
    }

    //method that calculates the evaluation function
    private double evalFunction(GameState myCurrentState){
        double functionSum = 0;
        Character[][] myMatrix = myCurrentState.getMatrix();

        //loop through and add/subtract from functionsum accordingly
        for(int i =0;i<8;i++){
            for(int j = 0;j<8;j++){
                if (myMatrix[i][j].equals('K')) {
                    functionSum =  functionSum+ 10000;
                }else if (myMatrix[i][j].equals('Q')) {
                    functionSum =  functionSum+ 8;
                }else if (myMatrix[i][j].equals('R')) {
                    functionSum =  functionSum+ 5;
                }else if (myMatrix[i][j].equals('B')) {
                    functionSum =  functionSum+ 3;
                }else if (myMatrix[i][j].equals('N')) {
                    functionSum =  functionSum+ 3;
                }else if (myMatrix[i][j].equals('P')) {
                    functionSum =  functionSum+ 1;
                }else if (myMatrix[i][j].equals('k')) {
                    functionSum =  functionSum-10000;
                }else if (myMatrix[i][j].equals('q')) {
                    functionSum =  functionSum-8;
                }else if (myMatrix[i][j].equals('r')) {
                    functionSum =  functionSum- 5;
                }else if (myMatrix[i][j].equals('b')) {
                    functionSum =  functionSum- 3;
                }else if (myMatrix[i][j].equals('n')) {
                    functionSum =  functionSum- 3;
                }else if (myMatrix[i][j].equals('p')) {
                    functionSum =  functionSum- 1;
                }

            }
        }
        //decrease depending on how far away the value occurs (to prefer closer solutions first)
        functionSum = functionSum * (1-( myCurrentState.getDepth()*0.0001));
        return functionSum;
    }

    //method that checks if the game has ended, and exits appropriately
    private boolean isEndGame(GameState currentState){
        boolean whiteWin = true;
        boolean blackWin = true;
        Character[][] matrix = currentState.getMatrix();

        //see if the white king and black king are present
        for(int i =0;i<8;i++){
            for(int j = 0;j<8;j++){
                if(matrix[i][j].equals('K')){
                    blackWin = false;
                } else if(matrix[i][j].equals('k')){
                    whiteWin = false;
                }

            }
        }
        //if white king isn't there, black player wins
        if(whiteWin){
            //make a dialog box informing of the win.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("The white player has won!");
            builder.setTitle("Game Over");
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button, the game exits
                    finish();
                }
            });


            AlertDialog dialog = builder.create();
            dialog.show();

            Log.d(mTAG,"the white player has won");
        } else if(blackWin){
            //make a dialog box informing of the win.
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("The black player has won!");
            builder.setTitle("Game Over");
            builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // User clicked OK button, the game exits
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
            Log.d(mTAG,"the black player has won");
        }

        return(whiteWin || blackWin);

    }

    private PruningResult alpha_beta_min(GameState myCurrentState, double alpha, double beta, int myDepth){
        //keep track of the nodes explored
        int nodesExplored = 0;
        //keep track of the bestState
        GameState bestState = null;

        if(myDepth == maxDepth){
            return new PruningResult(evalFunction(myCurrentState),null,1);
        }
        //get all the pieces for the current player
        ArrayList<Coordinates> pieces = explore(myCurrentState);

        for(int index = 0; index < pieces.size(); index++){
            //as we go through the pieces, we retrieve all the possible moves for a piece
            ArrayList<Coordinates> moves = validMoves(myCurrentState,pieces.get(index));
            //shuffle the moves too for randomness in exploration
            Collections.shuffle(moves);

            //iterate through the moves
            for(int moveIndex = 0; moveIndex < moves.size();moveIndex++){
                GameState nextState = move(myCurrentState,pieces.get(index),moves.get(moveIndex));
                PruningResult myResult = alpha_beta_max(nextState,alpha,beta, nextState.getDepth());
                nodesExplored = nodesExplored + myResult.getNodesExplored();

                if(bestState == null){
                    bestState = nextState;
                }
                if(myResult.getFunctionValue() <= alpha){ //if alpha cutoff is exceeded
                    return new PruningResult(myResult.getFunctionValue(),nextState,nodesExplored);
                }
                if(myResult.getFunctionValue() < beta){
                    //beta is used to keep the minimum value
                    bestState = nextState;
                    beta = myResult.getFunctionValue();
                }


            }

        }
        return new PruningResult(beta,bestState,nodesExplored);

    }

    private PruningResult alpha_beta_max(GameState myCurrentState, double alpha, double beta, int myDepth){
        //keep track of the nodes explored
        int nodesExplored = 0;
        //keep track of the bestState
        GameState bestState = null;

        if(myDepth == maxDepth){
            return new PruningResult(evalFunction(myCurrentState),null,1);
        }
        //get all the pieces for the current player
        ArrayList<Coordinates> pieces = explore(myCurrentState);

        for(int index = 0; index < pieces.size(); index++){
            //as we go through the pieces, we retrieve all the possible moves for a piece
            ArrayList<Coordinates> moves = validMoves(myCurrentState,pieces.get(index));
            //shuffle the moves too for randomness in exploration
            Collections.shuffle(moves);

            //iterate through the moves
            for(int moveIndex = 0; moveIndex < moves.size();moveIndex++){
                GameState nextState = move(myCurrentState,pieces.get(index),moves.get(moveIndex));
                PruningResult myResult = alpha_beta_min(nextState,alpha,beta, nextState.getDepth());
                nodesExplored = nodesExplored + myResult.getNodesExplored();

                if(bestState == null){
                    bestState = nextState;
                }
                if(myResult.getFunctionValue() >= beta){ //if beta cutoff is exceeded
                    return new PruningResult(myResult.getFunctionValue(),nextState,nodesExplored);
                }
                if(myResult.getFunctionValue() > alpha){
                    //alpha is used to keep the maximum value
                    bestState = nextState;
                    alpha = myResult.getFunctionValue();
                }


            }

        }

        return new PruningResult(alpha,bestState,nodesExplored);


    }

    //Method: Move
    //Paramters:
    //currentState: the state we are currenlty at
    //posFrom: The position of the piece currenlty
    //posTo: The position we want to move the piece to
    //Returns: A new state, with the updated board and the next
    //player's turn
    private GameState move(GameState myCurrentState, Coordinates posFrom, Coordinates posTo){
        //make a deep copy
        Character[][] tempMatrix = new Character[8][];
        for(int index =0; index < 8; index++){
            tempMatrix[index] = Arrays.copyOf(myCurrentState.getMatrix()[index],8);
        }

        tempMatrix[posTo.getX()][posTo.getY()] = tempMatrix[posFrom.getX()][posFrom.getY()];
        tempMatrix[posFrom.getX()][posFrom.getY()] = '_';

        return new GameState(myCurrentState,tempMatrix,myCurrentState.nextTurn(),myCurrentState.getDepth()+1);
    }

    //method that returns valid moves available for the piece
    private ArrayList<Coordinates> validMoves(GameState currentState, Coordinates mCoordinates){
        ArrayList<Coordinates> moves =  new ArrayList<Coordinates>();
        Character[][] matrix = currentState.getMatrix();
        Boolean whiteTurn = currentState.getWhiteTurn();
        int x = mCoordinates.getX();
        int y = mCoordinates.getY();
        //for white pawn
        if(matrix[x][y] == 'P' && whiteTurn) {
            if(x == 6) {//in the initial position add a possible movement of two
                if (matrix[x-2][y] == '_'){
                    moves.add(new Coordinates((x - 2), y));
                }

            }
            if (x > 0){ //in all other positions, add one movement or possible attack
                if (matrix[x-1][y] == '_') { //move straight
                 moves.add(new Coordinates((x-1),y));
                }
                if(y>0){
                    if(  (Character.isLowerCase(matrix[x-1][y-1]))){
                        moves.add(new Coordinates((x-1),(y-1)));
                    }
                }
                if(y<7){
                    if ((Character.isLowerCase(matrix[x-1][y+1]))){
                        moves.add(new Coordinates((x-1),(y+1)));
                    }
                }
            }
        }


        //for black pawn
        else if(matrix[x][y] == 'p' && !whiteTurn) {
            if(x == 1) {//in the initial position add a possible movement of two
                if (matrix[x+2][y] == '_'){
                    moves.add(new Coordinates((x + 2), y));
                }

            }
            if (x < 7){ //in all other positions, add one movement or possible attack
                if (matrix[x+1][y] == '_') { //move straight
                    moves.add(new Coordinates((x+1),y));
                }
                if(y>0){
                    if((Character.isUpperCase(matrix[x+1][y-1]))){
                        moves.add(new Coordinates((x+1),(y-1)));
                    }
                }
                if(y<7){
                    if ( (Character.isUpperCase(matrix[x+1][y+1]))){
                        moves.add(new Coordinates((x+1),(y+1)));
                    }
                }
            }
        }

        //for white king
        else if(matrix[x][y]=='K' && whiteTurn){
            for(int i = x-1;i<(x+2);i++){
                if (i >=0 && i<=7){
                    for(int j=y-1;j <(y+2);j++){
                        if(j>=0 && j<= 7){
                            if(!Character.isUpperCase(matrix[i][j])){
                                moves.add(new Coordinates(i,j));
                            }else{
                            }

                        }
                    }
                }
            }
        }

        //for black king
        else if(matrix[x][y]=='k' && !whiteTurn){
            for(int i = x-1;i<x+2;i++){
                if ((i >=0) && (i<=7)){
                    for(int j=y-1;j <y+2;j++){
                        if((j>=0) && (j<= 7)){
                            if(!Character.isLowerCase(matrix[i][j])){
                                moves.add(new Coordinates(i,j));
                            }
                        }
                    }
                }
            }
        }

        //for white rook
        else if(matrix[x][y]=='R' && whiteTurn){
            for(int j= y-1;j>=0;j--){
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isLowerCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int j= y+1;j<8;j++){
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isLowerCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int i= x+1;i<8;i++){
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isLowerCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }

            for(int i= x-1;i>=0;i--){
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isLowerCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }
        }


        //for black rook
        else if(matrix[x][y]=='r' && !whiteTurn){
            for(int j= y-1;j>=0;j--){//left
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isUpperCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int j= y+1;j<8;j++){//right
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isUpperCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int i= x+1;i<8;i++){//down
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isUpperCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }

            for(int i= x-1;i>=0;i--){//up
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isUpperCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }
        }

        //for white knight
        else if(matrix[x][y]=='N' && whiteTurn){
            if((x-1) >= 0 && (y-2)>=0){
                if(!Character.isUpperCase(matrix[x-1][y-2])){
                    moves.add(new Coordinates(x-1,y-2));
                }
            }
            if((x-2) >= 0 && (y-1)>=0){
                if(!Character.isUpperCase(matrix[x-2][y-1])){
                    moves.add(new Coordinates(x-2,y-1));
                }
            }
            if((x+1) <= 7 && (y-2)>=0){
                if(!Character.isUpperCase(matrix[x+1][y-2])){
                    moves.add(new Coordinates(x+1,y-2));
                }
            }
            if((x+2) <=7 && (y-1)>=0){
                if(!Character.isUpperCase(matrix[x+2][y-1])){
                    moves.add(new Coordinates(x+2,y-1));
                }
            }
            if((x-2) >= 0 && (y+1)<=7){
                if(!Character.isUpperCase(matrix[x-2][y+1])){
                    moves.add(new Coordinates(x-2,y+1));
                }
            }
            if((x-1) >= 0 && (y+2)<=7){
                if(!Character.isUpperCase(matrix[x-1][y+2])){
                    moves.add(new Coordinates(x-1,y+2));
                }
            }
            if((x+2) <=7 && (y+1)<=7){
                if(!Character.isUpperCase(matrix[x+2][y+1])){
                    moves.add(new Coordinates(x+2,y+1));
                }
            }
            if((x+1) <= 7 && (y+2)<=7){
                if(!Character.isUpperCase(matrix[x+1][y+2])){
                    moves.add(new Coordinates(x+1,y+2));
                }
            }
        }

        //for black knight
        else if(matrix[x][y]=='n' && !whiteTurn){
            if((x-1) >= 0 && (y-2)>=0){
                if(!Character.isLowerCase(matrix[x-1][y-2])){
                    moves.add(new Coordinates(x-1,y-2));
                }
            }
            if((x-2) >= 0 && (y-1)>=0){
                if(!Character.isLowerCase(matrix[x-2][y-1])){
                    moves.add(new Coordinates(x-2,y-1));
                }
            }
            if((x+1) <= 7 && (y-2)>=0){
                if(!Character.isLowerCase(matrix[x+1][y-2])){
                    moves.add(new Coordinates(x+1,y-2));
                }
            }
            if((x+2) <=7 && (y-1)>=0){
                if(!Character.isLowerCase(matrix[x+2][y-1])){
                    moves.add(new Coordinates(x+2,y-1));
                }
            }
            if((x-2) >= 0 && (y+1)<=7){
                if(!Character.isLowerCase(matrix[x-2][y+1])){
                    moves.add(new Coordinates(x-2,y+1));
                }
            }
            if((x-1) >= 0 && (y+2)<=7){
                if(!Character.isLowerCase(matrix[x-1][y+2])){
                    moves.add(new Coordinates(x-1,y+2));
                }
            }
            if((x+2) <=7 && (y+1)<=7){
                if(!Character.isLowerCase(matrix[x+2][y+1])){
                    moves.add(new Coordinates(x+2,y+1));
                }
            }
            if((x+1) <= 7 && (y+2)<=7){
                if(!Character.isLowerCase(matrix[x+1][y+2])){
                    moves.add(new Coordinates(x+1,y+2));
                }
            }
        }

        //for white bishop
        else if(matrix[x][y]=='B' && whiteTurn){
            for(int i =1; i<8;i++){//up and left diagonal
                if ((x-i) >= 0 && (y-i) >=0){
                    if(matrix[x-i][y-i].equals('_')){
                        moves.add(new Coordinates(x-i,y-i));
                    } else if(Character.isLowerCase(matrix[x-i][y-i])){
                        moves.add(new Coordinates(x-i,y-i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//up and right diagonal
                if ((x + i) <=7 && (y - i) >= 0) {
                    if (matrix[x + i][y - i].equals('_')) {
                        moves.add(new Coordinates(x + i, y - i));
                    } else if (Character.isLowerCase(matrix[x +i][y - i])) {
                        moves.add(new Coordinates(x + i, y - i));
                        break;
                    } else {
                        break;
                    }
                }
            }

            for(int i =1; i<8;i++){//down and left diagonal
                if ((x-i) >= 0 && (y+i<=7)){
                    if(matrix[x-i][y+i].equals('_')){
                        moves.add(new Coordinates(x-i,y+i));
                    } else if(Character.isLowerCase(matrix[x-i][y+i])){
                        moves.add(new Coordinates(x-i,y+i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//down and right diagonal
                if ((x + i) <=7 && (y + i) <= 7) {
                    if (matrix[x + i][y + i].equals('_')) {
                        moves.add(new Coordinates(x + i, y + i));
                    } else if (Character.isLowerCase(matrix[x +i][y + i])) {
                        moves.add(new Coordinates(x + i, y + i));
                        break;
                    } else {
                        break;
                    }
                }
            }
        }

        //for black bishop
        else if(matrix[x][y]=='b' && !whiteTurn){
            for(int i =1; i<8;i++){//up and left diagonal
                if ((x-i) >= 0 && (y-i) >=0){
                    if(matrix[x-i][y-i].equals('_')){
                        moves.add(new Coordinates(x-i,y-i));
                    } else if(Character.isUpperCase(matrix[x-i][y-i])){
                        moves.add(new Coordinates(x-i,y-i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//up and right diagonal
                if ((x + i) <=7 && (y - i) >= 0) {
                    if (matrix[x + i][y - i].equals('_')) {
                        moves.add(new Coordinates(x + i, y - i));
                    } else if (Character.isUpperCase(matrix[x +i][y - i])) {
                        moves.add(new Coordinates(x + i, y - i));
                        break;
                    } else {
                        break;
                    }
                }
            }

            for(int i =1; i<8;i++){//down and left diagonal
                if ((x-i) >= 0 && (y+i<=7)){
                    if(matrix[x-i][y+i].equals('_')){
                        moves.add(new Coordinates(x-i,y+i));
                    } else if(Character.isUpperCase(matrix[x-i][y+i])){
                        moves.add(new Coordinates(x-i,y+i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//down and right diagonal
                if ((x + i) <=7 && (y + i) <= 7) {
                    if (matrix[x + i][y + i].equals('_')) {
                        moves.add(new Coordinates(x + i, y + i));
                    } else if (Character.isUpperCase(matrix[x +i][y + i])) {
                        moves.add(new Coordinates(x + i, y + i));
                        break;
                    } else {
                        break;
                    }
                }
            }
        }

        //for white queen
        else if(matrix[x][y]=='Q' && whiteTurn){
            for(int j= y-1;j>=0;j--){//left
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isLowerCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int j= y+1;j<8;j++){//right
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isLowerCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int i= x+1;i<8;i++){//down
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isLowerCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }

            for(int i= x-1;i>=0;i--){//up
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isLowerCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }
            for(int i =1; i<8;i++){//up and left diagonal
                if ((x-i) >= 0 && (y-i) >=0){
                    if(matrix[x-i][y-i].equals('_')){
                        moves.add(new Coordinates(x-i,y-i));
                    } else if(Character.isLowerCase(matrix[x-i][y-i])){
                        moves.add(new Coordinates(x-i,y-i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//up and right diagonal
                if ((x + i) <=7 && (y - i) >= 0) {
                    if (matrix[x + i][y - i].equals('_')) {
                        moves.add(new Coordinates(x + i, y - i));
                    } else if (Character.isLowerCase(matrix[x +i][y - i])) {
                        moves.add(new Coordinates(x + i, y - i));
                        break;
                    } else {
                        break;
                    }
                }
            }

            for(int i =1; i<8;i++){//down and left diagonal
                if ((x-i) >= 0 && (y+i<=7)){
                    if(matrix[x-i][y+i].equals('_')){
                        moves.add(new Coordinates(x-i,y+i));
                    } else if(Character.isLowerCase(matrix[x-i][y+i])){
                        moves.add(new Coordinates(x-i,y+i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//down and right diagonal
                if ((x + i) <=7 && (y + i) <= 7) {
                    if (matrix[x + i][y + i].equals('_')) {
                        moves.add(new Coordinates(x + i, y + i));
                    } else if (Character.isLowerCase(matrix[x +i][y + i])) {
                        moves.add(new Coordinates(x + i, y + i));
                        break;
                    } else {
                        break;
                    }
                }
            }
        }

        //for black queen
        else if(matrix[x][y]=='q' && !whiteTurn){
            for(int j= y-1;j>=0;j--){//left
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isUpperCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int j= y+1;j<8;j++){//right
                if(matrix[x][j].equals('_')){
                    moves.add(new Coordinates(x,j));
                } else if(Character.isUpperCase(matrix[x][j])){
                    moves.add(new Coordinates(x,j));
                    break;
                }else{
                    break;
                }
            }
            for(int i= x+1;i<8;i++){//down
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isUpperCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }

            for(int i= x-1;i>=0;i--){//up
                if(matrix[i][y].equals('_')){
                    moves.add(new Coordinates(i,y));
                } else if(Character.isUpperCase(matrix[i][y])){
                    moves.add(new Coordinates(i,y));
                    break;
                }else{
                    break;
                }
            }
            for(int i =1; i<8;i++){//up and left diagonal
                if ((x-i) >= 0 && (y-i) >=0){
                    if(matrix[x-i][y-i].equals('_')){
                        moves.add(new Coordinates(x-i,y-i));
                    } else if(Character.isUpperCase(matrix[x-i][y-i])){
                        moves.add(new Coordinates(x-i,y-i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//up and right diagonal
                if ((x + i) <=7 && (y - i) >= 0) {
                    if (matrix[x + i][y - i].equals('_')) {
                        moves.add(new Coordinates(x + i, y - i));
                    } else if (Character.isUpperCase(matrix[x +i][y - i])) {
                        moves.add(new Coordinates(x + i, y - i));
                        break;
                    } else {
                        break;
                    }
                }
            }

            for(int i =1; i<8;i++){//down and left diagonal
                if ((x-i) >= 0 && (y+i<=7)){
                    if(matrix[x-i][y+i].equals('_')){
                        moves.add(new Coordinates(x-i,y+i));
                    } else if(Character.isUpperCase(matrix[x-i][y+i])){
                        moves.add(new Coordinates(x-i,y+i));
                        break;
                    }else {
                        break;
                    }
                }
            }
            for(int i =1; i<8;i++) {//down and right diagonal
                if ((x + i) <=7 && (y + i) <= 7) {
                    if (matrix[x + i][y + i].equals('_')) {
                        moves.add(new Coordinates(x + i, y + i));
                    } else if (Character.isUpperCase(matrix[x +i][y + i])) {
                        moves.add(new Coordinates(x + i, y + i));
                        break;
                    } else {
                        break;
                    }
                }
            }
        }

        return moves;
    }
}
