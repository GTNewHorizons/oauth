package com.sintinium.oauth.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

public class ActionButton extends GuiButton {

    private final Runnable onClicked;

    public ActionButton(int buttonId, int x, int y, int widthIn, int heightIn, String buttonText, Runnable onClicked) {
        super(buttonId, x, y, widthIn, heightIn, buttonText);
        this.onClicked = onClicked;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY) {
        if (super.mousePressed(mc, mouseX, mouseY)) {
            onClicked.run();
            return true;
        }
        return false;
    }
}
