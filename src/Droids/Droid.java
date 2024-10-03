package Droids;

import Abilities.*;
import Loggers.*;
import Utilities.Utilities;

public abstract class Droid {

    protected String name;
    protected String type;
    protected int health;
    protected final int maxHealth;
    protected int hitChance;
    protected int minDamage;
    protected int maxDamage;
    protected Ability ability;

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
    }

    // Конструктор копіювання
    public Droid(Droid other) {
        this.name = other.name;
        this.type = other.type;
        this.health = other.health;
        this.maxHealth = other.maxHealth;
        this.hitChance = other.hitChance;
        this.minDamage = other.minDamage;
        this.maxDamage = other.maxDamage;
        this.ability = other.ability;
        this.ability = (other.ability != null) ? other.ability.copy() : null; // копіюємо здібність
    }

    // Використання здібності
    public void activateAbility() {
        ability.activate(this); // Активуємо здібність
    }

    //--------------------------Методи для виведення інформації про дроїда---------------------------------
    @Override
    public String toString() {
        return String.format("Ім'я: %-10s | Тип: %-15s | Здоров'я: %-5d | Здібність: %s", name, type, health, ability);
    }

    public String inTeamFightInfo() {
        return String.format("%-10s | HP: %-5d | %-15s | " + (ability + ((ability.isActive()) ? " * " : "   ")), name, health, type);
    }

    public String inSingleFightInfo() {
        return name + " | HP: " + health + " | " + type + " | " + ability + ((ability.isActive()) ? " * " : "   ");
    }

    //--------------------------Методи для виведення інформації про дроїда---------------------------------

    public boolean isAlive() {
        return health > 0;
    }

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getHitChance() {
        return hitChance;
    }

    public int getMinDamage() {
        return minDamage;
    }

    public int getMaxDamage() {
        return maxDamage;
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
    public void abilityReset(Droid target) {
        ability.reset(target);
    }

    //-------------------------- Cетери ---------------------------------

    public void setHealth(int health) {
        this.health = health;
    }

    public void setHitChance(int hitChance) {
        this.hitChance = hitChance;
    }

    public void setMinDamage(int minDamage) {
        this.minDamage = minDamage;
    }

    public void setMaxDamage(int maxDamage) {
        this.maxDamage = maxDamage;
    }

    public void takeDamage(int damage) {
        setHealth(health - damage);
        if (health <= 0) {
            health = 0;
            ability.reset(this);
        }
    }

    // Функція для роботи зі здібностями
    public void handleAbility() {

        // Перевіряємо чи активна здібність
        if (this.isActiveAbility()) {
            this.abilityResetCooldown();
            if (this.isAbilityEnd()) {
                this.abilityReset(this);
            }
        } else if (this.isAbilityReady()) {
            ConsoleLogger.log("\n\tВи хочете використати здібність " + Logger.PURPLE + this.getAbility() + Logger.RESET + " для дроїда " + Logger.YELLOW + this.getName() + Logger.RESET + "?");

            ConsoleLogger.log("\n\t1 - Так");
            ConsoleLogger.log("\n\t2 - Ні");

            int choice = Utilities.getValidatedInput(1, 2);

            if (choice == 1) {
                this.activateAbility();
            }
        } else {
            ConsoleLogger.log("\n\tЗдібність " + Logger.PURPLE + this.ability + Logger.RESET + " не готова до використання для дроїда " + Logger.YELLOW + this.name + Logger.RESET + ".");
            this.abilityUpdateCooldown();
        }
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Droid clonedDroid = (Droid) super.clone();
        clonedDroid.ability = (Ability) this.ability.clone();
        return clonedDroid;
    }
}