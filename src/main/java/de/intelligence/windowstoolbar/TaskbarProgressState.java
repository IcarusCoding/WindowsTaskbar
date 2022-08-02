package de.intelligence.windowstoolbar;

public enum TaskbarProgressState {
    
    NO_PROGRESS(IWindowsTaskbarInternal.TBPFLAG.TBPF_NOPROGRESS),
    INDETERMINATE(IWindowsTaskbarInternal.TBPFLAG.TBPF_INDETERMINATE),
    NORMAL(IWindowsTaskbarInternal.TBPFLAG.TBPF_NORMAL),
    ERROR(IWindowsTaskbarInternal.TBPFLAG.TBPF_ERROR),
    PAUSED(IWindowsTaskbarInternal.TBPFLAG.TBPF_PAUSED);

    private final int flag;

    TaskbarProgressState(int flag) {
        this.flag = flag;
    }

    public int getFlag() {
        return this.flag;
    }

}
