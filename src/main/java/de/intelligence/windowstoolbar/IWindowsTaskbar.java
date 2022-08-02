package de.intelligence.windowstoolbar;

import com.sun.jna.platform.win32.WinDef;

import java.util.Collection;
import java.util.Optional;

public interface IWindowsTaskbar {

    void init();

    //TODO remove or beautify
    void addTab(WinDef.HWND hWnd, WinDef.HWND hWnd2);

    void setTabOrder(WinDef.HWND hWnd, WinDef.HWND hWnd2);

    void setProgressValue(TaskbarProgressState progressState, int progress);

    void setProgressState(TaskbarProgressState progressState);

    void setButtons(Collection<TaskbarButton> buttons);

    void updateButton(Integer id);

    void updateButtons(Collection<Integer> ids);

    Optional<TaskbarButton> getButton(int id);

    Collection<TaskbarButton> getButtons();

    Optional<ITaskbarButtonClickListener> getClickListener(int id);

    void overrideWndProcCallback();

    void setVisibleInTaskbar(boolean visible);

}
