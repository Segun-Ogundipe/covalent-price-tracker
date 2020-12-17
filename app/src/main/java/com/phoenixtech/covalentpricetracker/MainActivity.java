package com.phoenixtech.covalentpricetracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends WearableActivity {

    public static final String EXTRA_MESSAGE = "com.phoenixtech.covalentpricetracker.MESSAGE";
    private EditText searchTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchTxt = findViewById(R.id.search_field);
        searchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
                    if (!textView.getText().toString().isEmpty()) {
                        Intent intent = new Intent(MainActivity.this, Ticker.class);
                        String searchTerm = searchTxt.getText().toString();
                        intent.putExtra(EXTRA_MESSAGE, searchTerm);
                        startActivity(intent);
                    } else
                        textView.setError("Search filed is required");
                }
                return false;
            }
        });

        // Enables Always-on
        setAmbientEnabled();
    }

    public void getAll(View view) {
        Intent intent = new Intent(this, Ticker.class);
        startActivity(intent);
    }
}
