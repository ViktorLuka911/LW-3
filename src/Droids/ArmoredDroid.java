package Droids;

import Abilities.Ability;

public class ArmoredDroid extends Droid {
    // Конструктор для ArmoredDroid
    public ArmoredDroid(String name, Ability ability) {
        super(name, "Броньований", 100, 100, 90, 20, 25, ability);
    }

    public ArmoredDroid(Droid selectedDroid) {
        super(selectedDroid);
    }
}