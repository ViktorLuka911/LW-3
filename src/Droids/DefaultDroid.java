package Droids;

import Abilities.Ability;

public class DefaultDroid extends Droid {
    // Конструктор для StandardDroid
    public DefaultDroid(String name, Ability ability) {
        super(name, "Звичайний", 75, 75, 70, 13, 18, ability);
    }

    public DefaultDroid(Droid selectedDroid) {
        super(selectedDroid);
    }
}