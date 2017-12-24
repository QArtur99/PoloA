package com.artf.poloa.data.network;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class RetrofitModule {

    public static final String BASE_PUBLIC_URL = "https://poloniex.com/public";
    public static final String BASE_TRADING_URL = "https://poloniex.com/tradingApi";
    public static final String BASE_URL_V1 = "https://poloniex.com/";

    @Provides
    public OkHttpClient provideClient() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return new OkHttpClient.Builder().addInterceptor(interceptor)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    @Provides
    public Retrofit provideRetrofit(String baseURL, OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseURL)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // .addConverterFactory(new NullOnEmptyConverterFactory())
    }

    @Provides
    public PoloniexPublicAPI providePublicApiService() {
        return provideRetrofit(BASE_URL_V1, provideClient()).create(PoloniexPublicAPI.class);
    }

    @Provides
    public PoloniexTradingAPI provideTradingApiService() {
        return provideRetrofit(BASE_URL_V1, provideClient()).create(PoloniexTradingAPI.class);
    }

}
