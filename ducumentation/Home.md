## Goal of this project
The goal of this repository is to render data from Open Historical Data Map. OHDM has its own data base structure but can provide data for a defined spatial temporal context - in other words: a data (a day) and a region (polygon). Those data can be produced in the OSM XML format which is well known from [Open Street Maps](https://www.openstreetmap.de/). This viewer is an offline viewer. Map data are stored and rendered on the phone which makes apps independent from actual network connections.

Maps can either be your own .map files or maps from the OHDM database.
It also should be possible to request maps from every date and location which are not available via download at the given time.  

## Reaching the goals
We used [mapsforge's](https://github.com/mapsforge/mapsforge) MapView and its file format `.map` to render and display the maps.
We created a [web service](https://github.com/OpenHistoricalDataMap/DowloadWebService) and used SFTP and HTTP for the download and request functionalities.

## Index 
### Getting Started: <br/>
[Deployment and Installation](https://github.com/OpenHistoricalDataMap/OHDMOfflineViewer/wiki/Deployment-and-Installation)

### Documentation: <br/>
* [ServerCommunication](https://github.com/OpenHistoricalDataMap/OHDMOfflineViewer/wiki/Server-Connection)

* [FileStructure](https://github.com/OpenHistoricalDataMap/OHDMOfflineViewer/wiki/File-Structure)