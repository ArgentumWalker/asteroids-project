package ru.spbau.svidchenko.asteroids_project.graphics_common.menu;

import javafx.scene.text.Font;

public  class MenuButton {
    protected String text;

    protected MenuButton(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
