package com.artf.poloa.utility;

/**
 * Created by ART_F on 2017-04-25.
 */

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
