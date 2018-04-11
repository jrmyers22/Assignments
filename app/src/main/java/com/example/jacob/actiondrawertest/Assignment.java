package com.example.jacob.actiondrawertest;

import com.google.gson.Gson;

import java.io.Serializable;
/**
 * POJO class for an individual assignment.
 * Created by Jacob on 2/17/18.
 */

class Assignment implements Serializable {

    /** Name of the assignment */
    private String name;

    /** Name of the class */
    private String className;

    /** Time the assignment is due */
    private String timeDue;

    /** Date the assignment is due */
    private String dateDue;

    /** Whether the assignment is done */
    private boolean isDone;

    /** Time until assignment is due */
    private int timeRemaining;

    // Constructor
    public Assignment(String name, String className, String timeDue, String dateDue) {
        this.name = name;
        this.className = className;
        this.timeDue = timeDue;
        this.dateDue = dateDue;
        this.isDone = false;
    }

    /**
     * Gets and returns name of
     * the assignment.
     * @return String name of the assignment.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets and returns name of
     * the class.
     * @return String name of the class.
     */
    public String getClassName() {
        return className;
    }

    /**
     * Gets and returns time the
     * assignment is due.
     * @return String representation of time due.
     */
    public String getTimeDue() {
        return timeDue;
    }

    /**
     * Gets and returns date the
     * assignment is due.
     * @return String representation of date due.
     */
    public String getDateDue() {
        return dateDue;
    }

    /**
     * Gets and returns time
     * remaining (days 'til).
     * Set by the ViewAssignment class.
     * @return TimeRemaining (days 'til)
     */
    public String getTimeRem() {
        String retVal = "";
        if (timeRemaining == 1) {
            retVal = "~ " + timeRemaining + " day";
        } else {
            retVal = "~ " + timeRemaining + " days";
        }
        return retVal;
    }

    /**
     * Whether the assignment has been marked
     * done.
     * @return True if the assignment has been marked done.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Sets the value for the time remaining.
     * Called/Set by the ViewAssignment class.
     * @param timeRem integer representing days until due date.
     */
    public void setTimeRemaining(int timeRem) {
        this.timeRemaining = timeRem;
    }

    /**
     * Sets the done value to true.
     */
    public void setDone() { this.isDone = true; }

    /**
     * Serializes the object.
     * Used when storing locally into Shared Prefs
     * @return String serialized object
     */
    public String serialize() {
        // Serialize this class into a JSON string using GSON
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    /**
     * Creates a POJO out of the serialized string.
     * Used when converting SharedPrefs to objects.
     * @param serializedData String from the SharedPrefs.
     * @return Assignment object decoded.
     */
    static public Assignment create(String serializedData) {
        // Use GSON to instantiate this class using the JSON representation
        Gson gson = new Gson();
        return gson.fromJson(serializedData, Assignment.class);
    }
}