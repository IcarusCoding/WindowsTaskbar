package de.intelligence.windowstoolbar;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.COM.COMUtils;

public final class COM {

    private static volatile boolean coinitialize;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (COM.coinitialize) {
                COM.counitialize();
            }
        }));
    }

    private COM() {}

    public static synchronized boolean isCoinitialize() {
        return COM.coinitialize;
    }

    public static synchronized void coinitialize() {
        if (COM.coinitialize) {
            return;
        }
        COMUtils.checkRC(Ole32.INSTANCE.CoInitializeEx(Pointer.NULL, 0x02));
        COM.coinitialize = true;
    }

    public static synchronized void counitialize() {
        if (!COM.coinitialize) {
            return;
        }
        COM.coinitialize = false;
        Ole32.INSTANCE.CoUninitialize();
    }

}
