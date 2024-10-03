package Loggers;

public class ConsoleLogger extends Logger {

    public static void log(String message) {
        System.out.print(message);
    }
}
