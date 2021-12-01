package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a1024.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener {

    int scoreN = 0;

    GestureDetectorCompat mDetector;

    Button resetMove;
    TextView score;
    TextView c00, c10, c20, c30, c01, c11, c21, c31, c02, c12, c22, c32, c03, c13, c23, c33;
    TextView[][] cellsMap;

    int[][] previousGameMap = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };

    int[][] gameMap = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0}
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, this);

        identifyButtons();

        generateCell();
        updateGameStatus(gameMap);
    }

    private void identifyButtons() {

        resetMove = (Button) findViewById(R.id.rewind_move);
        resetMove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("aaa");
                resetLastMove();
            }
        });

        score = (TextView) findViewById(R.id.score);

        c00 = (TextView) findViewById(R.id.button00);
        c10 = (TextView) findViewById(R.id.button10);
        c20 = (TextView) findViewById(R.id.button20);
        c30 = (TextView) findViewById(R.id.button30);
        c01 = (TextView) findViewById(R.id.button01);
        c11 = (TextView) findViewById(R.id.button11);
        c21 = (TextView) findViewById(R.id.button21);
        c31 = (TextView) findViewById(R.id.button31);
        c02 = (TextView) findViewById(R.id.button02);
        c12 = (TextView) findViewById(R.id.button12);
        c22 = (TextView) findViewById(R.id.button22);
        c32 = (TextView) findViewById(R.id.button32);
        c03 = (TextView) findViewById(R.id.button03);
        c13 = (TextView) findViewById(R.id.button13);
        c23 = (TextView) findViewById(R.id.button23);
        c33 = (TextView) findViewById(R.id.button33);

        cellsMap = new TextView[][]{
                {c00, c01, c02, c03},
                {c10, c11, c12, c13},
                {c20, c21, c22, c23},
                {c30, c31, c32, c33}
        };
    }

    private void generateCell() {

        boolean spaceAvailable = false;
        for (int[] gameRow : gameMap) {
            for (int cellValue : gameRow) {
                if (cellValue == 0) spaceAvailable = true;
            }
        }

        if (spaceAvailable) {

            while (true) {
                Random r = new Random();
                int randomCell = r.nextInt(16);

                int rX = randomCell / 4;
                int rY = randomCell % 4;

                if (gameMap[rX][rY] == 0) {

                    int v2or4 = new Random().nextInt(100);

                    if (v2or4 < 95) {
                        updateCell(rX, rY, 2);
                        scoreN += 2;
                        break;
                    } else {
                        updateCell(rX, rY, 4);
                        scoreN += 4;
                        break;
                    }
                }
            }

        } else System.out.println("GAME OVER");
        score.setText(String.valueOf(scoreN));
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        double XDisplacement = e2.getX() - e1.getX();
        double YDisplacement = e2.getY() - e1.getY();

        boolean right = false;
        if (XDisplacement > 0) {
            right = true;
        } else XDisplacement = -XDisplacement;

        boolean up = true;
        if (YDisplacement > 0) {
            up = false;
        } else YDisplacement = -YDisplacement;

        String direction = "N";

        if (XDisplacement > YDisplacement + 200) {
            if (right) {
                direction = "E";
            } else direction = "W";
        } else if (YDisplacement > XDisplacement + 200) {
            if (up) {
                direction = "N";
            } else direction = "S";
        }

        moveCells(direction);
        generateCell();

        return true;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);

    }

    // ---------------------------------------

    private void updateCell(int x, int y, int newValue) {
        gameMap[x][y] = newValue;

        if (newValue != 0) cellsMap[x][y].setText(String.valueOf(newValue));
        else cellsMap[x][y].setText("");
        cellsMap[x][y].setBackgroundColor(getNumberColor(newValue));
    }

    public void moveCells(String direction) {

        int[][] easySort = new int[4][4];

        switch (direction) {
            case "N":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) easySort[x][y] = gameMap[y][3 - x];
                }
                break;
            case "E":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) easySort[x][y] = gameMap[x][3 - y];
                }
                break;
            case "S":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) easySort[x][y] = gameMap[3 - y][x];
                }
                break;
            case "W":
                for (int x = 0; x < gameMap.length; x++) {
                    for (int y = 0; y < gameMap[x].length; y++) {
                        easySort[x][y] = gameMap[x][y];
                    }
                }
                break;
        }

        linearMovement(easySort);

        int[][] finalEasySort = new int[4][4];

        switch (direction) {
            case "N":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) finalEasySort[x][y] = easySort[3 - y][x];
                }
                break;
            case "E":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) finalEasySort[x][y] = easySort[x][3 - y];
                }
                break;
            case "S":
                for (int x = 0; x < 4; x++) {
                    for (int y = 0; y < 4; y++) finalEasySort[x][y] = easySort[y][3 - x];
                }
                break;
            case "W":
                for (int x = 0; x < easySort.length; x++) {
                    for (int y = 0; y < easySort[x].length; y++) {
                        finalEasySort[x][y] = easySort[x][y];
                    }
                }
                break;
        }

        updateGameStatus(finalEasySort);

    }

    private int getNumberColor(int value) {

        switch (value) {
            case 2:
                return getResources().getColor(R.color.cell_2);
            case 4:
                return getResources().getColor(R.color.cell_4);
            case 8:
                return getResources().getColor(R.color.cell_8);
            case 16:
                return getResources().getColor(R.color.cell_16);
            case 32:
                return getResources().getColor(R.color.cell_32);
            case 64:
                return getResources().getColor(R.color.cell_64);
            case 128:
                return getResources().getColor(R.color.cell_128);
            case 256:
                return getResources().getColor(R.color.cell_256);
            case 512:
                return getResources().getColor(R.color.cell_512);
            /*case 1024:
                return getResources().getColor(R.color.cell_1024);
            case 2048:
                return getResources().getColor(R.color.cell_2048);*/
            default:
                return getResources().getColor(R.color.empty_cell);
        }

    }

    private void linearMovement(int[][] easySort) {

        for (int[] move : easySort) {

            boolean[] cellsFusionated = new boolean[4];

            boolean actionPerformed = true;
            while (actionPerformed) {

                actionPerformed = false;

                for (int i = 1; i < 4; i++) {
                    if (move[i - 1] == move[i] && cellsFusionated[i - 1] == false) {
                        move[i - 1] += move[i - 1];
                        move[i] = 0;
                        cellsFusionated[i - 1] = true;
                        actionPerformed = true;
                    } else if (move[i] != 0 && move[i - 1] == 0) {
                        move[i - 1] = move[i];
                        move[i] = 0;
                        cellsFusionated[i - 1] = cellsFusionated[i];
                        cellsFusionated[i] = false;
                        actionPerformed = true;
                    }

                }
            }
        }
    }

    private void updateGameStatus(int[][] easySort) {
        //Save last game status
        for (int x = 0; x < gameMap.length; x++) {
            for (int y = 0; y < gameMap[x].length; y++) {
                previousGameMap[x][y] = gameMap[x][y];
            }
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                updateCell(x, y, easySort[x][y]);
            }
        }
    }

    private void resetLastMove() {
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                updateCell(x, y, previousGameMap[x][y]);
            }
        }
    }


    // FORCED OVERRIDES  --IGNORE
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent motionEvent) {

    }
}