package de.intelligence.windowstoolbar;

public enum TaskbarButtonFlag {

    ENABLED(IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONFLAGS.THBF_ENABLED),
    DISABLED(IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONFLAGS.THBF_DISABLED),
    DISMISS_ON_CLICK(IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONFLAGS.THBF_DISMISSONCLICK),
    NO_BACKGROUND(IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONFLAGS.THBF_NOBACKGROUND),
    HIDDEN(IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONFLAGS.THBF_HIDDEN),
    NON_INTERACTIVE(IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONFLAGS.THBF_NONINTERACTIVE);

    private final int flag;

    TaskbarButtonFlag(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }

}
