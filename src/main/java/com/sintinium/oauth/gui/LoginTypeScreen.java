package com.sintinium.oauth.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;

import com.sintinium.oauth.login.LoginUtil;
import com.sintinium.oauth.login.MicrosoftLogin;

public class LoginTypeScreen extends GuiScreenCustom {

    private GuiMultiplayer lastScreen;

    private int mojangButtonId = 0;
    private int microsoftLoginId = 1;
    private int cancelId = 2;

    public LoginTypeScreen(GuiMultiplayer last) {
        lastScreen = last;
    }

    @Override
    public void initGui() {
        this.addButton(
                new ActionButton(
                        mojangButtonId,
                        this.width / 2 - 100,
                        this.height / 2 - 20 - 2,
                        200,
                        20,
                        I18n.format("oauth.btn.login.mojang"),
                        () -> { Minecraft.getMinecraft().displayGuiScreen(new LoginScreen(this, lastScreen)); }));
        this.addButton(
                new ActionButton(
                        microsoftLoginId,
                        this.width / 2 - 100,
                        this.height / 2 + 2,
                        200,
                        20,
                        I18n.format("oauth.btn.login.microsoft"),
                        () -> {
                            final MicrosoftLogin login = new MicrosoftLogin();
                            LoginLoadingScreen loadingScreen = new LoginLoadingScreen(this, login::cancelLogin, true);
                            login.setUpdateStatusConsumer(loadingScreen::updateText);
                            Thread thread = new Thread(() -> login.login(() -> {
                                LoginUtil.updateOnlineStatus();
                                Minecraft.getMinecraft().displayGuiScreen(lastScreen);
                            }));
                            if (login.getErrorMsg() != null) {
                                System.err.println(login.getErrorMsg());
                            }
                            Minecraft.getMinecraft().displayGuiScreen(loadingScreen);
                            thread.start();
                        }));

        this.addButton(
                new ActionButton(
                        cancelId,
                        this.width / 2 - 100,
                        this.height / 2 + 60,
                        200,
                        20,
                        I18n.format("gui.cancel"),
                        () -> { Minecraft.getMinecraft().displayGuiScreen(lastScreen); }));
    }

    @Override
    protected void actionPerformed(GuiButton button) {
        if (button instanceof ActionButton) {
            ((ActionButton) button).onClicked();
        } else {
            throw new RuntimeException("Missing button action!");
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawBackground(0);
        drawCenteredString(
                Minecraft.getMinecraft().fontRenderer,
                I18n.format("oauth.screen.title"),
                this.width / 2,
                this.height / 2 - 60,
                0xFFFFFF);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

}
