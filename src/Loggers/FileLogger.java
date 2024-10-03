package Loggers;

import java.io.PrintWriter;

public class FileLogger extends Logger {

    public static PrintWriter writer = null;

    public static void log(String message) {
        if (writer != null) {
            writer.println(message);
        }
    }

    // Закриваємо потік виведення
    static public void close() {
        if (writer != null) {
            writer.close();
            writer = null;
        }
    }
}
