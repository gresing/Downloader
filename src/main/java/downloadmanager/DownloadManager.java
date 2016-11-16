package downloadmanager;

import file.DownloadFile;

public interface DownloadManager {
    LoadStatus loadFile(DownloadFile downloadFile);
}
