package de.intelligence.windowstoolbar;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class TaskbarButtonListBuilder {

    public static TaskbarButtonListBuilder builder() {
        return new TaskbarButtonListBuilder();
    }

    private final Set<TaskbarButton> buttons;

    private TaskbarButtonListBuilder() {
        this.buttons = new HashSet<>();
    }

    public Set<TaskbarButton> build() {
        return Collections.unmodifiableSet(this.buttons);
    }

    public TaskbarButtonBuilder buttonBuilder() {
        Conditions.checkState(this.buttons.size() < IWindowsTaskbarInternal.MAX_BUTTONS, "Maximum number of buttons exceeded");
        return new TaskbarButtonBuilder(this);
    }

    private void addButton(TaskbarButton taskbarButton) {
        this.buttons.add(taskbarButton);
    }

    public static class TaskbarButtonBuilder {

        private final TaskbarButtonListBuilder taskbarButtonListBuilder;

        private Icon icon;
        private String tooltip;
        private final EnumSet<TaskbarButtonFlag> flags;

        private ITaskbarButtonClickListener listener = evt -> {};

        private TaskbarButtonBuilder(TaskbarButtonListBuilder taskbarButtonListBuilder) {
            this.taskbarButtonListBuilder = taskbarButtonListBuilder;
            this.flags = EnumSet.noneOf(TaskbarButtonFlag.class);
        }

        public TaskbarButtonListBuilder build() {
            this.taskbarButtonListBuilder.addButton(new TaskbarButton(this.icon, this.tooltip, this.flags, this.listener));
            return this.taskbarButtonListBuilder;
        }

        public TaskbarButtonBuilder setIcon(Icon icon) {
            this.icon = Conditions.notNull(icon);
            return this;
        }

        public TaskbarButtonBuilder setTooltip(String tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        public TaskbarButtonBuilder setOnClicked(ITaskbarButtonClickListener listener) {
            this.listener = listener;
            return this;
        }

        public TaskbarButtonBuilder withFlag(TaskbarButtonFlag flag) {
            this.flags.add(flag);
            return this;
        }

    }

}
