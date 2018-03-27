package com.artf.poloa.utility;

import com.artf.poloa.data.entity.TradeObject;

import java.util.HashMap;

public class Settings {

    public static class Trade {
        public static final HashMap<String, TradeObject> CC_LIST = new HashMap<String, TradeObject>() {{
            put("XMR", new TradeObject(0d, 0.02411091));
            put("XRP", new TradeObject(0d, 0.00008900));
            put("ZEC", new TradeObject(0d, 0.03446435));
        }};

        public static final double AVAILABLE_BTC_FOR_TRADE_PERCENTAGE = 0.02;
        public static final double SELL_IF_DROPPED_PERCENTAGE = 0.99;
        public static final boolean CAN_I_LOSE = false;
    }


    public static class RMI {
        public static final int TIME_PERIOD = Constant.PERIOD_5M;
        public static final int RMI_OVER_SIGNAL = 2;
        public static final int SIGNAL_OVER_RMI = 1;
        public static final int LENHTH = 132;
        public static final int MOMENTUM = 4;
        public static final int SIGNAL = 3;
        public static final int OVER_BOUGHT = 70;
        public static final int OVER_SOLD = 30;
    }


    public static class EMA {
        public static final int TIME_PERIOD = Constant.PERIOD_2H;
        public static final int LENHTH = 30;
        public static final int PERCENTAGE_VALUE = 100;
    }


}
