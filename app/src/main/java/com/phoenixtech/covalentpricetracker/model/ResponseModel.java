package com.phoenixtech.covalentpricetracker.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseModel {

    @SerializedName("data")
    public Data data;
    @SerializedName("error")
    public boolean error;
    @SerializedName("error_message")
    public String errorMessage;
    @SerializedName("error_code")
    public Integer errorCode;

    public static class Data {
        @SerializedName("items")
        public List<Ticker> tickers = null;
        @SerializedName("pagination")
        public Pagination pagination;
    }

    public static class Ticker {
        @SerializedName("contract_ticker_symbol")
        public String symbol;
        @SerializedName("logo_url")
        public String logoURL;
        @SerializedName("quote_rate")
        public String fiatValue;
    }

    public static class Pagination {

        @SerializedName("has_more")
        public boolean morePages;
        @SerializedName("page_number")
        public int pageNumber;
        @SerializedName("page_size")
        public int pageSize;
        @SerializedName("total_count")
        public int totalCount;
    }
}
