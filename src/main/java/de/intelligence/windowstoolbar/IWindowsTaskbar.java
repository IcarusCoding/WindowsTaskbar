package de.intelligence.windowstoolbar;

import java.util.Collection;
import java.util.Optional;

public interface IWindowsTaskbar {

    void init();

    void setProgressValue(TaskbarProgressState progressState, int progress);

    void setProgressState(TaskbarProgressState progressState);

    void setButtons(Collection<TaskbarButton> buttons);

    void updateButton(Integer id);

    void updateButtons(Collection<Integer> ids);

    Optional<TaskbarButton> getButton(int id);

    Collection<TaskbarButton> getButtons();

    Optional<ITaskbarButtonClickListener> getClickListener(int id);

    void overrideWndProcCallback();

}
