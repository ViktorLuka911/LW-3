package Droids;

import Abilities.Ability;

public class LightDroid extends Droid {
    // Конструктор для LightDroid
    public LightDroid(String name, Ability ability) {
        super(name, "Легкий", 50, 50, 50, 5, 10, ability);
    }

    public LightDroid(Droid selectedDroid) {
        super(selectedDroid);
    }
}