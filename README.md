# Assignments

A continuation of the Materials and Assignments Management Assistant.

## Description

The plan for this project is to interface with an already existing Google Sheets spreadsheet to make keeping track of learning portals and assignments easier. The Android app will have the ability to add assignments, mark them as "Done", remove assignments, opt-into notifications, and change the theme of the homescreen list.

## Concept Drawings

Mockups of the GUI, flows, and push notifications can be found (with the proper permission) at:
https://drive.google.com/drive/u/0/folders/1IuitPQHB_kF8mRTA-AcNIBug6orwwu1s

## Current State (4/11/2018)

- [x] Add an assignment to the homescreen list
- [x] Create "Assignment" POJO class for getters and setters
- [x] Click on a list item to view more information
- [x] Retrieve assignments on app start
- [x] Use SharedPreferences to store Assignment objects 
- [x] Ability to long press on a list item to "Remove" 
- [x] Keep list of "Done" items
- [x] Ability to long press on a list item to "Mark Done" (does not remove from list)
- [x] Cancel/Back floating button in Done list
- [x] Add "status" option to the assignment view
- [x] Edit an existing list item and save the edits
- [x] Calculate daysTil when in different month (did not use JodaTime)
- [x] Back link to the spreadsheet inside of the google sheets app
- [ ] Add days 'til in the list view
- [ ] Calculate days 'til each time the MainActivity is shown
- [ ] Add class name in the list view
- [ ] The list items change color based on the days 'til value
- [ ] The list items rearrange to have the most important assignments at the top
- [ ] AddAssignment AlertDialog to select class
- [ ] AddAssignment AlertDialog to select portal (based on the class or list of all portals?)
- [ ] Send notifications daily at ~7am
- [ ] (Depends on pushbullet integration) Integrate the app with the existing Google spreadsheet
- [ ] Convert app to run on multiple threads for better performance
- [ ] Use LeakCanary to find memory leaks
- [ ] Use ProGuard (built-in) to minimize code and make it harder to reverse engineer

## Stretch Goals
### Nice to have, not necessary for MVP use

- [ ] Put screenshots in README
- [ ] Dialog box on first open to tell you what is most important (like spreadsheet)
- [ ] Ability to "Mark Done" from the ViewAssignment screen
- [ ] "Date of Class's Final" screen
- [ ] Tutorial on first open
- [ ] Ability to "revert mark done" from the Done assignments screen
- [ ] Pop up to confirm when "remove all" is chosen (both normal and done lists)
- [ ] Pull down to sync (with loading animation)
- [ ] Notification when assignment has passed, ability to "Mark Done"
- [ ] App shortcuts by long pressing the home screen shortcut - https://github.com/MatthiasRobbers/shortbread
- [ ] Google assistant integration to be able to ask for upcoming assignments
- [ ] Fingerprint authentication (?? just to see how to implement)
