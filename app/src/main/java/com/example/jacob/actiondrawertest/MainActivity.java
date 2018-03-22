package com.example.jacob.actiondrawertest;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuInflater;
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

    /** Shared Preferences name for the regular list */
    final String PREF_NAME = "Saved_Assignments";

    /** Shared Preferences name for the done list */
    final String DONE_NAME = "Done_Assignments";

    boolean removeAll = false;

    /** List of regular assignments retrieved from SP */
    public ArrayList<Assignment> assignments;

    /** List of done assignments retrieved from SP */
    public ArrayList<Assignment> doneAssignments;

    /** List of assignment names used when populating list view */
    public ArrayList<String> assignmentNames;

    /** Adapter for the main list view */
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

    /**
     * Determines whether to refresh the list view, based
     * on the resultCode from the "AddActivity" class. Refreshes
     * when notified that a new activity has been added.
     * @param requestCode request code
     * @param resultCode Code returned from child activity
     * @param data data returned
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            displayListView();
        }
    }

    /**
     * Closes the Navigation Drawer when the back
     * button is pressed. Useful when the user returns
     * to the Main Activity from a different activity.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Creates the options menu in the Action Bar.
     * @param menu object containing menu properties.
     * @return true if the options menu is inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * Either shows a tutorial or removes all of the
     * main list entries, depending on the parameter.
     * @param item The item selected.
     * @return true if the item's task has succeeded.
     */
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
        if (id == R.id.remove_all) {
            SharedPreferences preferencesReader = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            Map<String, ?> allEntries = preferencesReader.getAll();
            if (allEntries.size() == 0) {
                toast("No Assignments to Remove");
            } else {
                getSharedPreferences(PREF_NAME, 0).edit().clear().apply();
                this.recreate();
                toast("All Assignments Removed");
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Determines which Activity to send the user
     * to based on which Navigation Drawer Item they
     * select.
     * @param item Navigation Drawer item selected.
     * @return true if the activity was started correctly.
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.marked_done) {
            Intent intent = new Intent(MainActivity.this, DoneAssignments.class);
            startActivity(intent);
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
                toast("Unable to open Browser.");
            }
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Creates the small context menu when a list item
     * is long pressed.
     * @param menu menu created
     * @param v View metadata
     * @param menuInfo menu metadata
     */
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
        menu.add(0, v.getId(), 0, "Mark Done");
        menu.add(0, v.getId(), 0, "Remove");
    }

    /**
     * Either marks the assignment "done" (doesn't remove from home list)
     * or removes the assignment from home list view (not done list).
     * @param item selected by the user.
     * @return true if the corresponding code fires correctly.
     */
    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        if(item.getTitle()=="Mark Done"){
            Assignment tmp = assignments.get(info.position);
            tmp.setDone();

            // Get the serialized string
            String doneAssignment = tmp.serialize();

            // Write the edited assignment to original list
            SharedPreferences preferencesReader = getSharedPreferences(PREF_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencesReader.edit();
            editor.putString(tmp.getName(), doneAssignment);
            editor.apply();

            // Write the serialized object to the "done" shared prefs
            SharedPreferences dPreferencesReader = getSharedPreferences(DONE_NAME,
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor dEditor = dPreferencesReader.edit();
            dEditor.putString(tmp.getName(), doneAssignment);
            dEditor.apply();

            // Add the assignment to the "done" list
            doneAssignments.add(tmp);

            // Remove the assignment from the normal shared prefs
            getSharedPreferences(PREF_NAME, 0).edit().remove(tmp.getName()).apply();

//            this.recreate();
            displayListView();
            toast(tmp.getName() + " Marked Done");
        }
        if(item.getTitle()=="Remove"){ // Removes assignment from sharedPrefs then recreates
            assignmentNames.remove(info.position);
            Assignment tmp = assignments.get(info.position);
            assignments.remove(tmp);
            getSharedPreferences(PREF_NAME, 0).edit().remove(tmp.getName()).apply();
            this.recreate();
            toast(tmp.getName() + " Removed");
        }

        return true;
    }

    /**
     * Populates the main list view with all of the non-done
     * assignments pulled from SharedPrefs.
     */
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
                View view = super.getView(position, convertView, parent);

                // Get the Layout Parameters for ListView Current Item View
                // Set the height of the Item View
                ViewGroup.LayoutParams params = view.getLayoutParams();
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

                        // Updates the status for the main list view
                        if (selectedAssignment.isDone()) {
                            intent.putExtra("done", selectedAssignment);
                        } else {
                            intent.putExtra("selected", selectedAssignment);
                        }
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
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}