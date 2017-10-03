package ru.spbau.svidchenko.asteroids_project.graphics_common.menu;

import javafx.scene.text.Font;

public abstract class MenuButton {
    protected String text;

    protected MenuButton(String text) {
        this.text = text;
    }

    public abstract void onClick();

    public String getText() {
        return text;
    }
}
