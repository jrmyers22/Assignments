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

public class ChangeRules extends AppCompatActivity {

    Activity activity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_rules);

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
        if (id.contains("mostImportant")) {
            setUpMenu(menu, v, "Most Important");
        } else if (id.contains("secondMost")) {
            setUpMenu(menu, v, "2nd Most");
        } else if (id.contains("leastImportant")) {
            setUpMenu(menu, v, "Least Important");
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Call"){
            Toast.makeText(getApplicationContext(),"calling code",Toast.LENGTH_LONG).show();
        }
        else if(item.getTitle()=="SMS"){
            Toast.makeText(getApplicationContext(),"sending sms code",Toast.LENGTH_LONG).show();
        }else{
            return false;
        }
        return true;
    }

    public void setUpMenu(ContextMenu menu, View v, String id) {
        String titleStr = "Assignments";
        if (id.equals("Most Important")) {
            titleStr = id;
        } else if (id.equals("2nd Most")) {
            titleStr = id;
        } else if (id.equals("Least Important")) {
            titleStr = id;
        }

        menu.setHeaderTitle("Change \'" + titleStr + "\' Rule");
        menu.add(0, v.getId(), 0, "less than 2 days");//groupId, itemId, order, title
        menu.add(0, v.getId(), 0, "less than 3 days");
        menu.add(0, v.getId(), 0, "less than 4 days");
        menu.add(0, v.getId(), 0, "less than 5 days");
        menu.add(0, v.getId(), 0, "more than 5 days");
    }
}
