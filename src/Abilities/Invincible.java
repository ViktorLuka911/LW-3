package Abilities;

import Droids.Droid;
import Loggers.*;

public class Invincible extends Ability {
    private int originalHitChance;

    public Invincible() {
        super("Недосяжність", 3, 1);
    }

    @Override
    public void activate(Droid target) {
        set();
        originalHitChance = target.getHitChance();
        target.setHitChance(0);

        LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + target.getName() + Logger.RESET + " став невразливим",
                "\n\tДроїд " + target.getName() + " став невразливим");
    }

    @Override
    public void reset(Droid target) {
        super.reset(target);
        target.setHitChance(originalHitChance);
    }

    @Override
    public Ability copy() {
        return new Invincible(); // або копіюємо специфічні поля
    }
}