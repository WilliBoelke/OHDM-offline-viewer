# OHDM Offline Viewer
> View OHDM maps offline
 
![HOME](screenshots/home.png)
![MAP](screenshots/map.png)

![DOWNLOAD](screenshots/download.png)
![ABOUT](screenshots/about.png)

        
## Goal of this project
The goal of this repository is to render [Open Street Maps](https://www.openstreetmap.de/), without the need of a network connection.
To avoid caching dozens of different zoom layers, vector tiles are being used. This makes the navigation and zoom interaction fast.

## Getting Started
*Follow these instructions to build and run the OHDM Offline Viewer*
1. ```git clone https://github.com/OpenHistoricalDataMap/OfflineViewer.git```
2. [Install Android Studio](https://developer.android.com/sdk/index.html).
3. [Downlaod Open Historical Data maps](http://www.ohdm.net/)
4. Import the project. Open Android Studio, click `Open an existing Android
   Studio project` and select the project. Gradle will build the project.
4. Connect your Android Device with your Computer.
5. Run the app. Click `Run > Run 'app'`. After the project builds you'll be
   prompted to build or launch an emulator. You then can choose your mobile phone. 
6. Open ```View > Tool Windows > Device File Explorer``` and place your maps in your mobile phone device storage. There should be a ```OHDM``` (In the android emulator, you may have to create it: `Device Explorerer > sdcard > New`) directory in the internal storage. 

## Convert ```.osm``` to ```.map```-files
We're using [osmosis](https://github.com/openstreetmap/osmosis) with the [mapsforge-map-writer-plugin](https://github.com/mapsforge/mapsforge/blob/master/docs/Getting-Started-Map-Writer.md)
to convert ```.osm``` maps to ```.map```.

In order to use ```osmosis```, just execute ```/osm2map/osm2map.sh``` as sudo.

**The following code-block shows a successful execution:**
```
user@host:~$ sudo bash osm2map.sh 

Downloading osmosis to /opt/osmosis.
...

Downloading mapwriter plugin /opt/osmosis.
...

Download and build successful finished.
  
osmosis usage:
    /opt/osmosis/bin/osmosis --rx file=path-to-osm-file.osm --mw file=destination-path-map-file.map

```

Then you can begin to convert your own ```map-files``` with:
```
/opt/osmosis/bin/osmosis --rx file=path-to-osm-file.osm --mw file=destination-path-map-file.map
```

## Use custom map-file download center
This application has an integrated download functionality for map files. 
This makes it possible to host various map files on a remote server. 
You then can easily download them using the app. 
The following steps will guide you through the installation and configuration:

**WARNING:** At this point of development, the download center is a simple FTP server that does not meet any security requirements.
Using it in productivity, will make your server extremely vulnerable.


### Prerequisites


### Build 


### Configure 


### Deploy


## Contact
Developed by: [FalcoSuessgott](https://github.com/FalcoSuessgott)
Developed by: [WilliBoelke](https://github.com/WilliBoelke)
Developed by: [NoteFox](https://github.com/NoteFox)

Project Link: [Open Historical Data Map](https://github.com/OpenHistoricalDataMap)

Institution: [HTW Berlin](https://www.htw-berlin.de/)

Mail: [info@ohdm.net](info@ohdm.net)
 
