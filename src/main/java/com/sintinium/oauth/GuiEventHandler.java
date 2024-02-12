package com.sintinium.oauth;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.GuiScreenEvent;

import com.sintinium.oauth.gui.LoginTypeScreen;
import com.sintinium.oauth.gui.TextWidget;
import com.sintinium.oauth.login.LoginUtil;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class GuiEventHandler {

    private static TextWidget statusText = new TextWidget(10 + 66 + 3, 12, I18n.format("oauth.status.loading"));

    @SubscribeEvent
    public void multiplayerScreenOpen(GuiScreenEvent.InitGuiEvent.Post event) {
        if (!(event.gui instanceof GuiMultiplayer)) return;
        try {
            // Method addButtonMethod = ObfuscationReflectionHelper.findMethod(Screen.class, "addButton", Widget.class);
            // Method addButtonMethod = ObfuscationReflectionHelper.findMethod(Screen.class, "func_230480_a_",
            // Widget.class);
            List<GuiButton> buttonList = new ArrayList<>();
            GuiButton loginButton = new GuiButton(29183, 10, 6, 66, 20, I18n.format("oauth.btn.oauth.login"));
            // p_onPress_1_ ->
            buttonList.add(loginButton);
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

            event.buttonList.addAll(buttonList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void multiplayerScreenDraw(GuiScreenEvent.DrawScreenEvent.Post event) {
        if (!(event.gui instanceof GuiMultiplayer)) return;
        statusText.draw(event.gui);
    }

    @SubscribeEvent
    public void action(GuiScreenEvent.ActionPerformedEvent.Post event) {
        if (!(event.gui instanceof GuiMultiplayer)) return;
        if (event.button.id != 29183) return;
        GuiMultiplayer multiplayerScreen = (GuiMultiplayer) event.gui;
        Minecraft.getMinecraft().displayGuiScreen(new LoginTypeScreen(multiplayerScreen));
    }
}
