package filemanager;

import file.DownloadFile;

import java.util.ArrayList;
import java.util.List;

public class FileManagerImpl implements FileManager {

    List<DownloadFile> files = new ArrayList<>();

    public void parseFile(String filePath) {

    }

    public List<DownloadFile> getFiles() {
        return files;
    }
}
