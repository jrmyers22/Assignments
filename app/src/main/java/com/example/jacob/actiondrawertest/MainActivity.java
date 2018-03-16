package com.example.jacob.actiondrawertest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String PREF_NAME = "Saved_Assignments";
    public ArrayList<Assignment> assignments;
    public ArrayList<Assignment> doneAssignments;
    public ArrayList<String> assignmentNames;
    ListAdapter assignmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        assignments = new ArrayList<>();
        doneAssignments = new ArrayList<>();
        assignmentNames = new ArrayList<>();

        displayListView();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton addButton = findViewById(R.id.fab);
        addButton.setTooltipText("Add Assignment");
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddAssignment.class);
                startActivityForResult(intent, 1);
            }
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void testToast() {
        Toast.makeText(this, "Calling Update Display", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            displayListView();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.marked_done) {
//            Intent intent = new Intent(MainActivity.this, AddAssignment.class);
//            startActivity(intent);
        } else if (id == R.id.notifications) {
            Intent intent = new Intent(MainActivity.this, Notifications.class);
            startActivity(intent);
        } else if (id == R.id.change_rules) {
            Intent intent = new Intent(MainActivity.this, ChangeRules.class);
            startActivity(intent);
        } else if (id == R.id.spreadsheet) {
            Uri page = Uri.parse("https://www.google.com/sheets/about");
            Intent webIntent = new Intent(Intent.ACTION_VIEW, page);
            PackageManager packageManager = getPackageManager();
            List activities = packageManager.queryIntentActivities(webIntent,
                    PackageManager.MATCH_DEFAULT_ONLY);
            boolean isIntentSafe = activities.size() > 0;
            if (isIntentSafe) {
                startActivity(webIntent);
            } else {
                Toast.makeText(MainActivity.this, "Unable to open Browser.",
                        Toast.LENGTH_LONG).show();
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayListView() {

        // Put in loop to get all stored assignment
        SharedPreferences preferencesReader = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

        // Get names of assignment
        Map<String, ?> allEntries = preferencesReader.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            String serializedDataFromPreference = preferencesReader.getString(entry.getKey(), null);
            Assignment restoredAssignment = Assignment.create(serializedDataFromPreference);
            if (restoredAssignment != null) {
                if (restoredAssignment.isDone()) {
                    if (!doneAssignments.contains(restoredAssignment)) {
                        doneAssignments.add(restoredAssignment);
                    }
                } else {
                    if (!(assignments.contains(restoredAssignment))
                            && !(assignmentNames.contains(restoredAssignment.getName()))) {
                        assignments.add(restoredAssignment);
                        assignmentNames.add(restoredAssignment.getName());
                    }
                }

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

                // TODO: Replace
                if (position < 2) {
                    view.setBackgroundColor(getResources().getColor(R.color.a_red));
                } else if (position < 5) {
                    view.setBackgroundColor(getResources().getColor(R.color.a_yellow));
                }

                return view;
            }

        };
        final ListView mainListView = findViewById(R.id.mainListView);
        mainListView.setAdapter(assignmentAdapter);
        registerForContextMenu(mainListView);

        // OnClick action for list element
        mainListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                        String selection = "Selected: " + String.valueOf(adapterView.getItemAtPosition(position));
//                        Toast.makeText(MainActivity.this, selection, Toast.LENGTH_LONG).show();
//                        Snackbar.make(view, "Selected: " + String.valueOf(adapterView.getItemAtPosition(position)), Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                        String selected = String.valueOf(adapterView.getItemAtPosition(position));
                        Assignment selectedAssignment = null;
                        for (int i = 0; i < assignments.size(); i++){
                            if (assignments.get(i).getName().equals(selected)) {
                                selectedAssignment = assignments.get(i);
                            }
                        }
                        if (selectedAssignment == null) {
                            throw new IllegalArgumentException("Selected assignment was null");
                        }
                        Intent intent = new Intent(MainActivity.this, ViewAssignment.class);
                        intent.putExtra("selected", selectedAssignment);
                        startActivity(intent);
                    }
                }

        );


    }

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
        menu.add(0, v.getId(), 0, "Mark Done");
        menu.add(0, v.getId(), 0, "Remove");
    }

    public boolean onContextItemSelected(MenuItem item){
        if(item.getTitle()=="Mark Done"){
            Toast.makeText(getApplicationContext(), "Marked Done", Toast.LENGTH_LONG).show();
        }
        if(item.getTitle()=="Remove"){
            getSharedPreferences(PREF_NAME, 0).edit().clear().apply();
            displayListView();
//            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
//            Log.i("ASSIGNMENTS", "GET ITEM AT: " + item.getItemId());
//            String toRemove = assignmentNames.get(item.getItemId());
//            assignmentNames.remove(toRemove);
//            //assignmentAdapter.notify();
//            displayListView();
            Toast.makeText(getApplicationContext(), "Assignment Removed", Toast.LENGTH_LONG).show();
        }
        return true;
    }


}
