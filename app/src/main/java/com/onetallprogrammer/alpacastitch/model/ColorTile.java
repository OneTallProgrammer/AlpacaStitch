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
 * Created by joseph on 7/24/17.
 */

public class ColorTile extends android.support.v7.widget.AppCompatButton {
    private int color;
    private int length;
    private int index;

    public ColorTile(Context context, int width, int height) {
        super(context);
        this.setWidth(width);
        this.setHeight(height);

        //shadow parameters
        float radius = (float)15;
        float dx = (float)2.0;
        float dy = (float)2.0;

        this.setGravity(Gravity.CENTER);
        this.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
        this.setShadowLayer(radius, dx, dy, Color.BLACK);
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
