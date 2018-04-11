package com.example.jacob.actiondrawertest;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

// TODO: Comment
public class ChangeRules extends AppCompatActivity {

    Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rules);
        setTitle("Change Theme Rules");

        Button saveButton = findViewById(R.id.cancelButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        final TextView mostImp = findViewById(R.id.mostImportant);
        registerForContextMenu(mostImp);

        mostImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeRules.this);
                builder.setTitle("Testing");
                String[] cs = new String[] {"Copy Text", "Delete", "Details"};
                builder.setItems(cs, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int itemIdx) {
                                /* itemIdx is an index */
                    }

                });
                builder.show();
            }
        });

        TextView secMost = findViewById(R.id.secondMost);
        registerForContextMenu(secMost);

        secMost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openContextMenu(view);
            }
        });

        TextView leastImp = findViewById(R.id.leastImportant);
        registerForContextMenu(leastImp);

        leastImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openContextMenu(view);
            }
        });

    }

    /**
     * Wrapper to make a Toast message.
     * @param msg Message to display
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}