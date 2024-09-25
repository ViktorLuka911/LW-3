import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class FileLogger {
    private PrintWriter writer;
    private boolean saveInFile;  // Вказує, чи записувати у файл

    public FileLogger(String filename, boolean saveInFile) throws IOException {
        this.saveInFile = saveInFile;

        // Якщо потрібно зберігати у файл, відкриваємо файл для запису
        if (saveInFile) {
            writer = new PrintWriter(new FileWriter(filename, true)); // true для додавання в кінець файлу
        }
    }

    public void log(String messageConsole, String messageFile) {
        // Виводимо в консоль
        System.out.print(messageConsole);

        // Якщо потрібно зберігати у файл, записуємо туди
        if (saveInFile) {
            writer.print(messageFile);
        }
    }

    // Закриваємо потік виведення
    public void close() {
        if (saveInFile) {
            writer.close();
        }
    }
}