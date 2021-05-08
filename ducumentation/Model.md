# Model 

The App is implemented following the MVVM Pattern. 

The Package Model contain all classes related to persistence, the local files and the remote (ftp) files.

## FileStructure

The package FileStructure contains the classes `RemoteFile` and `RemoteDirectory` which are used throughout the app to represent files and directories on the server. 

## Repositories 

Repositories in MVVM are classes which retrieve data from Servers, Databases etc.
Here we differentiated between remote repositories and local repositories, where he remote repositories communicate with our server and the local repositories manage the local stored data such as the files in the OHDM directory or the `SharedPreferences`.

Repositories  should be accessed through the `ViewModel`.
They return `LiveData` objects which can be processed by the `ViewModel` and observed from the Views

## Files 

Local files will be stored in the "OHDM"
directory on the phone or tablet, this will be created at the first start of the App.
The `FragmentHome` will display the content of this directory in a recycler view.

`RemoteFiles` and `RemoteDirectories` (from the SFTP server) aren't completely persistent.
They will be retrieved at each start of the App. But to minimize Server interactions they will be persisted by the Repositories during runtime.



## Classes and Methods 



### MapFileSingleton

This singleton holds a reference to a local map `File` chosen my the user. 
This map will be displayed in the `FragmentNavigation` and can be changed by the user at any time in the `FragmentHome`.

####  Methods 

| Name               | Returns                        | Parameters | Description                                                  | Notes                           |
| ------------------ | ------------------------------ | ---------- | ------------------------------------------------------------ | ------------------------------- |
| MapFileSingleton() |                                |            |                                                              | (Singleton) private constructor |
| getInstance()      | MapFileSingleton, the instance |            | Will return the saved instance or a new instance in case it wasn't initialized before |                                 |
| getFile()          | File, the saved file           |            | returns the saved file                                       |                                 |
| setFile(File)      | void                           |            | setter for the referenced File                               |                                 |



### RemoteDirectory

A class to describes a remote directory (from the SFTP server)


####  Methods 

| Name                            | Returns                                        | Parameters                                                   | Description                  | Notes       |
| ------------------------------- | ---------------------------------------------- | ------------------------------------------------------------ | ---------------------------- | ----------- |
| RemoteDirectory(String, String) |                                                | path = the path of the dir on the SFTP server, String creationDate |                              | constructor |
| getFilename()                   | String, the name of the file                   |                                                              | Getter for the filename      |             |
| getCreationDate()               | String, the creation date of the dir           |                                                              | Getter for the creation date |             |
| getPath()                       | String, the path of the dir on the SFTP Server |                                                              | Getter for the path          |             |
| setFilename()                   | void                                           | String, the name of the file                                 | Setter for the filename      |             |
| setCreationDate()               | void                                           | String, the creation date                                    | Setter for the creation date |             |
| setPath()                       | void                                           | String, the path of the dir                                  | Setter for the path          |             |



### RemoteFile

A class to describes a remote file (from the SFTP Server).




####  Methods 

| Name                                     | Returns                                        | Parameters                                             | Description                  | Notes       |
| ---------------------------------------- | ---------------------------------------------- | ------------------------------------------------------ | ---------------------------- | ----------- |
| RemoteFile(String, String, Long, String) |                                                | the name of the file, its path, its size, and its date |                              | constructor |
| getFilename()                            | String, the name of the file                   |                                                        | Getter for the filename      |             |
| getCreationDate()                        | String, the creation date                      |                                                        | Getter for the creation date |             |
| getPath()                                | String, the path of the dir on the SFTP server |                                                        | Getter for the path          |             |
| getSize()                                | Long , the file size in KB                     |                                                        | Getter for the size          |             |
| setCreationDate(String)                  | void                                           | String, the creation date                              | Setter for the creation date |             |
| setPath(String)                          | void                                           | String, the path of the file                           | Setter for the path          |             |
| setFileSize(long)                        | void                                           | Long, the size of the file                             | Setter for fileSize          |             |
| setFilename(String)                      | void                                           | String, name of the file                               | Setter for the name          |             |



### LocalMapsRepository 

Singleton
Used in `FragmentHome` / `ViewModelHome`.
Reads files from the `OHDM` directory of the device and returns a `LiveData<ArrayList<File>>` Object which can be observed by the View.
**Note : Only files with .map ending will be returned**

####  Methods 

| Name                              | Returns                                                      | Parameters                                                   | Description                                                  | Notes                                                        |
| --------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| readLocalMapFiles(File targetDir) | void                                                         | targetDir, a `File`  usually with its path to the OHDM directory | Reads all .map files from the OHDM dir and adds them to the localMaps list | I passed  the targetDir, to improve testability              |
| deleteLocalMapFile(int)           | void                                                         | position of the files in the localMaps ArrayList             | Deletes the file from the ArrayList and from the local storage |                                                              |
| getLocalFiles(File targetDir)     | `LiveData<ArrayList<File>>`, the localMaps list inside a LiveData object | targetDir, a `File`  usually with its path to the OHDM directory | Calls readLocalMapFiles()<br />refreshes the LiveData object and returns it | I passed the targetDir to improve testability                |
| chooseMapAt(File targetDir)       | void                                                         | position of the file in the localMaps ArrayList              | Set the file at "position" as the chosen file in the `MapFileSingleton` | This can be done better : delete the MapFileSingleton and make its functionality part of this class |



### UserPreferences

Singleton
Initialized at App start by the MainActivity.
I needed that to make data stored within the SharedPreferences available throughout the App without0 passing Contexts everywhere

For example: The ID is used in the `HttpRequestRepository ` and it wouldn't be good practice to pass the context from the view 

####  Methods 

| Name                    | Returns                     | Parameters                        | Description                                                  | Notes |
| ----------------------- | --------------------------- | --------------------------------- | ------------------------------------------------------------ | ----- |
| init(SharedPreferences) | void                        | SharedPreferences from the App    | Called at app start by MainActivity, gets the saved data from the SharedPreferences |       |
| getUserID()             | String, the user ID         |                                   | Getter for the user ID                                       |       |
| isDarkmodeEnabled()     | true, if enabled else false |                                   | Getter for the darkmode boolean                              |       |
| enableDarkmode(Boolean) | void                        | true, to enable false, to disable | Setter for the darkmode boolean, changes value in SharedPreferences too |       |
| setUserID(String)       | void                        | String, the user ID               | setter for the user ID,, changes value in SharedPrefs too    |       |



### FtpRepository

Singleton

Manages Data from the STP server (or Sftp), returns LiveData Objects which can be observed by Views, Persists the Data during runtime.
Uses the async FtpTasks to communicate with the server, used by the map download fragments / their view models.

####  Methods 

| Name                                             | Returns                                                      | Parameters                | Description                                                  | Notes |
| ------------------------------------------------ | ------------------------------------------------------------ | ------------------------- | ------------------------------------------------------------ | ----- |
| getDirectoryContents()                           | `MutableLiveData<HashMap<String, ArrayList<RemoteFile>>> ` , A HashMap which contains the Name of the directories as Key and a ArrayList of  Remote as their contents |                           | Getter of the dirContents                                    |       |
| getMostRecentMaps()                              | `MutableLiveData<ArrayList<RemoteFile>>`, List of all remoteFiles inside the most recent dir. |                           | Getter for the most recently requested maps                  |       |
| getAllMaps()                                     | `MutableLiveData<ArrayList<RemoteFile>>`, A list of all maps inside  the root dir |                           | Getter for the allMaps                                       |       |
| getDirectories(Boolean)                          | `MutableLiveData<ArrayList<RemoteDirectory>>` ; a list of all Directories inside  the root dir |                           | Getter for the directories                                   |       |
| retrieveRemoteFilesFrom(String, MutableLiveData) | void                                                         | Path of the directory     | Gets all File's from path using FtpTaskFileListing and publishes them in the LiveData Object |       |
| retrieveRemoteDirectories()                      | void                                                         |                           | Gets all Directories inside the root dir using FtpTaskDirListing and publishes them in the LiveData Object. Calls retireveDirContent() |       |
| retrieveDirContent(String)                       | void                                                         | Path of the directory     |                                                              |       |
| refresh()                                        | void                                                         |                           | Calls all retrieve Methods, used in the views SwipeToRefreshLayout, observing views will be notified about changes |       |
| downloadMap(RemoteFile)                          | void                                                         | the File to be downloaded | Downloads the file using FtpTaskFileDownload                 |       |



### HttpRequestsRepository

Singleton

Manages all Http requests and responses, persist the data during runtime.

**Note : At the moment just the FragmentRequestStatus / its ViewModel are using this Repository - the Main Activity and the FragmentRequestNewMap still making their Http request still from their ViewModel  -  this needs to be changed **



#### Methods

| Name               | Returns                                           | Parameters | Description                                                  | Notes |
| ------------------ | ------------------------------------------------- | ---------- | ------------------------------------------------------------ | ----- |
| getRequests()      | A LiveData Object with the String of all requests |            | Getter for the LiveData Object                               |       |
| retrieveRequests() | void                                              |            | retrieves all request which the user made from the Server using HttpTaskRequest |       |



### Variables 

Class containing static final Variables such as the Server Ports and IP, Paths etc.