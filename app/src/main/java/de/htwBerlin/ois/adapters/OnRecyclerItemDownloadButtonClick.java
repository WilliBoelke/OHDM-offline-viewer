package de.htwBerlin.ois.adapters;

import de.htwBerlin.ois.models.fileStructure.RemoteFile;

public interface OnRecyclerItemDownloadButtonClick
{
    void onButtonClick(RemoteFile fileToDownload);
}
