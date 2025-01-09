package com.sintinium.oauth;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;

import com.sintinium.oauth.gui.ActionButton;
import com.sintinium.oauth.gui.LoginLoadingScreen;
import com.sintinium.oauth.gui.TextWidget;
import com.sintinium.oauth.login.LoginUtil;
import com.sintinium.oauth.login.MicrosoftLogin;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

@SuppressWarnings("unused")
public class GuiEventHandler {

    private static final TextWidget statusText = new TextWidget(10 + 66 + 3, 12, I18n.format("oauth.status.loading"));

    @SuppressWarnings("unchecked")
    @SubscribeEvent
    public void multiplayerScreenOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiMultiplayer)) return;
        try {
            event.buttonList.add(new ActionButton(29183, 10, 6, 66, 20, I18n.format("oauth.btn.oauth.login"), () -> {
                final MicrosoftLogin login = new MicrosoftLogin();
                LoginLoadingScreen loadingScreen = new LoginLoadingScreen(event.gui, login::cancelLogin, true);
                login.setUpdateStatusConsumer(loadingScreen::updateText);
                Thread loginThread = new Thread(() -> login.login(() -> {
                    LoginUtil.updateOnlineStatus();
                    Minecraft.getMinecraft().displayGuiScreen(event.gui);
                }));
                if (login.getErrorMsg() != null) {
                    System.err.println(login.getErrorMsg());
                }
                Minecraft.getMinecraft().displayGuiScreen(loadingScreen);
                loginThread.start();
            }));

            Thread thread = new Thread(() -> {
                boolean isOnline = LoginUtil.isOnline();
                if (isOnline) {
                    statusText.setText(I18n.format("oauth.status.online"));
                    statusText.setColor(0x55FF55);
                } else {
                    statusText.setText(I18n.format("oauth.status.offline"));
                    statusText.setColor(0xFF5555);
                }
            });
            thread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void multiplayerScreenDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiMultiplayer)) return;
        statusText.draw(event.gui);
    }
}
