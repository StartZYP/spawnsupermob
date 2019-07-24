package com.github.qq44920040.getmobs;

public class MobEntity {
    int Damage;
    String Playername;
    boolean Nodamage;

    public MobEntity(int damage, String playername, boolean nodamage) {
        Damage = damage;
        Playername = playername;
        Nodamage = nodamage;
    }

    public int getDamage() {
        return Damage;
    }

    public String getPlayername() {
        return Playername;
    }

    public boolean isNodamage() {
        return Nodamage;
    }
}
