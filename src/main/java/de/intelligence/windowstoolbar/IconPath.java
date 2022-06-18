package de.intelligence.windowstoolbar;

public final class IconPath {

    private final String path;
    private final IconPathType type;

    public IconPath(String path, IconPathType type) {
        this.path = Conditions.notNull(path);
        this.type = type;
    }

    public String getPath() {
        return this.path;
    }

    public IconPathType getType() {
        return this.type;
    }

    public enum IconPathType {

        ABSOLUTE,
        CLASSPATH

    }

}
