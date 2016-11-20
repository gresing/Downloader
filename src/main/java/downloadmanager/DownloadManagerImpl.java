package downloadmanager;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import file.DownloadFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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

    @Override
    public LoadStatus loadFile(List<DownloadFile> downloadFile) {
        ExecutorService ex = Executors.newFixedThreadPool(threadCount);
        List<String> strings = Arrays.asList(new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"});
        for (String string : strings)
            ex.execute(() -> {
                String threadName = Thread.currentThread().getName();
                System.out.println("Hello " + threadName + ":" + string);
                try {
                    loadFile();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

            });
        ex.shutdown();
        try {
            ex.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {

        }
        System.out.println("finished");
//        for (DownloadFile df : downloadFile) {
//
//        }
        return null;
    }

    private int parseInt(String stringValue) {
        int mn = 1;
        if (stringValue.endsWith("k")) {
            stringValue = stringValue.substring(1, stringValue.length() - 1);
            mn = 1024;
        }
        int value;
        try {
            value = Integer.parseInt(stringValue);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Expected integer argument. Received " + stringValue, e);
        }
        return value * mn;
    }

    private void loadFile() throws IOException, InterruptedException {
        InputStream input = new URL("www.yandex.ru").openStream();
        byte[] b = new byte[speedLimit];
        long lastSleepTime = System.currentTimeMillis();
        while (true) {
            long timeElapsed = System.currentTimeMillis() - lastSleepTime;
            if (input.read(b) == 0)
                break;
            Thread.sleep(Math.max(1000 - timeElapsed, 0));
            lastSleepTime = System.currentTimeMillis();
            System.out.println("readed " + lastSleepTime);

        }
        input.close();
        System.out.println(b);

    }
}
