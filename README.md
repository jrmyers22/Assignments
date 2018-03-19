# Assignments

A continuation of the Materials and Assignments Management Assistant.

## Description

The plan for this project is to interface with an already existing Google Sheets spreadsheet to make keeping track of learning portals and assignments easier. The Android app will have the ability to add assignments, mark them as "Done", remove assignments, opt-into notifications, and change the theme of the homescreen list.

## Concept Drawings

Mockups of the GUI, flows, and push notifications can be found (with the proper permission) at:
https://drive.google.com/drive/u/0/folders/1IuitPQHB_kF8mRTA-AcNIBug6orwwu1s

## Current State (3/15/2018)

- [x] Add an assignment to the homescreen list
- [x] Create "Assignment" POJO class for getters and setters
- [x] Click on a list item to view more information
- [x] Retrieve assignments on app start
- [x] Use SharedPreferences to store Assignment objects 
- [x] Ability to long press on a list item to "Remove" 
- [x] Keep list of "Done" items
- [x] Ability to long press on a list item to "Mark Done" (does not remove from list)
- [x] Cancel/Back floating button in Done list
- Add "status" option to the assignment view
- Add class name and days 'til in the list view
- The list items change color based on the days 'til value
- The list items rearrange to have the most important assignments at the top
- Edit an existing list item and save the edits
- Send notifications daily at ~7am
- Integrate the app with the existing Google spreadsheet
- Convert app to run on multiple threads for better performance

## Stretch Goals

- Tutorial on first open
- Google assistant integration to be able to ask for upcoming assignments
- Pull down to sync (with loading animation)
- App shortcuts by long pressing the home screen shortcut
- Fingerprint authentication (?? just to see how to implement)
