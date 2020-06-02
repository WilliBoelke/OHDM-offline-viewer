package de.htwBerlin.ois.Fragments;

import android.support.v4.app.Fragment;

public abstract class FragmentWithServerConnection extends Fragment
{
    /**
     * used in {@link this#changeVisibilities}
     * to define which views are visible/invisible
     * while trying to connect with the server
     */
    final byte STATE_CONNECTING = 1;
    /**
     * used in {@link this#changeVisibilities}
     * to define which views are visible/invisible
     * when the server hast responded
     */
    final byte STATE_NO_CONNECTION = 2;
    /**
     * used in {@link this#changeVisibilities}
     * to define which views are visible/invisible
     * when connected with the server / file listing was successful
     */
    final byte STATE_CONNECTED = 3;

    public void changeVisibilities(int State)
    {
        switch (State)
        {
            case STATE_CONNECTED:
                this.onConnected();
                break;
            case STATE_CONNECTING:
                this.onConnecting();
                break;
            case STATE_NO_CONNECTION:
                this.onNoConnection();
                break;
        }
    }

    protected abstract void onNoConnection();

    protected abstract void onConnecting();

    protected abstract void onConnected();
}
