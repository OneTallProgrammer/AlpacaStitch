package com.onetallprogrammer.alpacastitch.ui;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.onetallprogrammer.alpacastitch.R;
import com.onetallprogrammer.alpacastitch.model.ColorTile;
import com.onetallprogrammer.alpacastitch.model.KnitPainter;
import com.onetallprogrammer.alpacastitch.model.PoolingView;
import com.onetallprogrammer.alpacastitch.model.RepeatListener;

import java.util.ArrayList;

public class PlannedPoolingActivity extends AppCompatActivity {
    private FrameLayout poolingFrame;
    private HorizontalScrollView colorTileScrollView;
    private LinearLayout colorTileLinearLayout;
    private ImageButton increaseStitchesButton;
    private ImageButton decreaseStitchesButton;
    private TextView stitchesInRowDisplay;
    private Button addColorButton;
    private PoolingView plannedPoolingView;

    private ArrayList<ColorTile> colorTiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_pooling);

        poolingFrame = (FrameLayout) findViewById(R.id.poolingFrameLayout);
        colorTileScrollView = (HorizontalScrollView) findViewById(R.id.colorTileScrollView);
        colorTileLinearLayout = (LinearLayout) findViewById(R.id.colorTileLinearLayout);
        increaseStitchesButton = (ImageButton) findViewById(R.id.increaseStitchesButton);
        decreaseStitchesButton = (ImageButton) findViewById(R.id.decreaseStitchesButton);
        stitchesInRowDisplay = (TextView) findViewById(R.id.stitchInRowDisplay);
        addColorButton = (Button) findViewById(R.id.addColorButton);
        plannedPoolingView = new PoolingView(this);

        poolingFrame.addView(plannedPoolingView);

        int initialInterval = 400;
        int interval = 30;
        increaseStitchesButton.setOnTouchListener(new RepeatListener(initialInterval, interval, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stitchesInRowDisplay.setText(
                        String.valueOf(
                                Integer.parseInt(stitchesInRowDisplay.getText().toString()) + 1
                        )
                );

                updatePool();
            }
        }));

        decreaseStitchesButton.setOnTouchListener(new RepeatListener(initialInterval, interval, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentStitches = Integer.parseInt(stitchesInRowDisplay.getText().toString());

                if (currentStitches != 1) {
                    stitchesInRowDisplay.setText(
                            String.valueOf(
                                    Integer.parseInt(stitchesInRowDisplay.getText().toString()) - 1
                            )
                    );
                }

                updatePool();
            }
        }));

        stitchesInRowDisplay.setText("1");

        addColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorTile newTile = new ColorTile(getApplicationContext(),
                        addColorButton.getWidth(),
                        addColorButton.getHeight());

                newTile.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
                newTile.setGravity(Gravity.CENTER);
                newTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openColorTileOptionsDialog(newTile);
                    }
                });

                selectColorTileColor(newTile, true);
            }
        });
    }

    private void layoutColorTiles() {
        colorTileLinearLayout.removeAllViews();
        LayoutParams params = new LayoutParams(addColorButton.getWidth(), LayoutParams.MATCH_PARENT);

        for (int i = 0; i < colorTiles.size(); i++) {
            colorTileLinearLayout.addView(colorTiles.get(i), params);
        }

        colorTileLinearLayout.addView(addColorButton, params);
    }

    public void selectColorTileColor(final ColorTile tile, final boolean isNew) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose a Color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        Toast.makeText(getApplicationContext(),
                                "onColorSelected: 0x" + Integer.toHexString(selectedColor),
                                Toast.LENGTH_LONG)
                                .show();
                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] allColors) {
                        tile.setColor(selectedColor);
                        tile.setBackgroundColor(selectedColor);

                        if (isNew) {
                            selectYarnLength(tile, isNew);
                        }

                        updatePool();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    public void selectYarnLength(final ColorTile tile, final boolean isNew) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.yarn_length_dialog, null);

        final EditText yarnLengthEditText = view.findViewById(R.id.yarnLengthEditText);
        Button okButton = view.findViewById(R.id.okButton);
        Button cancelButton = view.findViewById(R.id.cancelButton);

        builder.setView(view);
        final AlertDialog yarnLengthDialog = builder.create();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!yarnLengthEditText.getText().toString().isEmpty()) {
                    int length = Integer.parseInt(yarnLengthEditText.getText().toString());
                    tile.setLength(length);
                    tile.setText(String.valueOf(length));

                    if (isNew) {
                        tile.setIndex(colorTiles.size());
                        colorTiles.add(tile);
                        layoutColorTiles();
                    }

                    updatePool();

                    yarnLengthDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter a length or cancel",
                            Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                yarnLengthDialog.dismiss();
            }
        });

        yarnLengthDialog.show();

    }

    public void openColorTileOptionsDialog(final ColorTile tile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.color_tile_options_dialog, null);

        Button editColorButton = view.findViewById(R.id.editColorButton);
        Button editLengthButton = view.findViewById(R.id.editLengthButton);
        Button deleteButton = view.findViewById(R.id.deleteColorTileButton);
        Button cancelButton = view.findViewById(R.id.cancelColorTileOptionsButton);

        builder.setView(view);
        final AlertDialog colorTileOptionsDialog = builder.create();

        editColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColorTileColor(tile, false);
            }
        });

        editLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectYarnLength(tile, false);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorTiles.remove(tile.getIndex());
                layoutColorTiles();
                updatePool();
                colorTileOptionsDialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorTileOptionsDialog.dismiss();
            }
        });

        colorTileOptionsDialog.show();

    }

    public void updatePool(){
        plannedPoolingView.getKnitPainter().setColors(colorTiles);
        plannedPoolingView.getKnitPainter().setStitchesInRow(Integer.parseInt(
                stitchesInRowDisplay.getText().toString()
        ));
        plannedPoolingView.getKnitPainter().calculateRectangleSideLength();
        plannedPoolingView.setCurrent(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        plannedPoolingView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        plannedPoolingView.pause();

    }
}
