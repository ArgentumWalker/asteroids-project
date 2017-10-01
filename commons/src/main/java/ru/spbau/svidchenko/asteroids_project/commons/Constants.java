package ru.spbau.svidchenko.asteroids_project.commons;

public class Constants {

    ////MATH

    public static final double EPS = 0.00001;

    ////WORLD CONSTANTS

    //Common
    public static final double WORLD_HALF_WIDTH = 1200.0;
    public static final double WORLD_HALF_HEIGHT = 1200.0;

    //Health
    public static final double SPEED_TO_DAMAGE_KOEF = 2.0;
    public static final long SHIP_START_HEALTH = 10;
    public static final long STONE_START_HEALTH = 10;
    public static final long BULLET_HEALTH = 60;
    public static final long BULLET_DAMAGE_PER_MOVE = 1;

    //Entities sizes
    public static final double SHIP_RADIUS = 20.0;
    public static final double WEAPON_RADIUS = 13.0;
    public static final double STONE_RADIUS = 32.0;
    public static final double BULLET_RADIUS = 1.0;

    //Velocity constraints
    public static final double SHIP_MAX_VELOCITY = 10.0;
    public static final double STONE_MAX_VELOCITY = 10.0;
    public static final double BULLET_BASE_VELOCITY = 30.0;

    //Weapon
    public static final long WEAPON_COOLDOWN = 5;
    public static final long WEAPON_MOVES_TO_TURN = 30;

    //Vehicle
    public static final long VEHICLE_MOVES_TO_TURN = 30;


    ////GAME CONSTANTS

    //Score
    public static final long SCORE_FOR_DEATH = -50;
    public static final long SCORE_FOR_DESTROY_STONE = 10;
    public static final long SCORE_FOR_DESTROY_SHIP = 150;

    ////PLAYER CONSTANTS
    public static final long WINDOW_HALF_WIDTH_Px = 400;
    public static final long WINDOW_HALF_HEIGHT_PX = 400;
    public static final double PIXELS_IN_WORLD_POINT = 2.0;
}
