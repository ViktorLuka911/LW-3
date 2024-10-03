package Abilities;

import Droids.Droid;
import Loggers.*;

public class Spikes extends Ability {

    public Spikes() {
        super("Шипи", 2, 3);
    }

    @Override
    public void activate(Droid target) {
        set();
        LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + target.getName() + Logger.RESET + " активував шипи. Атакуючі дроїди отримають шкоду.",
                             "\n\tДроїд " + target.getName() + " активував шипи. Атакуючі дроїди отримають шкоду.");
    }

     public static void onHit(Droid attacker) {

        int spikeDamage = 5;
        attacker.takeDamage(spikeDamage);

        LoggerPrint.log("\t" + Logger.YELLOW + attacker.getName() + Logger.RESET + " отримав шкоду від шипів: " + spikeDamage,
                             "\t" + attacker.getName() + " отримав шкоду від шипів: " + spikeDamage);
    }

    @Override
    public Ability copy() {
        return new Spikes(); // або копіюємо специфічні поля
    }
}