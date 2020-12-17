package com.phoenixtech.covalentpricetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.phoenixtech.covalentpricetracker.model.ResponseModel;
import com.phoenixtech.covalentpricetracker.network.RetrofitClientInstance;
import com.phoenixtech.covalentpricetracker.network.TickerApi;
import com.phoenixtech.covalentpricetracker.view.TickerAdapter;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Ticker extends WearableActivity {
    private RecyclerView recyclerView;
    private TickerApi tickerApi;
    private TickerAdapter tickerAdapter;
    private ProgressBar progressBar;
    private ResponseModel responseModel;
    private String searchTerm;
    private int pageNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticker);
        recyclerView = findViewById(R.id.tickers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        tickerAdapter = new TickerAdapter();
        recyclerView.setAdapter(tickerAdapter);
        recyclerView.addOnScrollListener(scrollListener);

        progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Get the Intent that started this activity and extract the string
        Intent intent =  getIntent();
        searchTerm = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        // Enables Always-on
        setAmbientEnabled();

        // create an instance of the tickerApi
        tickerApi = RetrofitClientInstance.getRetrofitInstance().create(TickerApi.class);

        // Call endpoint with the search term from the calling Activity
        callEndpoint(searchTerm);

    }

    private void callEndpoint(String searchTerm) {
        Map<String, String> queries = new HashMap<>();

        if (searchTerm != null && !searchTerm.isEmpty())
            queries.put("tickers", searchTerm);

        queries.put("page-number", "" + pageNumber++);

        // make a request by calling the corresponding method
        Observable<ResponseModel> response = tickerApi.getTickers(queries);
        response.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::handleResults, this::handleError);
    }

    private void handleResults(ResponseModel response) {
        responseModel = response;
        if (responseModel != null && responseModel.data != null && responseModel.data.tickers != null && responseModel.data.tickers.size() != 0) {
            tickerAdapter.setData(response.data.tickers);
            progressBar.setVisibility(View.INVISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "NO RESULTS FOUND", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }


    private void handleError(Throwable t) {
        progressBar.setVisibility(View.INVISIBLE);
        Toast.makeText(this, "ERROR IN FETCHING API RESPONSE. Try again", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }


    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView rView, int newState) {
            super.onScrollStateChanged(rView, newState);

            if (!rView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (responseModel.data.pagination.morePages) {
                    Toast.makeText(Ticker.this, "Loading...", Toast.LENGTH_SHORT).show();
                    callEndpoint(searchTerm);
                }
            }
        }
    };

    public void goBack(View view) {
        onBackPressed();
    }
}
