public abstract class Ability {
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

    public abstract void useAbility(Droid target);

    public void reset() {
        active = false;
    }

    public void set() {
        reloadCount = 0;
        activeCount = activeTiming;
        active = true;
    }

    public void updateCooldown() {
        if (reloadCount < reloadTiming) {
            reloadCount++; // Оновлюємо лічильник охолодження
        }
    }

    public void resetCooldown() {
        if (activeCount > 0) {
            activeCount--;
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public abstract void reset(Droid target);
}

class Healing extends Ability {
    public Healing() {
        super("Регенерація", 2, 0);
    }

    @Override
    public void useAbility(Droid target) {
        target.health = Math.min(target.health + 20, target.maxHealth);
        System.out.println("\n\tДроїд " + YELLOW + target.name + RESET + " відновив здоров'я до " + target.health);
    }

    @Override
    public void reset(Droid target) {
        super.reset();
    }
}

class Invincible extends Ability {
    private int originalHitChance;

    public Invincible() {
        super("Недосяжність", 3, Integer.MAX_VALUE);
    }

    @Override
    public void useAbility(Droid target) {
        originalHitChance = target.hitChance;
        target.hitChance = 0;
        System.out.println("\n\tДроїд " + YELLOW + target.name + RESET + " став невразливим");

    }

    @Override
    public void reset(Droid target) {
        super.reset();
        target.hitChance = originalHitChance;
    }
}

class EnhancedDamage extends Ability {
    private int damageBonus = 10;

    public EnhancedDamage() {
        super("Збільшена шкода", 2, 1); // Ability lasts for 1 turn and has 2 turns cooldown
    }

    @Override
    public void useAbility(Droid target) {
        target.minDamage += damageBonus;
        target.maxDamage += damageBonus;
        System.out.println("\n\tДроїд " + YELLOW + target.name + RESET + " збільшує шкоду на " + damageBonus + " очок");

    }

    @Override
    public void reset(Droid target) {
        super.reset();
        target.minDamage -= damageBonus;
        target.maxDamage -= damageBonus;
    }
}

class EnhancedArmor extends Ability {

    public EnhancedArmor() {
        super("Збільшена броня", 2, 1); // Lasts for 1 turn, cooldown 2 turns
    }

    @Override
    public void useAbility(Droid target) {
        System.out.println("\n\tДроїд " + YELLOW + target.name + RESET + " отримав збільшену броню, зменшуючи кількість очок шкоди ");
    }

    @Override
    public void reset(Droid target) {
        super.reset();
    }
}

class Spikes extends Ability {
    private int spikeDamage = 5;

    public Spikes() {
        super("Шипи", 2, Integer.MAX_VALUE); // Spikes are passive and last indefinitely
    }

    @Override
    public void useAbility(Droid target) {
        // Spike effect doesn't have an immediate effect but deals damage when hit
        System.out.println("\n\tДроїд " + YELLOW + target.name + RESET + " активував шипи. Атакуючі дроїди отримають шкоду");
    }

    public void onHit(Droid attacker) {
        System.out.println(YELLOW + attacker.name + RESET + " отримав шкоду від шипів: " + spikeDamage);
        attacker.health -= spikeDamage;
        if (attacker.health <= 0) {
            attacker.alive = false;
        }
        reset();
    }

    @Override
    public void reset(Droid target) {
        super.reset();
    }
}