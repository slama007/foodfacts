package com.slama.foodfacts;

import android.os.Bundle;

import com.slama.foodfacts.scanLib.BaseScannerActivity;

public class SimpleScannerActivity extends BaseScannerActivity {
    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.full_screen_scanner);
        setupToolbar();
    }
}