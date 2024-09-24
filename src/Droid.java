import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Droid {
    public static final String RESET = "\u001B[0m";  // Скидання кольору
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    protected String name;
    protected String type;
    protected int health;
    protected final int maxHealth;
    protected int hitChance;
    protected int minDamage;
    protected int maxDamage;
    protected Ability ability;
    protected boolean alive;
    // Статичний список здібностей, спільний для всіх дроїдів
    protected static final List<Ability> abilities = new ArrayList<>();

    // Статичний блок для ініціалізації здібностей
    static {
        abilities.add(new Healing());
        abilities.add(new Invincible());
        abilities.add(new EnhancedDamage());
        abilities.add(new EnhancedArmor());
        abilities.add(new Spikes());
    }

    // Конструктор батьківського класу
    public Droid(String name, String type, int health, int maxHealth, int hitChance, int minDamage, int maxDamage, Ability ability) {
        this.name = name;
        this.type = type;
        this.health = health;
        this.maxHealth = maxHealth;
        this.hitChance = hitChance;
        this.minDamage = minDamage;
        this.maxDamage = maxDamage;
        this.ability = ability;
        this.alive = true;
    }

    public void action(Droid target) {
        Scanner scanner = new Scanner(System.in);

        if (ability.active) {
            ability.resetCooldown();
            if (ability.activeCount == 0) {
                ability.reset();
            }
        } else if (ability.reloadCount == ability.reloadTiming) {
            System.out.println("\n\tВи хочете використати здібність " + PURPLE + ability.name + RESET + " для дроїда " + YELLOW + name + RESET + " ?");
            System.out.println("\n\t1 - Так");
            System.out.println("\t2 - Ні");

            int choice;
            do {
                System.out.print("\n\tВаш вибір: ");
                choice = scanner.nextInt();
            } while (choice < 1 || choice > 2);

            if (choice == 1) {
                ability.set(); // Активуємо здібність
                ability.useAbility(this);
            }
        } else {
            System.out.println("\n\tЗдібність " + PURPLE + ability.name + RESET + " не готова до використання для дроїда " + YELLOW + name + RESET + ".");
            ability.updateCooldown(); // Оновлюємо час охолодження
        }
    }

    public void attack(Droid target) {
        Random random = new Random();
        int hit = random.nextInt(100);

        System.out.print("\n\tДроїд " + YELLOW + name + RESET + " атакує " + YELLOW + target.name + RESET + "    -->    ");

        if (hit < target.hitChance) {
            // Обчислюємо пошкодження з урахуванням мінімального та максимального значення
            int damage = random.nextInt((maxDamage - minDamage) + 1) + minDamage;

            // Якщо активована здатність "Збільшена броня", зменшуємо шкоду
            if (target.hasActiveAbility(EnhancedArmor.class)) {
                damage -= damage / 2;
            }

            target.health -= damage;

            if (target.health <= 0) {
                target.alive = false;
            }

            System.out.println(YELLOW + target.name + RESET + " отримав пошкодження: -" + damage + "\n\n");

            // Перевіряємо, чи активована здатність "Шипи" у цілі
            if (target.hasActiveAbility(Spikes.class)) {
                Spikes spikesAbility = (Spikes) target.getActiveAbility(Spikes.class);
                spikesAbility.onHit(this); // Завдаємо шкоди атакуючому
            }

        } else {
            System.out.println("Промах\n\n");
        }
    }


    @Override
    public String toString() {
        return String.format("Ім'я: %-10s | Тип: %-15s | Здоров'я: %-5d | Здібність: %s", name, type, health, ability.name);
    }

    public String inTeamFightInfo() {
        return String.format("%-10s | HP: %-5d | %-15s | %s", name, health, type, ability.name);
    }

    public String inSingleFightInfo() {
        return name + " | HP: " + health + " | " + type + " | " + ability.name + ((ability.active) ? " * " : "   ");
    }

    public boolean isAlive() {
        return alive;
    }

    public String getName() {
        return name;
    }

    public boolean hasActiveAbility(Class<? extends Ability> abilityClass) {
        for (Ability ability : abilities) {
            if (ability.getClass().equals(abilityClass) && ability.active) {
                return true;
            }
        }
        return false;
    }

    public Ability getActiveAbility(Class<? extends Ability> abilityClass) {
        for (Ability ability : abilities) {
            if (ability.getClass().equals(abilityClass) && ability.active) {
                return ability;
            }
        }
        return null;
    }

}

class LightDroid extends Droid {
    // Конструктор для LightDroid
    public LightDroid(String name, Ability ability) {
        super(name, "Легкий", 50, 50, 30, 5, 10, ability);
    }
}

class DefaultDroid extends Droid {
    // Конструктор для StandardDroid
    public DefaultDroid(String name, Ability ability) {
        super(name, "Звичайний", 75, 75, 50, 13, 18, ability);
    }
}

class ArmoredDroid extends Droid {
    // Конструктор для ArmoredDroid
    public ArmoredDroid(String name, Ability ability) {
        super(name, "Броньований", 100, 100, 70, 20, 25, ability);
    }
}