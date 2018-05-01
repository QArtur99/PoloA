package com.artf.poloa.presenter.utility;

import java.text.DecimalFormat;

public class Constant {
    public static final DecimalFormat decimalFormat = new DecimalFormat("##.##");
    public static final DecimalFormat decimalFormatEight = new DecimalFormat("##.########");
    public static final String BTC_NAME = "BTC";
    public static final String FILL_OR_KILL = "fillOrKill";
    public static final String POST_ONLY = "postOnly";

    public static final double VALID_AMOUNT_OF_BTC = 0.0015; //RULE FOR BUY - IF MORE BUY ELSE SELL IT
    public static final int PERIOD_1M = 60;
    public static final int PERIOD_2M = 120;
    public static final int PERIOD_3M = 180;
    public static final int PERIOD_5M = 300;
    public static final int PERIOD_15M = 900;
    public static final int PERIOD_30M = 1800;
    public static final int PERIOD_2H = 7200;
    public static final int PERIOD_4H = 14400;
    public static final int PERIOD_1D = 86400;


}
