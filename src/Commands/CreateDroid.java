package Commands;

import Droids.*;
import Abilities.*;
import Utilities.Utilities;
import Loggers.*;
import java.util.Scanner;

public class CreateDroid extends Command {

    public static void executeCommand() {
        Scanner scanner = new Scanner(System.in);

        ConsoleLogger.log("\n\tВведіть ім'я дроїда: ");
        String name = scanner.nextLine();

        ConsoleLogger.log(Logger.GREEN + "\n\t*********************************** Меню роботів *************************************" + Logger.RESET + "\n");
        ConsoleLogger.log("\t1 - Легкий        (HP: 50   |   Сила удару: 5-10    |   Шанс отримання шкоди: 50%)" + "\n");
        ConsoleLogger.log("\t2 - Звичайний     (HP: 75   |   Сила удару: 13-18   |   Шанс отримання шкоди: 70%)" + "\n");
        ConsoleLogger.log("\t3 - Броньований   (HP: 100  |   Сила удару: 20-25   |   Шанс отримання шкоди: 90%)" + "\n");
        ConsoleLogger.log(Logger.GREEN + "\t**************************************************************************************" + Logger.RESET + "\n");

        int choiceDroid = Utilities.getValidatedInput(1, 3);

        ConsoleLogger.log(Logger.PURPLE + "\n\t********************************* Меню здібностей ************************************" + Logger.RESET + "\n");
        ConsoleLogger.log("\t1 - Регенерація (відновлює здоров'я на 20 очок)" + "\n");
        ConsoleLogger.log("\t2 - Недосяжність (недосяжний для атак ворожих дроїдів)" + "\n");
        ConsoleLogger.log("\t3 - Збільшена шкода (збільшує наносиму шкоду ворогу на 10 очок)" + "\n");
        ConsoleLogger.log("\t4 - Збільшена броня (зменшує атаку ворога вдвічі)" + "\n");
        ConsoleLogger.log("\t5 - Шипи (при попаданні ворожого дроїда наносить удар у відповідь на 5 очок)" + "\n");
        ConsoleLogger.log(Logger.PURPLE + "\t**************************************************************************************" + Logger.RESET + "\n");

        int choiceAbility = Utilities.getValidatedInput(1, 5);

        Droid droid = getDroid(choiceAbility, choiceDroid, name);

        listDroid.add(droid);
    }

    // Отримання дроїда заказаним вибором
    public static Droid getDroid(int choiceAbility, int choiceDroid, String name) {
        Ability ability = switch (choiceAbility) {
            case 1 -> new Healing();
            case 2 -> new Invincible();
            case 3 -> new EnhancedDamage();
            case 4 -> new EnhancedArmor();
            case 5 -> new Spikes();
            default -> null;
        };

        return switch (choiceDroid) {
            case 1 -> new LightDroid(name, ability);
            case 2 -> new DefaultDroid(name, ability);
            case 3 -> new ArmoredDroid(name, ability);
            default -> null;
        };
    }
}
