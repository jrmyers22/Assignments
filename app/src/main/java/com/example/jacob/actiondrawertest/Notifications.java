package com.example.jacob.actiondrawertest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Finish & Comment
public class Notifications extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        setTitle("Notifications");

        Switch switch_button = findViewById(R.id.on_switch);
        switch_button.setChecked(true);
        // Set a checked change listener for switch button
        switch_button.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    TextView device_not = findViewById(R.id.device_not);
                    TextView email_not = findViewById(R.id.email_not);
                    TextView device_val = findViewById(R.id.device_val);
                    TextView email_val = findViewById(R.id.email_val);
                    device_not.setAlpha(1);
                    device_not.setEnabled(true);
                    email_not.setAlpha(1);
                    email_not.setEnabled(true);
                    device_val.setAlpha(1);
                    device_val.setEnabled(true);
                    email_val.setAlpha(1);
                    email_val.setEnabled(true);
                }
                else {
                    TextView device_not = findViewById(R.id.device_not);
                    TextView email_not = findViewById(R.id.email_not);
                    TextView device_val = findViewById(R.id.device_val);
                    TextView email_val = findViewById(R.id.email_val);
                    device_not.setAlpha(0);
                    device_not.setEnabled(false);
                    email_not.setAlpha(0);
                    email_not.setEnabled(false);
                    device_val.setAlpha(0);
                    device_val.setEnabled(false);
                    email_val.setAlpha(0);
                    email_val.setEnabled(false);
                }
            }
        });
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finishes the activity view
            }
        });
    }
}
