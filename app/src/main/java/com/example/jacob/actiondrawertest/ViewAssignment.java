package com.example.jacob.actiondrawertest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * ViewAssignment receives an assignment from
 * MainActivity, sets the values of the TextViews
 * based on that assignment's props, and optionally
 * sends that assignment to AddAssignment if the
 * Edit button is pushed.
 */
public class ViewAssignment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_assignment);

        // Get the assignment passed from the MainActivity
        Intent passedIntent = getIntent();
        Assignment selectedAssignment = (Assignment) passedIntent.getSerializableExtra("selected");

        // Handles the case where the user clicks an item in the "done" list
        if (selectedAssignment == null) {
            selectedAssignment = (Assignment) passedIntent.getSerializableExtra("done");
            createElements(selectedAssignment, true);
            //toast("Assignment is Done");
        } else {
            createElements(selectedAssignment, false);
            //toast("NOT Done");
        }

    }

    /**
     * Creates the elements on screen based on the name/status of the
     * passed assignment. Put in a separate method to handle the case of
     * clicking on a "done" item.
     * @param selectedAssignment assignment passed from the MainAct or DoneAct
     */
    private void createElements(final Assignment selectedAssignment, boolean isDone) {

        // Doesn't display the edit button if the assignment is marked done.
        Button editButton = findViewById(R.id.editButton);
        if (!isDone) {
            // Action Bar title
            setTitle(selectedAssignment.getName() + " Properties");

            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ViewAssignment.this, AddAssignment.class);
                    intent.putExtra("selected", selectedAssignment);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            // Action Bar title
            setTitle(selectedAssignment.getName() + " Properties [DONE]");

            editButton.setAlpha(0);
            editButton.setEnabled(false);
        }

        // Populate the properties fields
        TextView aName = findViewById(R.id.name);
        aName.setText(selectedAssignment.getName());
        TextView cName = findViewById(R.id.className);
        cName.setText(selectedAssignment.getClassName());
        TextView tDate = findViewById(R.id.timeDue);
        tDate.setText(selectedAssignment.getTimeDue());
        TextView dDate = findViewById(R.id.dayDue);
        String dateStr = selectedAssignment.getDateDue();
        dDate.setText(dateStr);

        // Calculate the days 'til when the assignment is clicked on
        int daysTil = -1;
        final Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR); // current year
        int cMonth = c.get(Calendar.MONTH) + 1; // current month
        int cDay = c.get(Calendar.DAY_OF_MONTH); // current day
        int month = Integer.valueOf(dateStr.substring(0, dateStr.indexOf('/')));
        String dateSubStr = dateStr.substring(dateStr.indexOf('/') + 1, dateStr.length());
        int day = Integer.valueOf(dateSubStr.substring(0, dateSubStr.indexOf('/')));
        dateSubStr = dateSubStr.substring(dateSubStr.indexOf('/') + 1, dateSubStr.length());
        int year = Integer.valueOf(dateSubStr);
        if (cYear == year) {
            if (cMonth == month) {
                daysTil = day - cDay;
            }
        }
        TextView timeRem = findViewById(R.id.timeRem);
        selectedAssignment.setTimeRemaining(daysTil);
        timeRem.setText(selectedAssignment.getTimeRem());

        TextView statusText = findViewById(R.id.statusText);
        if (isDone) {
            statusText.setTextColor(getResources().getColor(R.color.a_green));
            statusText.setText("DONE");
        } else {
            statusText.setTextColor(getResources().getColor(R.color.a_yellow));
            statusText.setText("In Progress");
        }

        // TODO: Delete Removes the assignment (same as Remove functionality)
        Button deleteButton = findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * Wrapper to make a Toast message.
     * @param msg Message to display
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
