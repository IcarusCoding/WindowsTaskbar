package de.intelligence.windowstoolbar;

public final class Utils {

    public static final StackWalker WALKER;

    static {
        WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
    }

    private Utils() {}

}
