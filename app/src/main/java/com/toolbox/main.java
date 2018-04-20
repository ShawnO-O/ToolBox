package com.toolbox;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class main extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Tools().addComma("");
    }

}
