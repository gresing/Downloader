import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        String argsString = Arrays.toString(args);
        String[] argPairs = argsString.split("-");

        for (String arg : argPairs) {
            String[] pair = arg.trim().split(" ");

            System.out.println(pair[0] + " par name");
            System.out.println(pair[1] + " par value");
        }
    }
}
