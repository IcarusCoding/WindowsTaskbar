package de.intelligence.windowstoolbar;

import java.util.Collection;
import java.util.HashSet;

import com.sun.jna.platform.win32.WinDef;

public final class TaskbarBuilder {

    public static TaskbarBuilder builder() {
        return new TaskbarBuilder();
    }

    private final Collection<TaskbarButton> buttons;

    private WinDef.HWND hWnd;
    private boolean autoInit;
    private boolean overrideWndProc;
    private INativeIconHandler nativeIconHandler;

    private TaskbarBuilder() {
        this.buttons = new HashSet<>();
    }

    public IWindowsTaskbar build() {
        Conditions.checkState(this.hWnd != null, "Required field \"hWnd\" missing");
        final IWindowsTaskbar taskbar;
        if (this.nativeIconHandler == null) {
            taskbar = new WindowsTaskbar(this.hWnd);
        } else {
            taskbar = new WindowsTaskbar(this.hWnd, this.nativeIconHandler);
        }
        if (this.overrideWndProc) {
            taskbar.overrideWndProcCallback();
        }
        if (this.autoInit) {
            taskbar.init();
        }
        if (!this.buttons.isEmpty()) {
            //TODO add init listener to taskbar
            taskbar.setButtons(this.buttons);
        }
        return taskbar;
    }

    public TaskbarBuilder setHWnd(WinDef.HWND hWnd) {
        this.hWnd = Conditions.notNull(hWnd);
        return this;
    }

    public TaskbarBuilder autoInit() {
        this.autoInit = true;
        return this;
    }

    public TaskbarBuilder overrideWndProc() {
        this.overrideWndProc = true;
        return this;
    }

    public TaskbarBuilder addButtons(TaskbarButton button) {
        this.buttons.add(button);
        return this;
    }

    public TaskbarBuilder addButtons(Collection<TaskbarButton> buttons) {
        this.buttons.addAll(buttons);
        return this;
    }

    public TaskbarBuilder withNativeIconHandler(INativeIconHandler nativeIconHandler) {
        this.nativeIconHandler = Conditions.notNull(nativeIconHandler);
        return this;
    }

}
