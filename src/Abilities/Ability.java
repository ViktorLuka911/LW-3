package Abilities;

import Droids.Droid;
import Loggers.*;

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

    public abstract void activate(Droid target);
    public abstract Ability copy();

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    // Ввімкнення здібності
    public void set() {
        reloadCount = 0;
        activeCount = activeTiming;
        active = true;
    }

    // Вимкнення здібності
    public void reset(Droid target) {
        active = false;
        LoggerPrint.log("\n\t" + Logger.YELLOW + target.getName() + Logger.RESET + " скинув здібність " + name,
                "\n\t" + target.getName() + " скинув здібність " + name);
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
}