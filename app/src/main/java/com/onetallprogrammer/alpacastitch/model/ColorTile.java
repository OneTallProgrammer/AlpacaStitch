package com.onetallprogrammer.alpacastitch.model;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.lang.UCharacter;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

/**
 * A modified button class that will display a color chosen by the user as it's background
 * and a length as it's text
 *
 * Color tiles are created dynamically and designed to be placed in arrays
 */

public class ColorTile extends android.support.v7.widget.AppCompatButton {
    private int color;
    private int length;
    private int index;  // color tile's position in an array of color tiles

    private float shadowRadius;
    private float shadowDx;
    private float shadowDy;
    private int shadowColor;

    private final int SP_TEXT_SIZE = 40;

    public ColorTile(Context context, int width, int height) {
        super(context);
        this.setWidth(width);
        this.setHeight(height);

        /*
            parameters for a shadow around the button text
            used to make the button more visible across different colors
         */
        this.shadowRadius = (float)15;
        this.shadowDx = (float)2.0;
        this.shadowDy = (float)2.0;
        this.shadowColor = Color.BLACK;


        /*
            set the layout for the button
         */
        this.setGravity(Gravity.CENTER);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_TEXT_SIZE);
        this.setShadowLayer(shadowRadius, shadowDx, shadowDy, shadowColor);
    }

    /**
     * Constructor used to clone another color tile
     * @param tile the color tile being cloned
     */
    public ColorTile(ColorTile tile){
        super(tile.getContext());
        this.setWidth(tile.getWidth());
        this.setHeight(tile.getHeight());

        this.setColor(tile.getColor());
        this.setLength(tile.getLength());
        this.setBackgroundColor(color);
        this.setText(String.valueOf(length));

        this.setGravity(tile.getGravity());
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, SP_TEXT_SIZE);
        this.setShadowLayer(tile.getShadowRadius(),
                tile.getShadowDx(),
                tile.getShadowDy(),
                tile.getShadowColor());
    }

    @Override
    public float getShadowRadius() {
        return shadowRadius;
    }

    @Override
    public float getShadowDx() {
        return shadowDx;
    }

    @Override
    public float getShadowDy() {
        return shadowDy;
    }

    @Override
    public int getShadowColor() {
        return shadowColor;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
