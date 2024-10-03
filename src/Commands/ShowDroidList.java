package Commands;

import Droids.Droid;
import Loggers.*;

public class ShowDroidList extends Command {

    public static void executeCommand() {
        ConsoleLogger.log("\n\tСписок створених дроїдів:\n\n");
        for (Droid droid : listDroid) {
            ConsoleLogger.log("\t" + droid + "\n");
        }
    }
}
