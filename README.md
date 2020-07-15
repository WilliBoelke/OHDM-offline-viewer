# OHDM Offline Viewer

## Goal of this project
The goal of this repository is to render data from Open Historical Data Map. OHDM has its own data base structure but can provide data for a defined spatial temporal context - in other words: a data (a day) and a region (polygon). Those data can be produced in the OSM XML format which is well known from Open Street Maps. This viewer is an offline viewer. Map data are stored and rendered on the phone which makes apps independent from actual network connections.

To avoid caching dozens of different zoom layers, vector tiles are being used. This makes the navigation and zoom interaction fast.

## Getting Started

Please read the **Deployment and Installation** Guide from the Wiki.

## Use custom map-file download center
This application has an integrated download functionality for map files. 
This makes it possible to host various map files on a remote server. 
You then can easily download them using the app. 

**WARNING:** At this point of development, the download center is a simple FTP server that does not meet any security requirements.
Using it in productivity, will make your server extremely vulnerable.

## Showcase

Home                                               |  Request
:-----------------------------------:|:---------------------------------------:
![HOME](screenshots/home.png )       | ![REQUEST](screenshots/request.png)

DownloadCenter AllMaps               |  DownloadCenter Cateories
:-----------------------------------:|:---------------------------------------:
![HOME](screenshots/download1.png)   | ![DOWNLOAD](screenshots/download2.png)

Settings                             |  About
:-----------------------------------:|:---------------------------------------:
![HOME](screenshots/settings.png)    | ![DOWNLOAD](screenshots/about.png)


## Contact
Developed by: [FalcoSuessgott](https://github.com/FalcoSuessgott)
Developed by: [WilliBoelke](https://github.com/WilliBoelke)
Developed by: [NoteFox](https://github.com/NoteFox)

Project Link: [Open Historical Data Map](https://github.com/OpenHistoricalDataMap)

Institution: [HTW Berlin](https://www.htw-berlin.de/)

Mail: [info@ohdm.net](info@ohdm.net)
 
