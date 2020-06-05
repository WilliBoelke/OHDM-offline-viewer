package de.htwBerlin.ois.FileStructure;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RemoteListsSingletonTest
{

    //------------Test Objects------------

    private ArrayList<RemoteFile> allMaps;
    private ArrayList<RemoteFile> latestMaps;
    private ArrayList<RemoteDirectory> directories;
    private HashMap<String, ArrayList<RemoteFile>> directoryContents;


    //------------Setup------------

    /**
     * The tests needed to run one by one- thats why the Lock is there
     */
    Lock sequential = new ReentrantLock();

    @BeforeEach
    protected void setUp()
    {
        sequential.lock();
        allMaps = new ArrayList<>();
        latestMaps = new ArrayList<>();
        directories = new ArrayList<>();
        directoryContents = new HashMap<>();

        latestMaps.add(new RemoteFile("name1", "path1",  123l, "date"));
        latestMaps.add(new RemoteFile("name2", "path2",  123l, "date"));
        latestMaps.add(new RemoteFile("name3", "path3",  123l, "date"));

        allMaps.add(new RemoteFile("name1", "path1",  123l, "date"));
        allMaps.add(new RemoteFile("name2", "path2",  123l, "date"));
        allMaps.add(new RemoteFile("name3", "path3",  123l, "date"));

        directories.add(new RemoteDirectory("path_one", "date"));
        directories.add(new RemoteDirectory("path_two", "date"));
        directories.add(new RemoteDirectory("path_three", "date"));

        directoryContents.put ("dirname1", allMaps);
        directoryContents.put ("dirname2", latestMaps);
        directoryContents.put ("dirname3", allMaps);
    }

    @AfterEach
    protected void tearDown()
    {
        sequential.unlock();
        RemoteListsSingleton.getInstance().resetInstance();
    }


    //------------Lists------------

    @Test
    public void listsAreEmpty()
    {
        //Lists should be initialized, but empty
        assertEquals(RemoteListsSingleton.getInstance().getAllMaps().size(), 0);
        assertEquals(RemoteListsSingleton.getInstance().getDirectories().size(), 0);
        assertEquals(RemoteListsSingleton.getInstance().getLatestMaps().size(), 0);
        assertEquals(RemoteListsSingleton.getInstance().getDirectories().size(), 0);
    }


    //------------AllMaps List------------

    @Test
    public void setAllMapsTest()
    {
        RemoteListsSingleton.getInstance().setAllMaps(allMaps);

        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());
    }

    @Test
    public void allMapsReplaceTest()
    {
        // This test proofs that:
        // The list will be cleared before new elements are added. (needs to be cleared first, because i use .addAll())

        RemoteListsSingleton.getInstance().setAllMaps(allMaps);
        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());

        allMaps.clear();
        allMaps.add(new RemoteFile("name3", "path1",  123l, "date"));
        allMaps.add(new RemoteFile("name4", "path2",  123l, "date"));
        RemoteListsSingleton.getInstance().setAllMaps(allMaps);
        assertEquals(2, RemoteListsSingleton.getInstance().getAllMaps().size());

    }

    @Test
    public void allMapsNotAReferenceTest()
    {
        // This test proofs that:
        // The the saved list isnt just a reference

        RemoteListsSingleton.getInstance().setAllMaps(allMaps);
        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());

        // not a reference :
        allMaps.clear();
        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());
    }


    //------------LatestMaps List------------

    @Test
    public void setLatestMapsTest()
    {
        RemoteListsSingleton.getInstance().setAllMaps(allMaps);

        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());
    }

    @Test
    public void latestMapsReplaceTest()
    {
        // This test proofs that:
        // The list will be cleared before new elements are added. (needs to be cleared first, because i use .addAll())

        RemoteListsSingleton.getInstance().setLatestMaps(latestMaps);
        assertEquals(3, RemoteListsSingleton.getInstance().getLatestMaps().size());

        latestMaps.clear();
        latestMaps.add(new RemoteFile("name3", "path1",  123l, "date"));
        latestMaps.add(new RemoteFile("name4", "path2",  123l, "date"));
        RemoteListsSingleton.getInstance().setLatestMaps(latestMaps);
        assertEquals(2, RemoteListsSingleton.getInstance().getLatestMaps().size());

    }

    @Test
    public void latestMapsNotAReferenceTest()
    {
        // This test proofs that:
        // The the saved list isnt just a reference

        RemoteListsSingleton.getInstance().setAllMaps(allMaps);
        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());

        // not a reference :
        allMaps.clear();
        assertEquals(3, RemoteListsSingleton.getInstance().getAllMaps().size());
    }


    //------------Directories List------------

    @Test
    public void setDirsTest()
    {
        RemoteListsSingleton.getInstance().setDirectories(directories);
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectories().size());
    }

    @Test
    public void dirsReplaceTest()
    {
        // This test proofs that:
        // The list will be cleared before new elements are added. (needs to be cleared first, because i use .addAll())

        RemoteListsSingleton.getInstance().setDirectories(directories);
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectories() .size());

        directories.clear();
        directories.add(new RemoteDirectory("name3", "date"));
        directories.add(new RemoteDirectory("name4",  "date"));
        RemoteListsSingleton.getInstance().setDirectories(directories);
        assertEquals(2, RemoteListsSingleton.getInstance().getDirectories().size());

    }

    @Test
    public void dirsNotAReferenceTest()
    {
        // This test proofs that:
        // The the saved list isnt just a reference

        RemoteListsSingleton.getInstance().setDirectories(directories);
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectories().size());

        // not a reference :
        directories.clear();
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectories().size());
    }


    //------------Directories List------------

    @Test
    public void setDirContentsTest()
    {
        RemoteListsSingleton.getInstance().setDirectoryContents(directoryContents);
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectoryContents().size());
    }

    @Test
    public void dirContentsReplaceTest()
    {
        // This test proofs that:
        // The list will be cleared before new elements are added. (needs to be cleared first, because i use .addAll())

        RemoteListsSingleton.getInstance().setDirectoryContents(directoryContents);
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectoryContents() .size());

        directoryContents.clear();
        directoryContents.put ("dirname1", allMaps);
        directoryContents.put ("dirname2", latestMaps);
        RemoteListsSingleton.getInstance().setDirectoryContents(directoryContents);
        assertEquals(2, RemoteListsSingleton.getInstance().getDirectoryContents().size());

    }

    @Test
    public void dirContentsNotAReferenceTest()
    {
        // This test proofs that:
        // The the saved list isnt just a reference

        RemoteListsSingleton.getInstance().setDirectoryContents(directoryContents);
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectoryContents().size());

        // not a reference :
        directoryContents.clear();
        assertEquals(3, RemoteListsSingleton.getInstance().getDirectoryContents().size());
    }

}