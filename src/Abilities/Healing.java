package Abilities;

import Droids.Droid;
import Loggers.*;

public class Healing extends Ability {
    public Healing() {
        super("Регенерація", 2, 0);
    }

    @Override
    public void activate(Droid target) {
        set();
        target.setHealth(Math.min(target.getHealth() + 20, target.getHealth()));

        LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + target.getName() + Logger.RESET + " відновив здоров'я до " + target.getHealth(),
                             "\n\tДроїд " + target.getName() + " відновив здоров'я до " + target.getHealth());
    }

    @Override
    public Ability copy() {
        return new Healing();
    }
}