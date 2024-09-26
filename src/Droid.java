import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public abstract class Droid {

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

    // Використання здібності
    public void activateAbility(FileLogger fileLogger) {
        ability.set(fileLogger, this); // Активуємо здібність
    }

    // Атака
    public void attack(Droid target, FileLogger fileLogger) {
        Random random = new Random();
        int hit = random.nextInt(100);

        String attackLogConsole = "\n\n\tДроїд " + ConsoleColors.YELLOW + name + ConsoleColors.RESET + " атакує " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + ":";
        String attackLogFile = "\n\n\tДроїд " + name + " атакує " + target.name + ":";

        fileLogger.log(attackLogConsole, attackLogFile);

        if (hit < target.hitChance) {
            int damage = random.nextInt((maxDamage - minDamage) + 1) + minDamage;

            // Якщо активована здатність "Збільшена броня", зменшуємо шкоду вдвічі
            if ("Збільшена броня".equals(target.ability.getName()) && target.ability.isActive()) {
                damage -= damage / 2;
            }

            target.health -= damage;
            String damageLogConsole = "\n\tДроїд " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " отримав пошкодження: -" + damage + "\n\n";
            String damageLogFile = "\n\tДроїд " + target.name + " отримав пошкодження: -" + damage + "\n\n";

            fileLogger.log(damageLogConsole, damageLogFile);

            if (target.health <= 0) {
                String deathLogConsole = "\n\tДроїда " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " знищено.\n";
                String deathLogFile = "\n\tДроїда " + target.name + " знищено.\n";

                fileLogger.log(deathLogConsole, deathLogFile);
                target.alive = false;
                target.health = 0;
            }

            // Якщо активована здатність "Шипи" у цілі
            if ("Шипи".equals(ability.getName()) && target.ability.isActive()) {
                Spikes spikesAbility = (Spikes) target.ability;
                spikesAbility.onHit(this, fileLogger); // Завдаємо шкоди атакуючому
                String spikesLogConsole = "\n\tШипи завдали шкоди атакуючому дроїду " + ConsoleColors.YELLOW + name + ConsoleColors.RESET;
                String spikesLogFile = "\n\tШипи завдали шкоди атакуючому дроїду " + name;

                fileLogger.log(spikesLogConsole, spikesLogFile);
            }

        } else {
            String missLog = "\n\tПромах\n\n";
            fileLogger.log(missLog, missLog);
        }
    }

    //--------------------------Методи для виведення інформації про дроїда---------------------------------
    @Override
    public String toString() {
        return String.format("Ім'я: %-10s | Тип: %-15s | Здоров'я: %-5d | Здібність: %s", name, type, health, ability.name);
    }

    public String inTeamFightInfo() {
        return String.format("%-10s | HP: %-5d | %-15s | %s" + (ability.name + ((ability.active) ? " * " : "   ")), name, health, type, ability.name);
    }

    public String inSingleFightInfo() {
        return name + " | HP: " + health + " | " + type + " | " + ability.name + ((ability.active) ? " * " : "   ");
    }

    //--------------------------Методи для виведення інформації про дроїда---------------------------------

    public boolean isAlive() {
        return alive;
    }

    public String getName() {
        return name;
    }

    //--------------------------Методи для роботи зі здібністю---------------------------------

    public Ability getAbility() {
        return ability;
    }

    public boolean isActiveAbility() {
        return ability.isActive();
    }

    public boolean isAbilityReady() {
        return ability.getReloadCount() == ability.getReloadTiming();
    }

    public boolean isAbilityEnd() {
        return ability.getActiveCount() == 0;
    }

    // Розрядка здібності
    public void abilityResetCooldown() {
        ability.resetCooldown();
    }

    // Зарядка здібності
    public void abilityUpdateCooldown() {
        ability.updateCooldown();
    }

    // Скидання здібності
    public void abilityReset(FileLogger fileLogger, Droid target) {
        ability.reset(fileLogger, target);
    }

}

//---------------------------------------Дочірні класи, які реалізують різні види дроїдів-----------------------------

class LightDroid extends Droid {
    // Конструктор для LightDroid
    public LightDroid(String name, Ability ability) {
        super(name, "Легкий", 50, 50, 50, 5, 10, ability);
    }
}

class DefaultDroid extends Droid {
    // Конструктор для StandardDroid
    public DefaultDroid(String name, Ability ability) {
        super(name, "Звичайний", 75, 75, 70, 13, 18, ability);
    }
}

class ArmoredDroid extends Droid {
    // Конструктор для ArmoredDroid
    public ArmoredDroid(String name, Ability ability) {
        super(name, "Броньований", 100, 100, 90, 20, 25, ability);
    }
}