package com.sintinium.oauth.login;

import java.lang.reflect.Field;
import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

import com.mojang.authlib.exceptions.AuthenticationException;
import com.mojang.authlib.minecraft.MinecraftSessionService;

import cpw.mods.fml.relauncher.ReflectionHelper;

public class LoginUtil {

    public static boolean needsRefresh = true;
    public static boolean wasOnline = false;
    private static long lastCheck = -1L;

    public static void updateOnlineStatus() {
        needsRefresh = true;
        isOnline();
    }

    public static boolean isOnline() {
        if (!needsRefresh && System.currentTimeMillis() - lastCheck < 1000 * 10) {
            return wasOnline;
        }
        Session session = Minecraft.getMinecraft().getSession();
        MinecraftSessionService sessionService = Minecraft.getMinecraft().func_152347_ac();
        String uuid = UUID.randomUUID().toString();
        needsRefresh = false;
        lastCheck = System.currentTimeMillis();
        try {
            sessionService.joinServer(session.func_148256_e(), session.getToken(), uuid);
            if (sessionService.hasJoinedServer(session.func_148256_e(), uuid).isComplete()) {
                wasOnline = true;
                return true;
            } else {
                wasOnline = false;
                return false;
            }
        } catch (AuthenticationException e) {
            wasOnline = false;
            return false;
        }
    }

    public static void loginMs(MicrosoftLogin.MinecraftProfile profile) {
        Session session = new Session(profile.name, profile.id, profile.token.accessToken, Session.Type.MOJANG.name());
        setSession(session);
    }

    private static void setSession(Session session) {
        needsRefresh = true;
        updateOnlineStatus();
        Field field = ReflectionHelper.findField(Minecraft.class, "field_71449_j", "session");
        field.setAccessible(true);
        try {
            field.set(Minecraft.getMinecraft(), session);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
