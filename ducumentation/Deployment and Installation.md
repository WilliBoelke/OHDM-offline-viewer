## Preferred way: Deploy pre-compiled project
1. Download the [stable version](https://github.com/OpenHistoricalDataMap/OHDMOfflineViewer/releases) of the app.
2. Move the `.apk` to the local storage of your device
3. Allow installation from unknown sources in your device settings
4. Install the `.apk` on your device by clicking on it

## Create project from sources
1. ```git clone https://github.com/OpenHistoricalDataMap/OfflineViewer.git```
2. [Install Android Studio](https://developer.android.com/sdk/index.html).
3. Import the project. Open Android Studio, click `Open an existing Android
   Studio project` and select the project. Gradle will build the project.
4. Connect your Android Device with your Computer.
5. Run the app. Click `Run > Run 'app'`. After the project builds you'll be
   prompted to build or launch an emulator. You then can choose your mobile phone. 
6. Open ```View > Tool Windows > Device File Explorer``` and place your maps in your mobile phone device storage. There should be a ```OHDM``` (In the android emulator, you may have to create it: `Device Explorer > sdcard > New`) directory in the internal storage. 

## Follow these instructions to run the download server
1. ```git clone https://github.com/OpenHistoricalDataMap/OfflineViewer.git``` 
2. ```git clone https://github.com/OpenHistoricalDataMap/DowloadWebService```.
3. Follow the instructions `DownloadWebService/Readme.md`.
4. Change the Variables in ```OfflineViewer/ServerCommunication/Variables``` to your needs.