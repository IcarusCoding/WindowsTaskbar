package de.intelligence.windowstoolbar;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.sun.jna.platform.win32.WinDef;

import de.intelligence.windowstoolbar.exceptions.NotInitializedException;

public final class WindowsTaskbar implements IWindowsTaskbar {

    static {
        if (!System.getProperty("os.name", "").startsWith("Windows")) {
            throw new InvalidOperatingSystemException();
        }
    }

    private final LinkedHashMap<TaskbarButton, IWindowsTaskbarInternal.THUMBBUTTON> buttons;
    private final WinDef.HWND hwnd;
    private final INativeIconHandler nativeIconHandler;

    private IWindowsTaskbarInternal windowsTaskbarInternal;
    private WndProcCallbackOverride override;
    private boolean initialized;

    WindowsTaskbar(WinDef.HWND hwnd) {
        this(hwnd, new NativeIconHandler());
    }

    WindowsTaskbar(WinDef.HWND hwnd, INativeIconHandler nativeIconHandler) {
        this.buttons = new LinkedHashMap<>();
        this.hwnd = Conditions.notNull(hwnd);
        this.nativeIconHandler = Conditions.notNull(nativeIconHandler);
    }

    @Override
    public void init() {
        if (!COM.isCoinitialize()) {
            COM.coinitialize();
        }
        this.windowsTaskbarInternal = WindowsTaskbarInternal.getInstance();
        this.initialized = true;
    }

    @Override
    public void addTab(WinDef.HWND hWnd, WinDef.HWND hWnd2) {
        this.windowsTaskbarInternal.AddTab(hWnd, hWnd2);
    }

    @Override
    public void setTabOrder(WinDef.HWND hWnd, WinDef.HWND hWnd2) {
        this.windowsTaskbarInternal.SetTabOrder(hWnd, hWnd2);
    }

    @Override
    public void setProgressValue(TaskbarProgressState progressState, int progress) {
        if (!this.initialized) {
            throw new NotInitializedException("Taskbar not initialized");
        }
        Conditions.checkState(progressState != TaskbarProgressState.NO_PROGRESS && progressState != TaskbarProgressState.INDETERMINATE,
                "Can not set the progress value of an indeterminate state");
        this.setProgressState(progressState);
        this.windowsTaskbarInternal.SetProgressValue(this.hwnd, progress, 100);
    }

    @Override
    public void setProgressState(TaskbarProgressState progressState) {
        if (!this.initialized) {
            throw new NotInitializedException("Taskbar not initialized");
        }
        this.windowsTaskbarInternal.SetProgressState(this.hwnd, progressState.getFlag());
    }

    @Override
    public void setButtons(Collection<TaskbarButton> buttons) {
        if (!this.initialized) {
            throw new NotInitializedException("Taskbar not initialized");
        }
        if (buttons.isEmpty()) {
            return;
        }
        this.windowsTaskbarInternal.ThumbBarAddButtons(this.hwnd, buttons.size(), this.createButtonArray(buttons));
    }

    @Override
    public void updateButton(Integer id) {
        this.updateButtons(Collections.singletonList(id));
    }

    /* TODO ugly update */
    @Override
    public void updateButtons(Collection<Integer> ids) {
        if (!this.initialized) {
            throw new NotInitializedException("Taskbar not initialized");
        }
        if (ids.isEmpty()) {
            return;
        }
        final Set<Integer> availableIds = this.buttons.keySet().stream().map(TaskbarButton::getId).collect(Collectors.toSet());
        if (!availableIds.containsAll(ids)) {
            throw new IllegalStateException("Cannot update a button that was not added first");
        }
        this.windowsTaskbarInternal.ThumbBarUpdateButtons(this.hwnd, buttons.size(),
                this.createButtonArray(this.buttons.keySet().stream().filter(button -> ids.contains(button.getId())).toList()));
    }

    @Override
    public Optional<TaskbarButton> getButton(int id) {
        return this.buttons.keySet().stream().filter(button -> button.getId() == id).findAny();
    }

    @Override
    public Collection<TaskbarButton> getButtons() {
        return Collections.unmodifiableCollection(this.buttons.keySet());
    }

    @Override
    public Optional<ITaskbarButtonClickListener> getClickListener(int id) {
        return this.buttons.keySet().stream().filter(btn -> btn.getId() == id).map(TaskbarButton::getListener).findAny();
    }

    @Override
    public void overrideWndProcCallback() {
        this.override = new WndProcCallbackOverride(this.hwnd, this);
    }

    @Override
    public void setVisibleInTaskbar(boolean visible) {
        User32.INSTANCE.SetWindowLongPtrA(this.hwnd, -20, User32.INSTANCE.GetWindowLongPtrA(this.hwnd, -20).longValue() | 0x80);
    }

    private IWindowsTaskbarInternal.THUMBBUTTON[] createButtonArray(Collection<TaskbarButton> buttons) {
        final Iterator<TaskbarButton> taskbarButtonIterator = buttons.iterator();
        IWindowsTaskbarInternal.THUMBBUTTON[] thumbButtonArr = null;
        for (int i = 0; i < buttons.size(); i++) {
            if (i == 0) {
                final TaskbarButton button = taskbarButtonIterator.next();
                final IWindowsTaskbarInternal.THUMBBUTTON thumbButton = new IWindowsTaskbarInternal.THUMBBUTTON();
                this.initButtonFields(button, thumbButton);
                thumbButtonArr = (IWindowsTaskbarInternal.THUMBBUTTON[]) thumbButton.toArray(buttons.size()); // force contiguous memory
                this.buttons.put(button, thumbButton);
                continue;
            }
            final TaskbarButton button = taskbarButtonIterator.next();
            this.initButtonFields(button, thumbButtonArr[i]);
            this.buttons.put(button, thumbButtonArr[i]);
        }
        return thumbButtonArr;
    }

    private void initButtonFields(TaskbarButton taskbarButton, IWindowsTaskbarInternal.THUMBBUTTON thumbButton) {
        thumbButton.iId = taskbarButton.getId();
        final AtomicInteger mask = new AtomicInteger();
        if (taskbarButton.getIcon() != null) {
            if (!this.nativeIconHandler.isRegistered(taskbarButton.getIcon())) {
                this.nativeIconHandler.createNativeFor(taskbarButton.getIcon());
            }
            this.nativeIconHandler.getNativeFor(taskbarButton.getIcon()).ifPresent(hIcon -> {
                mask.updateAndGet(v -> v | IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONMASK.THB_ICON);
                thumbButton.hIcon = hIcon;
            });
        }
        if (taskbarButton.getTooltip() != null) {
            mask.updateAndGet(v -> v | IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONMASK.THB_TOOLTIP);
            final char[] tooltip = taskbarButton.getTooltip().substring(0, Math.min(taskbarButton.getTooltip().length(),
                    IWindowsTaskbarInternal.THUMBBUTTON.MAX_TOOLTIP_LENGTH - 1)).toCharArray();
            System.arraycopy(tooltip, 0, thumbButton.szTip, 0, tooltip.length);
        }
        if (!taskbarButton.getFlags().isEmpty()) {
            mask.updateAndGet(v -> v | IWindowsTaskbarInternal.THUMBBUTTON.THUMBBUTTONMASK.THB_FLAGS);
            thumbButton.dwFlags = taskbarButton.getFlags().stream().map(TaskbarButtonFlag::getFlag)
                    .reduce(0, (curr, next) -> curr | next);
        }
        thumbButton.dwMask = mask.get();
    }

}
