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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

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
            timeStr = selectedAssignment.getTimeDue();
            dateStr = selectedAssignment.getDateDue();
            tv.setText(timeStr);
            dv.setText(dateStr.substring(0, dateStr.lastIndexOf('/')));
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
                String[] isValidMsg = dataIsValid();
                if (!(isValidMsg[0].equals(""))) {
                    toast(isValidMsg[0]);
                } else {
                    // Create our data object, set the Time remaining
                    Assignment newAssignment = new Assignment(nameStr, classNameStr, timeStr, dateStr);
                    newAssignment.setTimeRemaining(Integer.parseInt(isValidMsg[1]));

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
    private String[] dataIsValid(){
        // Validate the inputs
        if (nameStr.equals("")) {
            return new String[] {"Name is invalid.", "0"};
        } else if (classNameStr.equals("")) {
            return new String[] {"Class is invalid.", "0"};
        } else if (timeStr == null) {
            return new String[] {"Time is invalid.", "0"};
        } else if (dateStr == null) {
            return new String[] {"Date is invalid.", "0"};
        }

        // Reduce code redundancy
        int[] current_date_array = getCurrentDate();
        int cDay = current_date_array[0];
        int cMonth = current_date_array[1];
        int cYear = current_date_array[2];

        int month = Integer.valueOf(dateStr.substring(0, dateStr.indexOf('/')));
        String dateSubStr = dateStr.substring(dateStr.indexOf('/') + 1, dateStr.length());
        int day = Integer.valueOf(dateSubStr.substring(0, dateSubStr.indexOf('/')));
        dateSubStr = dateSubStr.substring(dateSubStr.indexOf('/') + 1, dateSubStr.length());
        int year = Integer.valueOf(dateSubStr);
        Log.d("Year","Current year: " + cYear + " Passed year: " + year);
        Log.d("Month","Current month: " + cMonth + " Passed year: " + month);
        Log.d("Day","Current day: " + cDay + " Passed day: " + day);
        String cDate = cDay + "/" + cMonth + "/" + cYear;
        String inDate = day + "/" + month + "/" + year;

//        selectedAssignment.setTimeRemaining(daysTil);
        if (year < cYear) {
            return new String[] {"Date must be in the future.", "0"};
        } else if (year == cYear) {
            if (month < cMonth) {
                return new String[] {"Date must be in the future.", "0"};
            } else if (month == cMonth) {
                if (day < cDay) { // TODO: less than or equal
                    return new String[] {"Date must be in the future.", "0"};
                }
            }
        } else {
            // Calculate and set the days remaining.
            int daysTil = getCountOfDays(cDate, inDate);
            return new String[] {"", "" + daysTil};
        }
        // Calculate and set the days remaining.
        int daysTil = getCountOfDays(cDate, inDate);
        return new String[] {"", "" + daysTil};
    }

    /**
     * Stack Overflow method to compute days in between
     * two dates. Could (preferably) use JodaTime API.
     * Can be found at:
     * https://stackoverflow.com/
     * questions/23323792/android-days-between-two-dates/37659716
     * @param todayDate current date.
     * @param dueDate Due date of the assignment.
     * @return int representing number of days between two dates.
     */
    public int getCountOfDays(String todayDate, String dueDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());

        Date createdConvertedDate = null, expireCovertedDate = null, todayWithZeroTime = null;
        try {
            createdConvertedDate = dateFormat.parse(todayDate);
            expireCovertedDate = dateFormat.parse(dueDate);

            Date today = new Date();

            todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int cYear = 0, cMonth = 0, cDay = 0;

        if (createdConvertedDate.after(todayWithZeroTime)) {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(createdConvertedDate);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);

        } else {
            Calendar cCal = Calendar.getInstance();
            cCal.setTime(todayWithZeroTime);
            cYear = cCal.get(Calendar.YEAR);
            cMonth = cCal.get(Calendar.MONTH);
            cDay = cCal.get(Calendar.DAY_OF_MONTH);
        }

        Calendar eCal = Calendar.getInstance();
        eCal.setTime(expireCovertedDate);

        int eYear = eCal.get(Calendar.YEAR);
        int eMonth = eCal.get(Calendar.MONTH);
        int eDay = eCal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(cYear, cMonth, cDay);
        date2.clear();
        date2.set(eYear, eMonth, eDay);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        return ((int) dayCount);
    }

    private int[] getCurrentDate() {
        // Validate the calendar data
        final Calendar c = Calendar.getInstance();
        int cYear = c.get(Calendar.YEAR); // current year
        int cMonth = c.get(Calendar.MONTH) + 1; // current month
        int cDay = c.get(Calendar.DAY_OF_MONTH); // current day
        int[] ret = new int[]{cDay, cMonth, cYear};
        return ret;
    }

    /**
     * Wrapper to make a Toast message.
     * @param msg Message to display
     */
    private void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}