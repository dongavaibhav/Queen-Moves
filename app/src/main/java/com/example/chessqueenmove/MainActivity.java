package com.example.chessqueenmove;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private GridLayout chessboard;
    private ImageButton[][] squares = new ImageButton[8][8];
    private int[] selectedPosition = {7, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        chessboard = findViewById(R.id.chessboard);
        setupChessboard();
    }

    private void setupChessboard() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int squareSize = screenWidth / 8;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                ImageButton square = new ImageButton(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = squareSize;
                params.height = squareSize;
                params.rowSpec = GridLayout.spec(i);
                params.columnSpec = GridLayout.spec(j);
                square.setLayoutParams(params);
                square.setScaleType(ImageView.ScaleType.FIT_CENTER);
                square.setPadding(2, 2, 2, 2);
                square.setOnClickListener(new SquareClickListener(i, j));
                squares[i][j] = square;
                chessboard.addView(square);

                // Set alternating colors for chess squares
                if ((i + j) % 2 == 0) {
                    square.setBackgroundColor(ContextCompat.getColor(this, R.color.lightSquare));
                } else {
                    square.setBackgroundColor(ContextCompat.getColor(this, R.color.darkSquare));
                }

                if (i == selectedPosition[0] && j == selectedPosition[1]) {
                    square.setImageResource(R.drawable.king);
                }
            }
        }
    }

    private class SquareClickListener implements View.OnClickListener {
        private int x, y;

        SquareClickListener(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public void onClick(View v) {
            if (x == selectedPosition[0] && y == selectedPosition[1]) {
                // Clicked on the queen's current position, do nothing
                return;
            }

            if (isValidQueenMove(selectedPosition[0], selectedPosition[1], x, y)) {
                // Valid move
                squares[x][y].setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.validMove));
                // Move the queen
                squares[selectedPosition[0]][selectedPosition[1]].setImageResource(0);
                squares[x][y].setImageResource(R.drawable.king);
                selectedPosition[0] = x;
                selectedPosition[1] = y;
            } else {
                // Invalid move
                squares[x][y].setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.invalidMove));
            }

            // Reset the board colors after a short delay
            chessboard.postDelayed(new Runnable() {
                @Override
                public void run() {
                    resetBoardColors();
                }
            }, 500); // 500ms delay
        }
    }

    private void resetBoardColors() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if ((i + j) % 2 == 0) {
                    squares[i][j].setBackgroundColor(ContextCompat.getColor(this, R.color.lightSquare));
                } else {
                    squares[i][j].setBackgroundColor(ContextCompat.getColor(this, R.color.darkSquare));
                }
            }
        }
    }

    private boolean isValidQueenMove(int startX, int startY, int endX, int endY) {
        int dx = Math.abs(endX - startX);
        int dy = Math.abs(endY - startY);
        return (dx == 0 && dy != 0) || (dx != 0 && dy == 0) || (dx == dy);
    }
}