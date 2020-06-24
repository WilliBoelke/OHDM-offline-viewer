package de.htwBerlin.ois.adapters;

import de.htwBerlin.ois.model.models.fileStructure.RemoteFile;

public interface OnRecyclerItemDownloadButtonClick
{
    void onButtonClick(RemoteFile fileToDownload);
}
