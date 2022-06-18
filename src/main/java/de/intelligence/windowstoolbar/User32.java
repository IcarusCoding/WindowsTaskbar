package de.intelligence.windowstoolbar;

import com.sun.jna.Callback;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;

public interface User32 extends StdCallLibrary {

    int IMAGE_ICON = 0x01;
    int LR_LOADFROMFILE = 0x10;
    int LR_DEFAULTSIZE = 0x40;
    int GWLP_WNDPROC = -4;
    int WM_COMMAND = 0x0111;

    User32 INSTANCE = Native.load("user32", User32.class);

    WinNT.HANDLE LoadImageA(WinDef.HINSTANCE hInst, String name, int type, int cx, int cy, int fuLoad);

    WinDef.BOOL DestroyIcon(WinDef.HICON hIcon);

    BaseTSD.LONG_PTR SetWindowLongPtrA(WinDef.HWND hWnd, int nIndex, WndProcCallback dwNewLong);

    WinDef.LRESULT CallWindowProcA(BaseTSD.LONG_PTR lpPrevWndFunc, WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam);

    int LookupIconIdFromDirectoryEx(Pointer presbits, boolean fIcon, int cxDesired, int cyDesired, int uFlags);

    WinDef.HICON CreateIconFromResourceEx(Pointer presbits, WinDef.DWORD dwResSize, boolean fIcon, WinDef.DWORD dwVer, int cxDesired, int cyDesired, int uFlags);

    WinDef.HWND FindWindowA(String lpClassName, String lpWindowName);

    WinDef.HWND GetActiveWindow();

    interface WndProcCallback extends Callback {

        WinDef.LRESULT callback(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam);

    }

}
