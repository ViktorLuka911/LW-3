package Commands;

import Droids.Droid;
import java.util.ArrayList;
import java.util.Random;

public abstract class Command {
    protected static ArrayList<Droid> listDroid = new ArrayList<>();

    public static void initializeDroids() {
        String[] names = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};
        Random random = new Random();

        for (String name : names) {
            int choiceDroid = random.nextInt(3) + 1; // Випадковий тип дроїда (1-легкий, 2-звичайний, 3-броньований)
            int choiceAbility = random.nextInt(5) + 1; // Випадкова здібність (1-5)

            Droid droid = CreateDroid.getDroid(choiceAbility, choiceDroid, name);
            listDroid.add(droid);
        }
    }
}