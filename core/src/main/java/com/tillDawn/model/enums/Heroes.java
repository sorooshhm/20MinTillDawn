package com.tillDawn.model.enums;


import com.badlogic.gdx.utils.Array;

public enum Heroes {
    SHANA("Shana", 4, 4),
    DIAMOND("Diamond", 1, 7),
    SCARLETT("Scarlett", 5, 3),
    LILITH("Lilith", 3, 5),
    DASHER("Dasher", 10, 2),
    ;

    private final String name;
    private final int speed;
    private final int hp;

    public String getName() {
        return name;
    }

    public int getSpeed() {
        return speed;
    }

    public int getHp() {
        return hp;
    }

    Heroes(String name, int speed, int hp) {
        this.name = name;
        this.speed = speed;
        this.hp = hp;
    }

    public static Array<String> getValuesStrings(){
        Array<String> arr = new Array<>();
        for(Heroes hero : Heroes.values()){
            arr.add(hero.getName());
        }
        return arr;
    }

    public static Heroes findHeroByName(String name) {
        for (Heroes h : Heroes.values()) {
            if (h.getName().equals(name)) {
                return h;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "name  : " + name + " , speed : " + speed + " , hp : " + hp;
    }
}
