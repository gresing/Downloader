package downloadmanager;

import file.DownloadFile;
import filemanager.FileManager;
import filemanager.FileManagerImpl;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class DownloadManagerImpl implements DownloadManager {

    private List<DownloadFile> downloadFiles;
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
    }

    private void parseArgPairs(String[] argPairs) {
        for (String args : argPairs) {
            String[] arg = args.split(" ");
            switch (arg[0]) {
                case "n":
                    try {
                        this.threadCount = Integer.valueOf(arg[1]);
                    } catch (NumberFormatException e) {
                        throw new IllegalArgumentException("Thread count parameter must contain integer but founded - " + arg[1], e);
                    }
                    break;
                case "l":
                    this.speedLimit = parseSpeedLimit(arg[1]);
                    break;
                case "o":
                    this.destination = arg[1];
                    break;
                case "f":
                    this.loadFilePath = arg[1];
                    FileManager fileManager = new FileManagerImpl();
                    fileManager.parseFile(arg[1]);
                    this.downloadFiles = fileManager.getFiles();
                    break;
                default:
                    throw new IllegalArgumentException("Incorrect argument " + args);
            }
        }
    }

    private int parseSpeedLimit(String stringValue) {
        Multiplier multiplier = Multiplier.getMultiplierByChr(stringValue.substring(stringValue.length() - 1, stringValue.length()));
        if (multiplier == Multiplier.NO_MULTIPLIER) {
            return Integer.valueOf(stringValue);
        }
        String val = stringValue.substring(0, stringValue.length() - multiplier.chr.length());
        return Integer.valueOf(val) * multiplier.multiplierValue;
    }

    @Override
    public LoadStatus loadFiles(List<DownloadFile> downloadFiles) {
        ExecutorService ex = Executors.newFixedThreadPool(threadCount);
        for (DownloadFile downloadFile : downloadFiles) {
            ex.execute(() -> {
                try {
                    loadFile(downloadFile);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        try {
            ex.shutdown();
            ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }
        System.out.println("finished");
//        for (DownloadFile df : downloadFile) {
//
//        }
        return null;
    }

    private enum Multiplier {
        K("k", 1024),
        M("m", 1024 * 1024),
        NO_MULTIPLIER("", 1);

        private String chr;
        private int multiplierValue;

        Multiplier(String chr, int multiplierValue) {
            this.chr = chr;
            this.multiplierValue = multiplierValue;
        }

        public static Multiplier getMultiplierByChr(String chr) {
            for (Multiplier multiplier : Multiplier.values()) {
                if (multiplier.chr.equals(chr)) {
                    return multiplier;
                }
            }
            return Multiplier.NO_MULTIPLIER;
        }
    }

    private void loadFile(DownloadFile downloadFile) throws IOException, InterruptedException {
        HttpURLConnection connection = (HttpURLConnection) new URL(downloadFile.getURL()).openConnection();
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream input = connection.getInputStream();
            byte[] b = new byte[speedLimit];
            long lastSleepTime = System.currentTimeMillis();
            int read;
            OutputStream outputStream = new FileOutputStream(new File(downloadFile.getSaveName()));
            while (((read = input.read(b)) != -1)) {
                System.out.println("readed " + read + " bytes");
                outputStream.write(b, 0, read);
                long timeElapsed = System.currentTimeMillis() - lastSleepTime;
                Thread.sleep(Math.max(1000 - timeElapsed, 0));
                lastSleepTime = System.currentTimeMillis();
            }
            System.out.println("one readed finished");
            input.close();
        } else {
            System.out.println("Can not connect to " + downloadFile.getURL());
        }
    }
}
