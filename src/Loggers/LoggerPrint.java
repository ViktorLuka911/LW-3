package Loggers;

public class LoggerPrint {

    static public void log(String consoleMessage, String fileMessage) {
        ConsoleLogger.log(consoleMessage);
        FileLogger.log(fileMessage);
    }
}