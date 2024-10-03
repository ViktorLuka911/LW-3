package Commands;

import java.util.ArrayList;
import java.util.Scanner;
import Utilities.Utilities;
import Droids.Droid;
import Loggers.*;
import Fights.SingleFight;

public class BattleOneOnOne extends Command {

    public static void executeCommand() {
        Scanner scanner = new Scanner(System.in);
        if (listDroid.size() < 2) {
            ConsoleLogger.log("\n\tНедостатньо дроїдів для бою!");
            return;
        }

        ArrayList<Droid> availableDroids = new ArrayList<>(listDroid);

        Droid droid1 = Utilities.selectDroid(availableDroids);

        Droid droid2 = Utilities.selectDroid(availableDroids);

        assert droid1 != null;
        assert droid2 != null;
        ConsoleLogger.log("\n\t" + droid1.getName() + " VS " + droid2.getName());

        ConsoleLogger.log("\n\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");
        scanner.nextLine();

        SingleFight fight = new SingleFight(droid1, droid2);
        fight.startFight();
    }
}