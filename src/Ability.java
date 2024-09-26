
public abstract class Ability {

    protected String name;

    protected int reloadTiming;
    protected int reloadCount = 0;
    protected boolean active = false;

    protected int activeTiming;
    protected int activeCount = 0;

    public Ability(String name, int reloadTiming, int activeTiming) {
        this.name = name;
        this.reloadTiming = reloadTiming;
        this.activeTiming = activeTiming;
    }

    //-----------------------------Робота зі здібністю---------------------------------

    // Вимкнення здібності
    public void reset(FileLogger fileLogger, Droid target) {
        active = false;

        String abilityMessageConsole = "\n\t" + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " скинув здібність " + name;
        String abilityMessageFile = "\n\t" + target.name + " скинув здібність " + name;

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    // Ввімкнення здібності
    public void set(FileLogger fileLogger, Droid target) {
        reloadCount = 0;
        activeCount = activeTiming;
        active = true;
    }

    // Зарядка здібності
    public void updateCooldown() {
        if (reloadCount < reloadTiming) {
            reloadCount++;
        }
    }

    // Розрядка здібності
    public void resetCooldown() {
        if (activeCount > 0) {
            activeCount--;
        }
    }

    //----------------------------Перевантажений метод виведення здібності------------------------

    @Override
    public String toString() {
        return name;
    }

    //------------------------------------Гетери------------------------------------------

    public int getActiveCount() {
        return activeCount;
    }

    public int getReloadCount() {
        return reloadCount;
    }

    public boolean isActive() {
        return active;
    }

    public int getReloadTiming() {
        return reloadTiming;
    }

    public String getName() {
        return name;
    }
}

//---------------------------------------Дочірні класи, які реалізують різні здібності-----------------------------

class Healing extends Ability {
    public Healing() {
        super("Регенерація", 2, 0);
    }

    public void set(FileLogger fileLogger, Droid target) {
        super.set(fileLogger, target);
        target.health = Math.min(target.health + 20, target.maxHealth);

        String abilityMessageConsole = "\n\tДроїд " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " відновив здоров'я до " + target.health;
        String abilityMessageFile = "\n\tДроїд " + target.name + " відновив здоров'я до " + target.health;

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    @Override
    public void reset(FileLogger fileLogger, Droid target) {
        super.reset(fileLogger, target);
    }
}

class Invincible extends Ability {
    private int originalHitChance;

    public Invincible() {
        super("Недосяжність", 3, 1);
    }

    public void set(FileLogger fileLogger, Droid target) {
        super.set(fileLogger, target);
        originalHitChance = target.hitChance;
        target.hitChance = 0;
        String abilityMessageConsole = "\n\tДроїд " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " став невразливим";
        String abilityMessageFile = "\n\tДроїд " + target.name + " став невразливим";

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    @Override
    public void reset(FileLogger fileLogger, Droid target) {
        super.reset(fileLogger, target);
        target.hitChance = originalHitChance;
    }
}

class EnhancedDamage extends Ability {
    private final int damageBonus = 10;

    public EnhancedDamage() {
        super("Збільшена шкода", 2, 1); // Ability lasts for 1 turn and has 2 turns cooldown
    }

    public void set(FileLogger fileLogger, Droid target) {
        super.set(fileLogger, target);
        target.minDamage += damageBonus;
        target.maxDamage += damageBonus;
        String abilityMessageConsole = "\n\tДроїд " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " збільшує шкоду на " + damageBonus + " очок";
        String abilityMessageFile = "\n\tДроїд " + target.name + " збільшує шкоду на " + damageBonus + " очок";

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    @Override
    public void reset(FileLogger fileLogger, Droid target) {
        super.reset(fileLogger, target);
        target.minDamage -= damageBonus;
        target.maxDamage -= damageBonus;
    }
}

class EnhancedArmor extends Ability {
    public EnhancedArmor() {
        super("Збільшена броня", 2, 1); // Lasts for 1 turn, cooldown 2 turns
    }

    public void set(FileLogger fileLogger, Droid target) {
        super.set(fileLogger, target);

        String abilityMessageConsole = "\n\tДроїд " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " отримав збільшену броню, зменшуючи кількість очок шкоди ";
        String abilityMessageFile = "\n\tДроїд " + target.name + " отримав збільшену броню, зменшуючи кількість очок шкоди ";

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    @Override
    public void reset(FileLogger fileLogger, Droid target) {
        super.reset(fileLogger, target);
    }
}

class Spikes extends Ability {

    public Spikes() {
        super("Шипи", 2, Integer.MAX_VALUE); // Spikes are passive and last indefinitely
    }

    public void set(FileLogger fileLogger, Droid target) {
        super.set(fileLogger, target);

        String abilityMessageConsole = "\n\tДроїд " + ConsoleColors.YELLOW + target.name + ConsoleColors.RESET + " активував шипи. Атакуючі дроїди отримають шкоду.";
        String abilityMessageFile = "\n\tДроїд " + target.name + " активував шипи. Атакуючі дроїди отримають шкоду.";

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    public void onHit(Droid attacker, FileLogger fileLogger) {

        int spikeDamage = 5;
        String abilityMessageConsole = "\t" + ConsoleColors.YELLOW + attacker.name + ConsoleColors.RESET + " отримав шкоду від шипів: " + spikeDamage;
        String abilityMessageFile = "\t" + attacker.name + " отримав шкоду від шипів: " + spikeDamage;

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
        attacker.health -= spikeDamage;
        if (attacker.health <= 0) {
            attacker.alive = false;
        }
        reset(fileLogger, attacker);
    }

    @Override
    public void reset(FileLogger fileLogger, Droid target) {
        super.reset(fileLogger, target);
    }
}