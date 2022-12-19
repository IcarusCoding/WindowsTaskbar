package de.intelligence.windowstoolbar;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public final class TaskbarButton {

    private static final AtomicInteger GLOBAL_ID;

    static {
        GLOBAL_ID = new AtomicInteger(0);
    }

    private final int id;
    private Icon icon;
    private String tooltip;
    private final EnumSet<TaskbarButtonFlag> flags;
    private ITaskbarButtonClickListener listener;

    public TaskbarButton() {
        this(null, null, EnumSet.noneOf(TaskbarButtonFlag.class), evt -> {});
    }

    public TaskbarButton(Icon icon, String tooltip, EnumSet<TaskbarButtonFlag> flags, ITaskbarButtonClickListener listener) {
        this.id = TaskbarButton.GLOBAL_ID.incrementAndGet();
        this.icon = icon;
        this.tooltip = tooltip;
        this.flags = flags;
        this.listener = listener;
    }

    public int getId() {
        return this.id;
    }

    public Icon getIcon() {
        return this.icon;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public EnumSet<TaskbarButtonFlag> getFlags() {
        return EnumSet.copyOf(this.flags);
    }

    public void setFlags(EnumSet<TaskbarButtonFlag> flags) {
        this.flags.clear();
        this.flags.addAll(flags);
    }

    public void setDisabled(boolean disabled) {
        this.flags.remove(TaskbarButtonFlag.DISABLED);
        this.flags.remove(TaskbarButtonFlag.ENABLED);
        if (disabled) {
            this.flags.add(TaskbarButtonFlag.DISABLED);
        } else {
            this.flags.add(TaskbarButtonFlag.ENABLED);
        }
    }

    ITaskbarButtonClickListener getListener() {
        return this.listener;
    }

    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public void setOnClicked(ITaskbarButtonClickListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final TaskbarButton that = (TaskbarButton) o;
        return this.id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
