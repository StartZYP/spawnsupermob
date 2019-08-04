package com.github.qq44920040.getmobs;

public class MobEntity {
    int Damage;
    String Playername;
    boolean Nodamage;
    String Cmd;

    public MobEntity(int damage, String playername, boolean nodamage,String cmd) {
        Damage = damage;
        Playername = playername;
        Nodamage = nodamage;
        Cmd = cmd;
    }

    public int getDamage() {
        return Damage;
    }

    public String getPlayername() {
        return Playername;
    }
    public String getCmd(){
        return Cmd;
    }

    public boolean isNodamage() {
        return Nodamage;
    }
}
