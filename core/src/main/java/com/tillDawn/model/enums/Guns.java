package com.tillDawn.model.enums;

public enum Guns {
    REVOLVER("Revolver", 6, 1, 1, 20),
    SHOTGUN("Shotgun", 2, 1, 4, 10),
    SMG("Smg", 24, 2, 1, 8);
    private final String name;
    private final int ammoMax;
    private final int timeReload;
    private final int projectile;
    private final int damage;

    public String getName() {
        return name;
    }

    public int getAmmoMax() {
        return ammoMax;
    }

    public int getTimeReload() {
        return timeReload;
    }

    public int getProjectile() {
        return projectile;
    }

    public int getDamage() {
        return damage;
    }

    Guns(String name, int ammoMax, int timeReload, int projectile, int damage) {
        this.name = name;
        this.ammoMax = ammoMax;
        this.timeReload = timeReload;
        this.projectile = projectile;
        this.damage = damage;
    }

    public static Guns findGunByName(String name) {
        for (Guns g : Guns.values()) {
            if (g.name.equals(name)) {
                return g;
            }
        }
        return null;
    }

}
