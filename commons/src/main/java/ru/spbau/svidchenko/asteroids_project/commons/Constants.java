package ru.spbau.svidchenko.asteroids_project.commons;

public class Constants {

    ////MATH

    public static final double EPS = 0.00001;

    ////WORLD CONSTANTS

    //Common
    public static final double WORLD_HALF_WIDTH = 1200.0;
    public static final double WORLD_HALF_HEIGHT = 1200.0;
    public static final double SPAWN_DISTANCE_KOEF = 2.5;
    public static final long MAX_STONE_COUNT = 150;

    //Entity random orientations
    public static final double STONE_MAX_ANGLE_DELTA = Math.PI / 40;

    //Health
    public static final double SPEED_TO_DAMAGE_KOEF = 8.0;
    public static final long SHIP_START_HEALTH = 1;
    public static final long STONE_START_HEALTH = 1;
    public static final long BULLET_HEALTH = 80;
    public static final long BULLET_DAMAGE_PER_MOVE = 1;

    //Entities sizes
    public static final double SHIP_RADIUS = 20.0;
    public static final double WEAPON_RADIUS = 13.0;
    public static final double STONE_RADIUS = 32.0;
    public static final double BULLET_RADIUS = 7.5;

    //Velocity constraints
    public static final double SHIP_MAX_VELOCITY = 5.5;
    public static final double STONE_MAX_VELOCITY = 4.0;
    public static final double BULLET_BASE_VELOCITY = 9.0;

    //Weapon
    public static final long WEAPON_COOLDOWN = 9;
    public static final long WEAPON_TURNS_TO_TURN_AROUND = 80;

    //Vehicle
    public static final long VEHICLE_TURNS_TO_TURN_AROUND = 80;


    ////GAME CONSTANTS

    //Processing
    public static final long IMPACTS_CALCULATION_SPLIT_DEPTH = 6;

    //Time
    public static final long TURNS_IN_GAME = 12000;

    //Score
    public static final long SCORE_FOR_DEATH = -150;
    public static final long SCORE_FOR_DESTROY_STONE = 10;
    public static final long SCORE_FOR_DESTROY_SHIP = 300;

    ////GUI CONSTANTS

    //Time
    public static final long MILLIS_PER_TURN = 25;

    //Window
    public static final long WINDOW_HALF_WIDTH_PX = 400;
    public static final long WINDOW_HALF_HEIGHT_PX = 400;
    public static final double PIXELS_IN_WORLD_POINT = 1.0;

    //Menu
    public static final double MENU_TITLE_HORIZONTAL_INDENT_PX = 80;
    public static final double MENU_TITLE_VERTICAL_INDENT_PX = 40;
    public static final double MENU_AFTER_TITLE_VERTICAL_INDENT_PX = 150;
    public static final double MENU_BUTTON_HORIZONTAL_INDENT_PX = 40;
    public static final double MENU_BUTTON_VERTICAL_INDENT_PX = 40;
    public static final int AFTER_ACTIVE_BUTTONS_COUNT = 3;
}
