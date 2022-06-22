# App-Blocker-Java---Prototype

This app is built using Java Programming Language. It works for API Level 21 and above and should work for 98% of the android devices. 

This is a prototype app which shows that when user tries to access Gmail app on their phone, the app blocker's lock screen will take over which will not allow the user to access Gmail.

# Layout and logic
1. The app is using a constraint layout with a button at the bottom of the screen.
2. The button actually starts a background service by confirming if user has enabled 'usage access' for the app in the settings menu.
3. Once the background service starts, it will keep detecting the last opened app and if that app's package name matches the Gmail package name (com.google.android.gm), it will send an intent to start a new activity (The Lock Activity) which basically, blocks the user from accessing the Gmail app. 
4. The Lock Activity displays a Text View stating "This app is blocked :(".

# Improvements
1. The app needs to be dynamic where the user should be alllowed to select apps which they want to block.
2. Specific design improvements in terms of using a recycler view or list to display the apps installed on the device and also to show the blocked apps by the user.
3. Since we are using a background service, battery consumption for the app was not looked at. If there is a better solution available which consumes less battery, that should definiely be looked at. 
