package Commands;

import Loggers.ConsoleLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class PlayBattleFile extends Command {

    public static void executeCommand() {
        Scanner scanner = new Scanner(System.in);

        // Стандартний шлях, до якого буде додаватися введене ім'я файлу
        String defaultPath = "D:\\2_kurs-1_sem\\PP\\LW-3\\Program\\Battles\\";  // Можете змінити цей шлях на потрібний вам

        ConsoleLogger.log("\n\tВведіть ім'я файлу: ");
        String fileName = scanner.nextLine();  // Зчитуємо лише ім'я файлу

        // Додаємо стандартний шлях до файлу
        String fullPath = defaultPath + fileName;

        try (BufferedReader br = new BufferedReader(new FileReader(fullPath))) {
            String line;
            while ((line = br.readLine()) != null) {
                ConsoleLogger.log(line + "\n");  // Виводимо кожен рядок на екран
            }
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        }
    }
}