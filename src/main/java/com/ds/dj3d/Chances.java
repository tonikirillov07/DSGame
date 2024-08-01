package com.ds.dj3d;

public final class Chances {
    public static final float SPRING_SPAWN_CHANCE = 0.3f;
    public static final float ENEMY_SPAWN_CHANCE = 0.03f;

    public static float calculateBluePlatformChance(int score){
        return Math.min(0.20f * ((float) score / 100), 0.5f);
    }
}
