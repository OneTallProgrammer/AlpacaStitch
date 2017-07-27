package com.onetallprogrammer.alpacastitch.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.onetallprogrammer.alpacastitch.R;

public class SelectionActivity extends AppCompatActivity {
    private Button mPlannedPoolingButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        mPlannedPoolingButton = (Button) findViewById(R.id.plannedPoolingButton);
        mPlannedPoolingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPlannedPooling();
            }
        });
    }

    private void startPlannedPooling() {
        Intent intent = new Intent(this, PlannedPoolingActivity.class);
        startActivity(intent);
    }
}
