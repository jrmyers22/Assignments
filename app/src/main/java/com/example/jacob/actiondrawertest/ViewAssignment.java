package com.example.jacob.actiondrawertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

public class ViewAssignment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        Intent passedIntent = getIntent();
        final Assignment selectedAssignment = (Assignment) passedIntent.getSerializableExtra("selected");

        // Populate the properties fields
        TextView aName = findViewById(R.id.name);
        aName.setText(selectedAssignment.getName());
        TextView cName = findViewById(R.id.className);
        cName.setText(selectedAssignment.getClassName());
        TextView tDate = findViewById(R.id.timeDue);
        tDate.setText(selectedAssignment.getTimeDue());
        TextView dDate = findViewById(R.id.dayDue);
        dDate.setText(selectedAssignment.getDateDue());

        // TODO: Calculate the days 'til when the assignment is clicked on
        TextView timeRem = findViewById(R.id.timeRem);
        timeRem.setText(selectedAssignment.getTimeRem());

        // TODO: Delete Removes the assignment (same as Remove functionality)
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Button editButton = findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAssignment.this, AddAssignment.class);
                intent.putExtra("selected", selectedAssignment);
                startActivity(intent);
                finish();
            }
        });
    }
}
