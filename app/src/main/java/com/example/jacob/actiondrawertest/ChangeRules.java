package com.example.jacob.actiondrawertest;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
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

        TextView mostImp = findViewById(R.id.mostImportant);
        registerForContextMenu(mostImp);

        mostImp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.openContextMenu(view);
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

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);
        String[] viewElem = v.toString().split(" ");
        String id = viewElem[viewElem.length - 1];

    }

    /**
     * Wrapper to make a Toast message.
     * @param msg Message to display
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
