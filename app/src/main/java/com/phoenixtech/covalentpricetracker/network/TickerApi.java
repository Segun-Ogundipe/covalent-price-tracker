package com.phoenixtech.covalentpricetracker.network;

import com.phoenixtech.covalentpricetracker.model.ResponseModel;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface TickerApi {

    @GET("pricing/tickers?key=ckey_718f22382cbc4b73bc8f53a340c&page-size=10")
    Observable<ResponseModel> getTickers(@QueryMap Map<String, String> options);
}
