import java.util.Scanner;
import java.util.ArrayList;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    public static final String RESET = "\u001B[0m";  // Скидання кольору
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String BLACK = "\u001B[30m";

    static ArrayList<Droid> listDroid = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        int choice;

        do {
            clearConsole();
            System.out.println(YELLOW + "\t****************** Меню команд *********************" + RESET);
            System.out.println("\t1 - Створити дроїда");
            System.out.println("\t2 - Показати список створених дроїдів");
            System.out.println("\t3 - Запустити бій 1 на 1");
            System.out.println("\t4 - Запустити бій команда на команду");
            System.out.println("\t5 - Відтворити проведений бій зі збереженого файлу");
            System.out.println("\t6 - Вийти");
            System.out.println(YELLOW + "\t****************************************************" + RESET);

            System.out.print("\n\tВиберіть команду: ");
            choice = scanner.nextInt();

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
            scanner.nextLine();
        } while (isRunning);

    }

    //-------------------------------------------Створення дроїдів-------------------------------

    public static void createDroid() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n\tВведіть ім'я дроїда: ");
        String name = scanner.nextLine();

        System.out.println(GREEN + "\n\t*********************************** Меню роботів *************************************" + RESET);
        System.out.println("\t1 - Легкий        (HP: 50   |   Сила удару: 5-10    |   Шанс отримання шкоди: 50%)");
        System.out.println("\t2 - Звичайний     (HP: 75   |   Сила удару: 13-18   |   Шанс отримання шкоди: 70%)");
        System.out.println("\t3 - Броньований   (HP: 100  |   Сила удару: 20-25   |   Шанс отримання шкоди: 90%)");
        System.out.println(GREEN + "\t**************************************************************************************" + RESET);

        int choiceDroid = getValidatedInput(1, 3);

        System.out.println(PURPLE + "\n\t********************************* Меню здібностей ************************************" + RESET);
        System.out.println("\t1 - Регенерація (відновлює здоров'я на 20 очок)");
        System.out.println("\t2 - Недосяжність (недосяжний для атак ворожих дроїдів)");
        System.out.println("\t3 - Збільшена шкода (збільшує наносиму шкоду ворогу на 10 очок)");
        System.out.println("\t4 - Збільшена броня (зменшує атаку ворога вдвічі)");
        System.out.println("\t5 - Шипи (при попаданні ворожого дроїда наносить удар у відповідь на 5 очок)");
        System.out.println(PURPLE + "\t**************************************************************************************" + RESET);

        int choiceAbility = getValidatedInput(1, 5);

        Ability ability = switch (choiceAbility) {
            case 1 -> new Healing();
            case 2 -> new Invincible();
            case 3 -> new EnhancedDamage();
            case 4 -> new EnhancedArmor();
            case 5 -> new Spikes();
            default -> null;
        };

        Droid droid = switch (choiceDroid) {
            case 1 -> new LightDroid(name, ability);
            case 2 -> new DefaultDroid(name, ability);
            case 3 -> new ArmoredDroid(name, ability);
            default -> null;
        };

        listDroid.add(droid);
    }

    //-------------------------------------------Список дроїдів-------------------------------

    public static void showDroidList() {
        System.out.println("\n\tСписок створених дроїдів:\n");
        for (Droid droid : listDroid) {
            System.out.println("\t" + droid);
        }
    }

    //-------------------------------------------Підготовка до битви-------------------------------

    public static void battleOneOnOne() {
        Scanner scanner = new Scanner(System.in);
        if (listDroid.size() < 2) {
            System.out.println("\n\tНедостатньо дроїдів для бою!");
            return;
        }

        ArrayList<Droid> availableDroids = new ArrayList<>(listDroid);

        System.out.println("\n\tОберіть першого дроїда:\n");

        for (int i = 0; i < availableDroids.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + availableDroids.get(i));
        }

        int droid1Index = getValidatedInput(1, availableDroids.size()) - 1;
        Droid droid1 = availableDroids.get(droid1Index);

        // Видаляємо першого дроїда з доступних
        availableDroids.remove(droid1Index);

        // Вибір другого дроїда
        System.out.println("\n\tОберіть другого дроїда:\n");
        for (int i = 0; i < availableDroids.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + availableDroids.get(i));
        }

        int droid2Index = getValidatedInput(1, availableDroids.size()) - 1;
        Droid droid2 = availableDroids.get(droid2Index);

        System.out.println("\n\t" + droid1.getName() + " VS " + droid2.getName());

        System.out.println("\n\tЧи записувати бій у файл?");

        System.out.println("\n\t1 - Так");
        System.out.println("\t2 - Ні");

        int choice = getValidatedInput(1, 2);
        boolean saveInFile = 1 == choice;

        System.out.print("\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");

        scanner.nextLine();

        singleFight(droid1, droid2, saveInFile);
    }

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

    //-------------------------------------------Проведення битв-------------------------------

    public static void singleFight(Droid droidA, Droid droidB, boolean saveInFile) {
        Scanner scanner = new Scanner(System.in);
        FileLogger fileLogger;

        // Запитуємо у користувача назву файлу
        System.out.print("\n\tВведіть назву файлу для збереження логів (наприклад, battle_log.txt): ");
        String fileName = scanner.nextLine(); // Зчитуємо назву файлу

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
            String fightStateConsole = "\t" + BLUE + droidA.inSingleFightInfo() + RESET + "                  " + RED + droidB.inSingleFightInfo() + RESET;
            String fightStateFile = "\t" + droidA.inSingleFightInfo() + "                  " + droidB.inSingleFightInfo();

            // Логуємо стан бою (в консоль або в файл + консоль)
            fileLogger.log(fightStateConsole, fightStateFile);

            if (activeDroid) {
                handleDroidAction(droidA, droidB, fileLogger);  // Використовуємо спільну функцію для дроїда A
            } else {
                handleDroidAction(droidB, droidA, fileLogger);  // Використовуємо спільну функцію для дроїда B
            }
            activeDroid = !activeDroid;
            scanner.nextLine(); // Чекаємо на натискання Enter
        }

        String winnerMessageConsole = "\tПереможець: " + YELLOW + ((droidA.health > 0) ? droidA.name : droidB.name) + RESET;
        String winnerMessageFile = "\tПереможець: " + ((droidA.health > 0) ? droidA.name : droidB.name);

        fileLogger.log(winnerMessageConsole, winnerMessageFile);

        // Закриваємо файл, якщо записували у файл
        fileLogger.close();
    }

    public static void teamFight(ArrayList<Droid> teamA, ArrayList<Droid> teamB, boolean saveInFile) {
        Scanner scanner = new Scanner(System.in);
        FileLogger fileLogger;

        ArrayList<Droid> allTeamA = teamA, allTeamB = teamB;

        // Запитуємо у користувача назву файлу
        System.out.print("Введіть назву файлу для збереження логів: ");
        String fileName = scanner.nextLine(); // Зчитуємо назву файлу

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
            for (int i = 0; i < allTeamA.size(); i++) {
                Droid droidTeamA = allTeamA.get(i), droidTeamB = allTeamB.get(i);
                String droidStateTeamA = allTeamA.get(i).inTeamFightInfo();
                String droidStateTeamB = allTeamB.get(i).inTeamFightInfo();

                String fightStateFile = droidStateTeamA + "                  " + droidStateTeamB;

                if (droidTeamA.isAlive()) {
                    droidStateTeamA = "\t" + BLUE + droidStateTeamA + RESET;
                } else {
                    droidStateTeamA = "\t" + BLACK + droidStateTeamA + RESET;
                }

                if (droidTeamB.isAlive()) {
                    droidStateTeamB = "\t" + RED + allTeamB.get(i).inTeamFightInfo() + RESET;
                } else {
                    droidStateTeamB = "\t" + BLACK + allTeamB.get(i).inTeamFightInfo() + RESET;
                }

                String fightStateConsole = droidStateTeamA + "                  " + droidStateTeamB;

                fileLogger.log(fightStateConsole, fightStateFile);
            }

            Droid attacker, target;

            if (activeTeam) {
                System.out.println("\n\tКоманда A атакує.");
                attacker = selectDroidForAttack(teamA, "Оберіть дроїда з команди A, який буде атакувати:");
                target = selectDroidForAttack(teamB, "Оберіть дроїда з команди B, якого будуть атакувати:");

            } else {
                System.out.println("\n\tКоманда B атакує.");
                attacker = selectDroidForAttack(teamB, "Оберіть дроїда з команди B, який буде атакувати:");
                target = selectDroidForAttack(teamA, "Оберіть дроїда з команди A, якого будуть атакувати:");
            }

            if (attacker.isAbilityReady()) {
                attacker.useAbility(target, fileLogger);
            } else {
                System.out.println("\n\tЗдібність " + PURPLE + attacker.ability.name + RESET + " не готова до використання для дроїда " + YELLOW + attacker.name + RESET + ".");
                attacker.abilityUpdateCooldown();
            }

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

        String winnerMessageConsole = "\n\tПереможна команда: Команда " + CYAN + ((!teamA.isEmpty()) ? "A" : "B") + RESET;
        String winnerMessageFile = "\n\tПереможна команда: Команда " + ((!teamA.isEmpty()) ? "A" : "B");

        fileLogger.log(winnerMessageConsole, winnerMessageFile);
        fileLogger.close();
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

    //-------------------------------------------Допоміжіні функції-------------------------------

    // Функція для роботи зі здібностями
    private static void handleDroidAction(Droid attacker, Droid target, FileLogger fileLogger) {
        // Перевіряємо чи активна здібність
        if (attacker.isActiveAbility()) {
            attacker.abilityResetCooldown();
            if (attacker.isAbilityEnd()) {
                attacker.abilityReset(fileLogger, attacker);
            }
        } else if (attacker.isAbilityReady()) {
            attacker.useAbility(attacker, fileLogger);
        } else {
            System.out.println("\n\tЗдібність " + PURPLE + attacker.ability.name + RESET + " не готова до використання для дроїда " + YELLOW + attacker.name + RESET + ".");
            attacker.abilityUpdateCooldown();
        }

        // Атака на ціль
        attacker.attack(target, fileLogger);
    }

    // Функція для вибору дроїда для атаки
    private static Droid selectDroidForAttack(ArrayList<Droid> team, String message) {
        System.out.println("\n\t" + message);
        for (int i = 0; i < team.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + team.get(i).getName());
        }
        int selectedIndex = getValidatedInput(1, team.size()) - 1;
        return team.get(selectedIndex);
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

    //Функція для правильного введення
    public static int getValidatedInput(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int input;
        do {
            System.out.print("\n\tВаш вибір: ");
            while (!scanner.hasNextInt()) {
                System.out.println("\tНевірний ввід! Введіть число.");
                scanner.next(); // Clear invalid input
                System.out.print("\tВаш вибір: ");
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