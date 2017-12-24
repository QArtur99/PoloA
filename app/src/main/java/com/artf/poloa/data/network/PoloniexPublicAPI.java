package com.artf.poloa.data.network;

import com.artf.poloa.data.entity.WrapJSONArray;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface PoloniexPublicAPI {

    @GET(RetrofitModule.BASE_PUBLIC_URL)
    Observable<WrapJSONArray> returnTradeHistory(@QueryMap Map<String, String> options);

    @GET(RetrofitModule.BASE_PUBLIC_URL)
    Observable<WrapJSONArray> returnChartData(@QueryMap Map<String, String> options);

    @GET(RetrofitModule.BASE_PUBLIC_URL)
    Call<WrapJSONArray> returnChartDataCall(@QueryMap Map<String, String> options);

}
