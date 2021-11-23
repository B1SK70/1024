package com.example.a2048;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.TextView;

import com.example.a1024.R;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements
        GestureDetector.OnGestureListener {


    int[][] cellsMap = new int[][]{
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0},
            {0,0,0,0}
    };
    GestureDetectorCompat mDetector;

    TextView b00, b10, b20, b30, b01, b11, b21, b31, b02, b12, b22, b32, b03, b13, b23, b33;
    TextView[] cells;

    int color2 = R.color.cell_2;
    int color4 = R.color.cell_4;
    int color8 = R.color.cell_8;
    int color16 = R.color.cell_16;
    int color32 = R.color.cell_32;
    int color64 = R.color.cell_64;
    int color128 = R.color.cell_128;
    int color256 = R.color.cell_256;
    int color512 = R.color.cell_512;
    /*int color1024 = R.color.cell_2;
    int color2048 = R.color.cell_2;*/



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, this);

        identifyButtons();

        generateCell();

    }

    private void identifyButtons() {
        b00 = (TextView) findViewById(R.id.button00);
        b10 = (TextView) findViewById(R.id.button10);
        b20 = (TextView) findViewById(R.id.button20);
        b30 = (TextView) findViewById(R.id.button30);
        b01 = (TextView) findViewById(R.id.button01);
        b11 = (TextView) findViewById(R.id.button11);
        b21 = (TextView) findViewById(R.id.button21);
        b31 = (TextView) findViewById(R.id.button31);
        b02 = (TextView) findViewById(R.id.button02);
        b12 = (TextView) findViewById(R.id.button12);
        b22 = (TextView) findViewById(R.id.button22);
        b32 = (TextView) findViewById(R.id.button32);
        b03 = (TextView) findViewById(R.id.button03);
        b13 = (TextView) findViewById(R.id.button13);
        b23 = (TextView) findViewById(R.id.button23);
        b33 = (TextView) findViewById(R.id.button33);

        cells = new TextView[]{b00, b01, b02, b03, b10, b11, b12, b13, b20, b21, b22, b23, b30, b31, b32, b33};
    }

    private void generateCell() {

        Random r = new Random();
        int low = 0;
        int high = 16;
        int randomCell = r.nextInt(high-low) + low;


        //Actual game state
        int [][]cellsData = detectCells();

        //Spot a empty cell
        boolean randomCellIsAvailable = false;
        while (!randomCellIsAvailable) {

            //Check if there's an empty cell left
            boolean cellAvailable = false;
            for (int i = 0 ; i <= 3 ; i++) {
                for(int y = 0 ; y <= 3 ; y++) {
                    if (cellsData[i][y] == 0 ) cellAvailable = true;
                }
            }

            if (cellAvailable) {
                //Check if the randomly selected cell is empty
                if ( cellsData[ randomCell / 4] [ randomCell % 4 ] == 0 ) {
                    randomCellIsAvailable = true;
                } else {
                    randomCell = r.nextInt(high - low) + low;
                }

            } else {
                System.out.println("GAME OVER");
                break;
            }
        }


        cells[randomCell].setText("2");
        cells[randomCell].setBackgroundColor(getResources().getColor(R.color.cell_2));
    }

    // ---------------------------------------

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if (this.mDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float v, float v1) {
        //Axis movement
        double XDisplacement = e2.getX() - e1.getX();
        double YDisplacement = e2.getY() - e1.getY();

        //Pass every axis movement to positive values
        boolean right = false;
        if ( XDisplacement > 0 ) {
            right = true;
        } else XDisplacement = -XDisplacement;

        boolean up = true;
        if ( YDisplacement > 0 ) {
            up = false;
        } else YDisplacement = -YDisplacement;

        //Move cells
        if ( XDisplacement > YDisplacement + 200) {
            if (right) {
                moveCells(22);
            } else moveCells(21);
        } else if (YDisplacement > XDisplacement + 200) {
            if (up) {
                moveCells(19);
            } else moveCells(20);
        }
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return super.onKeyDown(keyCode, event);

    }



    //moveCells(keyCode); 19Up,20Down,21West,22East
    //generateCell();

    // ---------------------------------------


    public int[][] detectCells() {

        int[][] coordsData = new int[4][4];

        for (TextView cell : cells) {
            String cellText = (String) cell.getText();
            if (cellText != null && cellText != "") {

                String cellId = getResources().getResourceEntryName( cell.getId() );
                String cellCords = cellId.split("button")[1];

                int cellX = Integer.parseInt(cellCords.split("")[0]);
                int cellY = Integer.parseInt(cellCords.split("")[1]);

                coordsData[cellX][cellY] = Integer.parseInt((String) cell.getText());

            }
        }
        return coordsData;
    }

    public void moveCells( int direction ) {
        //MOVEMENT KEYCODES
        //  N --> 19
        //  S --> 20
        //  W --> 21
        //  E --> 22

        int[][] cells = detectCells();

        for (int x = 0 ; x <= 3 ; x++ ) {
            for (int y = 0 ; y <= 3 ; y++ ) {

                if ( cells[x][y] != 0 ) {

                    int [] cellData = {x,y,cells[x][y],direction};
                    //x , y , value, direction

                    switch (direction) {
                        case (19):
                            if (x != 0  && cells[x-1][y] == 0 ) {
                                moveCell(cellData);
                            }
                            break;
                        case (20):
                            if (x != 3  && cells[x+1][y] == 0 ) {
                                moveCell(cellData);
                            }
                            break;
                        case (21):
                            if (y != 0  && cells[x][y-1] == 0 ) {
                                moveCell(cellData);
                            }
                            break;
                        case (22):
                            if (y != 3  && cells[x][y+1] == 0 ) {
                                moveCell(cellData);
                            }
                            break;
                    }
                }
            }
        }
        generateCell();
    }

    public void moveCell(int[] cellData) {
        //x , y , value, direction

        TextView oldCell = cells[(4*cellData[0] + cellData[1])];
        oldCell.setText("");
        oldCell.setBackgroundResource(R.color.empty_cell);

        TextView newCell = cells[(4*cellData[0] + cellData[1])];
        switch (cellData[3]) {
            case (19):
                newCell = cells[(4*cellData[0]-4 + cellData[1])];
                break;
            case (20):
                newCell = cells[(4*cellData[0]+4 + cellData[1])];
                break;
            case (21):
                newCell = cells[(4*cellData[0] + cellData[1]-1)];
                break;
            case (22):
                newCell = cells[(4*cellData[0] + cellData[1]+1)];
                break;
        }

        newCell.setText(String.valueOf(cellData[2]));
        newCell.setBackgroundResource(getStyle(cellData[2]));

    }

    public int getStyle(int value) {

        switch (value) {
            case 2:
                return color2;
            case 4:
                return color4;
            case 8:
                return color8;
            case 16:
                return color16;
            case 32:
                return color32;
            case 64:
                return color64;
            case 128:
                return color128;
            case 256:
                return color256;
            case 512:
                return color512;
            default:
                return R.color.empty_cell;
        }

    }




    // FORCED OVERRIDES  --IGNORE
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