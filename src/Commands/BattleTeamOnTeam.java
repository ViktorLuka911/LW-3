package Commands;

import Droids.ArmoredDroid;
import Droids.DefaultDroid;
import Droids.Droid;
import java.util.ArrayList;
import java.util.Scanner;
import Droids.LightDroid;
import Fights.TeamFight;
import Utilities.Utilities;
import Loggers.*;

public class BattleTeamOnTeam extends Command {

    public static void executeCommand() {
        Scanner scanner = new Scanner(System.in);

        if (listDroid.size() < 2) { // Мінімум 2 дроїди для командної битви
            ConsoleLogger.log("\n\tНедостатньо дроїдів для командної битви! Необхідно мінімум 2 дроїди.");
            return;
        }

        ConsoleLogger.log("\n\tОберіть розмір команди А: ");
        int teamASize = Utilities.getValidatedInput(1, listDroid.size() - 1);

        ConsoleLogger.log("\n\tОберіть розмір команди B: ");
        int teamBSize = Utilities.getValidatedInput(1, listDroid.size() - 1);

        ArrayList<Droid> availableDroids = new ArrayList<>(listDroid);

        ArrayList<Droid> team1 = selectTeam(availableDroids, teamASize);
        ArrayList<Droid> team2 = selectTeam(availableDroids, teamBSize);

        ConsoleLogger.log("\n\tКоманда 1:");
        for (Droid droid : team1) {
            ConsoleLogger.log("\n\t" + droid);
        }

        ConsoleLogger.log("\n\n\tКоманда 2:");
        for (Droid droid : team2) {
            ConsoleLogger.log("\n\t" + droid);
        }

        ConsoleLogger.log("\n\n\tПримітка: натискайте Enter для початку бою і для виконання ходу.");
        scanner.nextLine();

        TeamFight fight = new TeamFight(team1, team2);
        fight.startFight();
    }

    // Функція для вибору дроїдів для команди
    public static ArrayList<Droid> selectTeam(ArrayList<Droid> availableDroids, int teamSize) {
        ArrayList<Droid> team = new ArrayList<>();

        while (team.size() < teamSize) {

            Droid selectedDroid = Utilities.selectDroid(availableDroids);

            assert selectedDroid != null;
            Droid copyDroid = switch (selectedDroid.getHealth()) {
                case 50 -> new LightDroid(selectedDroid);
                case 75 -> new DefaultDroid(selectedDroid);
                case 100 -> new ArmoredDroid(selectedDroid);
                default -> null;
            };

            team.add(copyDroid);

            availableDroids.remove(selectedDroid); // Видаляємо вибраного дроїда з доступних
        }

        return team;
    }
}