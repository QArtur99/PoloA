package com.artf.poloa.utility;


public enum Mode {
    BUY(0), SELL(1);

    private final int value;

    Mode(int value) {
        this.value = value;
    }

    public boolean isBuy() {
        return value == 0;
    }

    public boolean isSell() {
        return value == 1;
    }
}
