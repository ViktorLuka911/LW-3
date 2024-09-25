public abstract class Ability {
    public static final String RESET = "\u001B[0m";  // Скидання кольору
    public static final String YELLOW = "\u001B[33m";

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

    // Абстрактний метод для активації здібності
    public abstract void useAbility(Droid target, FileLogger fileLogger);

    // Вимкнення здібності
    public void reset(FileLogger fileLogger, Droid target) {
        active = false;

        String abilityMessageConsole = "\n\t" + YELLOW + target.name + RESET + " скинув здібність " + name;
        String abilityMessageFile = "\n\t" + target.name + " скинув здібність " + name;

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    // Ввімкнення здібності
    public void set() {
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

    public int getActiveTiming() {
        return activeTiming;
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

    @Override
    public void useAbility(Droid target, FileLogger fileLogger) {
        target.health = Math.min(target.health + 20, target.maxHealth);

        String abilityMessageConsole = "\n\tДроїд " + YELLOW + target.name + RESET + " відновив здоров'я до " + target.health;
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

    @Override
    public void useAbility(Droid target, FileLogger fileLogger) {
        originalHitChance = target.hitChance;
        target.hitChance = 0;
        String abilityMessageConsole = "\n\tДроїд " + YELLOW + target.name + RESET + " став невразливим";
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
    private int damageBonus = 10;

    public EnhancedDamage() {
        super("Збільшена шкода", 2, 1); // Ability lasts for 1 turn and has 2 turns cooldown
    }

    @Override
    public void useAbility(Droid target, FileLogger fileLogger) {
        target.minDamage += damageBonus;
        target.maxDamage += damageBonus;
        String abilityMessageConsole = "\n\tДроїд " + YELLOW + target.name + RESET + " збільшує шкоду на " + damageBonus + " очок";
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

    @Override
    public void useAbility(Droid target, FileLogger fileLogger) {

        String abilityMessageConsole = "\n\tДроїд " + YELLOW + target.name + RESET + " отримав збільшену броню, зменшуючи кількість очок шкоди ";
        String abilityMessageFile = "\n\tДроїд " + target.name + " отримав збільшену броню, зменшуючи кількість очок шкоди ";

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    @Override
    public void reset(FileLogger fileLogger, Droid target) {
        super.reset(fileLogger, target);
    }
}

class Spikes extends Ability {
    private int spikeDamage = 5;

    public Spikes() {
        super("Шипи", 2, Integer.MAX_VALUE); // Spikes are passive and last indefinitely
    }

    @Override
    public void useAbility(Droid target, FileLogger fileLogger) {

        String abilityMessageConsole = "\n\tДроїд " + YELLOW + target.name + RESET + " активував шипи. Атакуючі дроїди отримають шкоду.";
        String abilityMessageFile = "\n\tДроїд " + target.name + " активував шипи. Атакуючі дроїди отримають шкоду.";

        fileLogger.log(abilityMessageConsole, abilityMessageFile);
    }

    public void onHit(Droid attacker, FileLogger fileLogger) {

        String abilityMessageConsole = "\t" + YELLOW + attacker.name + RESET + " отримав шкоду від шипів: " + spikeDamage;
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