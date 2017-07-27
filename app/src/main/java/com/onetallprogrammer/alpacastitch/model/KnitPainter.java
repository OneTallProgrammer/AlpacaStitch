package com.onetallprogrammer.alpacastitch.model;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by joseph on 7/24/17.
 */

public class KnitPainter {
    private View view;
    private Paint paint = new Paint();
    private Rect rect = new Rect();
    private ArrayList<ColorTile> colors = new ArrayList<>();
    private int stitchesInRow;
    private int rectangleSideLength;
    private int tileIndex = 0;

    public KnitPainter(View view) {
        this.view = view;

        paint.setStyle(Paint.Style.FILL);
    }

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

    public void changeColor(){
        tileIndex++;
        if(tileIndex == colors.size()){
            tileIndex = 0;
        }

        paint.setColor(colors.get(tileIndex).getColor());
    }

    public void calculateRectangleSideLength(){
        this.rectangleSideLength = (int)Math.ceil((float)view.getWidth() / stitchesInRow);
    }

    public void setColors(ArrayList<ColorTile> colors) {
        this.colors = colors;
    }

    public void setStitchesInRow(int stitchesInRow) {
        this.stitchesInRow = stitchesInRow;
    }
}
