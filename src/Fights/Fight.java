package Fights;

import Abilities.Spikes;
import Droids.Droid;
import Loggers.*;
import java.io.IOException;
import java.util.Random;

public abstract class Fight {

    public abstract void startFight();

    public void attack(Droid attacker, Droid target) {
        Random random = new Random();
        int hit = random.nextInt(100);

        LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + attacker.getName() + Logger.RESET + " атакує " + Logger.YELLOW + target.getName() + Logger.RESET + ":",
                             "\n\tДроїд " + attacker.getName() + " атакує " + target.getName() + ":");

        if (hit < target.getHitChance()) {
            int damage = random.nextInt((attacker.getMaxDamage() - attacker.getMinDamage()) + 1) + attacker.getMinDamage();

            // Якщо активована здатність "Збільшена броня", зменшуємо шкоду вдвічі
            if ("Збільшена броня".equals(target.getAbility().toString()) && target.getAbility().isActive()) {
                damage -= damage / 2;
            }

            LoggerPrint.log("\n\tДроїд " + Logger.YELLOW + target.getName() + Logger.RESET + " отримав пошкодження: -" + damage + "\n\n",
                                 "\n\tДроїд " + target.getName() + " отримав пошкодження: -" + damage + "\n\n");
            target.takeDamage(damage);

            if (target.getHealth() == 0) {
                LoggerPrint.log("\n\tДроїда " + Logger.YELLOW + target.getName() + Logger.RESET + " знищено.\n\n",
                                     "\n\tДроїда " + target.getName() + " знищено.\n\n");
            }

            // Якщо активована здатність "Шипи" у цілі
            if ("Шипи".equals(attacker.getAbility().toString()) && target.getAbility().isActive()) {
                Spikes.onHit(attacker); // Завдаємо шкоди дроїду, що атакував

                LoggerPrint.log("\n\tШипи завдали шкоди атакуючому дроїду " + Logger.YELLOW + attacker.getName() + Logger.RESET + "\n",
                                     "\n\tШипи завдали шкоди атакуючому дроїду " + attacker.getName() + "\n");
            }

        } else {
            LoggerPrint.log("\n\tПромах\n\n", "\n\tПромах\n\n");
        }
    }
}