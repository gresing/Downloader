import downloadmanager.DownloadManager;
import downloadmanager.DownloadManagerImpl;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String argsString = "";
        int count = 0;
        for (String string : args) {
            argsString = argsString + string;
            count++;
            if (count != args.length) {
                argsString = argsString + " ";
            }
        }
        String[] tempArray = argsString.split("-");
        String[] argPairs = Arrays.copyOfRange(tempArray, 1, tempArray.length);
        DownloadManager dm = new DownloadManagerImpl(argPairs);
    }
}
