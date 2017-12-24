package com.artf.poloa.data.network;


import com.artf.poloa.data.entity.Buy;
import com.artf.poloa.data.entity.WrapJSONObject;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface PoloniexTradingAPI {

    @FormUrlEncoded
    @POST(RetrofitModule.BASE_TRADING_URL)
    Observable<WrapJSONObject> returnBalances(@Header("Key") String key,
                                              @Header("Sign") String signature,
                                              @FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(RetrofitModule.BASE_TRADING_URL)
    Observable<Buy> buy(@Header("Key") String key,
                        @Header("Sign") String signature,
                        @FieldMap Map<String, String> options);

    @FormUrlEncoded
    @POST(RetrofitModule.BASE_TRADING_URL)
    Observable<WrapJSONObject> sell(@Header("Key") String key,
                                              @Header("Sign") String signature,
                                              @FieldMap Map<String, String> options);

}
