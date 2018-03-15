package com.example.jacob.actiondrawertest;

import com.google.gson.Gson;
import java.util.Calendar;

import java.io.Serializable;
import java.util.Date;

/**
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
            this.timeRemaining = calcTime(timeDue);
        }

        // getters
        public String getName() {
            return name;
        }

        public String getClassName() {
            return className;
        }

        public String getTimeDue() {
            return timeDue;
        }

        public String getDateDue() {
            return dateDue;
        }

        public boolean isDone() {
            return isDone;
        }

        public void setDone() { this.isDone = true; } ////////////// Planning on using this

        public int calcTime(String timeDue) {
            // 20:03 example
            Date currentTime = Calendar.getInstance().getTime();
            return 0;
        }

        public String serialize() {
            // Serialize this class into a JSON string using GSON
            Gson gson = new Gson();
            return gson.toJson(this);
        }

        static public Assignment create(String serializedData) {
            // Use GSON to instantiate this class using the JSON representation
            Gson gson = new Gson();
            return gson.fromJson(serializedData, Assignment.class);
        }
}
