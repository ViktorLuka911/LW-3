package Fights;

import Droids.Droid;
import Loggers.FileLogger;
import Utilities.Utilities;
import Loggers.*;
import java.util.ArrayList;

public class TeamFight extends Fight {

    final private ArrayList<Droid> teamA;
    final private ArrayList<Droid> teamB;

    public TeamFight(ArrayList<Droid> teamA, ArrayList<Droid> teamB) {
        this.teamA = teamA;
        this.teamB = teamB;
    }

    @Override
    public void startFight() {
        ArrayList<Droid> allTeamA = new ArrayList<>(teamA); // Створюємо копію teamA
        ArrayList<Droid> allTeamB = new ArrayList<>(teamB); // Створюємо копію teamB

        // Перевірка чи записувати бій у файл
        Utilities.writeInFile();

        Utilities.clearConsole();
        boolean activeTeam = true;

        while (!teamA.isEmpty() && !teamB.isEmpty()) {

            // Виведення інформації про команди і їх дроїдів
            PrintTeamsInfo(allTeamA, allTeamB);

            Droid attacker, target;

            if (activeTeam) {
                LoggerPrint.log("\n\tКоманда " + Logger.YELLOW + "A" + Logger.RESET + " атакує.", "\n\tКоманда A атакує.");

                if (teamA.size() < 2) attacker = allTeamA.getFirst();
                else attacker = selectDroidForAttack(teamA, "Оберіть дроїда з команди A, який буде атакувати:");

                if (teamB.size() < 2) target = allTeamB.getFirst();
                else target = selectDroidForAttack(teamB, "Оберіть дроїда з команди B, якого будуть атакувати:");
            } else {
                LoggerPrint.log("\n\tКоманда " + Logger.YELLOW + "B" + Logger.RESET + " атакує.", "\n\tКоманда B атакує.");

                if (teamB.size() < 2) attacker = allTeamB.getFirst();
                else attacker = selectDroidForAttack(teamB, "Оберіть дроїда з команди B, який буде атакувати:");

                if (teamA.size() < 2) target = allTeamA.getFirst();
                else target = selectDroidForAttack(teamA, "Оберіть дроїда з команди A, якого будуть атакувати:");
            }

            attacker.handleAbility();

            attack(attacker, target);

            if (!target.isAlive()) {
                if (activeTeam) {
                    teamB.remove(target);  // Видалення загиблого дроїда з команди B
                } else {
                    teamA.remove(target);  // Видалення загиблого дроїда з команди A
                }
            }
            activeTeam = !activeTeam;
        }

        LoggerPrint.log("\n\tПереможна команда: Команда " + Logger.CYAN + ((!teamA.isEmpty()) ? "A" : "B") + Logger.RESET + "\n",
                           "\n\tПереможна команда: Команда " + ((!teamA.isEmpty()) ? "A" : "B") + "\n");

        FileLogger.close();
    }

    private void PrintTeamsInfo(ArrayList<Droid> allTeamA, ArrayList<Droid> allTeamB) {
        // Виведення для команди A
        logTeamInfo(allTeamA, Logger.BLUE);

        LoggerPrint.log("\n", "\n"); // Відділяємо команди порожнім рядком

        // Виведення для команди B
        logTeamInfo(allTeamB, Logger.RED);
    }

    private void logTeamInfo(ArrayList<Droid> team, String aliveColor) {
        for (Droid droid : team) {
            String droidState = droid.inTeamFightInfo();

            // Забарвлюємо залежно від стану
            if (droid.isAlive()) {
                droidState = aliveColor + droidState + Logger.RESET;
            } else {
                droidState = Logger.BLACK + droidState + Logger.RESET;
            }

            // Форматуємо рядок для стовпців
            String fightStateConsole = String.format("\t%-30s\n", droidState);
            String fightStateFile = String.format("\t%-30s\n", droid.inTeamFightInfo());

            // Логування
            LoggerPrint.log(fightStateConsole, fightStateFile);
        }
    }

    // Функція для вибору дроїда для атаки у битві команда на команду
    private static Droid selectDroidForAttack(ArrayList<Droid> team, String message) {
        System.out.println("\n\t" + message);
        for (int i = 0; i < team.size(); i++) {
            System.out.println("\t" + (i + 1) + " - " + team.get(i).getName());
        }
        int selectedIndex = Utilities.getValidatedInput(1, team.size()) - 1;
        return team.get(selectedIndex);
    }
}