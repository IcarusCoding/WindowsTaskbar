package de.intelligence.windowstoolbar;

import java.util.Optional;

import com.sun.jna.platform.win32.WinDef;

public sealed interface INativeIconHandler permits NativeIconHandler {

    boolean isRegistered(String id);

    boolean isRegistered(Icon icon);

    void createNativeFor(Icon icon);

    boolean destroyNativeFor(String id);

    boolean destroyNativeFor(Icon icon);

    Optional<WinDef.HICON> getNativeFor(String id);

    Optional<WinDef.HICON> getNativeFor(Icon icon);

}
