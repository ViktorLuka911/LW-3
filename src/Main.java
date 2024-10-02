import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    static ArrayList<Droid> listDroid = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        int choice;

        do {
            clearConsole();
            System.out.println(ConsoleColors.YELLOW + "\t****************** Меню команд *********************" + ConsoleColors.RESET);
            System.out.println("\t1 - Створити дроїда");
            System.out.println("\t2 - Показати список створених дроїдів");
            System.out.println("\t3 - Запустити бій 1 на 1");
            System.out.println("\t4 - Запустити бій команда на команду");
            System.out.println("\t5 - Відтворити проведений бій зі збереженого файлу");
            System.out.println("\t6 - Вийти");
            System.out.println(ConsoleColors.YELLOW + "\t****************************************************" + ConsoleColors.RESET);

            choice = getValidatedInput(1, 6);

            switch (choice) {
                case 1 -> createDroid();
                case 2 -> showDroidList();
                case 3 -> battleOneOnOne();
                case 4 -> battleTeamOnTeam();
                case 5 -> playBattleFile();
                case 6 -> isRunning = false;
                default -> System.out.println("\tНеправильний вибір. Спробуйте ще раз.");
            }
            System.out.print("\n\tНатисніть Enter, щоб продовжити...");
            scanner.nextLine();
        } while (isRunning);

    }

    //-------------------------------------------Створення дроїдів-------------------------------

    // Створення дроїда
    public static void createDroid() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n\tВведіть ім'я дроїда: ");
        String name = scanner.nextLine();

        System.out.println(ConsoleColors.GREEN + "\n\t*********************************** Меню роботів *************************************" + ConsoleColors.RESET);
        System.out.println("\t1 - Легкий        (HP: 50   |   Сила удару: 5-10    |   Шанс отримання шкоди: 50%)");
        System.out.println("\t2 - Звичайний     (HP: 75   |   Сила удару: 13-18   |   Шанс отримання шкоди: 70%)");
        System.out.println("\t3 - Броньований   (HP: 100  |   Сила удару: 20-25   |   Шанс отримання шкоди: 90%)");
        System.out.println(ConsoleColors.GREEN + "\t**************************************************************************************" + ConsoleColors.RESET);

        int choiceDroid = getValidatedInput(1, 3);

        System.out.println(ConsoleColors.PURPLE + "\n\t********************************* Меню здібностей ************************************" + ConsoleColors.RESET);
        System.out.println("\t1 - Регенерація (відновлює здоров'я на 20 очок)");
        System.out.println("\t2 - Недосяжність (недосяжний для атак ворожих дроїдів)");
        System.out.println("\t3 - Збільшена шкода (збільшує наносиму шкоду ворогу на 10 очок)");
        System.out.println("\t4 - Збільшена броня (зменшує атаку ворога вдвічі)");
        System.out.println("\t5 - Шипи (при попаданні ворожого дроїда наносить удар у відповідь на 5 очок)");
        System.out.println(ConsoleColors.PURPLE + "\t**************************************************************************************" + ConsoleColors.RESET);

        int choiceAbility = getValidatedInput(1, 5);

        Droid droid = getDroid(choiceAbility, choiceDroid, name);

        listDroid.add(droid);
    }

    // Отримання дроїда звказаним вибором
    private static Droid getDroid(int choiceAbility, int choiceDroid, String name) {
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

    //-------------------------------------------Список дроїдів-------------------------------

    public static void showDroidList() {
        System.out.println("\n\tСписок створених дроїдів:\n");
        for (Droid droid : listDroid) {
            System.out.println("\t" + droid);
        }
    }

    //-------------------------------------------Підготовка до битви один на один-------------------------------

    public static void battleOneOnOne() {
        Scanner scanner = new Scanner(System.in);
        if (listDroid.size() < 2) {
            System.out.println("\n\tНедостатньо дроїдів для бою!");
            return;
        }

        ArrayList<Droid> availableDroids = new ArrayList<>(listDroid);

        Droid droid1 = selectDroid(availableDroids, "першого");

        availableDroids.remove(droid1);

        Droid droid2 = selectDroid(availableDroids, "другого");

        System.out.println("\n\t" + droid1.getName() + " VS " + droid2.getName());

        System.out.println("\n\tЧи записувати бій у файл?");
        System.out.println("\n\t1 - Так");
        System.out.println("\t2 - Ні");

        int choice = getValidatedInput(1, 2);
        boolean saveInFile = (1 == choice);

        System.out.print("\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");
        scanner.nextLine();

        singleFight(droid1, droid2, saveInFile);
    }

    // Вибір дроїда для битви один на один
    private static Droid selectDroid(ArrayList<Droid> availableDroids, String ordinal) {
        System.out.println("\n\tОберіть " + ordinal + " дроїда:\n");

        for (int i = 0; i < availableDroids.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + availableDroids.get(i));
        }

        int droidIndex = getValidatedInput(1, availableDroids.size()) - 1;
        return availableDroids.get(droidIndex);
    }

    //-------------------------------------------Підготовка до битви команда на команду-------------------------------

    public static void battleTeamOnTeam() {
        Scanner scanner = new Scanner(System.in);

        if (listDroid.size() < 4) { // Мінімум 4 дроїди для командної битви
            System.out.println("\n\tНедостатньо дроїдів для командної битви! Необхідно мінімум 4 дроїди.");
            return;
        }

        System.out.print("\n\tОберіть розмір команд (мінімум: 2, максимум: " + listDroid.size() / 2 +  "): ");
        int teamSize = getValidatedInput(2, listDroid.size() / 2);

        ArrayList<Droid> availableDroids = new ArrayList<>(listDroid);

        ArrayList<Droid> team1 = selectTeam(availableDroids, teamSize, "першої команди");
        ArrayList<Droid> team2 = selectTeam(availableDroids, teamSize, "другої команди");

        System.out.println("\n\tКоманда 1:");
        for (Droid droid : team1) {
            System.out.println("\t- " + droid);
        }

        System.out.println("\n\tКоманда 2:");
        for (Droid droid : team2) {
            System.out.println("\t- " + droid);
        }

        System.out.println("\n\tЧи записувати бій у файл?");

        System.out.println("\n\t1 - Так");
        System.out.println("\t2 - Ні");

        int choice = getValidatedInput(1, 2);
        boolean saveInFile = choice == 1;

        System.out.print("\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");
        scanner.nextLine();

        teamFight(team1, team2, saveInFile);
    }

    // Функція для вибору дроїдів для команди
    public static ArrayList<Droid> selectTeam(ArrayList<Droid> availableDroids, int teamSize, String teamName) {
        ArrayList<Droid> team = new ArrayList<>();
        System.out.println("\n\tОберіть дроїдів для " + teamName + ":");

        while (team.size() < teamSize) {
            System.out.println("\n\tДоступні дроїди:");
            for (int i = 0; i < availableDroids.size(); i++) {
                System.out.println("\t" + (i + 1) + " - " + availableDroids.get(i));
            }

            System.out.print("\n\tОберіть дроїда для " + teamName + ": ");
            int droidIndex = getValidatedInput(1, availableDroids.size()) - 1;

            Droid selectedDroid = availableDroids.get(droidIndex);
            team.add(selectedDroid);
            availableDroids.remove(droidIndex); // Видаляємо вибраного дроїда з доступних
            System.out.println("\tДодано до " + teamName + ": " + selectedDroid.getName());
        }

        return team;
    }

    //-------------------------------------------Проведення битв один на один-------------------------------

    public static void singleFight(Droid droidA, Droid droidB, boolean saveInFile) {
        Scanner scanner = new Scanner(System.in);
        FileLogger fileLogger;

        String fileName = "";

        // Запитуємо у користувача назву файлу
        if (saveInFile) {
            System.out.print("\n\tВведіть назву файлу для збереження логів: ");
            fileName = scanner.nextLine(); // Зчитуємо назву файлу

            fileName = "D:\\2_kurs-1_sem\\PP\\LW-3\\Program\\Battles\\" + fileName;

        }

        // Створюємо FileLogger для ведення логів
        try {
            fileLogger = new FileLogger(fileName, saveInFile); // Використовуємо введену назву файлу
        } catch (IOException e) {
            System.out.println("\n\tПомилка під час створення файлу для запису бою.");
            return;
        }

        clearConsole();
        boolean activeDroid = true;

        while (droidA.health > 0 && droidB.health > 0) {
            String fightStateConsole = "\t" + ConsoleColors.BLUE + droidA.inSingleFightInfo() + ConsoleColors.RESET + "                  " + ConsoleColors.RED + droidB.inSingleFightInfo() + ConsoleColors.RESET;
            String fightStateFile = "\t" + droidA.inSingleFightInfo() + "                  " + droidB.inSingleFightInfo();

            // Логуємо стан бою (в консоль або в файл + консоль)
            fileLogger.log(fightStateConsole, fightStateFile);

            if (activeDroid) {
                handleDroidAbility(droidA, droidB, fileLogger);
            } else {
                handleDroidAbility(droidB, droidA, fileLogger);
            }
            activeDroid = !activeDroid;
            scanner.nextLine(); // Чекаємо на натискання Enter
        }

        String winnerMessageConsole = "\tПереможець: " + ConsoleColors.YELLOW + ((droidA.health > 0) ? droidA.name : droidB.name) + ConsoleColors.RESET;
        String winnerMessageFile = "\tПереможець: " + ((droidA.health > 0) ? droidA.name : droidB.name);

        fileLogger.log(winnerMessageConsole, winnerMessageFile);

        // Закриваємо файл, якщо записували у файл
        fileLogger.close();
    }

    //-------------------------------------------Проведення битв команда на команду-------------------------------

    public static void teamFight(ArrayList<Droid> teamA, ArrayList<Droid> teamB, boolean saveInFile) {
        Scanner scanner = new Scanner(System.in);
        FileLogger fileLogger;

        ArrayList<Droid> allTeamA = new ArrayList<>(teamA); // Створюємо копію teamA
        ArrayList<Droid> allTeamB = new ArrayList<>(teamB); // Створюємо копію teamB

        // Запитуємо у користувача назву файлу
        System.out.print("\n\tВведіть назву файлу для збереження логів: ");
        String fileName = scanner.nextLine(); // Зчитуємо назву файлу

        fileName = "D:\\2_kurs-1_sem\\PP\\LW-3\\Program\\Battles\\" + fileName;

        // Створюємо FileLogger для ведення логів
        try {
            fileLogger = new FileLogger(fileName, saveInFile); // Використовуємо введену назву файлу
        } catch (IOException e) {
            System.out.println("Помилка під час створення файлу для запису бою.");
            return;
        }

        clearConsole();
        boolean activeTeam = true;

        while (!teamA.isEmpty() && !teamB.isEmpty()) {
            // Виведення інформації про команди і їх дроїдів
            for (int i = 0; i < allTeamA.size(); i++) {
                Droid droidTeamA = allTeamA.get(i), droidTeamB = allTeamB.get(i);
                String droidStateTeamA = allTeamA.get(i).inTeamFightInfo();
                String droidStateTeamB = allTeamB.get(i).inTeamFightInfo();

                String fightStateFile = droidStateTeamA + "                  " + droidStateTeamB + "\n";

                if (droidTeamA.isAlive()) {
                    droidStateTeamA = "\t" + ConsoleColors.BLUE + droidStateTeamA + ConsoleColors.RESET;
                } else {
                    droidStateTeamA = "\t" + ConsoleColors.BLACK + droidStateTeamA + ConsoleColors.RESET;
                }

                if (droidTeamB.isAlive()) {
                    droidStateTeamB = "\t" + ConsoleColors.RED + allTeamB.get(i).inTeamFightInfo() + ConsoleColors.RESET;
                } else {
                    droidStateTeamB = "\t" + ConsoleColors.BLACK + allTeamB.get(i).inTeamFightInfo() + ConsoleColors.RESET;
                }

                String fightStateConsole = droidStateTeamA + "                  " + droidStateTeamB + "\n";

                fileLogger.log(fightStateConsole, fightStateFile);
            }

            Droid attacker, target;

            if (activeTeam) {
                fileLogger.log("\n\tКоманда " + ConsoleColors.YELLOW + "A" + ConsoleColors.RESET + " атакує.", "\n\tКоманда A атакує.");
                attacker = selectDroidForAttack(teamA, "Оберіть дроїда з команди A, який буде атакувати:");
                target = selectDroidForAttack(teamB, "Оберіть дроїда з команди B, якого будуть атакувати:");

            } else {
                fileLogger.log("\n\tКоманда " + ConsoleColors.YELLOW + "B" + ConsoleColors.RESET + " атакує.", "\n\tКоманда B атакує.");
                attacker = selectDroidForAttack(teamB, "Оберіть дроїда з команди B, який буде атакувати:");
                target = selectDroidForAttack(teamA, "Оберіть дроїда з команди A, якого будуть атакувати:");
            }

            handleDroidAbility(attacker, target, fileLogger);

            attacker.attack(target, fileLogger);

            if (!target.isAlive()) {
                if (activeTeam) {
                    teamB.remove(target);  // Видалення загиблого дроїда з команди B
                } else {
                    teamA.remove(target);  // Видалення загиблого дроїда з команди A
                }
            }
            activeTeam = !activeTeam;
        }

        String winnerMessageConsole = "\n\tПереможна команда: Команда " + ConsoleColors.CYAN + ((!teamA.isEmpty()) ? "A" : "B") + ConsoleColors.RESET;
        String winnerMessageFile = "\n\tПереможна команда: Команда " + ((!teamA.isEmpty()) ? "A" : "B");

        fileLogger.log(winnerMessageConsole, winnerMessageFile);
        fileLogger.close();
    }

    // Функція для вибору дроїда для атаки у битві команда на команду
    private static Droid selectDroidForAttack(ArrayList<Droid> team, String message) {
        System.out.println("\n\t" + message);
        for (int i = 0; i < team.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + team.get(i).getName());
        }
        int selectedIndex = getValidatedInput(1, team.size()) - 1;
        return team.get(selectedIndex);
    }

    //-------------------------------------------Відтворення бою з файлу-------------------------------

    public static void playBattleFile() {
        Scanner scanner = new Scanner(System.in);

        // Стандартний шлях, до якого буде додаватися введене ім'я файлу
        String defaultPath = "D:\\2_kurs-1_sem\\PP\\LW-3\\Program\\Battles\\";  // Можете змінити цей шлях на потрібний вам

        System.out.print("\n\tВведіть ім'я файлу: ");
        String fileName = scanner.nextLine();  // Зчитуємо лише ім'я файлу

        // Додаємо стандартний шлях до файлу
        String fullPath = defaultPath + fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);  // Виводимо кожен рядок на екран
            }
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        }
    }

    //-------------------------------------------Допоміжні функції-------------------------------

    // Функція для роботи зі здібностями
    private static void handleDroidAbility(Droid attacker, Droid target, FileLogger fileLogger) {
        // Перевіряємо чи активна здібність
        if (attacker.isActiveAbility()) {
            attacker.abilityResetCooldown();
            if (attacker.isAbilityEnd()) {
                attacker.abilityReset(fileLogger, attacker);
            }
        } else if (attacker.isAbilityReady()) {
            System.out.println("\n\tВи хочете використати здібність " + ConsoleColors.PURPLE + attacker.getAbility().getName() + ConsoleColors.RESET + " для дроїда " + ConsoleColors.YELLOW + attacker.getName() + ConsoleColors.RESET + " ?");

            System.out.println("\n\t1 - Так");
            System.out.println("\t2 - Ні");

            int choice = getValidatedInput(1, 2);

            if (choice == 1) {
                attacker.activateAbility(fileLogger);
            }
        } else {
            System.out.println("\n\tЗдібність " + ConsoleColors.PURPLE + attacker.ability.name + ConsoleColors.RESET + " не готова до використання для дроїда " + ConsoleColors.YELLOW + attacker.name + ConsoleColors.RESET + ".");
            attacker.abilityUpdateCooldown();
        }

        // Атака на ціль
        attacker.attack(target, fileLogger);
    }

    //Функція для правильного введення
    public static int getValidatedInput(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int input;
        do {
            System.out.print("\n\tВаш вибір: ");
            while (!scanner.hasNextInt()) {
                System.out.println("\tНевірний ввід! Введіть число.");
                scanner.next(); // Clear invalid input
                System.out.print("\n\tВаш вибір: ");
            }
            input = scanner.nextInt();
        } while (input < min || input > max);
        return input;
    }

    // Функція для очищення консолі
    public static void clearConsole() {
        for(int clear = 0; clear < 100; clear++) {
            System.out.println("\b") ;
        }
    }
}