package com.artf.poloa.data.entity;

import com.artf.poloa.utility.Mode;


public class TradeObject {

    public double dontCareBalance;
    public double rateOfLastBuy;
    public double rmiValue, rmiSingal;
    public double emaValue;
    public double balanceSelectedCC;
    public double lastValueCC;
    public Mode tradeMode = Mode.SELL;

    public TradeObject(double dontCareBalance, double rateOfLastBuy) {
        this.dontCareBalance = dontCareBalance;
        this.rateOfLastBuy = rateOfLastBuy;
    }
}
