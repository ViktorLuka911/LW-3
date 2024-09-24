import java.util.Scanner;
import java.util.ArrayList;

public class Main {
    public static final String RESET = "\u001B[0m";  // Скидання кольору
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    static ArrayList<Droid> listDroid = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean isRunning = true;
        int choice = 0;

        do {
            clearConsole();
            System.out.println(YELLOW + "\t****************** Меню команд *********************" + RESET);
            System.out.println("\t1 - Створити дроїда");
            System.out.println("\t2 - Показати список створених дроїдів");
            System.out.println("\t3 - Запустити бій 1 на 1");
            System.out.println("\t4 - Запустити бій команда на команду");
            System.out.println("\t5 - Записати проведений бій у файл");
            System.out.println("\t6 - Відтворити проведений бій зі збереженого файлу");
            System.out.println("\t7 - Вийти");
            System.out.println(YELLOW + "\t****************************************************" + RESET);

            System.out.print("\n\tВиберіть команду: ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1 -> createDroid();
                case 2 -> showDroidList();
                case 3 -> battleOneOnOne();
                case 4 -> battleTeamOnTeam();
                case 5 -> recordBattleFile();
                case 6 -> playBattleFile();
                case 7 -> isRunning = false;
                default -> System.out.println("\tНеправильний вибір. Спробуйте ще раз.");
            }
            System.out.print("\n\tНатисніть Enter, щоб продовжити...");
            scanner.nextLine();
            scanner.nextLine();
        } while (isRunning);

    }

    public static void createDroid() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("\n\tВведіть ім'я дроїда: ");
        String name = scanner.nextLine();

        System.out.println(GREEN + "\n\t*********************************** Меню роботів *************************************" + RESET);
        System.out.println("\t1 - Легкий        (HP: 50   |   Сила удару: 5-10    |   Шанс отримання шкоди: 30%)");
        System.out.println("\t2 - Звичайний     (HP: 75   |   Сила удару: 13-18   |   Шанс отримання шкоди: 50%)");
        System.out.println("\t3 - Броньований   (HP: 100  |   Сила удару: 20-25   |   Шанс отримання шкоди: 70%)");
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

    public static void showDroidList() {
        System.out.println("\n\tСписок створених дроїдів:\n");
        for (Droid droid : listDroid) {
            System.out.println("\t" + droid);
        }
    }

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

        System.out.print("\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");

        scanner.nextLine();

        singleFight(droid1, droid2);
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

    // Оновлена функція battleTeamOnTeam
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

        System.out.print("\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");
        scanner.nextLine();

        teamFight(team1, team2);
    }

    public static void recordBattleFile() {
        System.out.println("\n\tЗапис бою у файл");

    }

    public static void playBattleFile() {
        System.out.println("\n\tВідтворення бою з файла");

    }


    public static void clearConsole() {
        for(int clear = 0; clear < 1000; clear++) {
            System.out.println("\b") ;
        }
    }

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

    public static void singleFight(Droid droidA, Droid droidB) {
        Scanner scanner = new Scanner(System.in);

        clearConsole();
        boolean activeDroid = true;
        while (droidA.health > 0 && droidB.health > 0) {
            System.out.println("\t" + BLUE + droidA.inSingleFightInfo() + RESET + "                  " + RED + droidB.inSingleFightInfo() + RESET);
            if (activeDroid) {
                droidA.action(droidA);
                droidA.attack(droidB);
            } else {
                droidB.action(droidB);
                droidB.attack(droidA);
            }
            activeDroid = !activeDroid;
            scanner.nextLine();
        }

        System.out.println("\tПереможець: " + YELLOW + ((droidA.health > 0) ? droidA.name : droidB.name) + RESET);
    }

    public static void teamFight(ArrayList<Droid> teamA, ArrayList<Droid> teamB) {
        clearConsole();
        boolean activeTeam = true;  // true - команда A атакує, false - команда B атакує

        ArrayList<Droid> allTeamA, allTeamB;
        allTeamA = teamA;
        allTeamB = teamB;

        while (!teamA.isEmpty() && !teamB.isEmpty()) {

            for (int i = 0; i < allTeamA.size(); i++) {
                System.out.println("\t" + BLUE + allTeamA.get(i).inTeamFightInfo() + RESET + "                  " + RED + allTeamB.get(i).inTeamFightInfo() + RESET);
            }
            Droid attacker = null, target = null;

            if (activeTeam) {
                System.out.println("\n\tКоманда " + CYAN + "A" + RESET + " атакує.");
                attacker = selectDroidForAttack(teamA, "Оберіть дроїда з команди " + CYAN + "A" + RESET + ", який буде атакувати:");
                target = selectDroidForAttack(teamB, "Оберіть дроїда з команди " + CYAN + "B" + RESET + ", якого будуть атакувати:");
            } else {
                System.out.println("\n\tКоманда " + CYAN + "B" + RESET + " атакує.");
                attacker = selectDroidForAttack(teamA, "Оберіть дроїда з команди " + CYAN + "B" + RESET + ", який буде атакувати:");
                target = selectDroidForAttack(teamB, "Оберіть дроїда з команди " + CYAN + "A" + RESET + ", якого будуть атакувати:");
            }
            attacker.action(target);
            attacker.attack(target);

            // Зміна активної команди
            activeTeam = !activeTeam;
        }

        // Визначення переможної команди
        System.out.println("\n\tПереможна команда: Команда " + CYAN + ((!teamA.isEmpty()) ? "А" : "B") + RESET);
    }

    private static Droid selectDroidForAttack(ArrayList<Droid> team, String message) {
        System.out.println("\n\t" + message);
        for (int i = 0; i < team.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + team.get(i).getName());
        }
        int selectedIndex = getValidatedInput(1, team.size()) - 1;
        return team.get(selectedIndex);
    }

}