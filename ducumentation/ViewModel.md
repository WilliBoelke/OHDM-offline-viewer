# ViewModel



In MVVM the `ViewModel`processes data for the views, the view can call methods on its `ViewModel` - not the other way around.
The `View` can react to changes made through the `ViewMode`  by observing its `LiveData`  objects. 

The `ViewModel` communicates with the Repositories to get the Data and passes them to the View.

And that's how it is implemented in the App.

Each fragment / activity has its own ViewModel class (`FragmentHome` -> `ViewModelHome`)



## ViewModelHome

Extends : ViewModel

Since `FragmentHome` doesn't have much logic this mostly passes data between `FragmentHome` and `LocalMapsRepository` 

####  Methods 

| Name                    | Returns                                                      | Parameters                              | Description                                                  | Notes |
| ----------------------- | ------------------------------------------------------------ | --------------------------------------- | ------------------------------------------------------------ | ----- |
| init()                  | void                                                         |                                         | initializes the `LocalMapsRepository` and gets the LiveData object with the local files from it |       |
| getLocalMapFiles()      | LiveData<ArrayList<File>>, a live data object which can be observed by the view |                                         | Getter for the localMapFiles LiveData Object                 |       |
| deleteLocalMapFile(int) | void                                                         | int, position of the File to be deleted | Calls LocalMapsRepository.deleteLocalMapFileAt()             |       |
| chooseMapAt(int)        | void                                                         | int, the position of the chosen File    | Calls LocalMapsRepository.chooseMapAt()                      |       |



## ViewModelOptions

Extends : ViewModel
The `FragmentOptions` displays and alters data from `UserPreferences` 

####  Methods 

| Returns                       | Parameters                        | Description                                                  | Notes |
| ----------------------------- | --------------------------------- | ------------------------------------------------------------ | ----- |
| void                          | SharedPreferences from the App    | Called at app start by MainActivity, gets the saved data from the SharedPreferences |       |
| String, the user ID           |                                   | Getter for the user ID                                       |       |
| true, if enabled false if not |                                   | Getter for the darkmode boolean                              |       |
| void                          | true, to enable false, to disable | Setter for the darkmode boolean, changes value in SharedPreferences too |       |
| void                          | String, the user ID               | setter for the user ID,, changes value in SharedPrefs too    |       |



## ViewModelMainActivity

Extends : ViewModel
Initialized at App start by the MainActivity.

####  Methods 

| Name                    | Returns                     | Parameters                     | Description                                                  | Notes                                     |
| ----------------------- | --------------------------- | ------------------------------ | ------------------------------------------------------------ | ----------------------------------------- |
| init(SharedPreferences) | void                        | SharedPreferences from the App | Called at app start by MainActivity, gets the saved data from the SharedPreferences |                                           |
| HttpGetIdFromServer()   | void                        |                                | Gets the User ID from the server                             | Note : this shouldn't be in the ViewModel |
| isDarkmodeEnabled()     | true, if enabled else false |                                | Getter for the darkmode boolean                              |                                           |
| createOhdmDirectory()   | void                        |                                | Creates the OHDM dir at first start of the app               |                                           |



## ViewModelNavigation

Extends :ViewModel

####  Methods 

| Name                          | Returns                                                      | Parameters                               | Description                                | Notes |
| ----------------------------- | ------------------------------------------------------------ | ---------------------------------------- | ------------------------------------------ | ----- |
| getLastKnownLocation(Context) | Location, an Object which contains Latitude and Longitude of the last known location of the device | Context, needed to check the permissions | Gets the last known location of the device |       |



## ViewModelRequestNewMap

Extends : ViewModel
Initialized at App start by the MainActivity.

####  Methods 

| Name                                                         | Returns                                                      | Parameters                                                   | Description                                                  | Notes                                                |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ---------------------------------------------------- |
| getDatePickerValuesAsString(DatePicker)                      | String, a String build from the `DatePicker` input           | `DatePicker`, the `DatePicke` where  will be read from       | Building a formatted String (yyyy-mm-dd), as its needed by the server |                                                      |
| getCoordinatesAsString(EditText, EditText, EditText, EditText) | String, coordinates as String                                | The four edit texts where the user inputs latitudes and longitudes | Building a String out of the coordinates, as expected from the server |                                                      |
| buildParamsString(String, String, String)                    | String, the parameter String as expected by the server       | the name, coordinates and date as strings                    | Builds a String as accepted by the server <br />example: name=mapname<br />&coords=13.005,15.123_13.005,<br />15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11&id=1JWd3wc l |                                                      |
| checkForNullCoordinates(Context, EditText, EditText, EditText, EditText) | true, if every coordinate was set, false if there are empty EditText's | Context, to make Toasts, and the four EditText's             | Check if the user entered all necessary coordinates          |                                                      |
| checkForNullName(Context, EditText)                          | True, if a name was given.                                   | Context to make a toast, EditText where the user inputs the name | Check if a name was entered by the user                      |                                                      |
| RequestMap(String)                                           | void                                                         | The paramsString                                             | Makes a Http request with the paramsString                   | **Note : this should be moved out of the ViewModel** |



## ViewModelRequestStatus

Extends : ViewModel
Initializes the `HttpRepository` to get the /requestById string as response and makes it available to be observed by the `FragmentRequestStatus`, Manages the communication between the two.
Process the String for the `FragmentRequestStatus`
Provides observable booleans to enable `FragmentRequestStatus` to show different connection states (Like: No Connection, No Requests, Connected ...)

####  Methods 

| Name                  | Returns                                                      | Parameters                  | Description                                                  | Notes |
| --------------------- | ------------------------------------------------------------ | --------------------------- | ------------------------------------------------------------ | ----- |
| processString(String) | void                                                         | String, the server response | Processes the String the server returned as response. Checks if its valid , the builds an ArrayList from it which can be displayed by the view. |       |
| getResponse()         | LiveData<String>                                             |                             | Gets the response from the HttpRequest as String, this will e observed by the view. As soon as the value changes the View will call processString() with it and the which will the alter the value of {@link requests}, and/or the Booleans which again are observed by the views |       |
| getRequests()         | LiveData<ArrayList<String>>, the ArrayList processString() builds from the server response as observable LiveData |                             | Provides a Live Data object to the view, which  will get notified as soon as it changes. * It will change when there came a valid response from the server by the processString() method |       |
| refresh()             | void                                                         |                             | Lets the HttpRepository make a server request again, as soon as the response changes everyone will get notified through the LiveData |       |

**There are Four more methods in this view model  `getNoConnectionBoolean`, `getRequestsReceivedBoolean`,  `getNoRequestsForIdBoolean`,  and  `getConnectionBoolean`. The all do pretty much the same, that's why  didn't wanted to mention them individually in the table above.** 

They all provide a LiveData<Boolean> which can be observed by the View. The value of them will be changed by the `processString()` method depending on the strings content, They enable the View to change its appearance depending on the Servers response .

 

## ViewModelDownloadCenterAll

Extends : ViewModel


####  Methods 

| Name                    | Returns                         | Parameters            | Description                                                  | Notes |
| ----------------------- | ------------------------------- | --------------------- | ------------------------------------------------------------ | ----- |
| downloadMap(RemoteFile) | void                            | File to be downloaded | Starts the download of a `RemoteFile` via the `FtpRepository` |       |
| refresh()               | void                            |                       | Triggers a refresh of ALL data manged by the `FtpRepository` also for the data displayed by the `FragmentDownloadCenterCategories` |       |
| init()                  | void                            |                       | Initializes all necessary Objects for the first time         |       |
| getAllMaps()            | LiveData<ArrayList<RemoteFile>> |                       | Returns the allMaps LiveData Object                          |       |
| getMostRecentMaps()     | LiveData<ArrayList<RemoteFile>> |                       | Returns the mostRecentMaps LiveData object                   |       |



## ViewModelDownloadCenterCategories

Extends : ViewModel
Initialized at App start by the MainActivity.

####  Methods 

| Name                           | Returns                                           | Parameters            | Description                                                  | Notes |
| ------------------------------ | ------------------------------------------------- | --------------------- | ------------------------------------------------------------ | ----- |
| downloadMap(RemoteFile)        | void                                              | File to be downloaded | Starts the download of a `RemoteFile` via the `FtpRepository` |       |
| refresh()                      | void                                              |                       | Triggers a refresh of ALL data manged by the `FtpRepository` also for the data displayed by the `FragmentDownloadCenterAll` |       |
| init()                         | void                                              |                       | Initializes all necessary Objects for the first time         |       |
| getDirectories()               | LiveData<ArrayList<RemoteDirectory>>              |                       | Returns the directories LiveData Object                      |       |
| getDirectoriesContents(String) | LiveData<HashMap<String, ArrayList<RemoteFile>>>> |                       | Returns the directoriesContents LiveData object              |       |