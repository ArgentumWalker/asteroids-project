package ru.spbau.svidchenko.asteroids_project.graphics_common.menu;

import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    protected String title;
    protected final List<MenuButton> buttons = new ArrayList<>();
    protected int activeButtonId = 0;
    public boolean isActiveButton(MenuButton button) {
        return !buttons.isEmpty() && buttons.get(activeButtonId) == button;
    }

    public MenuButton getActiveButton() {
        if (buttons.isEmpty()) {
            return null;
        }
        return buttons.get(activeButtonId);
    }

    public void activateNextButton() {
        if (activeButtonId < buttons.size() - 1) {
            activeButtonId++;
        }
    }

    public void activatePreviousButton() {
        if (activeButtonId > 0) {
            activeButtonId--;
        }
    }

    public int getActiveButtonPosition() {
        return activeButtonId;
    }

    public Menu(String title) {
        this.title = title;
    }

    public Menu addButton(MenuButton button) {
        buttons.add(button);
        return this;
    }

    public String getTitle() {
        return title;
    }

    public List<MenuButton> getButtons() {
        return buttons;
    }
}
