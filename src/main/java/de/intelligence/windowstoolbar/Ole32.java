package de.intelligence.windowstoolbar;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.win32.StdCallLibrary;

public interface Ole32 extends StdCallLibrary {

    Ole32 INSTANCE = Native.load(Ole32.class);

    WinNT.HRESULT CoInitializeEx(Pointer pvReserved, int dwCoInit);

    void CoUninitialize();

    WinNT.HRESULT CoCreateInstance(Guid.GUID rclsid, Pointer pUnkOuter, int dwClsContext, Guid.GUID riid, PointerByReference ppv);

}