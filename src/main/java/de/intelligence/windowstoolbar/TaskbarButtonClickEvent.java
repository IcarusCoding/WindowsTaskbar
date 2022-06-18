package de.intelligence.windowstoolbar;

import com.sun.jna.platform.win32.WinDef;

public record TaskbarButtonClickEvent(IWindowsTaskbar taskbar, TaskbarButton button, WinDef.HWND hWnd, int id) {}
