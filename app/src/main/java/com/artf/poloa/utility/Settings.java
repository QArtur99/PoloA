package com.artf.poloa.utility;

public class Settings {

    public class Trade {
        public static final String CC_NAME_PAIR = "BTC_ETH";
        public static final String CC_NAME = "ETH";
        public static final double AVAILABLE_BTC_FOR_TRADE_PERCENTAGE = 0.05;
        public static final double RATE_OF_LAST_BUY = 0.04808165; // IF SELL
        public static final double VALID_AMOUNT = 0.1; //RULE FOR BUY - IF MORE BUY ELSE SELL IT
        public static final double DONT_CARE_BALANCE = 0;
    }

    public class Trend {
        public static final double RULE_LONG_TREND = 0.85;
    }

    public class RMI {
        public static final int LENHTH = 20;
        public static final int MOMENTUM = 4;
        public static final int SIGNAL = 4;
        public static final int OVER_BOUGHT = 70;
        public static final int OVER_SOLD = 40;
    }

    public class Stochastic {
        public static final int LENHTH = 14;
        public static final int SIGNAL = 3;
    }


}
