package com.onetallprogrammer.alpacastitch.model;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by joseph on 7/23/17.
 */

public class PoolingView extends SurfaceView implements Runnable {
    Thread thread = null;
    SurfaceHolder holder;
    boolean isRunning = false;

    private KnitPainter knitPainter = new KnitPainter(this);
    private boolean current = false;

    private boolean isFlatKnit = true;

    public PoolingView(Context context) {
        super(context);
        holder = getHolder();
        holder.setFormat(PixelFormat.TRANSLUCENT);
    }

    @Override
    public void run() {
        while(isRunning){
            if(!holder.getSurface().isValid()){
                continue;
            }

            if(!current) {
                Canvas canvas = holder.lockCanvas();
                knitPainter.clearCanvas(canvas);
                if(isFlatKnit) {
                    knitPainter.paintFlatKnit(canvas);
                }
                else{
                    knitPainter.paintCircleKnit(canvas);
                }
                holder.unlockCanvasAndPost(canvas);
                current = true;
            }
        }
    }

    public void pause(){
        isRunning = false;
        while(true){
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            break;
        }
    }

    public void resume(){
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public KnitPainter getKnitPainter() {
        return knitPainter;
    }

    public void setFlatKnit(boolean isFlatKnit){
        this.isFlatKnit = isFlatKnit;
    }


}
