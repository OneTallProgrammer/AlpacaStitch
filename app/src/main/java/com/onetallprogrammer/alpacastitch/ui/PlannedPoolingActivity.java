package com.onetallprogrammer.alpacastitch.ui;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.onetallprogrammer.alpacastitch.R;
import com.onetallprogrammer.alpacastitch.model.ColorTile;
import com.onetallprogrammer.alpacastitch.model.PoolingView;
import com.onetallprogrammer.alpacastitch.model.RepeatListener;

import java.util.ArrayList;
import java.util.StringTokenizer;

public class PlannedPoolingActivity extends AppCompatActivity {
    private LinearLayout colorTileLinearLayout;
    private TextView stitchesInRowDisplay;
    private Button addColorButton;
    private PoolingView plannedPoolingView;

    private final int MIN_STITCHES = 1;
    private final int MAX_STITCHES = 250;

    private ArrayList<ColorTile> colorTiles = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planned_pooling);

        //initialize variables
        FrameLayout poolingFrame = (FrameLayout) findViewById(R.id.poolingFrameLayout);
        ImageButton increaseStitchesButton = (ImageButton) findViewById(R.id.increaseStitchesButton);
        ImageButton decreaseStitchesButton = (ImageButton) findViewById(R.id.decreaseStitchesButton);
        colorTileLinearLayout = (LinearLayout) findViewById(R.id.colorTileLinearLayout);
        stitchesInRowDisplay = (TextView) findViewById(R.id.stitchInRowDisplay);
        addColorButton = (Button) findViewById(R.id.addColorButton);

        plannedPoolingView = new PoolingView(this);
        poolingFrame.addView(plannedPoolingView);

        //initialize increase and decrease stitch buttons
        int initialInterval = 400; // how long to hold a button before it starts repeating
        int interval = 30;         // how fast the button repeats
        increaseStitchesButton.setOnTouchListener(new RepeatListener(initialInterval, interval, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentStitches = Integer.parseInt(stitchesInRowDisplay.getText().toString());

                if (currentStitches < MAX_STITCHES) {
                    stitchesInRowDisplay.setText(
                            String.valueOf(
                                    currentStitches + 1
                            )
                    );
                }

                updatePool();
            }
        }));

        decreaseStitchesButton.setOnTouchListener(new RepeatListener(initialInterval, interval, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int currentStitches = Integer.parseInt(stitchesInRowDisplay.getText().toString());

                if (currentStitches != MIN_STITCHES) {
                    stitchesInRowDisplay.setText(
                            String.valueOf(
                                    currentStitches - 1
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

                newTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openColorTileOptionsDialog(newTile);
                    }
                });

                selectColorTileColor(newTile, true);
            }
        });

        LoadPreferences();
    }

    @Override
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.planned_pooling_options_menu, menu);

        menu.setGroupCheckable(R.id.plannedPoolingOptionsGroup, true, true);
        menu.findItem(R.id.flatKnitMenuItem).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        item.setChecked(true);

        switch(item.getItemId()){
            case R.id.resetPlannedPoolingMenuItem:
                colorTiles.clear();
                stitchesInRowDisplay.setText(String.valueOf(MIN_STITCHES));
                layoutColorTiles();
                updatePool();
            case R.id.flatKnitMenuItem:
                plannedPoolingView.setFlatKnit(true);
                break;
            case R.id.circleKnitMenuItem:
                plannedPoolingView.setFlatKnit(false);
                break;
        }

        updatePool();

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        plannedPoolingView.pause();

    }

    @Override
    protected void onResume() {
        super.onResume();
        plannedPoolingView.resume();
    }

    /**
     * Opens dialog to edit a color tile
     * @param tile color tile being modified
     */

    public void openColorTileOptionsDialog(final ColorTile tile) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.color_tile_options_dialog, null);

        Button editColorButton = view.findViewById(R.id.editColorButton);
        Button editLengthButton = view.findViewById(R.id.editLengthButton);
        Button duplicateTileButton = view.findViewById(R.id.duplicateTileButton);
        Button deleteButton = view.findViewById(R.id.deleteColorTileButton);
        Button cancelButton = view.findViewById(R.id.cancelColorTileOptionsButton);

        builder.setView(view);
        final AlertDialog colorTileOptionsDialog = builder.create();

        editColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectColorTileColor(tile, false);
                colorTileOptionsDialog.dismiss();
            }
        });

        editLengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStitchCount(tile, false);
                colorTileOptionsDialog.dismiss();
            }
        });

        duplicateTileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ColorTile duplicate = new ColorTile(tile);
                duplicate.setIndex(colorTiles.size());
                colorTiles.add(duplicate);

                duplicate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openColorTileOptionsDialog(duplicate);
                    }
                });

                layoutColorTiles();
                updatePool();
                colorTileOptionsDialog.dismiss();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorTiles.remove(tile.getIndex());
                updateColorTileIndices();
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

    /**
     * Opens dialog to edit color tile's color
     * @param tile the tile being modified or created
     * @param isNew true if tile is new, false if tile is being modified
     */

    public void selectColorTileColor(final ColorTile tile, final boolean isNew) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose a Color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int selectedColor, Integer[] allColors) {
                        tile.setColor(selectedColor);
                        tile.setShadowLayer(tile.getShadowRadius(),
                                            tile.getShadowDx(),
                                            tile.getShadowDy(),
                                            tile.getShadowColor());

                        // if new, a length must also be selected before creation
                        if (isNew) {
                            selectStitchCount(tile, isNew);
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

    /**
     * @param tile the tile being modified or created
     * @param isNew true if tile is new, false if tile is being modified
     */

    public void selectStitchCount(final ColorTile tile, final boolean isNew) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.stitch_count_dialog, null);

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

                    // if new color tile, give it an index, add it to container, and reprint tiles
                    if (isNew) {
                        tile.setIndex(colorTiles.size());
                        colorTiles.add(tile);
                        layoutColorTiles();
                    }

                    updatePool();

                    yarnLengthDialog.dismiss();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter a stitch count or cancel",
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

    /**
     * Prints color tiles to frame
     */

    private void layoutColorTiles() {
        colorTileLinearLayout.removeAllViews();
        colorTileLinearLayout.setMinimumHeight(addColorButton.getHeight());
        LayoutParams params = new LayoutParams(addColorButton.getWidth(), addColorButton.getHeight());

        for (int i = 0; i < colorTiles.size(); i++) {
            colorTileLinearLayout.addView(colorTiles.get(i), params);
        }

        colorTileLinearLayout.addView(addColorButton, addColorButton.getWidth(), LayoutParams.MATCH_PARENT);
    }

    /**
     * updates color tiles position in array
     */

    private void SavePreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("stitchesInRow", Integer.parseInt(String.valueOf(stitchesInRowDisplay.getText().toString())));
        StringBuilder colorTileData = new StringBuilder();
        for(int i = 0; i < colorTiles.size(); i++){
            colorTileData.append(colorTiles.get(i).getLength())
                         .append(", ")
                         .append(colorTiles.get(i).getColor())
                         .append(", ");
        }
        editor.putString("colorTileData", colorTileData.toString());
        editor.commit();   // I missed to save the data to preference here,.
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        int stitchesInRow = sharedPreferences.getInt("stitchesInRow", 1);
        stitchesInRowDisplay.setText(String.valueOf(stitchesInRow));

        String colorTileData = sharedPreferences.getString("colorTileData", "");
        StringTokenizer st = new StringTokenizer(colorTileData, ", ");
        if(!colorTileData.isEmpty()){
            while(st.hasMoreTokens()){
                final ColorTile newTile = new ColorTile(this, addColorButton.getWidth(), addColorButton.getHeight());
                newTile.setLength(Integer.parseInt(st.nextToken()));
                newTile.setColor(Integer.parseInt(st.nextToken()));

                newTile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openColorTileOptionsDialog(newTile);
                    }
                });

                colorTiles.add(newTile);
            }
        }
    }

    private void updateColorTileIndices() {
        for(int i = 0; i < colorTiles.size(); i++){
            colorTiles.get(i).setIndex(i);
        }
    }

    /**
     * updates the planned pool based off the most current user input
     */

    private void updatePool(){
        plannedPoolingView.getKnitPainter().setColors(colorTiles);
        plannedPoolingView.getKnitPainter().setStitchesInRow(Integer.parseInt(
                stitchesInRowDisplay.getText().toString()
        ));
        plannedPoolingView.getKnitPainter().calculateRectangleSideLength();
        plannedPoolingView.setCurrent(false);
    }
}
