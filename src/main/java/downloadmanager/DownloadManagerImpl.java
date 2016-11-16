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

    public DownloadManagerImpl(String[] argPairs) {
        if (argPairs.length != 4) {
            throw new IllegalArgumentException("Arguments must contain 4 values. For example \"-n 5 -l 2000k -o output_folder -f links.txt\"");
        }
        parseArgPairs(argPairs);
        System.out.println(this);
    }

    private void parseArgPairs(String[] argPairs) {
        for (String args : argPairs) {
            String[] arg = args.split(" ");
            switch (arg[0]) {
                case "n":
                    this.threadCount = parseInt(arg[1]);
                    break;
                case "l":
                    this.speedLimit = parseInt(arg[1]);
                    break;
                case "o":
                    this.destination = arg[1];
                    break;
                case "f":
                    this.loadFilePath = arg[1];
                    break;
                default:
                    throw new IllegalArgumentException("Incorrect argument " + args);

            }
        }
    }

    public LoadStatus loadFile(DownloadFile downloadFile) {
        return null;
    }

    private int parseInt(String stringValue) {
        stringValue.endsWith("k");
        int value;
        try {
            value = Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected integer argument. Received " + stringValue, e);
        }
        return value;
    }
}
