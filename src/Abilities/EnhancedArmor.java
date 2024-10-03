package Abilities;

import Droids.Droid;
import Loggers.*;

public class EnhancedArmor extends Ability {
    public EnhancedArmor() {
        super("Збільшена броня", 2, 1); // Lasts for 1 turn, cooldown 2 turns
    }

    @Override
    public void activate(Droid target) {
        set();

        LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + target.getName() + Logger.RESET + " отримав збільшену броню, зменшуючи кількість очок шкоди.",
                             "\n\tДроїд " + target.getName() + " отримав збільшену броню, зменшуючи кількість очок шкоди.");
    }

    @Override
    public Ability copy() {
        return new EnhancedArmor();
    }
}