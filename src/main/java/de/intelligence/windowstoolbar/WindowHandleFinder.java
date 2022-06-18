package de.intelligence.windowstoolbar;

import java.awt.Component;
import java.lang.reflect.Method;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinDef;

public final class WindowHandleFinder {

    private WindowHandleFinder() {}

    public static WinDef.HWND findByName(String name) {
        return User32.INSTANCE.FindWindowA(null, name);
    }

    public static WinDef.HWND getFromInternalAWT(Component component) {
        return new WinDef.HWND(Native.getComponentPointer(Conditions.notNull(component)));
    }

    public static WinDef.HWND getFromInternalJavaFX(Object stage) {
        try {
            final Method getPeerMethod = Class.forName("javafx.stage.Window").getDeclaredMethod("getPeer");
            getPeerMethod.setAccessible(true);
            final Object tkStageObj = getPeerMethod.invoke(stage);
            final Method getRawHandleMethod = tkStageObj.getClass().getMethod("getRawHandle");
            getRawHandleMethod.setAccessible(true);
            return new WinDef.HWND(new Pointer((long) getRawHandleMethod.invoke(tkStageObj)));
        } catch (ReflectiveOperationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static WinDef.HWND getFromActiveWindow() {
        return User32.INSTANCE.GetActiveWindow();
    }

}
