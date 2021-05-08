# Adapters



This package contains all necessary (`RecyclerView`) Adapters and related classes and interfaces like the `OnRecyclerItemDownloadButtonClick` interface
and the `RecylcerAdapterSwipeGestures`



## Used Recycler Views 

There are five Recycler Views needed throughout the App:

1. A vertical `RecyclerView` to display files from the server (`RemoteFile`). 
   Used in `FragmentDownloadCenterAll`. 

   It shows  the Name of the file, its size,  date and a download button.
   
2. A horizontal `RecyclerView` to display files located on the Server which is used in
      `FragmentDownloadCenterAll` and  `RecyclerAdapterRemoteDirectories` 
      It displays the name of the file, its size,  date and a download button.

3. A horizontal `RecyclerView`  to display the status of map request made by the user.
   It shows : the name of the request, the time passed since the request was submitted, the status, and a short description of that the status means.
   Used in `FragmentRequestStatus`

4.  A recycler view to display directories (on the server).
   It it displays a formatted name of the directory, and its contents (maps as `RemoteFile`)  using the recycler view described in 2

5. Another recycler view to display the same as the first one, but this time it needs to display files on the local storage (`File`)
   Equally to 1. it displays the name, date and size of a 

   Additionally it can show a little green dot to show which map the user has chosen.
   This is invisible by default and can be made visible if needed,

   Also it is possible do delete and/or chose Maps by swiping the recycler item.



## Implementation 



### RecyclerAdapterLocalFiles 

The `RecyclerAdapterLocalFiles ` is the adapter for the recycler view described in 5.

It takes a `ArrayList<File>` which will be created by the `LocalMapsRepository` and a
`int` resource, which is the layout id of the used item layout. in this case the `recycler_item_vertical`

It provides a `File` which is used to search in the recycler view by the name of the item 
Also it has a setter for a `OnItemClick` interface which isn't used at the moment.

If you want to use it :

```java
mRecyclerAdapter.setOnItemClickListener(new RecyclerAdapterLocalFiles.OnItemClickListener()
{
    @Override
    public void onItemClick(int position)
    {
			//Your code here
    }
});
```



As described there should be Swipe gestures to delete and choose a map. This isn't implemented in the adapter itself but in the class `RecyclerAdapterSwipeGestures`  ...later more on that.



### RecyclerAdapterRemoteFiles


The `RecyclerAdapterRemoteFiles` is the adapter for the recycler view describe in 1. and 2 

It takes an `ArrayList<RemoteFile>`  which will come from the corresponding Repository.
and a `int` resource. Now as i said there should be a vertical and a horizontal version of this recycler view. That's why in this case 2 layouts can be used
The `recycler_item_vertical` as known from RecyclerAdapterLocalFiles and the `recycler_item_horizontal` both have the same views with the same IDs, but look slightly different.

Again this Adapter provides a `Filter` to search items by their names and a `OnItemClickListener` which -again - isn't used yet but can be implemented like described before.



### RecyclerAdatpterRequestStatus 

This is the adapter for the recycler view described in 3. 

As the requests come as string from the server it takes a `ArrayList<String>` .
The layout resource in this case should be the `recycler_item_request_status`
Again this recycler adapter provides the same functions like the other ones. Namely Filer and  OnItemClickListener.

As mentioned this time we get a String ArrayList  This means the recycler view needs to process the strings for himself to set the right information to the right view. This happens in `onBindViewHolder` using a `StringTokenizer`

**Note : The additional information texts to the status come from the String resources and not from the server **



 ### RecyclerAdapterDirectories

The recycler view described in 4.

In this case its a bit more complicated The recycler view takes a `ArrayList<RemoteDirectory>` which are the directories 
it shall display but also a `HashMap<String, ArrayList<RemoteFile>>` which is a HashMap of the name of the directories and another ArrayList, which contains the files which are in these directories.

With that in mind the recycler view takes the layout `recycler_item_directories` which -again has a recycler view in it.

So  : I use the ArrayList with the RemoteDirectories to set the name in each recycler view item and then fill the recycler view in the single items using the `RecyclerAdapterRemoteFiles` with the `recycler_item_horizontal_layout` and the ArrayList<RemoteFile>  which is in the HashMap.

I hope i made it clear ...but its basically a recycler view inside a recycler view item

**Note : I think this can be implemented  better using just the HashSet but i haven't done it yet because ...it works for now**



### A few more things

You may have seen the setData methods in some of the Adapters, they are there because the ```mAdapter.notifyDataSetChanged()`` doesn't work with LiveData objects as they are used here.

You also may have noticed that the adapters always have a  "backupList" . this is needed to restore the list again after it was filtered.
The filter directly alters the list which is displayed but after finishing the search the list should be complete again. That's where the backup comes in.

also I used horizontal recycler views in some cases. 

That can be achieved by 

```java
LinearLayoutManager recyclerLayoutManager = new LinearLayoutManager(this.getContext());
recyclerLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

mRecyclerView.setLayoutManager(recyclerLayoutManager)
```



## Swipe Gestures 

As described in 5.  it should be possible to delete or choose a map via swiping on the recycler item.
this is achieved using the `RecyclerViewSwipeGestures ` which can be initialized implementing `SwipeCallbackLeft`, `SwipeCallbackRight` or both.

The `RecyclerAdapterSwipeGestures` class does the animation and calls the implemented methods from the interfaces when a left or right swipe was made.

Example: 

```java
//For both swipe directions
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerAdapterSwipeGestures(this.swipeCallbackRight, this.swipeCallbackLeft));

// for just the left swipe
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(newSwipeCallbackLeft()
{
    @Override
    public void onLeftSwipe(int position)
    {
			//Your code here
    }
});

// For just the right swipe
ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeCallbackRight()
{
    @Override
    public void onRightSwipe(int position)
    {
			//Your code here
    }
});

// and then: 

  itemTouchHelper.attachToRecyclerView(mRecyclerView);
```



**Note : I initially intended to make it possible that by just passing one interface the animation for the other swipe direction wouldn't show up that would make it a lot more flexible ..but I didn't got it working. So two options here : either make it work that way, then the SwipeGestures could be used in other places too or unify the interface to just one with two methods**



## OnRecyclerItemDownloadButtonClick

That's simply the interface which can be passes do the recycler view with buttons as they are used in the download fragments to implement the download functionality.