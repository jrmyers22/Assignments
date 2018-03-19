package com.example.jacob.actiondrawertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Map;

public class DoneAssignments extends AppCompatActivity {

    final String DONE_NAME = "Done_Assignments";
    public ArrayList<Assignment> doneAssignments;
    ListAdapter assignmentAdapter;
    ArrayList<String> assignmentNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_done_assignments);
        setTitle("Done Assignments");

        FloatingActionButton addButton = findViewById(R.id.fab);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Instantiate Lists
        doneAssignments = new ArrayList<>();
        assignmentNames = new ArrayList<>();

        // Put in loop to get all stored assignment
        SharedPreferences preferencesReader = getSharedPreferences(DONE_NAME, Context.MODE_PRIVATE);

        // Get names of assignment
        Map<String, ?> allEntries = preferencesReader.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String serializedDataFromPreference = preferencesReader.getString(entry.getKey(), null);
            Assignment restoredAssignment = Assignment.create(serializedDataFromPreference);
            if (restoredAssignment != null) {
                doneAssignments.add(restoredAssignment);
                assignmentNames.add(restoredAssignment.getName());
            }
        }


        assignmentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                assignmentNames){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){

                // Get the current item from ListView
                View view = super.getView(position,convertView,parent);

                // Get the Layout Parameters for ListView Current Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();

                // Set the height of the Item View
                params.height = 300;
                view.setLayoutParams(params);

                // Set the background color of the cells
//                if (position < 2) {
                    view.setBackgroundColor(getResources().getColor(R.color.a_green));
//                }

                return view;
            }

        };
        final ListView mainListView = findViewById(R.id.mainListView);
        mainListView.setAdapter(assignmentAdapter);

        // OnClick action for list element
        mainListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        String selected = String.valueOf(adapterView.getItemAtPosition(position));
                        Assignment selectedAssignment = null;
                        for (int i = 0; i < doneAssignments.size(); i++){
                            if (doneAssignments.get(i).getName().equals(selected)) {
                                selectedAssignment = doneAssignments.get(i);
                            }
                        }
                        if (selectedAssignment == null) {
                            throw new IllegalArgumentException("Selected assignment was null");
                        }
                        Intent intent = new Intent(DoneAssignments.this, ViewAssignment.class);
                        intent.putExtra("done", selectedAssignment);
                        startActivity(intent);
                    }
                }

        );
    }

    /**
     * Wrapper to make a Toast message.
     * @param msg Message to display
     */
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}
