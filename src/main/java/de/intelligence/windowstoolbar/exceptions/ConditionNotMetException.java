package de.intelligence.windowstoolbar.exceptions;

public final class ConditionNotMetException extends RuntimeException {

    public ConditionNotMetException(String msg) {
        super(msg);
    }

    public ConditionNotMetException(Throwable throwable) {
        super(throwable);
    }

}
