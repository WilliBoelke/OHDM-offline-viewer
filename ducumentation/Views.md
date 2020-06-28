# Views 

Package which contains all the view (Activities and Fragments). Since the App is implemented using the MVVM pattern there shouldn't be much logic in the View classes (I know in some cases there still is ...that should be fixed) That's why I don't think that it is necessary to explain each fragment.

Here just a few things in general 

## FragmentWithServerConnection

There are some fragments which display data which needs to be retrieved from the server. Namely `FragmentDownloadCenterAll`, `FragmentDownloadCenterCategories` and `FragmentRequestStatus`.

All of them need to retrieve the data before they can show them. So there should be a little bit of an indication what just happens.

Like a text saying "Connecting..." and a progress bar, or "No Connection"  do achieve that some Views need to change their visibility depending on what happens.

The Abstract class `FragmentWithServerConnection`  gives states which each means a state of the connection process: `STATE_CONNECTED`, etc.
Also it wants us to implement a method for each state. 
That's where the view visibility change should happen.

A Fragment which extents `FragmentWithServerConnection`  can then implement the methods depending on its own views and the can call `changeVisibility(STATE)`to change the visibilities. 

In the `FragmentRequestStatus` that happens in the observer for the Boolean (As described in the ViewModel chapter)



## FragmentFactory 

The fragment factory comes with the AndroidX dependencies and makes it easy to pass dependencies of the fragments (Like a mocked SftpClient) to the fragments while testing. 

**Look into the Espresso test I already wrote to see how it works. Currently it isn't used anymore since I rebuild the App in MVVM but it should be used again to make the tests work again**

For using the FragmentFactory to pass dependencies write a constructor inside the fragment and then Pass the parameter / dependency through the Fragment Factory.

**Note : if you write another constructor inside a Fragment, make sure to write an empty constructor because Fragments always need an empty constructor !!**