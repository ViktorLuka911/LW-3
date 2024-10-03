package Menu;

import java.util.Scanner;
import Commands.*;
import Loggers.*;
import Utilities.Utilities;

public class Menu {

    public static void startMenu() {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning;

        do {
            printMenu();
            isRunning = getChoice();
            ConsoleLogger.log("\n\tНатисніть Enter, щоб продовжити...");
            scanner.nextLine();
        } while (isRunning);
    }

    public static void printMenu() {
        Utilities.clearConsole();
        ConsoleLogger.log(Logger.YELLOW + "\t****************** Меню команд *********************" + Logger.RESET + "\n");
        ConsoleLogger.log("\t1 - Створити дроїда" + "\n");
        ConsoleLogger.log("\t2 - Показати список створених дроїдів" + "\n");
        ConsoleLogger.log("\t3 - Запустити бій 1 на 1" + "\n");
        ConsoleLogger.log("\t4 - Запустити бій команда на команду" + "\n");
        ConsoleLogger.log("\t5 - Відтворити проведений бій зі збереженого файлу" + "\n");
        ConsoleLogger.log("\t6 - Вийти" + "\n");
        ConsoleLogger.log(Logger.YELLOW + "\t****************************************************" + Logger.RESET + "\n");
    }

    public static boolean getChoice() {
        int choice;
        boolean isRunning = true;
        choice = Utilities.getValidatedInput(1, 6);

        switch (choice) {
            case 1 -> CreateDroid.executeCommand();
            case 2 -> ShowDroidList.executeCommand();
            case 3 -> BattleOneOnOne.executeCommand();
            case 4 -> BattleTeamOnTeam.executeCommand();
            case 5 -> PlayBattleFile.executeCommand();
            case 6 -> isRunning = false;
            default -> ConsoleLogger.log("\tНеправильний вибір. Спробуйте ще раз." + "\n");
        }

        return isRunning;
    }
}