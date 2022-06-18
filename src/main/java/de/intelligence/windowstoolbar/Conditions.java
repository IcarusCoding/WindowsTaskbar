package de.intelligence.windowstoolbar;

import java.io.File;

import de.intelligence.windowstoolbar.exceptions.ConditionNotMetException;

public final class Conditions {

    private Conditions() {}

    public static <T> T notNull(T t) {
        if (t == null) {
            throw new ConditionNotMetException("Non nullable parameter is null");
        }
        return t;
    }

    public static File fileExists(File f) {
        if (!f.exists()) {
            throw new ConditionNotMetException("File \"" + f.getAbsolutePath() + "\" does not exist");
        }
        return f;
    }

    public static void checkState(boolean state, String msg) {
        if(!state) {
            throw new ConditionNotMetException(msg);
        }
    }

}
