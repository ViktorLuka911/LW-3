package Fights;

import Droids.Droid;
import Loggers.*;
import Utilities.Utilities;
import java.util.Scanner;

public class SingleFight extends Fight {

    final private Droid droidA;
    final private Droid droidB;

    public SingleFight(Droid droidA, Droid droidB) {
        this.droidA = droidA;
        this.droidB = droidB;
    }

    @Override
    public void startFight() {
        Scanner scanner = new Scanner(System.in);

        // Перевірка чи записувати бій у файл
        Utilities.writeInFile();

        Utilities.clearConsole();

        boolean activeDroid = true;
        while (droidA.getHealth() > 0 && droidB.getHealth() > 0) {

            LoggerPrint.log("\t" + Logger.BLUE + droidA.inSingleFightInfo() + Logger.RESET + "                  " + Logger.RED + droidB.inSingleFightInfo() + Logger.RESET,
                                 "\t" + droidA.inSingleFightInfo() + "                  " + droidB.inSingleFightInfo());

            if (activeDroid) {
                droidA.handleAbility();
                attack(droidA, droidB);
            } else {
                droidB.handleAbility();
                attack(droidB, droidA);
            }

            activeDroid = !activeDroid;
            scanner.nextLine(); // Чекаємо на натискання Enter
        }

        LoggerPrint.log("\tПереможець: " + Logger.YELLOW + ((droidA.getHealth() > 0) ? droidA.getName() : droidB.getName()) + Logger.RESET +"\n",
                           "\tПереможець: " + ((droidA.getHealth() > 0) ? droidA.getName() : droidB.getName()) + "\n");

        FileLogger.close();
    }
}