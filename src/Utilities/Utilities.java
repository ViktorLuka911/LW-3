package Utilities;

import Droids.ArmoredDroid;
import Droids.DefaultDroid;
import Droids.Droid;
import Droids.LightDroid;
import Loggers.ConsoleLogger;
import Loggers.FileLogger;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Utilities {

    //Функція для правильного введення
    public static int getValidatedInput(int min, int max) {
        Scanner scanner = new Scanner(System.in);
        int input;
        do {
            ConsoleLogger.log("\n\tВаш вибір: ");
            while (!scanner.hasNextInt()) {
                ConsoleLogger.log("\n\tНевірний ввід! Введіть число.");
                scanner.next(); // Clear invalid input
                ConsoleLogger.log("\n\n\tВаш вибір: ");
            }
            input = scanner.nextInt();
        } while (input < min || input > max);
        return input;
    }

    // Функція для очищення консолі
    public static void clearConsole() {
        for(int clear = 0; clear < 10; clear++) {
            ConsoleLogger.log("\n") ;
        }
    }

    // Функція для перевірки чи записувати бій у файл
    public static void writeInFile() {
        Scanner scanner = new Scanner(System.in);

        ConsoleLogger.log("\n\tЧи записувати бій у файл?");
        ConsoleLogger.log("\n\t1 - Так");
        ConsoleLogger.log("\n\t2 - Ні");

        int choice = Utilities.getValidatedInput(1, 2);
        boolean saveInFile = (1 == choice);

        // Запитуємо у користувача назву файлу
        if (saveInFile) {
            String fileName;
            ConsoleLogger.log("\n\tВведіть назву файлу для збереження логів: ");
            fileName = scanner.nextLine(); // Зчитуємо назву файлу

            fileName = "D:\\2_kurs-1_sem\\PP\\LW-3\\Program\\Battles\\" + fileName;
            try {
                FileLogger.writer = new PrintWriter(fileName);
            } catch (IOException e) {
                ConsoleLogger.log("\n\tПомилка під час створення файлу для запису бою.");
            }
        }
    }

    public static Droid selectDroid(ArrayList<Droid> availableDroids) {
        ConsoleLogger.log("\n\tОберіть дроїда:\n");

        for (int i = 0; i < availableDroids.size(); i++) {
            ConsoleLogger.log(String.format("\n\t%3d - %s", (i + 1), availableDroids.get(i)));
        }

        int droidIndex = Utilities.getValidatedInput(1, availableDroids.size()) - 1;

        Droid selectedDroid = availableDroids.get(droidIndex);
        availableDroids.remove(selectedDroid);

        return switch (selectedDroid.getHealth()) {
            case 50 -> new LightDroid(selectedDroid);
            case 75 -> new DefaultDroid(selectedDroid);
            case 100 -> new ArmoredDroid(selectedDroid);
            default -> null;
        };
    }

}
