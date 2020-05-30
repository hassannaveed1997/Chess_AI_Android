package com.example.chessai;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    //settings for the game. This will be accessed by the game class
    static  boolean puzzle = false;
    static boolean AI = false;
    //have variables for each of the 3 buttons
    Button mpbutton;
    Button aibutton;
    Button puzzleButton;
    static String puzzleSelection;
    final String[] myPuzzleArray = {"1","2","3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mpbutton = findViewById(R.id.MPbutton);
        mpbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launch the game
                AI = false;
                Intent myIntent = new Intent(MainActivity.this, Game.class);
                startActivityForResult(myIntent, 0);
            }
        });

        aibutton = findViewById(R.id.AIbutton);
        aibutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AI = true; //change setting so Ai is turned on for the black player.

                //launch the game
                Intent myIntent = new Intent(MainActivity.this, Game.class);
                startActivityForResult(myIntent, 0);
            }
        });
        puzzleButton = findViewById(R.id.Puzzlebutton);
        puzzleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create an alert
                dialogBoxCreator();

                AI = true;
                puzzle = true;

            }
        });
    }

    public void dialogBoxCreator(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.puzzleInstructions);
        builder.setItems(myPuzzleArray, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                puzzleSelection=myPuzzleArray[which];
                Log.d("chessAI","puzzle selected is" + puzzleSelection);

                Intent myIntent = new Intent(MainActivity.this, Game.class);
                startActivityForResult(myIntent, 0);

            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public static boolean isAI() {
        return AI;
    }

    public static boolean isPuzzle() {
        return puzzle;
    }

    public static String getPuzzleSelection() {
        return puzzleSelection;
    }
}
