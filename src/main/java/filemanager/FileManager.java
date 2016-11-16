package filemanager;

import file.DownloadFile;

import java.util.List;

public interface FileManager {
    void parseFile(String filePath);

    List<DownloadFile> getFiles();
}
