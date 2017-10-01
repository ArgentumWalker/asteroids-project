package ru.spbau.svidchenko.asteroids_project.graphics_common.commons;

import ru.spbau.svidchenko.asteroids_project.game_logic.world.EntityRelative;

public interface GraphicStyleContainer {
    Sprite getSpriteFor(EntityRelative relative);
}
