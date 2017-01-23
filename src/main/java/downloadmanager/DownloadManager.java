package downloadmanager;

import file.DownloadFile;

import java.util.List;

public interface DownloadManager {
    LoadStatus loadFiles(List<DownloadFile> downloadFile);
}
