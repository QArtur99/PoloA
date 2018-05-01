package com.artf.poloa.view.activity;

import android.content.Intent;
import android.widget.TextView;

import com.artf.poloa.R;
import com.artf.poloa.data.entity.TradeObject;
import com.artf.poloa.data.entity.WrapDetailsData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import butterknife.BindView;

import static com.artf.poloa.presenter.utility.Constant.decimalFormat;
import static com.artf.poloa.presenter.utility.Constant.decimalFormatEight;

public class DetailsActivity extends BaseActivity {

    WrapDetailsData wrapDetailsData;
    TradeObject tradeObject;
    @BindView(R.id.dontCareBalance) TextView dontCareBalance;
    @BindView(R.id.balanceSelectedCC) TextView balanceSelectedCC;
    @BindView(R.id.tradeMode) TextView tradeMode;
    @BindView(R.id.lastValueCC) TextView lastValueCC;
    @BindView(R.id.rateOfLastBuy) TextView rateOfLastBuy;
    @BindView(R.id.emaValue) TextView emaValue;
    @BindView(R.id.rmiValue) TextView rmiValue;
    @BindView(R.id.rmiSingal) TextView rmiSingal;

    @Override
    public int getContentLayout() {
        return R.layout.activity_details;
    }

    @Override
    public void initNavigation() {

    }

    @Override
    public void initComponents() {
        Intent intent = getIntent();
        wrapDetailsData = new Gson().fromJson(intent.getStringExtra("link"), new TypeToken<WrapDetailsData>() {}.getType());
        tradeObject = wrapDetailsData.tradeObject;
        getSupportActionBar().setTitle(wrapDetailsData.coinName);

        dontCareBalance.setText(decimalFormatEight.format(tradeObject.dontCareBalance));
        balanceSelectedCC.setText(decimalFormatEight.format(tradeObject.balanceSelectedCC));
        tradeMode.setText(String.valueOf(tradeObject.tradeMode));
        lastValueCC.setText(decimalFormatEight.format(tradeObject.lastValueCC));
        rateOfLastBuy.setText(decimalFormatEight.format(tradeObject.rateOfLastBuy));
        emaValue.setText(decimalFormatEight.format(tradeObject.emaValue));
        rmiValue.setText(decimalFormat.format(tradeObject.rmiValue));
        rmiSingal.setText(decimalFormat.format(tradeObject.rmiSingal));
    }
}
