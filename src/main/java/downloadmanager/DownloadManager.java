package downloadmanager;

import file.DownloadFile;

import java.util.List;

public interface DownloadManager {
    LoadStatus loadFile(List<DownloadFile> downloadFile);
}
