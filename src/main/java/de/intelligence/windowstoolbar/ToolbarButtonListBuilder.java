package de.intelligence.windowstoolbar;

import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class ToolbarButtonListBuilder {

    public static ToolbarButtonListBuilder builder() {
        return new ToolbarButtonListBuilder();
    }

    private final Set<TaskbarButton> buttons;

    private ToolbarButtonListBuilder() {
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

        private final ToolbarButtonListBuilder toolbarButtonListBuilder;

        private Icon icon;
        private String tooltip;
        private final EnumSet<TaskbarButtonFlag> flags;

        private ITaskbarButtonClickListener listener = evt -> {};

        private TaskbarButtonBuilder(ToolbarButtonListBuilder toolbarButtonListBuilder) {
            this.toolbarButtonListBuilder = toolbarButtonListBuilder;
            this.flags = EnumSet.noneOf(TaskbarButtonFlag.class);
        }

        public ToolbarButtonListBuilder build() {
            this.toolbarButtonListBuilder.addButton(new TaskbarButton(this.icon, this.tooltip, this.flags, this.listener));
            return this.toolbarButtonListBuilder;
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
