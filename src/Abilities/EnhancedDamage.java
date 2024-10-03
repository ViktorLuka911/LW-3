package Abilities;

import Droids.Droid;
import Loggers.*;

public class EnhancedDamage extends Ability {
    private final int damageBonus = 10;

    public EnhancedDamage() {
        super("Збільшена шкода", 2, 1); // Ability lasts for 1 turn and has 2 turns cooldown
    }

    @Override
    public void activate(Droid target) {
        set();

        target.setMinDamage(target.getMinDamage() + damageBonus);
        target.setMaxDamage(target.getMaxDamage() + damageBonus);

        LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + target.getName() + Logger.RESET + " збільшує шкоду на " + damageBonus + " очок",
                             "\n\tДроїд " + target.getName() + " збільшує шкоду на " + damageBonus + " очок");
    }

    @Override
    public void reset(Droid target) {
        super.reset(target);
        target.setMinDamage(target.getMinDamage() - damageBonus);
        target.setMaxDamage(target.getMaxDamage() - damageBonus);
    }

    @Override
    public Ability copy() {
        return new EnhancedDamage();
    }
}