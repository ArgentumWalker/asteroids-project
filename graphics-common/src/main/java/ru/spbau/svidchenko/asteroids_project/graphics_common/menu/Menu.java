package ru.spbau.svidchenko.asteroids_project.graphics_common.menu;

import ru.spbau.svidchenko.asteroids_project.graphics_common.GraphicStyleContainer;

import java.util.ArrayList;
import java.util.List;

public class Menu {
    protected String title;
    protected final List<MenuButton> buttons = new ArrayList<>();
    protected MenuButton activeButton = null;

    public boolean isActiveButton(MenuButton button) {
        return activeButton == button;
    }

    public MenuButton getActiveButton() {
        if (buttons.isEmpty()) {
            return null;
        }
        if (activeButton == null) {
            activeButton = buttons.get(0);
        }
        return activeButton;
    }

    public void setActiveButton(MenuButton button) {
        if (buttons.contains(button)) {
            activeButton = button;
        }
    }

    public int getActiveButtonPosition() {
        if (activeButton == null) {
            return 0;
        }
        return buttons.indexOf(activeButton);
    }

    public Menu(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public List<MenuButton> getButtons() {
        return buttons;
    }
}
