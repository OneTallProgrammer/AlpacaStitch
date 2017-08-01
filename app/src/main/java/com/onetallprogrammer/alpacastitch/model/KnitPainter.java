package com.onetallprogrammer.alpacastitch.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

/**
 * Paints a canvas object with a desired pattern
 */

public class KnitPainter {
    private View view;
    private Paint paint = new Paint();
    private Rect rect = new Rect();
    private ArrayList<ColorTile> colors = new ArrayList<>();
    private int stitchesInRow;
    private int rectangleSideLength;
    private int tileIndex = 0;

    /*
        Knit Painters are only created when starting the Planned Pooling activity
        All variables of the Knit Painter must be initialized from user input

        TODO: add full constructor that will be used when coming back to a knitting project
     */
    public KnitPainter(View view) {
        this.view = view;

        paint.setStyle(Paint.Style.FILL);
    }

    /**
     * Calculates and assigns the appropriate square side-length of painting rectangle object
     *
     * width of screen / stitches in row
     */

    public void calculateRectangleSideLength(){
        this.rectangleSideLength = (int)Math.ceil((float)view.getWidth() / stitchesInRow);
    }

    /**
     * Paints a circle knit pattern, left to right and row by row, on a canvas object
     * @param canvas The canvas being painted
     */
    public void paintCircleKnit(Canvas canvas){
        if(colors.isEmpty()){
           return;
        }

        tileIndex = 0;
        paint.setColor(colors.get(tileIndex).getColor());

        int stitchCounter = 0;
        int xPos = 0;

        for(int yPos = 0; yPos < view.getHeight(); yPos += rectangleSideLength){
            for(int stitch = 0; stitch < stitchesInRow; stitch++){
                rect.set(xPos, yPos, xPos + rectangleSideLength, yPos + rectangleSideLength);
                canvas.drawRect(rect, paint);
                stitchCounter++;
                xPos += rectangleSideLength;
                if(stitchCounter == colors.get(tileIndex).getLength()){
                    changeColor();
                    stitchCounter = 0;
                }
            }

            xPos = 0;
        }
    }

    /**
     * Prints a flat knit pattern, alternating left to right and right to left
     * @param canvas The canvas being painted
     */

    public void paintFlatKnit(Canvas canvas){
        if(colors.isEmpty()){
            return;
        }

        int xPos = 0;
        int stitchCounter = 0;
        boolean goingRight = false;

        tileIndex = 0;
        paint.setColor(colors.get(tileIndex).getColor());

        for(int yPos = 0; yPos < view.getHeight(); yPos += rectangleSideLength){
            goingRight = !goingRight;
            for (int stitch = 0; stitch < stitchesInRow; stitch++) {
                if (goingRight) {
                    rect.set(xPos, yPos, xPos + rectangleSideLength, yPos + rectangleSideLength);
                    xPos += rectangleSideLength;
                } else {
                    rect.set(xPos - rectangleSideLength, yPos, xPos, yPos + rectangleSideLength);
                    xPos -= rectangleSideLength;
                }

                canvas.drawRect(rect, paint);

                stitchCounter++;
                if(stitchCounter == colors.get(tileIndex).getLength()){
                    stitchCounter = 0;
                    changeColor();
                }

            }
        }
    }

    public void setColors(ArrayList<ColorTile> colors) {
        this.colors = colors;
    }

    public void setStitchesInRow(int stitchesInRow) {
        this.stitchesInRow = stitchesInRow;
    }

    /**
     * Switches to the next available color in ArrayList colors
     * Restarts at index 0 if ending color is reached
     */

    private void changeColor(){
        tileIndex++;
        if(tileIndex == colors.size()){
            tileIndex = 0;
        }

        paint.setColor(colors.get(tileIndex).getColor());
    }
}
