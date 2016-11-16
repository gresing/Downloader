package downloadmanager;

import file.DownloadFile;


public class DownloadManagerImpl implements DownloadManager {

    private int threadCount;
    private int speedLimit;
    private String loadFilePath;
    private String destination;

    public DownloadManagerImpl(int threadCount, int speedLimit, String loadFilePath, String destination) {
        this.threadCount = threadCount;
        this.speedLimit = speedLimit;
        this.loadFilePath = loadFilePath;
        this.destination = destination;
    }

    public LoadStatus loadFile(DownloadFile downloadFile) {
        return null;
    }
}
