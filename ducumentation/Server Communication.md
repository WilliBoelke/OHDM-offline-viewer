# Server Communication

The Package `ServerCommunication` contains all Classes witch directly communicates with the the Server.
Since the app is implemented following the MVVM pattern classes and methods from this package shall only be used in Model classes such as `FtpRepository` and `HttpRepository`.

##  Communication

There are two whys to communicate with the Server: HTTP and SFTP.  SFTP is used to list and download the available files (maps).
While HTTP is used to send simpler requests to the server such as getting the users ID, sending requests for new maps or getting the status of the requests.

###  SFTP

All SFTP communication happens asynchronously and makes use of the `SFTPClien`. You can find the server IP and Ports in `repositories/localRepositories/Variables` they are currently directed to the server at the HTW-Berlin.

There you also will find 2 paths `MOST_RECENT_PATH` and `FTP_ROOT_DIRECTORY`. they determine which directories on the server the `SFTPCliet` will access to retrieve the Files. All files need to be in the `FTP_ROOT_DIRECTORY` the `MOST_RECENT_PATH` is a sub directory of the root directory and is intended to display the last ~10 requests. 

You can also create other directories inside the root dir. They don't need to be hard coded within the app, but they will show up as categories in the `FragmentMapDownloadCenterCategories`.

### HTTP 

The HTTP communication also happens completely asynchronous and make use of the `HTTPClient`. Here again the you will access the server described in `repositories/localRepositories/Variables` in that case with `SERVER_IP` and `HTTP_PORT`.

####  HTTP requests and responses

There are three requests the server will respond to, two of them 

 **/id**  this request will be executed at the very first start of the app *(and on the following starts if for some reasons (no internet connection) the server hasn't  response).*  The server will respond with a simple 8 character string which like `zwFH2aL6` which will be used as the users unique identification at the server

**/request** followed by a string like  ` name=mapname&coords=13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123_13.005,15.123&date=2117-12-11&id=zwFH2aL6`. This will let the server generate a new map with the given parameter. As you can see the last parameter is the users ID. This is necessary to let the user track his own requests. if there is no ID the server will generate a new one for this request.

**/statusByID** followed by a string like `id=zwFH2aL6`  this will again return a string . Which contain all request made by the user with the given ID and looks like that:  `["testRequest1 DONE 00d_00h-00m-06s","testRequest2 DOWNLOADING 00d_00h-00m-39s"]`.

All three of this requests exists as static final Variables in the `HttpClient`

**Note : For more information read the Server documentation as well**



## Classes and Methods 



###  FtpClient

**Please use the SftpClient instead * - the FtpClient is deprecated and not used by the server*

Wraps the Apache [FTPClient](https://commons.apache.org/proper/commons-net/apidocs/org/apache/commons/net/ftp/FTPClient.html) to our needs.

Implements:  Client 

#### Methods 

| Name                                                     | Returns                                                      | Parameters                                                   | Description                                                  | Notes |
| -------------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ----- |
| connect()                                                | 0 = successful connection<br />1 = FTP server refused connection<br />2 = Could not login to FTP Server (probably wrong password)  3 = Socket exception thrown, Server not found 4 = IO Exception  5 = if already connected |                                                              | connects to the server specified in `Variables`              |       |
| isConnected()                                            | `true` = connected `false` = not connected                   |                                                              | returns if the instance is connected                         |       |
| closeConnection()                                        | void                                                         |                                                              | closes the connection                                        |       |
| getDirList(String path)                                  | `ArrayList<RemoteDirectory>` which contains all directories in ``path`` | the path                                                     | Retrieves all directories in `path` from the FTPServer       |       |
| getFileList(String path)                                 | `ArrayList<RemoteFile>` which contains all files in ``path`` | the path                                                     | Retrieves all files in `path` from the FTPServer             |       |
| getAllFilesList(String path)                             | `ArrayList<RemoteFile>` which contains all files in ``path`` | the path                                                     | Retrieves all files in `path` and includes files from sub dirs |       |
| downloadFile(String remoteFileName, String downloadPath) | `true` = success `false` = not successful                    | `remoteFileName` = the name of the file to be downloaded, `downloadPath` = the directory path | downloads a file from the FTPServer and writes it to the device local storage |       |



###  SftpClient

Uses the same Client interface as FtpClient but with a completely different implementation

Implements:  Client 

#### Methods 

| Name                                                     | Returns                                                      | Parameters                                                   | Description                                                  | Notes |
| -------------------------------------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ | ----- |
| connect()                                                | 0 = successful connection<br />1 = FTP server refused connection<br />2 = Could not login to FTP Server (probably wrong password)  3 = Socket exception thrown, Server not found 4 = IO Exception  5 = if already connected |                                                              | connects to the server specified in `Variables`              |       |
| isConnected()                                            | `true` = connected `false` = not connected                   |                                                              | returns if the instance is connected                         |       |
| closeConnection()                                        | void                                                         |                                                              | closes the connection                                        |       |
| getDirList(String path)                                  | `ArrayList<RemoteDirectory>` which contains all directories in ``path`` | the path                                                     | Retrieves all directories in `path` from the FTPServer       |       |
| getFileList(String path)                                 | `ArrayList<RemoteFile>` which contains all files in ``path`` | the path                                                     | Retrieves all files in `path` from the FTPServer             |       |
| getAllFilesList(String path)                             | `ArrayList<RemoteFile>` which contains all files in ``path`` | the path                                                     | Retrieves all files in `path` and includes files from sub dirs |       |
| downloadFile(String remoteFileName, String downloadPath) | `true` = success `false` = not successful                    | `remoteFileName` = the name of the file to be downloaded, `downloadPath` = the directory path | downloads a file from the FTPServer and writes it to the device local storage |       |
| updateOutput(String path)                                | void                                                         | the path                                                     | called by the listing methods to update the output. Then calls the processing methods to make the output usable |       |
| analyseOutput()                                          | A String array containing a string for each file on the server |                                                              | Builds a String Array from the output string                 |       |
| readDate(String[] currentSplit)                          | A Date as String                                             |                                                              |                                                              |       |



### FtpTaskDirListing

Lists directories hosted on the remote FTPServer.
Extends    : AsyncTask<Void,Void,String>
See also   : FtpClient.getDirList

#### Methods 

| Name                                             | Returns     | Parameters                                                   | Description                                                  | Notes       |
| ------------------------------------------------ | ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ----------- |
| FtpTaskDirListing(Context,String, AsyncResponse) |             | context, the path, a implementation of `AsyncResponse` which will be executed onPreExecute() |                                                              | Constructor |
| doInBackground(Void... params)                   | String null |                                                              | Initializes an instance of `FtpClient()` and uses its getDirList() method to retrieve the directories in `path` |             |
| onPostExecute(String result)                     | void        |                                                              | If there where any data to retrieve = executes `asyncResponse.getRemoteDirectories()` |             |



### FtpTaskFileListing

Lists files hosted on the remote FTPServer.
Extends    : AsyncTask<Void,Void,String>
See also   : FtpClient.getFileList

#### Methods 

| Name                                              | Returns     | Parameters                                                   | Description                                                  | Notes       |
| ------------------------------------------------- | ----------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ----------- |
| FtpTaskFileListing(Context,String, AsyncResponse) |             | context, the path, a implementation of `AsyncResponse` which will be executed onPreExecute() |                                                              | Constructor |
| doInBackground(Void... params)                    | String null |                                                              | Initializes an instance of `FtpClient()` and uses its getFileList() method to retrieve the files in `path` |             |
| onPostExecute(String result)                      | void        |                                                              | if there where any data to retrieve = executed `asyncResponse.getRemoteFiles()` |             |



### FtpTaskFileDownloading

Lists files hosted on the remote FTPServer.
Extends    : AsyncTask<RemoteFile, Integer, Long>
See also   : FtpClient.downloadFile

#### Methods 

| Name                                   | Returns     | Parameters        | Description                                                  | Notes       |
| -------------------------------------- | ----------- | ----------------- | ------------------------------------------------------------ | ----------- |
| FtpTaskFileDownloading(Context,String) |             | context, the path |                                                              | Constructor |
| doInBackground(Void... params)         | String null |                   | Initializes an instance of `FtpClient()` and uses its downloadFile(filename, path) method to download the files |             |
| onPostExecute(Long params)             | void        |                   | just makes a `Toast` message                                 |             |



### AsyncResponse

An interface with three methods, which needs to be implemented when using the FtpListings and HttpRequests .
The implemented code will be executed in their `.onPostExecute()` method.
This allows to handle the outcome of the AsyncTasks flexible and asynchronous .

#### Methods 

| Name                                                         | Returns | Parameters                              | Description | Notes                                                   |
| ------------------------------------------------------------ | ------- | --------------------------------------- | ----------- | ------------------------------------------------------- |
| getRemoteFiles(Context,ArrayList<RemoteFile> remoteFiles)    | void    | list of the retrieved `RemoteFile`      |             | Needs to be implemented when using `FtpTaskFileListing` |
| getHttpResponse                                              | void    | String server response                  |             | Can be implemented when using `HttpTaskRequest`         |
| getRemoteDirectories(ArrayList<RemoteDirectory> remoteDirectories) | void    | list of the retrieved `RemoteDirectory` |             | Needs to be implemented when using `FtpTaskDirListing`  |



###  HttpClient

Wraps the `java.net.HttpUrlConnections`

Implements:  Client 

#### Methods 

| Name                             | Returns                                                      | Parameters        | Description                                                  | Notes |
| -------------------------------- | ------------------------------------------------------------ | ----------------- | ------------------------------------------------------------ | ----- |
| connect(String requestType)      | 0 = successful connection<br/>1 = ProtocolException<br />2 = MalformedURLException<br/>3 = IOException |                   | connects to the server specified in `Variables`              |       |
| closeConnection()                | void                                                         |                   | closes the connection                                        |       |
| sendRequest(String paramsString) | 0 = successful connection<br />1 = ProtocolException<br/>2 =  IOException | the params string | Sends the paramsString to the server                         |       |
| retreiveResponse()               |                                                              |                   | Waits for the server response , which later can be accessed through getServerResponse() |       |
| getServerResponse()              | String serverResponse                                        |                   | Getter for the server response                               |       |



### HttpTaskRequest

Sends a request to the server and gets its response
Extends    : AsyncTask<Void,Void,String>
See also   : HttpClient

#### Methods 

| Name                           | Returns     | Parameters | Description                                           | Notes       |
| ------------------------------ | ----------- | ---------- | ----------------------------------------------------- | ----------- |
| HttpTaskRequest()              |             |            |                                                       | Constructor |
| doInBackground(Void... params) | String null |            | Sends request to the server and gets its response     |             |
| onPostExecute(String result)   | void        |            | executes the AsyncResponse` .getHttpResponse() method |             |
| onPreExecute()                 |             |            | Connects to the Server                                |             |

