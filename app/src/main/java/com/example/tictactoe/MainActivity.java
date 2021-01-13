package com.example.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    boolean gameActive = true;
    int ai = 1, human = 0;

    //winner => 0 - human wins, 1 - ai wins, 2 - draw
    //initially no winner i.e, -1
    int winner = -1;

    //game state => 0 - humans token, 1 - ai token, 2 - blank
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    ImageView[] img = new ImageView[9]; //imageview array to access the images

    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}
    };

    // to be performed when player taps on an image view
    public void playerTap(View view) {
        ImageView curImg = (ImageView) view;
        int tappedImage = Integer.parseInt(curImg.getTag().toString());

        //if game is not active then reset the game
        if(!gameActive){
            gameActive = true;
            TextView status = findViewById(R.id.status);
            status.setText("Tap to play");
            gameReset(view);
            return;
        }

        //if the tapped image is blank then perform the following algorithm
        if (gameState[tappedImage] == 2) {

            //set the tapped image to human token
            TextView status = findViewById(R.id.status);
            status.setText("");
            gameState[tappedImage] = human;
            curImg.setImageResource(R.drawable.x);

            // check if the game is over
            if (isGameOver()) {
                gameActive = false;
                updateStatus();
                return;
            }

            //algorithm for ai players turn(minimax algorithm)
            int score = Integer.MIN_VALUE;
            int curPosition = 0;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i] == 2) {
                    gameState[i] = ai;
                    int curScore = miniMax(false);
                    if (score < curScore) {
                        score = curScore;
                        curPosition = i;
                    }
                    gameState[i] = 2;
                }
            }

            //set the optimal image to ai token
            gameState[curPosition] = ai;
            ImageView ai = img[curPosition];
            ai.setImageResource(R.drawable.o);

            //check if the game is over
            if (isGameOver()) {
                gameActive = false;
                updateStatus();
            }
        }
    }

    //updates the status bar to show the result
    public void updateStatus(){
        TextView status = findViewById(R.id.status);
        if (winner == 0) {
            status.setText("You Win. Tap to play again");
        } else if (winner == 1) {
            status.setText("You Loose. Tap to play again");
        } else {
            status.setText("It's a Draw. Tap to play again");
        }
    }

    //minimax algorithm maximzing player is ai, minimizing player is human
    public int miniMax(boolean isMaximizing) {
        if(isGameOver()){
            if(winner == ai) return 1;
            if(winner == human) return -1;
            return 0;
        }
        if (isMaximizing) {
            int score = Integer.MIN_VALUE;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i] == 2) {
                    gameState[i] = ai;
                    int curScore = miniMax(false);
                    gameState[i] = 2;
                    score = Math.max(score, curScore);
                }
            }
            return score;
        } else {
            int score = Integer.MAX_VALUE;
            for (int i = 0; i < gameState.length; i++) {
                if (gameState[i] == 2) {
                    gameState[i] = human;
                    int curScore = miniMax(true);
                    gameState[i] = 2;
                    score = Math.min(score, curScore);
                }
            }
            return score;
        }
    }

    //checks if the game is over and is there a winner
    public boolean isGameOver(){
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[1]] != 2) {
                winner = gameState[winPosition[0]];
                return true;
            }
        }
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (gameState[i] != 2)
                count++;
        }
        if (count == gameState.length) {
            winner = 2;
            return true;
        }
        return false;
    }

    //resets game state & imageviews back to blank/none
    public void gameReset(View view) {
        for (int i = 0; i < gameState.length; i++)
            gameState[i] = 2;
        img[0].setImageResource(0);
        img[1].setImageResource(0);
        img[2].setImageResource(0);
        img[3].setImageResource(0);
        img[4].setImageResource(0);
        img[5].setImageResource(0);
        img[6].setImageResource(0);
        img[7].setImageResource(0);
        img[8].setImageResource(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img[0] = (ImageView) findViewById(R.id.imageView0);
        img[1] = (ImageView) findViewById(R.id.imageView1);
        img[2] = (ImageView) findViewById(R.id.imageView2);
        img[3] = (ImageView) findViewById(R.id.imageView3);
        img[4] = (ImageView) findViewById(R.id.imageView4);
        img[5] = (ImageView) findViewById(R.id.imageView5);
        img[6] = (ImageView) findViewById(R.id.imageView6);
        img[7] = (ImageView) findViewById(R.id.imageView7);
        img[8] = (ImageView) findViewById(R.id.imageView8);
    }
}