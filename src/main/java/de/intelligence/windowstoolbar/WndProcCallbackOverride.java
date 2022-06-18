package de.intelligence.windowstoolbar;

import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinDef;

final class WndProcCallbackOverride implements User32.WndProcCallback {

    private final WinDef.HWND hWnd;
    private final IWindowsTaskbar taskbar;
    private final BaseTSD.LONG_PTR lpPrevWndFunc;

    WndProcCallbackOverride(WinDef.HWND hWnd, IWindowsTaskbar taskbar) {
        this.hWnd = hWnd;
        this.taskbar = taskbar;
        this.lpPrevWndFunc = User32.INSTANCE.SetWindowLongPtrA(Conditions.notNull(hWnd), User32.GWLP_WNDPROC, this);
    }

    @Override
    public WinDef.LRESULT callback(WinDef.HWND hWnd, int uMsg, WinDef.WPARAM wParam, WinDef.LPARAM lParam) {
        if (uMsg == User32.WM_COMMAND) {
            final int id = new WinDef.DWORD(wParam.intValue()).getLow().intValue();
            this.taskbar.getClickListener(id).ifPresent(listener ->
                    listener.onTaskbarButtonClicked(new TaskbarButtonClickEvent(this.taskbar,
                            this.taskbar.getButton(id).orElse(null), this.hWnd, id)));
        }
        return User32.INSTANCE.CallWindowProcA(this.lpPrevWndFunc, hWnd, uMsg, wParam, lParam);
    }

}
