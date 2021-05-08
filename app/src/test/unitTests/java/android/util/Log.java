package android.util;

/**
 * This class is here because its simply the easiest way to unit test with Log.d etc.
 * calls within the class.
 */
public class Log
{
    public static int v(String tag, String msg)
    {
        System.out.println("VERBOSE: " + tag + ": " + msg);
        return 0;
    }

    public static int d(String tag, String msg)
    {
        System.out.println("DEBUG: " + tag + ": " + msg);
        return 0;
    }

    public static int i(String tag, String msg)
    {
        System.out.println("INFO: " + tag + ": " + msg);
        return 0;
    }

    public static int w(String tag, String msg)
    {
        System.out.println("WARN: " + tag + ": " + msg);
        return 0;
    }

    public static int e(String tag, String msg)
    {
        System.err.println("ERROR: " + tag + ": " + msg);
        return 0;
    }
}