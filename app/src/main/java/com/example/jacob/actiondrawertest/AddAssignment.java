package com.example.jacob.actiondrawertest;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * This class takes care of adding the assignment by verifying
 * the data and making sure the date is not in the past.
 * It also handles editing existing assignments.
 */
public class AddAssignment extends AppCompatActivity {

    /** Demonstrates the cutoff number for time spacing */
    final int SPECIAL_NUM = 10;

    /** Keeps track of number of assignments */
    static int numAssignments = 0;

    /** Name of shared prefs for assignments */
    final String PREF_NAME = "Saved_Assignments";

    /** List of assignments */
    static ArrayList<Assignment> assignments = new ArrayList<>();

    /** Assignment Name instance */
    String nameStr;

    /** Class name instance */
    String classNameStr;

    /** Time instance */
    String timeStr;

    /** Date instance */
    String dateStr;

    /** Determines whether we are editing an existing assignment */
    boolean isEditMode;

    /** Instance used in Editing the assignment */
    Assignment selectedAssignment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_assignment);
        setTitle("Add an Assignment");

        isEditMode = false;
        final EditText nameBlank = findViewById(R.id.nameBlank);
        final EditText classBlank = findViewById(R.id.classBlank);
        final TextView tv = findViewById(R.id.timetv);
        timeStr = "23:00";
        tv.setText("23:00"); // Default of 11:00 PM
        final TextView dv = findViewById(R.id.datetv);
        nameBlank.requestFocus(); // Place the cursor here first
        nameBlank.setHint("Title");
        classBlank.setHint("Class");

        // For the "Edit Assignment" case
        Intent passedIntent = getIntent();
        if (passedIntent.hasExtra("edit")) {
            isEditMode = true;
            selectedAssignment = (Assignment) passedIntent.getSerializableExtra("edit");
            setTitle("Edit \'" + selectedAssignment.getName() + "\'");
            getSharedPreferences(PREF_NAME, 0).edit().remove(selectedAssignment.getName()).apply();
            nameBlank.setText(selectedAssignment.getName());
            classBlank.setText(selectedAssignment.getClassName());
            tv.setText(selectedAssignment.getTimeDue());
            dv.setText(selectedAssignment.getDateDue());
        }

        // Button to set the time
        Button timeSetButton = findViewById(R.id.timeSetButton);
        timeSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final TextView tv = findViewById(R.id.timetv);
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddAssignment.this,
                        new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String minStr = "";
                        if (selectedMinute < SPECIAL_NUM) {
                            minStr = "0" + selectedMinute;
                        } else {
                            minStr = "" + selectedMinute;
                        }
                        timeStr = selectedHour + ":" + minStr;
                        tv.setText(timeStr);
                    }
                }, hour, minute, false);//False: 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // Button to set the date
        Button dateSetButton = findViewById(R.id.dateSetButton);
        dateSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog;
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR); // current year
                int mMonth = c.get(Calendar.MONTH); // current month
                int mDay = c.get(Calendar.DAY_OF_MONTH); // current day
                datePickerDialog = new DatePickerDialog(AddAssignment.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // set day of month , month and year value in the edit text
                                dateStr = (monthOfYear + 1) + "/" + dayOfMonth + "/" + year;
                                String displayDateStr = (monthOfYear + 1) + "/" + dayOfMonth;
                                dv.setText(displayDateStr);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        // Cancel Button
        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish(); // Finishes the activity view in favor of previous activity
            }
        });

        /**
         * Save button. On Click, it makes sure
         * all of the fields are filled out,
         * makes sure the data is valid,
         * and uses SharedPrefs to store locally.
         */
        final Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nameStr = nameBlank.getText().toString();
                classNameStr = classBlank.getText().toString();
                String isValidMsg = dataIsValid();
                if (!(isValidMsg.equals(""))) {
                    toast(isValidMsg);
                } else {
                    // Create our data object
                    Assignment newAssignment = new Assignment(nameStr, classNameStr, timeStr, dateStr);

                    // Serialize the object into a string
                    String serializedData = newAssignment.serialize();

                    // Save the serialized data into a shared preference
                    try {
                        SharedPreferences preferencesReader = getSharedPreferences(PREF_NAME,
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferencesReader.edit();
                        editor.putString(nameStr, serializedData);
                        editor.apply();
                        assignments.add(newAssignment);
                        numAssignments++;
                    } catch (Exception e) {
                        toast("Error: Could not add assignment");
                        finish();
                    }

                    if (isEditMode) {
                        toast(newAssignment.getName() + " [" + newAssignment.getClassName()
                                + "] edited.");
                        setResult(RESULT_OK, null);
                        Intent intent = new Intent(AddAssignment.this,
                                MainActivity.class);
                        startActivity(intent);
                    } else {
                        toast(newAssignment.getName() + " [" + newAssignment.getClassName()
                                + "] added.");
                        setResult(RESULT_OK, null);
                        finish();
                    }

                }
            }
        });

    }

    /**
     * Assures that the given data is valid.
     * Returns "__ is invalid" if they are empty,
     * as well as if a date is in the
     * past (including current day)
     * @return Whether the entry is valid
     */
    private String dataIsValid(){
        // Validate the inputs
        if (nameStr.equals("")) {
            return "Name is invalid.";
        } else if (classNameStr.equals("")) {
            return "Class is invalid.";
        } else if (timeStr == null) {
            return "Time is invalid.";
        } else if (dateStr == null) {
            return "Date is invalid.";
        }

        // Validate the calendar data
        final Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR); // current year
        int cMonth = c.get(Calendar.MONTH) + 1; // current month
        int cDay = c.get(Calendar.DAY_OF_MONTH); // current day
        int month = Integer.valueOf(dateStr.substring(0, dateStr.indexOf('/')));
        String dateSubStr = dateStr.substring(dateStr.indexOf('/') + 1, dateStr.length());
        int day = Integer.valueOf(dateSubStr.substring(0, dateSubStr.indexOf('/')));
        dateSubStr = dateSubStr.substring(dateSubStr.indexOf('/') + 1, dateSubStr.length());
        int year = Integer.valueOf(dateSubStr);
        Log.d("Year","Current year: " + cYear + " Passed year: " + year);
        Log.d("Month","Current month: " + cMonth + " Passed year: " + month);
        Log.d("Day","Current day: " + cDay + " Passed day: " + day);
        if (year < cYear) {
            return "Date must be in the future.";
        } else if (year == cYear) {
            if (month < cMonth) {
                return "Date must be in the future.";
            } else if (month == cMonth) {
                if (day < cDay) { // TODO: less than or equal
                    return "Date must be in the future.";
                }
            }
        } else {

            return "";
        }

        return "";
    }

    /**
     * Wrapper to make a Toast message.
     * @param msg Message to display
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
